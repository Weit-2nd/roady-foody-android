package com.weit2nd.presentation.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.IntRange
import androidx.annotation.StringRes

fun Context.showToast(
    @StringRes messageRes: Int,
    @IntRange(from = 0, to = 1) duration: Int = Toast.LENGTH_SHORT,
) {
    Toast.makeText(this, getString(messageRes), duration).show()
}
