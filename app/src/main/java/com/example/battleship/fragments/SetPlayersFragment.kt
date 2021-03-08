package com.example.battleship.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.battleship.R
import kotlinx.android.synthetic.main.fragment_set_players.*
import com.example.battleship.config.Constants
import com.example.battleship.viewModels.GameViewModel


class SetPlayersFragment : Fragment() {

    private val viewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.game.player1.getName().observe(viewLifecycleOwner, Observer {
            updatePlayerName(Constants.Indices.FIRST, it)
        })
        viewModel.game.player2.getName().observe(viewLifecycleOwner, Observer {
            updatePlayerName(Constants.Indices.SECOND, it)
        })

        // Reset boards.
        viewModel.game.resetBoards()

        return inflater.inflate(R.layout.fragment_set_players, container, false)
    }

    private fun updatePlayerName(index: Constants.Indices, name: String) {
        when (index) {
            Constants.Indices.FIRST -> txt_player1.editText?.setText(name)
            Constants.Indices.SECOND -> txt_player2.editText?.setText(name)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btn_start.setOnClickListener {
            if (txt_player1.editText?.text.toString()
                    .isEmpty() || txt_player2.editText?.text.toString().isEmpty()
            ) {
                Toast.makeText(
                    activity, Constants.ERR_MISSING_NAMES,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val nextFrag = viewModel.game.step()
                it.findNavController().navigate(nextFrag)
            }
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
    }
}