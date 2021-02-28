package com.example.battleship

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.battleship.viewModels.GameViewModel
import com.example.battleship.viewModels.GameViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: GameViewModel
    private lateinit var viewModelFactory: GameViewModelFactory


    fun getViewModel() = mainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModelFactory = GameViewModelFactory(this.application)
        mainViewModel = ViewModelProvider(this, viewModelFactory).get(GameViewModel::class.java)

    }

    // Suppress back button.
    override fun onBackPressed() {
        return
    }
}