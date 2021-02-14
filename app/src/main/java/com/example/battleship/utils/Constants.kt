package com.example.battleship.utils

object Constants {
    // Name of the file where names of players are stored.
    const val fileNames = "playersNames"

    // Name of the key for index of the player. (for bundles in intents)
    const val KEY_PLAYER_ID = "playerID"
    // Name of the key for action of the button. (for bundles in intents)
    const val KEY_BUTTON_ACT = "buttonAct"

    // Indices of the players.
    enum class Indices {
        FIRST, SECOND
    }
    // Actions of the buttons in the middle screen.
    public enum class ButtonActions(a: String) {
        PLACE("Place"), PLAY("Play"),
    }
}