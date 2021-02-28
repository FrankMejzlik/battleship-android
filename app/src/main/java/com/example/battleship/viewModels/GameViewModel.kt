package com.example.battleship.viewModels

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.battleship.Game

class GameViewModel(private val app: Application?) : ViewModel() {

    val game = Game(app)
}