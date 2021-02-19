package com.example.battleship.shipBoardsViewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.battleship.main.data.Board
import com.example.battleship.main.data.Cell
import com.example.battleship.utils.BoardArray
import com.example.battleship.utils.CellPair
import com.example.battleship.utils.Constants

class ShipBoards(application: Application?) {

    private lateinit var _board: Board
    private val app = application

    var selectedCellLiveData = MutableLiveData<CellPair>()
    var cellsLiveData = MutableLiveData<BoardArray>()

    private var selectedRow = -1
    private var selectedCol = -1

    init {
        val cells = BoardArray(Constants.boardSideSize) {
            i -> Array(Constants.boardSideSize) {
                j -> Cell(i, j, Constants.CellStates.EMPTY)}
        }
        _board = Board(Constants.boardSideSize * Constants.boardSideSize, cells)

        selectedCellLiveData.postValue(Pair(selectedRow, selectedCol))
        cellsLiveData.postValue(_board.cells)
    }

    fun handleInput(state: Constants.CellStates) {
        if (selectedRow == -1 || selectedCol == -1) return

        _board.getCell(selectedRow, selectedCol).value = state
        cellsLiveData.postValue(_board.cells)
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

    fun setField(row: Int, col: Int, value: Constants.CellStates) {}
}