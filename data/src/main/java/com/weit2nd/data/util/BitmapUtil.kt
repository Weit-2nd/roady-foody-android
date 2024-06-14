package com.weit2nd.data.util

import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Build
import androidx.annotation.IntRange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

private const val DEFAULT_QUALITY = 90
private const val DEFAULT_RESIZE = 1024

/**
 * Bitmap을 Webp로 압축한 ByteArray를 반환 합니다.
 * @param quality 압축 품질
 */
suspend fun Bitmap.getCompressedBytes(
    @IntRange(from = 1, to = 100) quality: Int = DEFAULT_QUALITY,
): ByteArray =
    withContext(Dispatchers.IO) {
        ByteArrayOutputStream().use { outputStream ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                compress(Bitmap.CompressFormat.WEBP_LOSSY, quality, outputStream)
            } else {
                compress(Bitmap.CompressFormat.WEBP, quality, outputStream)
            }
            outputStream.toByteArray()
        }
    }

/**
 * Bitmap의 width, height 모두 maximumSize 보다 크다면
 * width, height 중 크기가 작은 값을 maximumSize로 잡고 비율에 맞게 리사이징을 합니다.
 */
suspend fun Bitmap.getScaledBitmap(
    maximumSize: Int = DEFAULT_RESIZE,
): Bitmap =
    withContext(Dispatchers.IO) {
        val (width, height) = getScaledWidthAndHeight(
            width = width,
            height = height,
            maximumSize = maximumSize,
        )
        Bitmap.createScaledBitmap(this@getScaledBitmap, width, height, false)
    }

private fun getScaledWidthAndHeight(
    width: Int,
    height: Int,
    maximumSize: Int,
) = when {
    width in maximumSize until height -> maximumSize to (maximumSize / width.toFloat() * height).toInt()
    height in maximumSize until width -> (maximumSize / height.toFloat() * height).toInt() to maximumSize
    else -> width to height
}

/**
 * 회전한 Bitmap을 반환 합니다.
 * @param degree 회전 각도
 */
fun Bitmap.getRotatedBitmap(degree: Float): Bitmap {
    val matrix = Matrix().apply { postRotate(degree) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true) ?: this
}
