package com.weit2nd.presentation.util

import android.content.Context
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter

fun uriToPainter(context: Context, imgUri: Uri): BitmapPainter {
    val bitmap = ImageDecoder.decodeBitmap(
        ImageDecoder.createSource(
            context.contentResolver,
            imgUri
        )
    )
    return BitmapPainter(bitmap.asImageBitmap())
}
