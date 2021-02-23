package com.example.battleship

import android.app.Application
import com.example.battleship.config.Constants

class Game(val app: Application?) {
    var currPlayer: Player? = null
    var state = Constants.GameStates.INIT
    var player1 = Player(app)
    var player2 = Player(app)
}