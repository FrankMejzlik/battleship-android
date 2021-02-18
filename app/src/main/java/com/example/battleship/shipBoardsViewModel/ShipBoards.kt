package com.example.battleship.shipBoardsViewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.battleship.utils.Board
import com.example.battleship.utils.Cell
import com.example.battleship.utils.Constants

class ShipBoards(application: Application?) {

    private lateinit var _board: Board
    private val app = application

    var selectedCellLiveData = MutableLiveData<Cell>()

    private var selectedRow = -1
    private var selectedCol = -1

    init {
        selectedCellLiveData.postValue(Pair(selectedRow, selectedCol))
    }

    fun updateSelectedCell(row: Int, col: Int) {
        selectedRow = row
        selectedCol = col
        selectedCellLiveData.postValue(Pair(row, col))
    }

    fun getBoard(): Board {
        return _board
    }

    fun getField(row: Int, col: Int) {}

    fun setField(row: Int, col: Int, value: Constants.FieldStates) {}
}