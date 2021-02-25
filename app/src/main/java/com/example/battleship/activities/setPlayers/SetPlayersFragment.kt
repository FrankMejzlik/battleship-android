package com.example.battleship.activities.setPlayers

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.battleship.R
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_set_players.*
import com.example.battleship.config.Constants
import com.example.battleship.activities.middleScreen.MiddleScreenActivity
import com.example.battleship.viewModels.GameViewModel
import com.example.battleship.viewModels.GameViewModelFactory


class SetPlayersFragment : Fragment() {

    private lateinit var viewModel: GameViewModel
    private lateinit var viewModelFactory: GameViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val playerID = Constants.Indices.FIRST
        val state = Constants.GameStates.INPUT_NAMES

        viewModelFactory = GameViewModelFactory(activity?.application, state, playerID)
        viewModel = ViewModelProvider(this, viewModelFactory).get(GameViewModel::class.java)
        viewModel.game.player1.getName().observe(viewLifecycleOwner, Observer {
            updatePlayerName(Constants.Indices.FIRST, it)
        })
        viewModel.game.player2.getName().observe(viewLifecycleOwner, Observer {
            updatePlayerName(Constants.Indices.SECOND, it)
        })
        return inflater.inflate(R.layout.fragment_set_players, container, true)
    }

    private fun updatePlayerName(index: Constants.Indices, name: String) {
        when (index) {
            Constants.Indices.FIRST -> txt_player1.editText?.setText(name)
            Constants.Indices.SECOND -> txt_player2.editText?.setText(name)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btn_start.setOnClickListener {
            val name = Constants.Indices.FIRST //txtPlayer1.editText?.text.toString()
            val button = Constants.ButtonActions.PLACE //"Place"
            val intent = Intent(activity, MiddleScreenActivity::class.java)
            intent.putExtra(Constants.KEY_PLAYER_ID, name)
            intent.putExtra(Constants.KEY_BUTTON_ACT, button)
            startActivity(intent)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.game.player1.setName(txt_player1.editText?.text.toString())
        viewModel.game.player2.setName(txt_player2.editText?.text.toString())
    }

    override fun onStop() {
        super.onStop()
        viewModel.game.player1.setName(txt_player1.editText?.text.toString())
        viewModel.game.player2.setName(txt_player2.editText?.text.toString())
        viewModel.game.saveNames()
    }
}