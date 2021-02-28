package com.example.battleship.config

object Constants {

    const val ERR_MISSING_NAMES = "Missing names."

    // Name of the file where names of players are stored.
    const val fileNames = "playersNames"

    // Names of files where ship boards are stored.
    const val filePlayer1Ships = "player1Ships"
    const val filePlayer2Ships = "player2Ships"

    // Name of the key for index of the player. (for bundles in intents)
    const val KEY_PLAYER_ID = "playerID"

    // Name of the key for action of the button. (for bundles in intents)
    const val KEY_BUTTON_ACT = "buttonAct"

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

    // Actions of the buttons in the middle screen.
    public enum class ButtonActions {
        PLACE, PLAY
    }

    // Values of cells in the board.
    public enum class CellStates {
        EMPTY, SHIP, MISS, HIT
    }

    // Action of ships on the board.
    public enum class ShipAction {
        PLACE, ROTATE, ERASE, SHOOT
    }

}