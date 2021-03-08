package com.example.battleship

import androidx.lifecycle.MutableLiveData
import com.example.battleship.config.BoardArray
import com.example.battleship.config.Constants
import com.example.battleship.config.CellPair

class Board() {

    var selectedCellLiveData = MutableLiveData<CellPair>()
    var cellsLiveData = MutableLiveData<BoardArray>()

    private var selectedRow = -1
    private var selectedCol = -1

    var cells: BoardArray

    // Counter of number of placed ships.
    var counter = Array(4) { 0 }

    // Maximum limit of ships for each type
    var maxLimit = arrayOf(4, 3, 2, 1)

    init {
        val initCells = BoardArray(Constants.boardSideSize) { i ->
            Array(Constants.boardSideSize) { j -> Cell(i, j, Constants.CellStates.EMPTY, null) }
        }

        this.cells = initCells

        selectedCellLiveData.postValue(Pair(selectedRow, selectedCol))
        cellsLiveData.postValue(cells)
    }

    fun resetBoard() {
        val initCells = BoardArray(Constants.boardSideSize) { i ->
            Array(Constants.boardSideSize) { j -> Cell(i, j, Constants.CellStates.EMPTY, null) }
        }

        this.cells = initCells

        selectedCellLiveData.postValue(Pair(-1, -1))
        cellsLiveData.postValue(initCells)
    }

    fun getCell(row: Int, col: Int): Cell? {
        if (row < 0 || row > Constants.boardSideSize - 1) return null
        if (col < 0 || col > Constants.boardSideSize - 1) return null
        return cells[row][col]
    }

    private fun copyCells(cells: BoardArray): BoardArray {
        return BoardArray(Constants.boardSideSize) { i ->
            Array(Constants.boardSideSize) { j ->
                Cell(i, j, cells[i][j].state, cells[i][j].ship)
            }
        }
    }

    /*
    * Returns:
    * -bool: -true if max limit of the placed ships is exceeded
    *        -false if max limit of the placed ships is not exceeded
    * -size of ship
    * -error message while placing ship
    * */
    fun handleInput(inputShipSize: Int, action: Constants.ShipAction): Triple<Boolean, Int, String> {
        var shipSize = inputShipSize
        var errMsg = ""
        if (selectedRow == -1 || selectedCol == -1) return Triple(true, shipSize, errMsg)

        var isExceeded = false

        val cellState = cells[selectedRow][selectedCol].state

        when (action) {
            Constants.ShipAction.PLACE -> {
                val (isSuccess, errorMessage) = placeShip(shipSize, true)
                if (isSuccess) {
                    handleCounter(Constants.ShipAction.PLACE, shipSize)
                    isExceeded = handleShipLimit(Constants.ShipAction.PLACE, shipSize)
                } else errMsg = errorMessage

            }
            Constants.ShipAction.ROTATE -> if (cellState == Constants.CellStates.SHIP) errMsg = rotateShip()
            Constants.ShipAction.ERASE -> {
                if (cellState == Constants.CellStates.SHIP) {
                    shipSize = cells[selectedRow][selectedCol].ship?.size ?: 0
                    eraseShip()
                    handleCounter(Constants.ShipAction.ERASE, shipSize)
                    isExceeded = handleShipLimit(Constants.ShipAction.ERASE, shipSize)
                }
            }
            Constants.ShipAction.SHOOT -> handleShoot()
        }

        cellsLiveData.postValue(cells)
        return Triple(isExceeded, shipSize, errMsg)
    }

    private fun handleShoot() {
        val shotCell = cells[selectedRow][selectedCol]
        when (shotCell.state) {
            Constants.CellStates.SHIP -> cells[selectedRow][selectedCol].state =
                Constants.CellStates.HIT
            Constants.CellStates.HIT -> cells[selectedRow][selectedCol].state =
                Constants.CellStates.HIT
            else -> cells[selectedRow][selectedCol].state = Constants.CellStates.MISS
        }
    }

    private fun handleCounter(action: Constants.ShipAction, shipSize: Int) {
        when (action) {
            Constants.ShipAction.PLACE -> ++counter[shipSize - 2]
            Constants.ShipAction.ERASE -> --counter[shipSize - 2]
            else -> return
        }
    }

    /*
    * Returns:
    * true if max limit of the placed ships is exceeded
    * false if max limit of the placed ships is not exceeded
    * */
    private fun handleShipLimit(action: Constants.ShipAction, shipSize: Int): Boolean {
        var isExceeded = false

        val shipIndex = shipSize - 2
        if (action == Constants.ShipAction.PLACE) {
            if (counter[shipIndex] >= maxLimit[shipIndex]) {
                counter[shipIndex] = maxLimit[shipIndex]
                isExceeded = true
            }
        }

        return isExceeded
    }

