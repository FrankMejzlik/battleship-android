package com.example.battleship

import android.app.Application
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.battleship.config.BoardArray
import com.example.battleship.config.Constants
import com.example.battleship.config.CellPair

class Board(private val app: Application?, val size: Int) {

    var selectedCellLiveData = MutableLiveData<CellPair>()
    var cellsLiveData = MutableLiveData<BoardArray>()

    private var selectedRow = -1
    private var selectedCol = -1

    private var _cells: BoardArray

    // Counter of number of placed ships.
    var counter = Array(4) { 0 }

    // Maximum limit of ships for each type
    var maxLimit = arrayOf(4, 3, 2, 1)
    val buttonNames = arrayOf("btn_two_ship", "btn_three_ship", "btn_four_ship", "btn_five_ship")

    init {
        val cells = BoardArray(Constants.boardSideSize) { i ->
            Array(Constants.boardSideSize) { j -> Cell(i, j, Constants.CellStates.EMPTY, null) }
        }

        _cells = cells

        selectedCellLiveData.postValue(Pair(selectedRow, selectedCol))
        cellsLiveData.postValue(cells)
    }

    fun resetBoard() {
        val cells = BoardArray(Constants.boardSideSize) { i ->
            Array(Constants.boardSideSize) { j -> Cell(i, j, Constants.CellStates.EMPTY, null) }
        }

        _cells = cells

        selectedCellLiveData.postValue(Pair(-1, -1))
        cellsLiveData.postValue(cells)
    }

    fun getCell(row: Int, col: Int): Cell? {
        if (row < 0 || row > Constants.boardSideSize - 1) return null
        if (col < 0 || col > Constants.boardSideSize - 1) return null
        return _cells[row][col]
    }

    private fun copyCells(cells: BoardArray): BoardArray {
        return BoardArray(Constants.boardSideSize) { i ->
            Array(Constants.boardSideSize) { j ->
                Cell(i, j, cells[i][j].state, cells[i][j].ship)
            }
        }
    }

    fun handleInput(view: View, shipSize: Int, action: Constants.ShipAction) {
        if (selectedRow == -1 || selectedCol == -1) return

        val cellState = _cells[selectedRow][selectedCol].state

        when (action) {
            Constants.ShipAction.PLACE -> {
                if (placeShip(shipSize, true)) {
                    handleCounter(Constants.ShipAction.PLACE, shipSize)
                    handleShipButtons(view, Constants.ShipAction.PLACE, shipSize)
                }

            }
            Constants.ShipAction.ROTATE -> if (cellState == Constants.CellStates.SHIP) rotateShip()
            Constants.ShipAction.ERASE -> {
                if (cellState == Constants.CellStates.SHIP) {
                    val size = _cells[selectedRow][selectedCol].ship?.size ?: 0
                    eraseShip()
                    handleCounter(Constants.ShipAction.ERASE, size)
                    handleShipButtons(view, Constants.ShipAction.ERASE, size)
                }
            }
            Constants.ShipAction.SHOOT -> handleShoot()
        }

        cellsLiveData.postValue(_cells)
    }

    private fun handleShoot() {
        val shotCell = _cells[selectedRow][selectedCol]
        when(shotCell.state) {
            Constants.CellStates.SHIP -> _cells[selectedRow][selectedCol].state = Constants.CellStates.HIT
            Constants.CellStates.HIT -> _cells[selectedRow][selectedCol].state = Constants.CellStates.HIT
            else -> _cells[selectedRow][selectedCol].state = Constants.CellStates.MISS
        }
    }

    private fun handleCounter(action: Constants.ShipAction, shipSize: Int) {
        when (action) {
            Constants.ShipAction.PLACE -> ++counter[shipSize - 2]
            Constants.ShipAction.ERASE -> --counter[shipSize - 2]
            else -> return
        }
    }

