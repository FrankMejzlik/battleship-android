package com.example.battleship.main.data

import com.example.battleship.utils.BoardArray

class Board(val size: Int, val cells: BoardArray) {
    fun getCell(row: Int, col: Int) = cells[row][col]
}