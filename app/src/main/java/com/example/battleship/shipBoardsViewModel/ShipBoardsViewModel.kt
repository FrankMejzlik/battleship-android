package com.example.battleship.shipBoardsViewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.battleship.utils.Board
import com.example.battleship.utils.Constants

class ShipBoardsViewModel(app: Application?) : ViewModel() {
    private val shipBoards: ShipBoards by lazy { ShipBoards(app) }

    private val _board = MutableLiveData<Board>()

    fun loadBoard() {
        _board.value = shipBoards.getBoard()
    }

    fun getField(row: Int, col: Int) {}

    fun setField(row: Int, col: Int, value: Constants.FieldStates) {}
}