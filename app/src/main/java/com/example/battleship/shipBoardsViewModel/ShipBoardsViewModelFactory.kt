package com.example.battleship.shipBoardsViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ShipBoardsViewModelFactory(private val app: Application?) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShipBoardsViewModel::class.java)) {
            return ShipBoardsViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}