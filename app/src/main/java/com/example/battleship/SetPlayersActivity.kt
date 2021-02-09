package com.example.battleship

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SetPlayersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_main_play).setOnClickListener {
            // set listener (Toast)
            // and show
            Toast.makeText(this, "Game over", Toast.LENGTH_LONG).show()
        }
        }
}