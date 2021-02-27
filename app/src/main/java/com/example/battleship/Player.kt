package com.example.battleship

import android.app.Application
import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.battleship.utils.BoardArray
import com.example.battleship.utils.CellPair
import com.example.battleship.config.Constants
import java.io.File

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