package com.example.battleship

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // get reference to the main button play
        // set listener (Toast)
        // and show
        findViewById<Button>(R.id.btn_main_play).setOnClickListener {
            Toast.makeText(this, "Game over", Toast.LENGTH_LONG).show()
        }
    }
}