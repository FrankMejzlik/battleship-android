package com.example.battleship.config

object Constants {

    const val ERR_MISSING_NAMES = "Missing names."
    const val ERR_MISSING_SHOOT_CELL = "No cell chosen."
    const val ERR_OUT_OF_BOARD = "Out of board."
    const val ERR_OTHER_SHIP = "Other ship is there."

    // Board size
    const val boardSideSize = 10

    // All states of game.
    enum class GameStates {
        INIT, INPUT_NAMES, P1_PLACE_SWITCH, P1_PLACE, P2_PLACE_SWITCH, P2_PLACE, P1_SWITCH, P1_SHOOT, P1_RES, P2_SWITCH, P2_SHOOT, P2_RES, P1_WIN, P2_WIN
    }

    // Indices of the players.
    enum class Indices {
        FIRST, SECOND
    }

    // Values of cells in the board.
    public enum class CellStates {
        EMPTY, SHIP, MISS, HIT
    }

    // Action of ships on the board.
    public enum class ShipAction {
        PLACE, ROTATE, ERASE, SHOOT
    }

    // Eroors
    public enum class Errors {
        OUT_OF_BOARD, OTHER_SHIP
    }
}