package com.example.battleship.activities.middleScreen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.battleship.config.Constants
import com.example.battleship.R
import com.example.battleship.activities.placeShips.PlaceShipsActivity
import com.example.battleship.viewModels.GameViewModel
import com.example.battleship.viewModels.GameViewModelFactory
import kotlinx.android.synthetic.main.fragment_middle_screen.*
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
        return inflater.inflate(R.layout.fragment_middle_screen, container, false)
    }

    override fun onStart() {
        super.onStart()

//        viewModel.game.currPlayer?.loadNames()

        //val playerID = arguments?.getSerializable(Constants.KEY_PLAYER_ID) as Constants.Indices
        //viewModel.game.setCurrPlayer(playerID)
        val playerName = viewModel.game.currPlayer?.getName()?.value ?: ""
        val buttonAction =
            arguments?.getSerializable(Constants.KEY_BUTTON_ACT) as Constants.ButtonActions
        val playerText = when (viewModel.game.currPlayerID) {
            Constants.Indices.FIRST -> "First player: $playerName"
            Constants.Indices.SECOND -> "Second player: $playerName"
        }
        val buttonText = when (buttonAction) {
            Constants.ButtonActions.PLACE -> "Place ships"
            Constants.ButtonActions.PLAY -> "Play"
        }
        txt_player_name.text = playerText
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