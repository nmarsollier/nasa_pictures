package com.example.exercise.ui.images

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

private val DATE = "DATE"

class ImagesActivity : AppCompatActivity() {

    private val datesParam: ExtendedDateValue?
        get() = intent.getStringExtra(DATE).jsonToObject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        supportActionBar?.hide()

        datesParam?.let {
            setContent { ImagesScreen(it) }
        }
    }

    companion object {
        fun startActivity(context: Context, date: ExtendedDateValue) {
            val intent = Intent(context, ImagesActivity::class.java)
            intent.putExtra(DATE, date.toJson())
            ContextCompat.startActivity(context, intent, null)
        }
    }
}