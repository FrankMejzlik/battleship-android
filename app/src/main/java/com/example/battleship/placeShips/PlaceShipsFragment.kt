package com.example.battleship.placeShips

import android.app.ActionBar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.example.battleship.R
import com.example.battleship.middleScreen.MiddleScreenFragment
import com.example.battleship.utils.Constants
import kotlinx.android.synthetic.main.fragment_place_ships.*
import java.io.Serializable

class PlaceShipsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_place_ships, container, false)
    }

    override fun onStart() {
        super.onStart()

        // Creating buttons for ship board.
//        var index = 0
//        for (i in 0..9) {
//            for (j in 0..9) {
//                var button = Button(context)
//                button.id = index
//                button.layoutParams = LinearLayout.LayoutParams(ConstraintLayout.LayoutParams(3,3))
//                button.text = index.toString()
//                //button.minHeight = 0
//                //button.minWidth = 0
//                gl_board.addView(button)
//                ++index
//            }
//        }

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