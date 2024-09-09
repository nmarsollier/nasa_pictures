package com.example.exercise.ui.imagePreview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.example.exercise.common.utils.jsonToObject
import com.example.exercise.common.utils.toJson
import com.example.exercise.models.api.images.ImageValue

private val IMAGE = "IMAGE"

class ImagePreviewActivity : AppCompatActivity() {
    private val viewModel: ImagePreviewViewModel by viewModels()

    private val imageValueParam: ImageValue?
        get() = intent.getStringExtra(IMAGE).jsonToObject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        supportActionBar?.hide()

        imageValueParam?.let {
            setContent { ImagePreviewScreen() }
            viewModel.init(it)
        }
    }

    companion object {
        fun startActivity(context: Context, imageValue: ImageValue) {
            val intent = Intent(context, ImagePreviewActivity::class.java)
            intent.putExtra(IMAGE, imageValue.toJson())
            ContextCompat.startActivity(context, intent, null)
        }
    }
}