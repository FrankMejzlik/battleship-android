package com.example.battleship.shipBoardsViewModel

import android.app.Application
import com.example.battleship.utils.Board
import com.example.battleship.utils.Constants

class ShipBoards(application: Application?) {

    private lateinit var _board: Board
    private val app = application

    init {}

    fun getBoard(): Board {
        return _board
    }

    fun getField(row: Int, col: Int) {}

    fun setField(row: Int, col: Int, value: Constants.FieldStates) {}
}