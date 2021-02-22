package com.example.battleship.data

import com.example.battleship.utils.Constants

class Cell(val row: Int, val col: Int, var state: Constants.CellStates?, var ship: Ship?) {
}