package com.example.battleship.viewModels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.battleship.Board
import com.example.battleship.Game
import com.example.battleship.Player
import com.example.battleship.utils.CellPair
import com.example.battleship.config.Constants

class GameViewModel() : ViewModel() {

    val game = Game()
}