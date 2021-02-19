package com.example.battleship.shipBoardsViewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.battleship.main.data.Board
import com.example.battleship.utils.BoardArray
import com.example.battleship.utils.CellPair
import com.example.battleship.utils.Constants

class ShipBoardsViewModel(app: Application?) : ViewModel() {

    val shipBoard = ShipBoards(app)

    private val shipBoards: ShipBoards by lazy { ShipBoards(app) }

    private val _board = MutableLiveData<Board>()
    private val _selectedCell = MutableLiveData<CellPair>()

    fun loadBoard() {
        _board.postValue(shipBoards.getBoard())
    }

    fun getField(row: Int, col: Int) {}

    fun setField(row: Int, col: Int, value: Constants.CellStates) {}
}