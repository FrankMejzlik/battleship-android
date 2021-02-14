package com.example.battleship.middleScreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.battleship.utils.Constants
import com.example.battleship.R

class MiddleScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_middle_screen)
        val playerName = intent.getSerializableExtra(Constants.KEY_PLAYER_ID)//intent.getStringExtra(MiddleScreenFragment.KEY_NAME).orEmpty()
        val buttonAction = intent.getSerializableExtra(Constants.KEY_BUTTON_ACT)//intent.getStringExtra(MiddleScreenFragment.KEY_BUTTON).orEmpty()
        supportFragmentManager.beginTransaction().add(R.id.fragment_container, MiddleScreenFragment.newInstance(playerName, buttonAction)).commit()
    }
}