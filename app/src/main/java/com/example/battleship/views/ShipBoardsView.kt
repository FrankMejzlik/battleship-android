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

    private var _cellSizePixels = 0F

    private var _selectedRow = -1
    private var _selectedCol = -1

    private var _listener: OnTouchListener? = null

    private var _cells: BoardArray? = null

    private val _thickLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 4F
    }

    private val _thinLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 2F
    }

    private val _selectedCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#248095")
    }

    private val _ship2CellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#afafb5")
    }

    private val _ship3CellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#7f7f87")
    }

    private val _ship4CellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#55555a")
    }

    private val _ship5CellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#2a2a2d")
    }

    private val _shipHitPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#DB4437")
    }

    private val _shipMissPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#96d6ed")
    }

    private val _emptyCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.WHITE
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val sizePixels = Math.min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(sizePixels, sizePixels)
    }

    override fun onDraw(canvas: Canvas) {
        _cellSizePixels = Math.min(
            (width / Constants.boardSideSize).toFloat(),
            (height / Constants.boardSideSize).toFloat()
        )
        fillCells(canvas)
        drawLines(canvas)
    }

    private fun fillCells(canvas: Canvas) {
        _cells?.forEach { column ->
            column.forEach { cell ->
                val row = cell.row
                val col = cell.col

                when (cell.state) {
                    // Paint cell with ship.
                    Constants.CellStates.SHIP -> {
                        val paint = when (cell.ship?.size) {
                            2 -> _ship2CellPaint
                            3 -> _ship3CellPaint
                            4 -> _ship4CellPaint
                            5 -> _ship5CellPaint
                            else -> Paint()
                        }
                        fillCell(canvas, row, col, paint)
                    }

                    Constants.CellStates.HIT -> fillCell(canvas, row, col, _shipHitPaint)

                    Constants.CellStates.MISS -> fillCell(canvas, row, col, _shipMissPaint)

                    Constants.CellStates.EMPTY -> fillCell(canvas, row, col, _emptyCellPaint)
                }

                // Paint chosen cell.
                if (row == _selectedRow && col == _selectedCol) {
                    if (_selectedRow != -1 && _selectedCol != -1) {
                        fillCell(canvas, row, col, _selectedCellPaint)
                    }
                }
            }
        }
    }

    private fun fillCell(canvas: Canvas, r: Int, c: Int, paint: Paint) {
        canvas.drawRect(
            c * _cellSizePixels,
            r * _cellSizePixels,
            (c + 1) * _cellSizePixels,
            (r + 1) * _cellSizePixels,
            paint
        )

    }

    private fun drawLines(canvas: Canvas) {
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), _thickLinePaint)

        for (i in 1 until Constants.boardSideSize) {
            canvas.drawLine(
                i * _cellSizePixels,
                0F,
                i * _cellSizePixels,
                height.toFloat(),
                _thinLinePaint
            )
            canvas.drawLine(
                0F,
                i * _cellSizePixels,
                width.toFloat(),
                i * _cellSizePixels,
                _thinLinePaint
            )
        }

        _cells?.forEach { column ->
            column.forEach { cell ->
                val row = cell.row
                val col = cell.col

                // Draw cross.
                if (cell.state == Constants.CellStates.HIT) {
                    canvas.drawLine(
                        col * _cellSizePixels,
                        row * _cellSizePixels,
                        col * _cellSizePixels + _cellSizePixels,
                        row * _cellSizePixels + _cellSizePixels,
                        _thinLinePaint
                    )

                    canvas.drawLine(
                        col * _cellSizePixels + _cellSizePixels,
                        row * _cellSizePixels,
                        col * _cellSizePixels,
                        row * _cellSizePixels + _cellSizePixels,
                        _thinLinePaint
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
        val possibleSelectedRow = (y / _cellSizePixels).toInt()
        val possibleSelectedCol = (x / _cellSizePixels).toInt()
        _listener?.onCellTouched(possibleSelectedRow, possibleSelectedCol)
    }

    fun updateSelectedCellUI(row: Int, col: Int) {
        _selectedRow = row
        _selectedCol = col
        invalidate()
    }

    fun updateCells(cells: BoardArray) {
        this._cells = cells
        invalidate()
    }

    fun registerListener(listener: OnTouchListener) {
        this._listener = listener
    }

    interface OnTouchListener {
        fun onCellTouched(row: Int, col: Int)
    }
}