package com.example.battleship.middleScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.battleship.Constants
import com.example.battleship.R
import kotlinx.android.synthetic.main.fragment_middle_screen.*
import java.io.Serializable

class MiddleScreenFragment: Fragment() {

    var playerName: String? = null
    var buttonAction: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_middle_screen, container, false)
    }

    override fun onStart() {
        super.onStart()
        playerName = arguments?.getSerializable(Constants.KEY_PLAYER_ID)?.toString()
        buttonAction = arguments?.getSerializable(Constants.KEY_BUTTON_ACT)?.toString()
        txt_player_name.text = playerName
        btn_action.text = buttonAction
    }

    companion object {

        // Factory method to create fragment instance. Framework requires empty default constructor.
        @JvmStatic
        fun newInstance(name: Serializable?, button: Serializable?): MiddleScreenFragment {
            val fragment = MiddleScreenFragment()
            fragment.arguments = Bundle().apply {
                putSerializable(Constants.KEY_PLAYER_ID, name)
                putSerializable(Constants.KEY_BUTTON_ACT, button)
            }
            return fragment
        }
    }
}