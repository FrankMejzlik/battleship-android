package com.example.battleship

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.battleship.viewModels.GameViewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<GameViewModel>()

    fun getViewModel() = mainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        return
    }
}