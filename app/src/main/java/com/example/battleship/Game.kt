package com.example.battleship

import android.app.Application
import android.content.Context
import com.example.battleship.config.Constants
import java.io.File

class Game(
    val app: Application?,
    val state: Constants.GameStates,
    val currPlayerID: Constants.Indices
) {
    var currPlayer: Player? = null

    var player1 = Player(app)
    var player2 = Player(app)

    init {
        // If names are not filled yet in the file, use empty strings.
        if (state != Constants.GameStates.INPUT_NAMES) {
            // Load names from file.
            val names = try {
                val file =
                    File(app?.applicationContext?.filesDir.toString() + "/" + Constants.fileNames)
                file.createNewFile()

                app?.applicationContext?.openFileInput(Constants.fileNames)?.bufferedReader()
                    ?.useLines { lines ->
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
                player1.setName(splittedNames[0])
                player2.setName(splittedNames[1])
            } else {
                player1.setName("")
                player2.setName("")
            }

            // Set current player.
            setCurrPlayer(currPlayerID)
        } else {
            player1.setName("")
            player2.setName("")
        }
    }

    private fun setCurrPlayer(index: Constants.Indices) {
        currPlayer = when (index) {
            Constants.Indices.FIRST -> player1
            Constants.Indices.SECOND -> player2
        }
    }

    fun saveNames() {
        try {
            app?.applicationContext?.openFileOutput(Constants.fileNames, Context.MODE_PRIVATE).use {
                it?.write(player1.getName().value?.toByteArray())
                it?.write("\n".toByteArray())
                it?.write(player2.getName().value?.toByteArray())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}