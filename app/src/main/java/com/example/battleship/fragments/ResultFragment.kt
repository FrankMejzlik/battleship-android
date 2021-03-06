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
import com.example.battleship.config.BoardArray
import com.example.battleship.config.CellPair
import com.example.battleship.viewModels.GameViewModel
import kotlinx.android.synthetic.main.fragment_result.*

class ResultFragment : Fragment() {

    private val viewModel: GameViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.game.getCurrPlayer().getName().observe(viewLifecycleOwner, Observer {
            updatePlayerName(it)
        })
        viewModel.game.getCurrPlayer().shootBoard.selectedCellLiveData.observe(
            viewLifecycleOwner,
            Observer {
                updateSelectedCellUI(it)
            })
        viewModel.game.getCurrPlayer().shootBoard.cellsLiveData.observe(
            viewLifecycleOwner,
            Observer { updateCells(it) })
        viewModel.game.getCurrPlayer().myBoard.selectedCellLiveData.observe(
            viewLifecycleOwner,
            Observer { updateMySelectedCellUI() })

        viewModel.game.getCurrPlayer().myBoard.cellsLiveData.observe(
            viewLifecycleOwner,
            Observer { updateMyCells(it) })

        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    private fun updateSelectedCellUI(cell: CellPair?) = cell?.let {
        view_result_shoot_board.updateSelectedCellUI(-1, -1)
    }

    private fun updateMySelectedCellUI() {
        view_result_my_board.updateSelectedCellUI(-1, -1)
    }

    private fun updatePlayerName(
        name: String
    ) {
        txt_result_player_name.text = name
    }

    private fun updateCells(cells: BoardArray?) = cells?.let {
        view_result_shoot_board.updateCells(cells)
    }

    private fun updateMyCells(cells: BoardArray?) = cells?.let {
        view_result_my_board.updateCells(cells)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btn_result_ok.setOnClickListener {
            val nextFrag = viewModel.game.step()
            it.findNavController().navigate(nextFrag)
        }
    }

    override fun onStart() {
        super.onStart()
    }
}