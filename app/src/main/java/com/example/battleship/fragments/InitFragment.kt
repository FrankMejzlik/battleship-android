package com.example.battleship.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.battleship.MainActivity
import com.example.battleship.R
import com.example.battleship.viewModels.GameViewModel
import kotlinx.android.synthetic.main.fragment_main.*

class InitFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btn_main_play.setOnClickListener {
            val viewModel =
                (activity as? MainActivity)?.getViewModel() ?: ViewModelProvider(this).get(
                    GameViewModel::class.java
                )
            val nextFrag = viewModel.game.step()
            it.findNavController().navigate(nextFrag)
        }
    }
}