    private fun handleShipButtons(view: View, action: Constants.ShipAction, shipSize: Int) {
        var disableButton = false

        val shipIndex = shipSize - 2
        if (action == Constants.ShipAction.PLACE) {
            if (counter[shipIndex] >= maxLimit[shipIndex]) {
                counter[shipIndex] = maxLimit[shipIndex]
                disableButton = true
            }
        }

        val shipButton = when (shipIndex) {
            0 -> R.id.btn_two_ship
            1 -> R.id.btn_three_ship
            2 -> R.id.btn_four_ship
            3 -> R.id.btn_five_ship
            else -> return
        }
        val isEnabled = view.findViewById<Button>(shipButton).isEnabled

        if (disableButton) {
            if (isEnabled) view.findViewById<Button>(shipButton).isEnabled = false
        } else
            if (!isEnabled) view.findViewById<Button>(shipButton).isEnabled = true
    }

    private fun placeShip(shipSize: Int, isHorizontal: Boolean): Boolean {
        // Check if ship is not out of board.
        val border = if (isHorizontal)
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

            if (_cells[row][col].state == Constants.CellStates.SHIP) {
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
            _cells[row][col].state = state
            _cells[row][col].ship = ship
        }
        return true
    }

    private fun rotateShip() {
        val isHorizontal = isShipHorizontal()

        val startCell = getStartCell(isHorizontal)
        val shipSize = _cells[selectedRow][selectedCol].ship?.size ?: 0

        val oldPair = Pair(selectedRow, selectedCol)
        selectedRow = startCell.row
        selectedCol = startCell.col
        val oldCells = copyCells(_cells)
        eraseShip()

        if (!placeShip(shipSize, !isHorizontal)) {
            _cells = copyCells(oldCells)
        }

        selectedRow = oldPair.first
        selectedCol = oldPair.second
    }

    private fun getStartCell(isHorizontal: Boolean): Cell {
        // If current position is start of the boat, return current cell as start cell.
        val currCell = _cells[selectedRow][selectedCol]
        if(selectedRow - 1 == -1 || selectedCol - 1 == -1) return currCell

        var index = 0
        while (true) {
            val row = when {
                isHorizontal -> selectedRow
                else -> selectedRow - index
            }

            val col = when {
                !isHorizontal -> selectedCol
                else -> selectedCol - index
            }

            val cell = _cells[row][col]

            if (cell.ship != currCell.ship) {
                val startRow = if (isHorizontal) selectedRow else selectedRow - index + 1
                val startCol = if (isHorizontal) selectedCol - index + 1 else selectedCol
                val startCell = _cells[startRow][startCol]
                return Cell(startRow, startCol, startCell.state, startCell.ship)
            }

            ++index
        }
    }

    private fun eraseShip() {
        val isHorizontal = isShipHorizontal()

        val startCell = getStartCell(isHorizontal)
        val shipSize = _cells[selectedRow][selectedCol].ship?.size ?: 0

        val oldPair = Pair(selectedRow, selectedCol)
        selectedRow = startCell.row
        selectedCol = startCell.col

        // Clear all cells of shipSize with given state.
        for (i in 0 until shipSize) {
            val row = if (isHorizontal) selectedRow else selectedRow + i
            val col = if (isHorizontal) selectedCol + i else selectedCol

            _cells[row][col].state = Constants.CellStates.EMPTY
            _cells[row][col].ship = null
        }

        selectedRow = oldPair.first
        selectedCol = oldPair.second
    }

    private fun isShipHorizontal(): Boolean {
        val currCellShip = _cells[selectedRow][selectedCol].ship
        val resLeft =
            if(selectedCol - 1 == -1) false
            else _cells[selectedRow][selectedCol - 1].ship == currCellShip
        val resRight =
            if(selectedCol + 1 >= Constants.boardSideSize) false
            else _cells[selectedRow][selectedCol + 1].ship == currCellShip

        return resLeft || resRight
    }

    fun updateState(row: Int, col: Int, state: Constants.CellStates) {
        _cells[row][col].state = state
        cellsLiveData.postValue(_cells)
    }

    fun updateSelectedCell(row: Int, col: Int) {
        selectedRow = row
        selectedCol = col
        selectedCellLiveData.postValue(Pair(row, col))
    }
}