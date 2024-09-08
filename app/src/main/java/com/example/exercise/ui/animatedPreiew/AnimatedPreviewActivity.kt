package com.example.exercise.ui.animatedPreiew

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.example.exercise.models.extendedDate.ExtendedDateValue
import com.example.exercise.ui.utils.jsonToObject
import com.example.exercise.ui.utils.toJson

private const val DATE = "DATE"

class AnimatedPreviewActivity : AppCompatActivity() {
    private val imageValueParam: ExtendedDateValue?
        get() = intent.getStringExtra(DATE).jsonToObject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        supportActionBar?.hide()

        imageValueParam?.let {
            setContent { AnimatedPreviewScreen(it) }
        }
    }

    companion object {
        fun startActivity(context: Context, dateValue: ExtendedDateValue) {
            val intent = Intent(context, AnimatedPreviewActivity::class.java)
            intent.putExtra(DATE, dateValue.toJson())
            ContextCompat.startActivity(context, intent, null)
        }
    }
}