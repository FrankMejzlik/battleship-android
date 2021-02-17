package com.example.battleship.placeShips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.battleship.R
import com.example.battleship.middleScreen.MiddleScreenFragment
import com.example.battleship.utils.Constants
import java.io.Serializable

class PlaceShipsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_place_ships, container, false)
    }

    companion object {

        // Factory method to create fragment instance. Framework requires empty default constructor.
        @JvmStatic
        fun newInstance(id: Serializable?): PlaceShipsFragment {
            val fragment = PlaceShipsFragment()
            fragment.arguments = Bundle().apply {
                putSerializable(Constants.KEY_PLAYER_ID, id)
            }
            return fragment
        }
    }
}