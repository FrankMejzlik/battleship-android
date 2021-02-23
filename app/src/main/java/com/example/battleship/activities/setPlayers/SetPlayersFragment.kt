package com.example.battleship.activities.setPlayers

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
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


    // Store players names
    private val txtPlayer1: TextInputLayout by lazy {
        txt_player1
    }

    private val txtPlayer2: TextInputLayout by lazy {
        txt_player2
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModelFactory = GameViewModelFactory(activity?.application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(GameViewModel::class.java)
        return inflater.inflate(R.layout.fragment_set_players, container, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Save names of players into current started instance from onSaveInstanceState
        if (savedInstanceState != null) {
            txtPlayer1.editText?.setText(savedInstanceState.getString(KEY_PLAYER1))
            txtPlayer2.editText?.setText(savedInstanceState.getString(KEY_PLAYER2))
        }

        btn_start.setOnClickListener {
            saveNames()
            Toast.makeText(activity, "Names were saved:" + readNames(), Toast.LENGTH_LONG).show()
            val name = Constants.Indices.FIRST //txtPlayer1.editText?.text.toString()
            val button = Constants.ButtonActions.PLACE //"Place"
            val intent = Intent(activity, MiddleScreenActivity::class.java)
            intent.putExtra(Constants.KEY_PLAYER_ID, name)
            intent.putExtra(Constants.KEY_BUTTON_ACT, button)
            startActivity(intent)
        }
    }

    private fun readNames(): String {
        //viewModel.loadNames()
        return viewModel.game.player1.getName(Constants.Indices.FIRST).value +
                viewModel.game.player2.getName(Constants.Indices.SECOND).value
    }

    private fun saveNames() {
        viewModel.game.currPlayer?.saveNames(
            txtPlayer1.editText?.text.toString(),
            txtPlayer2.editText?.text.toString()
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_PLAYER1, txtPlayer1.editText?.text.toString())
        outState.putString(KEY_PLAYER2, txtPlayer2.editText?.text.toString())
    }

    companion object {
        const val KEY_PLAYER1 = "player1"
        const val KEY_PLAYER2 = "player2"
    }
}