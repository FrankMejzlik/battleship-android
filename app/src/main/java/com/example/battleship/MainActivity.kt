package com.example.battleship

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.battleship.viewModels.GameViewModel

class MainActivity : AppCompatActivity() {

    lateinit var mainViewModel: GameViewModel
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProvider(this).get(GameViewModel::class.java)
    }

    // Suppress back button.
    override fun onBackPressed() {
        return
    }
}