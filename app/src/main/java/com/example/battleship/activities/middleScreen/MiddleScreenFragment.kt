package com.example.battleship.activities.middleScreen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.battleship.config.Constants
import com.example.battleship.R
import com.example.battleship.activities.placeShips.PlaceShipsActivity
import com.example.battleship.viewModels.GameViewModel
import com.example.battleship.viewModels.GameViewModelFactory
import kotlinx.android.synthetic.main.fragment_middle_screen.*
import kotlinx.android.synthetic.main.fragment_place_ships.*
import kotlinx.android.synthetic.main.fragment_set_players.*
import java.io.Serializable

class MiddleScreenFragment : Fragment() {

    private lateinit var viewModel: GameViewModel
    private lateinit var viewModelFactory: GameViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val playerID = arguments?.getSerializable(Constants.KEY_PLAYER_ID) as Constants.Indices
        val state = Constants.GameStates.PL_SWITCH
        viewModelFactory = GameViewModelFactory(activity?.application, state, playerID)
        viewModel = ViewModelProvider(this, viewModelFactory).get(GameViewModel::class.java)
        viewModel.game.currPlayer?.getName()?.observe(viewLifecycleOwner, Observer {
            updatePlayerName(it)
        })
        return inflater.inflate(R.layout.fragment_middle_screen, container, false)
    }

    private fun updatePlayerName(
        name: String
    ) {
        txt_player_name.text = name
    }

    override fun onStart() {
        super.onStart()

        val buttonText =
            when (arguments?.getSerializable(Constants.KEY_BUTTON_ACT) as Constants.ButtonActions) {
                Constants.ButtonActions.PLACE -> "Place ships"
                Constants.ButtonActions.PLAY -> "Play"
            }
        btn_action.text = buttonText

        btn_action.setOnClickListener {
            val intent = Intent(activity, PlaceShipsActivity::class.java)
            intent.putExtra(Constants.KEY_PLAYER_ID, viewModel.game.currPlayerID)
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