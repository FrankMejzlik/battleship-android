package com.example.battleship

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

class SetPlayersActivity : AppCompatActivity() {

    // Store players names
    private val txtPlayer1: TextInputLayout by lazy {
        findViewById<TextInputLayout>(R.id.txt_player1)
    }

    private val txtPlayer2: TextInputLayout by lazy {
        findViewById<TextInputLayout>(R.id.txt_player2)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_players)

        // Save names of players into current started instance from onSaveInstanceState
        if(savedInstanceState != null) {
            txtPlayer1.editText?.setText(savedInstanceState.getString(KEY_PLAYER1))
            txtPlayer2.editText?.setText(savedInstanceState.getString(KEY_PLAYER2))
        }


        findViewById<Button>(R.id.btn_start).setOnClickListener {
            // set listener (Toast)
            // and show
            Toast.makeText(this, "Game over", Toast.LENGTH_LONG).show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_PLAYER1, txtPlayer1.editText?.text.toString())
        outState.putString(KEY_PLAYER2, txtPlayer2.editText?.text.toString())
    }

    companion object {
        const val KEY_PLAYER1 = "player 1"
        const val KEY_PLAYER2 = "player 2"
    }
}