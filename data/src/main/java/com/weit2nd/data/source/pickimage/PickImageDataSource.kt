package com.weit2nd.data.source.pickimage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.weit2nd.data.util.ActivityProvider
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first

class PickImageDataSource(
    private val activityProvider: ActivityProvider,
) {

    private val selectImageEvent = MutableSharedFlow<List<Uri>>()

    suspend fun pickImage(): Uri? {
        startPickImage()
        return selectImageEvent.first().firstOrNull()
    }

    suspend fun pickImages(
        maximumSelect: Int,
    ): List<Uri> {
        startPickImage(maximumSelect)
        return selectImageEvent.first()
    }

    suspend fun emitImages(
        images: List<Uri>
    ) {
        selectImageEvent.emit(images)
    }

    private fun startPickImage(
        maximumSelect: Int = 1,
    ) {
        val bundle = Bundle().apply {
            putInt(PickImageActivity.MAXIMUM_SELECT_KEY, maximumSelect)
        }
        val intent = Intent(
            activityProvider.currentActivity,
            PickImageActivity::class.java,
        ).apply {
            putExtras(bundle)
        }
        activityProvider.currentActivity?.startActivity(intent)
    }
}
