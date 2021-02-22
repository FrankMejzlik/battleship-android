package com.example.battleship.data

import com.example.battleship.utils.BoardArray
import com.example.battleship.utils.Constants

class Board(val size: Int, var cells: BoardArray) {
    fun getCell(row: Int, col: Int): Cell? {
        if (row < 0 || row > Constants.boardSideSize - 1) return null
        if (col < 0 || col > Constants.boardSideSize - 1) return null
        return cells[row][col]
    }

    fun copyCells(): BoardArray {
        return BoardArray(Constants.boardSideSize) { i ->
            Array(Constants.boardSideSize) { j ->
                Cell(i, j, cells[i][j].state, cells[i][j].ship)
            }
        }
    }
}