package com.example.battleship

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
//import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get reference to the main button play.
        findViewById<Button>(R.id.btn_main_play).setOnClickListener {
            // Open to SetPlayersActivity.
            val intent = Intent(this, SetPlayersActivity::class.java)
            // Start activity.
            startActivity(intent)
        }
    }
}