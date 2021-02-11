package com.example.battleship.middleScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.battleship.R
import kotlinx.android.synthetic.main.fragment_middle_screen.*

class MiddleScreenFragment: Fragment() {

    var playerName: String? = null
    var buttonAction: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_middle_screen, container, false)
    }

    override fun onStart() {
        super.onStart()
        playerName = arguments?.getString(KEY_NAME).orEmpty()
        buttonAction = arguments?.getString(KEY_BUTTON).orEmpty()
        txt_player_name.text = playerName
        btn_action.text = buttonAction
    }

    companion object {
        var KEY_NAME = "playerName"
        var KEY_BUTTON = "buttonAction"

        // Factory method to create fragment instance. Framework requires empty default constructor.
        @JvmStatic
        fun newInstance(name: String, button: String): MiddleScreenFragment {
            val fragment = MiddleScreenFragment()
            fragment.arguments = Bundle().apply {
                putString(KEY_NAME, name)
                putString(KEY_BUTTON, button)
            }
            return fragment
        }
    }
}