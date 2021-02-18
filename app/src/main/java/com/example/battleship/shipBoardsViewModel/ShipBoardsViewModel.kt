package com.example.battleship.shipBoardsViewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.battleship.utils.Board
import com.example.battleship.utils.Cell
import com.example.battleship.utils.Constants

class ShipBoardsViewModel(app: Application?) : ViewModel() {

    val shipBoard = ShipBoards(app)

    private val shipBoards: ShipBoards by lazy { ShipBoards(app) }

    private val _board = MutableLiveData<Board>()
    private val _selectedCell = MutableLiveData<Cell>()

    fun loadBoard() {
        _board.value = shipBoards.getBoard()
    }

    fun getField(row: Int, col: Int) {}

    fun setField(row: Int, col: Int, value: Constants.FieldStates) {}
}