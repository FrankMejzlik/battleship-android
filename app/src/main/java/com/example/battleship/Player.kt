package com.example.battleship

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class Player(app: Application?) {

    private var _playerName = MutableLiveData<String>()
    private var _myBoard = Board(app)
    private var _shootBoard = Board(app)

    fun getName(): LiveData<String> = _playerName

    fun setName(name: String) {
        _playerName.postValue(name)
    }

    fun getMyBoard() = _myBoard
    fun getShootBoard() = _shootBoard
}