package com.weit2nd.data.source.localimage

import android.content.ContentResolver
import android.content.ContentUris
import android.os.Bundle
import android.provider.MediaStore
import com.weit2nd.data.model.LocalImageWithDirectory
import com.weit2nd.domain.model.localimage.LocalImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalImageDatasource @Inject constructor(
    private val contentResolver: ContentResolver,
) {
    private val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DATE_MODIFIED,
        MediaStore.Images.Media.DATA,
    )

    suspend fun getImages(
        path: String? = null,
        count: Int? = null,
        offset: Int = 0,
    ): List<LocalImageWithDirectory> =
        withContext(Dispatchers.IO) {
            val images = mutableListOf<LocalImageWithDirectory>()
            contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                getQueryBundle(path, count, offset),
                null,
            )?.use { cursor ->
                while (cursor.moveToNext()) {
                    val uri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)),
                    ).toString()
                    val lastModified = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED))
                    val directory = cursor.getString(
                        cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    ).substringBeforeLast('/')
                    val localImage = LocalImage(
                        lastModified = lastModified,
                        uri = uri,
                    )
                    images.add(
                        LocalImageWithDirectory(
                            localImage = localImage,
                            directory = directory,
                        )
                    )
                }
            }
            images
        }

    private fun getQueryBundle(path: String?, count: Int?, offset: Int): Bundle =
        Bundle().apply {
            if (path.isNullOrBlank().not()) {
                val selection = "${MediaStore.Images.Media.DATA} LIKE '$path/%' AND " +
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
}
