package com.example.battleship.activities.middleScreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.battleship.config.Constants
import com.example.battleship.R

class MiddleScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_middle_screen)
        val playerID = intent.getSerializableExtra(Constants.KEY_PLAYER_ID)
        val buttonAction = intent.getSerializableExtra(Constants.KEY_BUTTON_ACT)
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, MiddleScreenFragment.newInstance(playerID, buttonAction))
            .commit()
    }
}