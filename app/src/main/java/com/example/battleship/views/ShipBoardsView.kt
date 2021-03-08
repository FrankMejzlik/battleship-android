package com.example.battleship.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.battleship.config.BoardArray
import com.example.battleship.config.Constants

// Inspired by tutorial: https://www.youtube.com/watch?v=00QdlHuKGH8&t=45s

class ShipBoardsView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private var cellSizePixels = 0F

    private var selectedRow = -1
    private var selectedCol = -1

    private var listener: OnTouchListener? = null

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
        color = Color.parseColor("#248095")
    }

    private val ship2CellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#afafb5")
    }

    private val ship3CellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#7f7f87")
    }

    private val ship4CellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#55555a")
    }

    private val ship5CellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#2a2a2d")
    }

    private val shipHitPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#DB4437")
    }

    private val shipMissPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#96d6ed")
    }

    private val emptyCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.WHITE
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val sizePixels = Math.min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(sizePixels, sizePixels)
    }

    override fun onDraw(canvas: Canvas) {
        cellSizePixels = Math.min(
            (width / Constants.boardSideSize).toFloat(),
            (height / Constants.boardSideSize).toFloat()
        )
        fillCells(canvas)
        drawLines(canvas)
    }

    private fun fillCells(canvas: Canvas) {
        cells?.forEach { column ->
            column.forEach { cell ->
                val row = cell.row
                val col = cell.col

                when (cell.state) {
                    // Paint cell with ship.
                    Constants.CellStates.SHIP -> {
                        val paint = when (cell.ship?.size) {
                            2 -> ship2CellPaint
                            3 -> ship3CellPaint
                            4 -> ship4CellPaint
                            5 -> ship5CellPaint
                            else -> Paint()
                        }
                        fillCell(canvas, row, col, paint)
                    }

                    Constants.CellStates.HIT -> fillCell(canvas, row, col, shipHitPaint)

                    Constants.CellStates.MISS -> fillCell(canvas, row, col, shipMissPaint)

                    Constants.CellStates.EMPTY -> fillCell(canvas, row, col, emptyCellPaint)
                }

                // Paint chosen cell.
                if (row == selectedRow && col == selectedCol) {
                    if (selectedRow != -1 && selectedCol != -1) {
                        fillCell(canvas, row, col, selectedCellPaint)
                    }
                }
            }
        }
    }

    private fun fillCell(canvas: Canvas, r: Int, c: Int, paint: Paint) {
        canvas.drawRect(
            c * cellSizePixels,
            r * cellSizePixels,
            (c + 1) * cellSizePixels,
            (r + 1) * cellSizePixels,
            paint
        )

    }

    private fun drawLines(canvas: Canvas) {
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), thickLinePaint)

        for (i in 1 until Constants.boardSideSize) {
            canvas.drawLine(
                i * cellSizePixels,
                0F,
                i * cellSizePixels,
                height.toFloat(),
                thinLinePaint
            )
            canvas.drawLine(
                0F,
                i * cellSizePixels,
                width.toFloat(),
                i * cellSizePixels,
                thinLinePaint
            )
        }

        cells?.forEach { column ->
            column.forEach { cell ->
                val row = cell.row
                val col = cell.col

                // Draw cross.
                if (cell.state == Constants.CellStates.HIT) {
                    canvas.drawLine(
                        col * cellSizePixels,
                        row * cellSizePixels,
                        col * cellSizePixels + cellSizePixels,
                        row * cellSizePixels + cellSizePixels,
                        thinLinePaint
                    )

                    canvas.drawLine(
                        col * cellSizePixels + cellSizePixels,
                        row * cellSizePixels,
                        col * cellSizePixels,
                        row * cellSizePixels + cellSizePixels,
                        thinLinePaint
                    )
                }

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

    fun registerListener(listener: OnTouchListener) {
        this.listener = listener
    }

    interface OnTouchListener {
        fun onCellTouched(row: Int, col: Int)
    }
}