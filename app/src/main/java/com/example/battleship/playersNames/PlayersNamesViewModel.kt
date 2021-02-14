package com.example.battleship.playersNames

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.battleship.playersNames.PlayersNames
import com.example.battleship.utils.Constants

class PlayersNamesViewModel(app: Application?): ViewModel() {

    private val playersNames: PlayersNames by lazy { PlayersNames(app) }

    private val _player1Name = MutableLiveData<String>()

    private val _player2Name = MutableLiveData<String>()

    fun getName(index: Constants.Indices): LiveData<String> {
        return when(index){
            Constants.Indices.FIRST -> _player1Name
            Constants.Indices.SECOND -> _player2Name
        }
    }

    fun loadNames() {
        _player1Name.value = playersNames.getName(Constants.Indices.FIRST)
        _player2Name.value = playersNames.getName(Constants.Indices.SECOND)
    }

    fun saveNames(name1: String, name2: String) {
        playersNames.saveNames(name1, name2)
    }

}