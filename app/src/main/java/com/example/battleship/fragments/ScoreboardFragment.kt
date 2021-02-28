package com.example.battleship.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.battleship.MainActivity
import com.example.battleship.R
import com.example.battleship.viewModels.GameViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_middle_screen.*
import kotlinx.android.synthetic.main.fragment_scoreboard.*

class ScoreboardFragment : Fragment() {
    private lateinit var viewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = (activity as? MainActivity)?.getViewModel() ?: ViewModelProvider(this).get(
            GameViewModel::class.java
        )

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