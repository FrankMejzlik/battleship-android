package com.example.battleship

object constants {
    const val fileNames = "playersNames"
    enum class Indices {
        FIRST, SECOND
    }
    public enum class ButtonActions(a: String) {
        PLACE("Place"), PLAY("Play"),
    }
}