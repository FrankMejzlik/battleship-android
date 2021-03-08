package com.example.battleship

import androidx.lifecycle.MutableLiveData
import com.example.battleship.config.BoardArray
import com.example.battleship.config.Constants
import com.example.battleship.config.CellPair

class Board() {

    var selectedCellLiveData = MutableLiveData<CellPair>()
        private set
    var cellsLiveData = MutableLiveData<BoardArray>()
        private set

    private var _selectedRow = -1
    private var _selectedCol = -1

    private var _cells: BoardArray

    // Counter of number of placed ships.
    private var _counter = Array(4) { 0 }

    // Maximum limit of ships for each type
    private var _maxLimit = arrayOf(4, 3, 2, 1)

    init {
        val initCells = BoardArray(Constants.boardSideSize) { i ->
            Array(Constants.boardSideSize) { j -> Cell(i, j, Constants.CellStates.EMPTY, null) }
        }

        this._cells = initCells

        selectedCellLiveData.postValue(Pair(_selectedRow, _selectedCol))
        cellsLiveData.postValue(_cells)
    }

    fun resetBoard() {
        val initCells = BoardArray(Constants.boardSideSize) { i ->
            Array(Constants.boardSideSize) { j -> Cell(i, j, Constants.CellStates.EMPTY, null) }
        }

        this._cells = initCells

        selectedCellLiveData.postValue(Pair(-1, -1))
        cellsLiveData.postValue(initCells)
    }

    fun getCell(row: Int, col: Int): Cell? {
        if (row < 0 || row > Constants.boardSideSize - 1) return null
        if (col < 0 || col > Constants.boardSideSize - 1) return null
        return _cells[row][col]
    }

    fun updateState(row: Int, col: Int, state: Constants.CellStates) {
        _cells[row][col].state = state
        cellsLiveData.postValue(_cells)
    }

    fun updateSelectedCell(row: Int, col: Int) {
        _selectedRow = row
        _selectedCol = col
        selectedCellLiveData.postValue(Pair(row, col))
    }

    /*
    * Returns:
    * -bool: -true if max limit of the placed ships is exceeded
    *        -false if max limit of the placed ships is not exceeded
    * -size of ship
    * -error message while placing ship
    * */
    fun handleInput(
        inputShipSize: Int,
        action: Constants.ShipAction
    ): Triple<Boolean, Int, String> {
        var shipSize = inputShipSize
        var errMsg = ""
        if (_selectedRow == -1 || _selectedCol == -1) return Triple(true, shipSize, errMsg)

        var isExceeded = false

        val cellState = _cells[_selectedRow][_selectedCol].state

        when (action) {
            Constants.ShipAction.PLACE -> {
                val (isSuccess, errorMessage) = placeShip(shipSize, true)
                if (isSuccess) {
                    handleCounter(Constants.ShipAction.PLACE, shipSize)
                    isExceeded = handleShipLimit(Constants.ShipAction.PLACE, shipSize)
                } else errMsg = errorMessage

            }
            Constants.ShipAction.ROTATE -> if (cellState == Constants.CellStates.SHIP) errMsg =
                rotateShip()
            Constants.ShipAction.ERASE -> {
                if (cellState == Constants.CellStates.SHIP) {
                    shipSize = _cells[_selectedRow][_selectedCol].ship?.size ?: 0
                    eraseShip()
                    handleCounter(Constants.ShipAction.ERASE, shipSize)
                    isExceeded = handleShipLimit(Constants.ShipAction.ERASE, shipSize)
                }
            }
            Constants.ShipAction.SHOOT -> handleShoot()
        }

        cellsLiveData.postValue(_cells)
        return Triple(isExceeded, shipSize, errMsg)
    }

    private fun handleShoot() {
        val shotCell = _cells[_selectedRow][_selectedCol]
        when (shotCell.state) {
            Constants.CellStates.SHIP -> _cells[_selectedRow][_selectedCol].state =
                Constants.CellStates.HIT
            Constants.CellStates.HIT -> _cells[_selectedRow][_selectedCol].state =
                Constants.CellStates.HIT
            else -> _cells[_selectedRow][_selectedCol].state = Constants.CellStates.MISS
        }
    }

