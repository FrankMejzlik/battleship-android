package com.example.battleship.placeShips

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.battleship.R
import com.example.battleship.utils.Constants

class PlaceShipsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_ships)
        val playerID = intent.getSerializableExtra(Constants.KEY_PLAYER_ID)
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, PlaceShipsFragment.newInstance(playerID)).commit()
    }
}