package com.example.battleship.setPlayers

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.battleship.playersNames.PlayersNamesViewModel
import com.example.battleship.playersNames.PlayersNamesViewModelFactory
import com.example.battleship.R
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_set_players.*
import com.example.battleship.utils.Constants
import com.example.battleship.middleScreen.MiddleScreenActivity


class SetPlayersFragment : Fragment() {

    private lateinit var viewModel: PlayersNamesViewModel
    private lateinit var viewModelFactory: PlayersNamesViewModelFactory


    // Store players names
    private val txtPlayer1: TextInputLayout by lazy {
        txt_player1
    }

    private val txtPlayer2: TextInputLayout by lazy {
        txt_player2
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModelFactory = PlayersNamesViewModelFactory(activity?.application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PlayersNamesViewModel::class.java)
        return inflater.inflate(R.layout.fragment_set_players, container, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Save names of players into current started instance from onSaveInstanceState
        if(savedInstanceState != null) {
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
        viewModel.loadNames()
        return viewModel.getName(Constants.Indices.FIRST).value +
                viewModel.getName(Constants.Indices.SECOND).value
    }

    private fun saveNames() {
        viewModel.saveNames(txtPlayer1.editText?.text.toString(), txtPlayer2.editText?.text.toString())
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