    private fun handleCounter(action: Constants.ShipAction, shipSize: Int) {
        when (action) {
            Constants.ShipAction.PLACE -> ++_counter[shipSize - 2]
            Constants.ShipAction.ERASE -> --_counter[shipSize - 2]
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
            if (_counter[shipIndex] >= _maxLimit[shipIndex]) {
                _counter[shipIndex] = _maxLimit[shipIndex]
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
            Constants.boardSideSize - _selectedCol
        else
            Constants.boardSideSize - _selectedRow

        if (border < shipSize) {
            return Pair(false, Constants.ERR_OUT_OF_BOARD)
        }

        // Check if cells are occupied by other ship.
        for (i in 0 until shipSize) {
            val row = if (isHorizontal) _selectedRow else _selectedRow + i
            val col = if (isHorizontal) _selectedCol + i else _selectedCol

            if (_cells[row][col].state == Constants.CellStates.SHIP) {
                return Pair(false, Constants.ERR_OTHER_SHIP)
            }
        }

        // Set all cells of shipSize with given state and given type of ship.
        val ship = Ship(shipSize)
        for (i in 0 until shipSize) {
            val row = if (isHorizontal) _selectedRow else _selectedRow + i
            val col = if (isHorizontal) _selectedCol + i else _selectedCol
            val state = Constants.CellStates.SHIP
            _cells[row][col].state = state
            _cells[row][col].ship = ship
        }
        return Pair(true, "")
    }

    private fun rotateShip(): String {
        val isHorizontal = isShipHorizontal()

        val startCell = getStartCell(isHorizontal)
        val shipSize = _cells[_selectedRow][_selectedCol].ship?.size ?: 0

        val oldPair = Pair(_selectedRow, _selectedCol)
        _selectedRow = startCell.row
        _selectedCol = startCell.col
        val oldCells = copyCells(_cells)
        eraseShip()

        val (isSuccess, errMsg) = placeShip(shipSize, !isHorizontal)
        if (!isSuccess) {
            _cells = copyCells(oldCells)
        }

        _selectedRow = oldPair.first
        _selectedCol = oldPair.second

        return errMsg
    }

    private fun getStartCell(isHorizontal: Boolean): Cell {
        // If current position is start of the boat, return current cell as start cell.
        val currCell = _cells[_selectedRow][_selectedCol]
        if (isHorizontal) {
            if (_selectedCol - 1 == -1)
                return currCell
        } else
            if (_selectedRow - 1 == -1)
                return currCell

        var index = 0
        var isEndOfShip = false
        while (true) {
            val row = when {
                isHorizontal -> _selectedRow
                else -> _selectedRow - index
            }

            val col = when {
                !isHorizontal -> _selectedCol
                else -> _selectedCol - index
            }

            if (col == -1 || row == -1) isEndOfShip = true

            if (!isEndOfShip) {
                val cell = _cells[row][col]
                if (cell.ship != currCell.ship) isEndOfShip = true
            }

            if (isEndOfShip) {
                val startRow = if (isHorizontal) _selectedRow else _selectedRow - index + 1
                val startCol = if (isHorizontal) _selectedCol - index + 1 else _selectedCol
                val startCell = _cells[startRow][startCol]
                return Cell(startRow, startCol, startCell.state, startCell.ship)
            }

            ++index
        }
    }

    private fun eraseShip() {
        val isHorizontal = isShipHorizontal()

        val startCell = getStartCell(isHorizontal)
        val shipSize = _cells[_selectedRow][_selectedCol].ship?.size ?: 0

        val oldPair = Pair(_selectedRow, _selectedCol)
        _selectedRow = startCell.row
        _selectedCol = startCell.col

        // Clear all cells of shipSize with given state.
        for (i in 0 until shipSize) {
            val row = if (isHorizontal) _selectedRow else _selectedRow + i
            val col = if (isHorizontal) _selectedCol + i else _selectedCol

            _cells[row][col].state = Constants.CellStates.EMPTY
            _cells[row][col].ship = null
        }

        _selectedRow = oldPair.first
        _selectedCol = oldPair.second
    }

    private fun isShipHorizontal(): Boolean {
        val currCellShip = _cells[_selectedRow][_selectedCol].ship
        val resLeft =
            if (_selectedCol - 1 == -1) false
            else _cells[_selectedRow][_selectedCol - 1].ship == currCellShip
        val resRight =
            if (_selectedCol + 1 >= Constants.boardSideSize) false
            else _cells[_selectedRow][_selectedCol + 1].ship == currCellShip

        return resLeft || resRight
    }

    private fun copyCells(cells: BoardArray): BoardArray {
        return BoardArray(Constants.boardSideSize) { i ->
            Array(Constants.boardSideSize) { j ->
                Cell(i, j, cells[i][j].state, cells[i][j].ship)
            }
        }
    }
}