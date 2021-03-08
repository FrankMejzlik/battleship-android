package com.example.battleship

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class Player() {

    var myBoard = Board()
        private set
    var shootBoard = Board()
        private set
    private var _playerName = MutableLiveData<String>()

    fun getName(): LiveData<String> = _playerName

    fun setName(name: String) {
        _playerName.postValue(name)
    }
}