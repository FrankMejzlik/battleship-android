package com.example.battleship.shipBoardsViewModel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.battleship.main.data.Board
import com.example.battleship.main.data.Cell
import com.example.battleship.main.data.Ship
import com.example.battleship.utils.BoardArray
import com.example.battleship.utils.CellPair
import com.example.battleship.utils.Constants

class ShipBoards(val app: Application?) {

    // Direction for counting size of the ship
    private enum class Direction {
        LEFT, RIGHT
    }

    private lateinit var _board: Board

    var selectedCellLiveData = MutableLiveData<CellPair>()
    var cellsLiveData = MutableLiveData<BoardArray>()

    private var selectedRow = -1
    private var selectedCol = -1

    init {
        val cells = BoardArray(Constants.boardSideSize) { i ->
            Array(Constants.boardSideSize) { j -> Cell(i, j, Constants.CellStates.EMPTY, null) }
        }
        _board = Board(Constants.boardSideSize * Constants.boardSideSize, cells)

        selectedCellLiveData.postValue(Pair(selectedRow, selectedCol))
        cellsLiveData.postValue(_board.cells)
    }

    fun handleInput(shipSize: Int, action: Constants.ShipAction) {
        if (selectedRow == -1 || selectedCol == -1) return

        val cellState = _board.getCell(selectedRow, selectedCol)?.state

        when (action) {
            Constants.ShipAction.PLACE -> if (cellState == Constants.CellStates.EMPTY) placeShip(
                shipSize,
                true
            )
            Constants.ShipAction.ROTATE -> if (cellState == Constants.CellStates.SHIP) rotateShip()
            Constants.ShipAction.ERASE -> if (cellState == Constants.CellStates.SHIP) eraseShip()
        }

        cellsLiveData.postValue(_board.cells)
    }

    private fun placeShip(shipSize: Int, isHorizontal: Boolean): Boolean {
        // Check if ship is not out of board.
        var border = 0
        border = if (isHorizontal)
            Constants.boardSideSize - selectedCol
        else
            Constants.boardSideSize - selectedRow

        if (border < shipSize) {
            Toast.makeText(
                app?.applicationContext,
                "Out of board",
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        // Check if cells are occupied by other ship.
        for (i in 0 until shipSize) {
            val row = if (isHorizontal) selectedRow else selectedRow + i
            val col = if (isHorizontal) selectedCol + i else selectedCol

            if (_board.getCell(row, col)?.state == Constants.CellStates.SHIP) {
                Toast.makeText(
                    app?.applicationContext,
                    "Other ship is there",
                    Toast.LENGTH_LONG
                ).show()
                return false
            }
        }

        // Set all cells of shipSize with given state and given type of ship.
        val ship = Ship(shipSize)
        for (i in 0 until shipSize) {
            val row = if (isHorizontal) selectedRow else selectedRow + i
            val col = if (isHorizontal) selectedCol + i else selectedCol
            val state = Constants.CellStates.SHIP
            _board.getCell(row, col)?.state = state
            _board.getCell(row, col)?.ship = ship
        }
        return true
    }

    private fun rotateShip() {
        val isHorizontal = isShipHorizontal()

        val startCell = getStartCell(isHorizontal)
        val shipSize = _board.getCell(selectedRow, selectedCol)?.ship?.size ?: 0

        val oldPair = Pair(selectedRow, selectedCol)
        selectedRow = startCell.row
        selectedCol = startCell.col
        val oldBoard = Board(_board.size, _board.copyCells())
        eraseShip()

        if (!placeShip(shipSize, !isHorizontal)) {
            _board.cells = oldBoard.copyCells()
        }

        selectedRow = oldPair.first
        selectedCol = oldPair.second
    }

    private fun getStartCell(isHorizontal: Boolean): Cell {
        // If current position is start of the boat, return current cell as start cell.
        val currCell = _board.getCell(selectedRow, selectedCol)

        var index = 1
        while (true) {
            val row = when {
                isHorizontal -> selectedRow
                else -> selectedRow - index
            }

            val col = when {
                !isHorizontal -> selectedCol
                else -> selectedCol - index
            }

            val cell = _board.getCell(row, col)

            if (cell?.ship != currCell?.ship) {
                val startRow = if (isHorizontal) selectedRow else selectedRow - index + 1
                val startCol = if (isHorizontal) selectedCol - index + 1 else selectedCol
                val startCell = _board.getCell(startRow, startCol)
                return Cell(startRow, startCol, startCell?.state, startCell?.ship)
            }

            ++index
        }
    }

    private fun eraseShip() {
        val isHorizontal = isShipHorizontal()

        val startCell = getStartCell(isHorizontal)
        val shipSize = _board.getCell(selectedRow, selectedCol)?.ship?.size ?: 0

        val oldPair = Pair(selectedRow, selectedCol)
        selectedRow = startCell.row
        selectedCol = startCell.col

        // Clear all cells of shipSize with given state.
        for (i in 0 until shipSize) {
            val row = if (isHorizontal) selectedRow else selectedRow + i
            val col = if (isHorizontal) selectedCol + i else selectedCol

            _board.getCell(row, col)?.state = Constants.CellStates.EMPTY
            _board.getCell(row, col)?.ship = null
        }

        selectedRow = oldPair.first
        selectedCol = oldPair.second
    }

    private fun isShipHorizontal(): Boolean {
        var shipLeft = _board.getCell(selectedRow, selectedCol - 1)?.ship
        val shipRight = _board.getCell(selectedRow, selectedCol + 1)?.ship
        val currCell = _board.getCell(selectedRow, selectedCol)

        return shipLeft == currCell?.ship ||
                shipRight == currCell?.ship
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