package com.weit2nd.presentation.util

import android.content.ContentResolver
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter

fun Uri.toPainter(contentResolver: ContentResolver): BitmapPainter {
    val bitmap = ImageDecoder.decodeBitmap(
        ImageDecoder.createSource(
            contentResolver,
            this,
        )
    )
    return BitmapPainter(bitmap.asImageBitmap())
}
