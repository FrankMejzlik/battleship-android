package com.example.battleship.shipBoardsViewModel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.battleship.main.data.Board
import com.example.battleship.main.data.Cell
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
            Array(Constants.boardSideSize) { j -> Cell(i, j, Constants.CellStates.EMPTY) }
        }
        _board = Board(Constants.boardSideSize * Constants.boardSideSize, cells)

        selectedCellLiveData.postValue(Pair(selectedRow, selectedCol))
        cellsLiveData.postValue(_board.cells)
    }

    fun handleInput(shipSize: Int, action: Constants.ShipAction) {
        if (selectedRow == -1 || selectedCol == -1) return

        val cellState = _board.getCell(selectedRow, selectedCol)?.value

        when (action) {
            Constants.ShipAction.PLACE -> if (cellState == Constants.CellStates.EMPTY) placeShip(
                shipSize,
                true
            )
            Constants.ShipAction.ROTATE -> if (cellState == Constants.CellStates.SHIP || cellState == Constants.CellStates.SHIP_START) rotateShip()
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

            if (_board.getCell(row, col)?.value == Constants.CellStates.SHIP ||
                _board.getCell(row, col)?.value == Constants.CellStates.SHIP_START
            ) {
                Toast.makeText(
                    app?.applicationContext,
                    "Other ship is there",
                    Toast.LENGTH_LONG
                ).show()
                return false
            }
        }

        // Set all cells of shipSize with given state.
        for (i in 0 until shipSize) {
            val row = if (isHorizontal) selectedRow else selectedRow + i
            val col = if (isHorizontal) selectedCol + i else selectedCol
            val state = if (i == 0) Constants.CellStates.SHIP_START else Constants.CellStates.SHIP
            _board.getCell(row, col)?.value = state
        }
        return true
    }

    private fun rotateShip() {
        val isHorizontal = isShipHorizontal()

        // Count size of the ship.
        var shipSize = 1 // Count selected cell.
        // To the left (or up) from current cell
        val pair = countCellsAndStartCell(isHorizontal, Direction.LEFT)
        val startCell = pair.second
        shipSize += pair.first

        // To the right (or down) from current cell
        shipSize += countCellsAndStartCell(isHorizontal, Direction.RIGHT).first

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

    private fun countCellsAndStartCell(isHorizontal: Boolean, dir: Direction): Pair<Int, Cell> {
        // If current position is start of the boat, return current cell as start cell.
        val currCell = _board.getCell(selectedRow, selectedCol)
        if (dir == Direction.LEFT)
            if (currCell?.value == Constants.CellStates.SHIP_START)
                return Pair(0, currCell)

        var index = 1
        var cellsCount = 0
        while (true) {
            val row = when {
                isHorizontal -> selectedRow
                dir == Direction.LEFT -> selectedRow - index
                else -> selectedRow + index
            }

            val col = when {
                !isHorizontal -> selectedCol
                dir == Direction.LEFT -> selectedCol - index
                else -> selectedCol + index
            }

            val cellValue = _board.getCell(row, col)?.value

            if (cellValue == Constants.CellStates.SHIP)
                ++cellsCount
            else {
                if (dir == Direction.LEFT) {
                    if (cellValue == Constants.CellStates.SHIP_START) {
                        val startRow = if (isHorizontal) selectedRow else selectedRow - index
                        val startCol = if (isHorizontal) selectedCol - index else selectedCol
                        return Pair(cellsCount, Cell(startRow, startCol, cellValue))
                    }
                }
                return Pair(cellsCount, Cell(-1, -1, Constants.CellStates.EMPTY))
            }

            ++index
        }
    }

    private fun eraseShip() {
        val isHorizontal = isShipHorizontal()

        // Count size of the ship.
        var shipSize = 1 // Count selected cell.
        // To the left (or up) from current cell
        val pair = countCellsAndStartCell(isHorizontal, Direction.LEFT)
        val startCell = pair.second
        shipSize += pair.first

        // To the right (or down) from current cell
        shipSize += countCellsAndStartCell(isHorizontal, Direction.RIGHT).first

        val oldPair = Pair(selectedRow, selectedCol)
        selectedRow = startCell.row
        selectedCol = startCell.col

        // Clear all cells of shipSize with given state.
        for (i in 0 until shipSize) {
            val row = if (isHorizontal) selectedRow else selectedRow + i
            val col = if (isHorizontal) selectedCol + i else selectedCol

            _board.getCell(row, col)?.value = Constants.CellStates.EMPTY
        }

        selectedRow = oldPair.first
        selectedCol = oldPair.second
    }

    private fun isShipHorizontal(): Boolean {
        var valueLeft = _board.getCell(selectedRow, selectedCol - 1)?.value
        val valueRight = _board.getCell(selectedRow, selectedCol + 1)?.value
        val currCell = _board.getCell(selectedRow, selectedCol)
        if (currCell?.value == Constants.CellStates.SHIP_START)
            valueLeft = null

        return valueLeft == Constants.CellStates.SHIP ||
                valueLeft == Constants.CellStates.SHIP_START ||
                valueRight == Constants.CellStates.SHIP
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