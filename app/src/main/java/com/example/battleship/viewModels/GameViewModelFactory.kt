package com.example.battleship.viewModels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.battleship.config.Constants

class GameViewModelFactory(
    private val app: Application?,
    private val state: Constants.GameStates,
    private val currPlayerID: Constants.Indices
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(app, state, currPlayerID) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}