    /*
    * Returns:
    * - if placement was successful
    * - error message, if the placement was not successful
    * */
    private fun placeShip(shipSize: Int, isHorizontal: Boolean): Pair<Boolean, String> {
        // Check if ship is not out of board.
        val border = if (isHorizontal)
            Constants.boardSideSize - selectedCol
        else
            Constants.boardSideSize - selectedRow

        if (border < shipSize) {
            return Pair(false, Constants.ERR_OUT_OF_BOARD)
        }

        // Check if cells are occupied by other ship.
        for (i in 0 until shipSize) {
            val row = if (isHorizontal) selectedRow else selectedRow + i
            val col = if (isHorizontal) selectedCol + i else selectedCol

            if (cells[row][col].state == Constants.CellStates.SHIP) {
                return Pair(false, Constants.ERR_OTHER_SHIP)
            }
        }

        // Set all cells of shipSize with given state and given type of ship.
        val ship = Ship(shipSize)
        for (i in 0 until shipSize) {
            val row = if (isHorizontal) selectedRow else selectedRow + i
            val col = if (isHorizontal) selectedCol + i else selectedCol
            val state = Constants.CellStates.SHIP
            cells[row][col].state = state
            cells[row][col].ship = ship
        }
        return Pair(true, "")
    }

    private fun rotateShip(): String {
        val isHorizontal = isShipHorizontal()

        val startCell = getStartCell(isHorizontal)
        val shipSize = cells[selectedRow][selectedCol].ship?.size ?: 0

        val oldPair = Pair(selectedRow, selectedCol)
        selectedRow = startCell.row
        selectedCol = startCell.col
        val oldCells = copyCells(cells)
        eraseShip()

        val (isSuccess, errMsg) = placeShip(shipSize, !isHorizontal)
        if (!isSuccess) {
            cells = copyCells(oldCells)
        }

        selectedRow = oldPair.first
        selectedCol = oldPair.second

        return errMsg
    }

    private fun getStartCell(isHorizontal: Boolean): Cell {
        // If current position is start of the boat, return current cell as start cell.
        val currCell = cells[selectedRow][selectedCol]
        if (isHorizontal) {
            if (selectedCol - 1 == -1)
                return currCell
        } else
            if (selectedRow - 1 == -1)
                return currCell

        var index = 0
        var isEndOfShip = false
        while (true) {
            val row = when {
                isHorizontal -> selectedRow
                else -> selectedRow - index
            }

            val col = when {
                !isHorizontal -> selectedCol
                else -> selectedCol - index
            }

            if (col == -1 || row == -1) isEndOfShip = true

            if (!isEndOfShip) {
                val cell = cells[row][col]
                if (cell.ship != currCell.ship) isEndOfShip = true
            }

            if (isEndOfShip) {
                val startRow = if (isHorizontal) selectedRow else selectedRow - index + 1
                val startCol = if (isHorizontal) selectedCol - index + 1 else selectedCol
                val startCell = cells[startRow][startCol]
                return Cell(startRow, startCol, startCell.state, startCell.ship)
            }

            ++index
        }
    }

    private fun eraseShip() {
        val isHorizontal = isShipHorizontal()

        val startCell = getStartCell(isHorizontal)
        val shipSize = cells[selectedRow][selectedCol].ship?.size ?: 0

        val oldPair = Pair(selectedRow, selectedCol)
        selectedRow = startCell.row
        selectedCol = startCell.col

        // Clear all cells of shipSize with given state.
        for (i in 0 until shipSize) {
            val row = if (isHorizontal) selectedRow else selectedRow + i
            val col = if (isHorizontal) selectedCol + i else selectedCol

            cells[row][col].state = Constants.CellStates.EMPTY
            cells[row][col].ship = null
        }

        selectedRow = oldPair.first
        selectedCol = oldPair.second
    }

    private fun isShipHorizontal(): Boolean {
        val currCellShip = cells[selectedRow][selectedCol].ship
        val resLeft =
            if (selectedCol - 1 == -1) false
            else cells[selectedRow][selectedCol - 1].ship == currCellShip
        val resRight =
            if (selectedCol + 1 >= Constants.boardSideSize) false
            else cells[selectedRow][selectedCol + 1].ship == currCellShip

        return resLeft || resRight
    }

    fun updateState(row: Int, col: Int, state: Constants.CellStates) {
        cells[row][col].state = state
        cellsLiveData.postValue(cells)
    }

    fun updateSelectedCell(row: Int, col: Int) {
        selectedRow = row
        selectedCol = col
        selectedCellLiveData.postValue(Pair(row, col))
    }
}