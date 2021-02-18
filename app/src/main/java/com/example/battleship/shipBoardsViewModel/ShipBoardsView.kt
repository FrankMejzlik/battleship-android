package com.example.battleship.shipBoardsViewModel

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

// Tutorial: https://www.youtube.com/watch?v=00QdlHuKGH8&t=45s

class ShipBoardsView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private val size = 10

    private var cellSizePixels = 0F

    private var selectedRow = -1
    private var selectedCol = -1

    private var listener: ShipBoardsView.OnTouchListener? = null

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

    private val selectedCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#6ead3a")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val sizePixels = Math.min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(sizePixels, sizePixels)
    }

    override fun onDraw(canvas: Canvas) {
        cellSizePixels = (width / size).toFloat()
        fillCells(canvas)
        drawLines(canvas)
    }

    private fun fillCells(canvas: Canvas) {
        if(selectedRow == -1 || selectedCol == -1) return

        for (r in 0..size){
            for(c in 0..size) {
                if(r == selectedRow && c == selectedCol) {
                    fillCell(canvas, r, c, selectedCellPaint)
                }
            }
        }
    }

    private fun fillCell(canvas: Canvas, r: Int, c: Int, paint: Paint) {
        canvas.drawRect(c * cellSizePixels, r * cellSizePixels, (c + 1) * cellSizePixels, (r + 1) * cellSizePixels, paint)

    }

    private fun drawLines(canvas: Canvas) {
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), thickLinePaint)

        for (i in 1 until size) {
            canvas.drawLine(i * cellSizePixels, 0F, i * cellSizePixels, height.toFloat(), thinLinePaint)
            canvas.drawLine(0F, i * cellSizePixels, width.toFloat(), i * cellSizePixels, thinLinePaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                handleTouchEvent(event.x, event.y)
                true
            }
            else -> false
        }
    }

    private fun handleTouchEvent(x: Float, y: Float) {
        val possibleSelectedRow = (y / cellSizePixels).toInt()
        val possibleSelectedCol = (x / cellSizePixels).toInt()
        listener?.onCellTouched(possibleSelectedRow, possibleSelectedCol)
    }

    fun updateSelectedCellUI(row: Int, col: Int) {
        selectedRow = row
        selectedCol = col
        invalidate()
    }

    fun registerListener(listener: ShipBoardsView.OnTouchListener) {
        this.listener = listener
    }

    interface OnTouchListener {
        fun onCellTouched(row: Int, col: Int)
    }
}