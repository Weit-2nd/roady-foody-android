package com.weit2nd.data.source.pickimage

import android.net.Uri
import android.os.Bundle
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PickImageActivity : AppCompatActivity() {

    @Inject
    lateinit var pickImageDataSource: PickImageDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val maximumSelect = intent.getIntExtra(MAXIMUM_SELECT_KEY, 1)
        val pickMedia = if (maximumSelect > 1) {
            registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(maximumSelect)) { images ->
                sendSelectedImagesAndFinish(images)
            }
        } else {
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { image ->
                sendSelectedImagesAndFinish(listOfNotNull(image))
            }
        }
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun sendSelectedImagesAndFinish(
        images: List<Uri>
    ) {
        lifecycleScope.launch {
            pickImageDataSource.emitImages(images)
        }.invokeOnCompletion {
            finish()
        }
    }

    companion object {
        const val MAXIMUM_SELECT_KEY = "meximum_select"
    }
}
