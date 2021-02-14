package com.example.battleship.playersNamesViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PlayersNamesViewModelFactory(private val app: Application?): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayersNamesViewModel::class.java)) {
            return PlayersNamesViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}