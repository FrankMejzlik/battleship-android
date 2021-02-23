package com.example.battleship

import com.example.battleship.config.Constants

class Cell(val row: Int, val col: Int, var state: Constants.CellStates?, var ship: Ship?) {
}