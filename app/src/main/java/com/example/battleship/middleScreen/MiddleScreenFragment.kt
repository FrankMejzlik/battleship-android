package com.example.battleship.middleScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.battleship.PlayersNamesViewModel
import com.example.battleship.PlayersNamesViewModelFactory
import com.example.battleship.utils.Constants
import com.example.battleship.R
import kotlinx.android.synthetic.main.fragment_middle_screen.*
import java.io.Serializable

class MiddleScreenFragment: Fragment() {

    private lateinit var viewModel: PlayersNamesViewModel
    private lateinit var viewModelFactory: PlayersNamesViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModelFactory = PlayersNamesViewModelFactory(activity?.application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PlayersNamesViewModel::class.java)
        return inflater.inflate(R.layout.fragment_middle_screen, container, false)
    }

    override fun onStart() {
        super.onStart()

        viewModel.loadNames()

        val playerName = viewModel.getName(arguments?.getSerializable(Constants.KEY_PLAYER_ID) as Constants.Indices)
        val buttonAction = arguments?.getSerializable(Constants.KEY_BUTTON_ACT)?.toString()
        txt_player_name.text = playerName.value
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