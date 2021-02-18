package com.example.battleship.shipBoardsViewModel

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

// Tutorial: https://www.youtube.com/watch?v=00QdlHuKGH8&t=45s

class ShipBoardsView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private val size = 10

    private var cellSizePixels = 0F

    private val thickLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 4F
    }

    private val thinLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 2F
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val sizePixels = Math.min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(sizePixels, sizePixels)
    }

    override fun onDraw(canvas: Canvas) {
        cellSizePixels = (width / size).toFloat()
        drawLines(canvas)
    }

    private fun drawLines(canvas: Canvas) {
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), thickLinePaint)

        for (i in 1 until size) {
            canvas.drawLine(i * cellSizePixels, 0F, i * cellSizePixels, height.toFloat(), thinLinePaint)
            canvas.drawLine(0F, i * cellSizePixels, width.toFloat(), i * cellSizePixels, thinLinePaint)
        }
    }
}