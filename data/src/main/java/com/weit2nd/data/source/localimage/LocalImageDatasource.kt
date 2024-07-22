package com.weit2nd.data.source.localimage

import android.content.ContentResolver
import android.content.ContentUris
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.exifinterface.media.ExifInterface
import com.weit2nd.data.model.LocalImageWithDirectory
import com.weit2nd.data.util.getCompressedBytes
import com.weit2nd.data.util.getRotatedBitmap
import com.weit2nd.data.util.getScaledBitmap
import com.weit2nd.domain.model.localimage.LocalImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class LocalImageDatasource @Inject constructor(
    private val contentResolver: ContentResolver,
) {
    private val projection =
        arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATE_MODIFIED,
            MediaStore.Images.Media.DATA,
        )
    private val defaultMediaType = "image/webp".toMediaType()

    suspend fun getImages(
        path: String? = null,
        count: Int? = null,
        offset: Int = 0,
    ): List<LocalImageWithDirectory> =
        withContext(Dispatchers.IO) {
            val images = mutableListOf<LocalImageWithDirectory>()
            contentResolver
                .query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    getQueryBundle(path, count, offset),
                    null,
                )?.use { cursor ->
                    while (cursor.moveToNext()) {
                        val uri =
                            ContentUris
                                .withAppendedId(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)),
                                ).toString()
                        val lastModified = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED))
                        val directory =
                            cursor
                                .getString(
                                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA),
                                ).substringBeforeLast('/')
                        val localImage =
                            LocalImage(
                                lastModified = lastModified,
                                uri = uri,
                            )
                        images.add(
                            LocalImageWithDirectory(
                                localImage = localImage,
                                directory = directory,
                            ),
                        )
                    }
                }
            images
        }

    private fun getQueryBundle(
        path: String?,
        count: Int?,
        offset: Int,
    ): Bundle =
        Bundle().apply {
            if (path.isNullOrBlank().not()) {
                val selection =
                    "${MediaStore.Images.Media.DATA} LIKE '$path/%' AND " +
                        "${MediaStore.Images.Media.DATA} NOT LIKE '$path/%/%'"
                putString(ContentResolver.QUERY_ARG_SQL_SELECTION, selection)
            }
            if (count != null) {
                putInt(ContentResolver.QUERY_ARG_LIMIT, count)
            }
            putInt(ContentResolver.QUERY_ARG_OFFSET, offset)
            putStringArray(ContentResolver.QUERY_ARG_SORT_COLUMNS, arrayOf(MediaStore.Images.ImageColumns.DATE_MODIFIED))
            putInt(ContentResolver.QUERY_ARG_SORT_DIRECTION, ContentResolver.QUERY_SORT_DIRECTION_DESCENDING)
        }

    suspend fun getImageMultipartBodyPart(
        uri: String,
        formDataName: String,
        imageName: String,
        mediaType: MediaType = defaultMediaType,
    ): MultipartBody.Part {
        val imageBytes = getFormattedImageBytes(uri)
        val requestFile =
            imageBytes.toRequestBody(
                mediaType,
                0,
                imageBytes.size,
            )
        return MultipartBody.Part.createFormData(
            formDataName,
            "$imageName.${mediaType.subtype}",
            requestFile,
        )
    }

    private suspend fun getFormattedImageBytes(uri: String): ByteArray {
        val bitmap = getBitmapByUri(uri)
        val rotatedBitmap = bitmap.getRotatedBitmap(getRotate(uri))
        val scaledBitmap = rotatedBitmap.getScaledBitmap()
        val bytes = scaledBitmap.getCompressedBytes()
        scaledBitmap.recycle()
        return bytes
    }

    private suspend fun getBitmapByUri(uri: String): Bitmap =
        withContext(Dispatchers.IO) {
            contentResolver.openInputStream(Uri.parse(uri)).use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        }

    private suspend fun getRotate(uri: String): Float =
        withContext(Dispatchers.IO) {
            contentResolver.openInputStream(Uri.parse(uri))?.use { inputStream ->
                val exif = ExifInterface(inputStream)
                when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                    ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                    ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                    else -> 0f
                }
            } ?: 0f
        }

    fun checkImagesUriValid(images: List<String>): Boolean {
        return images.all { image -> checkImageUriValid(image) }
    }

    fun checkImageUriValid(uri: String): Boolean {
        if (uri.isEmpty()) return false
        Uri.parse(uri).apply {
            return (checkReadableUri(this) && checkImageType(this))
        }
    }

    fun findInvalidImage(images: List<String>): String? {
        images.forEach {
            if (checkImageUriValid(it).not()) return it
        }
        return null
    }

    private fun checkReadableUri(uri: Uri): Boolean {
        contentResolver.openInputStream(uri).use { inputStream ->
            if (inputStream == null) {
                return false
            }
        }
        return true
    }

    private fun checkImageType(uri: Uri): Boolean {
        val type = contentResolver.getType(uri)
        return type != null && type.startsWith("image/")
    }
}
