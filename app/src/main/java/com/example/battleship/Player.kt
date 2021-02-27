package com.example.battleship

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.battleship.config.Constants

class Player() {

    //private var _player1Name = MutableLiveData<String>()
    private var _playerName = MutableLiveData<String>()
    private var _myBoard = Board(Constants.boardSideSize * Constants.boardSideSize)
    private var _shootBoard = Board(Constants.boardSideSize * Constants.boardSideSize)

    fun getName(): LiveData<String> = _playerName

    fun setName(name: String) {
        _playerName.postValue(name)
    }

    fun getMyBoard() = _myBoard
    fun getShootBoard() = _shootBoard
}