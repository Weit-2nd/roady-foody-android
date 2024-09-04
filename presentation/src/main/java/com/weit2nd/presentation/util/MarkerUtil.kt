package com.weit2nd.presentation.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.weit2nd.presentation.R

object MarkerUtil {
    private var unSelectedMarker: Bitmap? = null
    private var selectedMarker: Bitmap? = null

    fun getSelectedMarker(context: Context): Bitmap {
        return selectedMarker ?: run {
            getBitmap(context, R.drawable.ic_marker_selected)
        }.also {
            selectedMarker = it
        }
    }

    fun getUnSelectedMarker(context: Context): Bitmap {
        return unSelectedMarker ?: run {
            getBitmap(context, R.drawable.ic_marker)
        }.also {
            unSelectedMarker = it
        }
    }

    private fun getBitmap(
        context: Context,
        @DrawableRes drawableRes: Int,
    ): Bitmap {
        val drawable = ContextCompat.getDrawable(context, drawableRes)!!
        val bitmap =
            Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888,
            )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}
