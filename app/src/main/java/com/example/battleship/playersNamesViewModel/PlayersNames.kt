package com.example.battleship.playersNamesViewModel

import android.app.Application
import com.example.battleship.utils.Constants
import android.content.Context
import java.io.File

class PlayersNames(application: Application?) {

    private var _player1Name = ""
    private var _player2Name = ""
    private val app = application

    init {
        val names = try {
            val file = File(app?.applicationContext?.filesDir.toString() + "/" + Constants.fileNames)
            file.createNewFile()

            app?.applicationContext?.openFileInput(Constants.fileNames)?.bufferedReader()?.useLines { lines ->
                lines.fold("") { some, text ->
                    "$some\n$text"
                }
            }.toString()
        } catch (e: Exception) {
            e.printStackTrace().toString()
        }
        val splittedNames = names.split('\n').filter {
            it.isNotEmpty()
        }

        if (splittedNames.size >= 2) {
            _player1Name = splittedNames[0]
            _player2Name = splittedNames[1]
        }

    }

    fun getName(index: Constants.Indices): String {
        return when (index) {
            Constants.Indices.FIRST -> _player1Name
            Constants.Indices.SECOND -> _player2Name
        }
    }

    fun saveNames(name1: String, name2: String) {
        try {
            app?.applicationContext?.openFileOutput(Constants.fileNames, Context.MODE_PRIVATE).use {
                it?.write(name1.toByteArray())
                it?.write("\n".toByteArray())
                it?.write(name2.toByteArray())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        _player1Name = name1
        _player2Name = name2
    }
}