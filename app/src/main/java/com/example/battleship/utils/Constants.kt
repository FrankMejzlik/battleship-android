package com.example.battleship.utils

object Constants {

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
}