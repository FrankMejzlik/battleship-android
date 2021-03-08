package com.example.battleship.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.battleship.Game

class GameViewModel(app: Application) : AndroidViewModel(app) {

    val game = Game()
}