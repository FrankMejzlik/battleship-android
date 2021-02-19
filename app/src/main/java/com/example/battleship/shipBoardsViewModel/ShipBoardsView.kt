package com.example.battleship.shipBoardsViewModel

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.battleship.utils.BoardArray
import com.example.battleship.utils.Constants

// Tutorial: https://www.youtube.com/watch?v=00QdlHuKGH8&t=45s

class ShipBoardsView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private var cellSizePixels = 0F

    private var selectedRow = -1
    private var selectedCol = -1

    private var listener: ShipBoardsView.OnTouchListener? = null

    private var cells: BoardArray? = null

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

    private val textPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.BLACK
        textSize = 24F
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val sizePixels = Math.min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(sizePixels, sizePixels)
    }

    override fun onDraw(canvas: Canvas) {
        cellSizePixels = (width / Constants.boardSideSize).toFloat()
        fillCells(canvas)
        drawLines(canvas)
        drawText(canvas)
    }

    private fun fillCells(canvas: Canvas) {
        if(selectedRow == -1 || selectedCol == -1) return

        cells?.forEach { column ->
            column.forEach { cell ->
                val row = cell.row
                val col = cell.col

                if(row == selectedRow && col == selectedCol) {
                    fillCell(canvas, row, col, selectedCellPaint)
                }
            }
        }
    }

    private fun fillCell(canvas: Canvas, r: Int, c: Int, paint: Paint) {
        canvas.drawRect(c * cellSizePixels, r * cellSizePixels, (c + 1) * cellSizePixels, (r + 1) * cellSizePixels, paint)

    }

    private fun drawLines(canvas: Canvas) {
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), thickLinePaint)

        for (i in 1 until Constants.boardSideSize) {
            canvas.drawLine(i * cellSizePixels, 0F, i * cellSizePixels, height.toFloat(), thinLinePaint)
            canvas.drawLine(0F, i * cellSizePixels, width.toFloat(), i * cellSizePixels, thinLinePaint)
        }
    }

    private fun drawText(canvas: Canvas) {
        cells?.forEach { column ->
            column.forEach { cell ->
                val row = cell.row
                val col = cell.col
                val stateString = when (cell.value) {
                    Constants.CellStates.EMPTY -> ""
                    Constants.CellStates.HIT -> "X"
                    Constants.CellStates.MISS -> "O"
                    Constants.CellStates.SHIP -> "S"
                }

                val textBounds = Rect()
                textPaint.getTextBounds(stateString, 0, stateString.length, textBounds)
                val textWidth = textPaint.measureText(stateString)
                val textHeight = textBounds.height()

                canvas.drawText(stateString, (col * cellSizePixels) + cellSizePixels / 2 - textWidth / 2,
                    (row * cellSizePixels) + cellSizePixels / 2 - textHeight / 2, textPaint)

            }
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

    fun updateCells(cells: BoardArray) {
        this.cells = cells
        invalidate()
    }

    fun registerListener(listener: ShipBoardsView.OnTouchListener) {
        this.listener = listener
    }

    interface OnTouchListener {
        fun onCellTouched(row: Int, col: Int)
    }
}