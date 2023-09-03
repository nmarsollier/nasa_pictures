package com.example.exercise.ui.utils

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import com.example.exercise.MainApplication
import com.facebook.drawee.drawable.ProgressBarDrawable

private val SECONDARY_COLOR = Color.parseColor("#656C78")
private val PRIMARY_COLOR = Color.parseColor("#B2B7BD")

private val Int.dp: Int
    get() = (this * MainApplication.context.resources.displayMetrics.density).toInt()

class CircleProgressBarDrawable : ProgressBarDrawable() {
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = SECONDARY_COLOR
        textSize = 16.dp.toFloat()
    }
    private val circlePaintSecondary = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = SECONDARY_COLOR
        style = Paint.Style.STROKE
        strokeWidth = 6.dp.toFloat()
    }
    private val circlePaintPrimary = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = PRIMARY_COLOR
        style = Paint.Style.STROKE
        strokeWidth = 6.dp.toFloat()
    }

    private var progress = 0.0

    override fun onLevelChange(level: Int): Boolean {
        progress = level.toDouble() / 100
        invalidateSelf()
        return true
    }

    override fun draw(canvas: Canvas) {
        if (hideWhenZero && progress == 0.0) {
            return
        }

        drawProgress(canvas)
        drawBar(canvas)
        drawInProgress(canvas)
    }

    private fun drawProgress(canvas: Canvas) {
        val p = "${progress.toInt()}%"
        val r = Rect()
        val b = textPaint.getTextBounds(p, 0, p.length, r)

        canvas.drawText(
            p,
            bounds.width() / 2 - r.width() / 2f,
            (bounds.height() / 2f) - r.height() / 2,
            textPaint
        )
    }

    private fun drawBar(canvas: Canvas) {
        val radius = 60.dp.toFloat()

        val left = bounds.width() / 2f - radius / 2
        val top = (bounds.height() / 2f - radius / 2) * 0.70f

        //Calculate the rect / bounds of oval
        val rectF = RectF(left, top, left + radius, top + radius)

        canvas.drawArc(rectF, 0f, 360f, false, circlePaintSecondary)

        canvas.drawArc(
            rectF, 270f, (progress / 100 * 360).toFloat(), false, circlePaintPrimary
        )
    }


    private fun drawInProgress(canvas: Canvas) {
        val p = "In progress"
        val width = textPaint.measureText(p)
        canvas.drawText(
            p, bounds.width() / 2 - width / 2, bounds.height() * 0.85f, textPaint
        )
    }
}