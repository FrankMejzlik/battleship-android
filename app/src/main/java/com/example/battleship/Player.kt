package com.example.battleship

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.battleship.config.Constants

class Player() {

    //private var _player1Name = MutableLiveData<String>()
    private var _playerName = MutableLiveData<String>()
    private var _board = Board(Constants.boardSideSize * Constants.boardSideSize)

    fun getName(): LiveData<String> = _playerName

    fun setName(name: String) {
        _playerName.postValue(name)
    }

    fun getBoard() = _board
}