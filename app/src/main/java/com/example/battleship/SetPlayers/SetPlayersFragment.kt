package com.example.battleship.setPlayers

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.battleship.R
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_set_players.*
import java.io.IOException

const val fileNames = "playersNames"

class SetPlayersFragment : Fragment() {
    // Store players names
    private val txtPlayer1: TextInputLayout by lazy {
        txt_player1
    }

    private val txtPlayer2: TextInputLayout by lazy {
        txt_player2
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_set_players, container, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Save names of players into current started instance from onSaveInstanceState
        if(savedInstanceState != null) {
            txtPlayer1.editText?.setText(savedInstanceState.getString(KEY_PLAYER1))
            txtPlayer2.editText?.setText(savedInstanceState.getString(KEY_PLAYER2))
        }

        btn_start.setOnClickListener {
            // set listener (Toast)
            // and show
            Toast.makeText(activity, readNames(), Toast.LENGTH_LONG).show()
            saveNames()
        }
    }

    private fun readNames(): String {
        return try {
            context?.openFileInput(fileNames)?.bufferedReader()?.useLines { lines ->
                lines.fold("") { some, text ->
                    "$some\n$text"
                }
            }.toString()
        }
        catch (e: Exception) {
            e.printStackTrace().toString()
        }
    }

    private fun saveNames() {
        try {
            context?.openFileOutput(fileNames, Context.MODE_PRIVATE).use {
                it?.write(txtPlayer1.editText?.text.toString().toByteArray())
                it?.write(txtPlayer2.editText?.text.toString().toByteArray())
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_PLAYER1, txtPlayer1.editText?.text.toString())
        outState.putString(KEY_PLAYER2, txtPlayer2.editText?.text.toString())
    }

    companion object {
        const val KEY_PLAYER1 = "player1"
        const val KEY_PLAYER2 = "player2"
    }
}