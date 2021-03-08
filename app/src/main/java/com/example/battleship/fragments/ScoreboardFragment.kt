package com.example.battleship.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.battleship.R
import com.example.battleship.viewModels.GameViewModel
import kotlinx.android.synthetic.main.fragment_scoreboard.*

class ScoreboardFragment : Fragment() {

    private val viewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.game.getCurrPlayer().getName().observe(viewLifecycleOwner, Observer {
            updatePlayerName(it)
        })

        return inflater.inflate(R.layout.fragment_scoreboard, container, false)
    }


    private fun updatePlayerName(
        name: String
    ) {
        txt_scoreboard_player_name.text = name
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btn_restart.setOnClickListener {
            val nextFrag = viewModel.game.step()
            it.findNavController().navigate(nextFrag)
        }
    }
}