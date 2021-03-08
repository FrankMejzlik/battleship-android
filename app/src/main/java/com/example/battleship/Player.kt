package com.example.battleship

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class Player() {

    private var _playerName = MutableLiveData<String>()
    private var _myBoard = Board()
    private var _shootBoard = Board()

    fun getName(): LiveData<String> = _playerName

    fun setName(name: String) {
        _playerName.postValue(name)
    }

    fun getMyBoard() = _myBoard
    fun getShootBoard() = _shootBoard
}