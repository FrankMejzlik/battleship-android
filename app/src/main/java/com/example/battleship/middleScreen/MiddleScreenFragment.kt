package com.example.battleship.middleScreen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.battleship.playersNamesViewModel.PlayersNamesViewModel
import com.example.battleship.playersNamesViewModel.PlayersNamesViewModelFactory
import com.example.battleship.utils.Constants
import com.example.battleship.R
import com.example.battleship.placeShips.PlaceShipsActivity
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

        val playerID = arguments?.getSerializable(Constants.KEY_PLAYER_ID) as Constants.Indices
        val playerName = viewModel.getName(playerID)
        val buttonAction = arguments?.getSerializable(Constants.KEY_BUTTON_ACT) as Constants.ButtonActions
        val playerText = when(playerID){
            Constants.Indices.FIRST -> "First player: " + playerName.value
            Constants.Indices.SECOND -> "Second player: " + playerName.value
        }
        val buttonText = when(buttonAction) {
            Constants.ButtonActions.PLACE -> "Place ships"
            Constants.ButtonActions.PLAY -> "Play"
        }
        txt_player_name.text = playerText
        btn_action.text = buttonText

        btn_action.setOnClickListener {
            val intent = Intent(activity, PlaceShipsActivity::class.java)
            intent.putExtra(Constants.KEY_PLAYER_ID, playerID)
            startActivity(intent)
        }

    }

    companion object {

        // Factory method to create fragment instance. Framework requires empty default constructor.
        @JvmStatic
        fun newInstance(id: Serializable?, button: Serializable?): MiddleScreenFragment {
            val fragment = MiddleScreenFragment()
            fragment.arguments = Bundle().apply {
                putSerializable(Constants.KEY_PLAYER_ID, id)
                putSerializable(Constants.KEY_BUTTON_ACT, button)
            }
            return fragment
        }
    }
}