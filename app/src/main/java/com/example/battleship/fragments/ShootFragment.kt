package com.example.battleship.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.battleship.Cell
import com.example.battleship.MainActivity
import com.example.battleship.R
import com.example.battleship.config.BoardArray
import com.example.battleship.config.CellPair
import com.example.battleship.config.Constants
import com.example.battleship.viewModels.GameViewModel
import com.example.battleship.views.ShipBoardsView
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_place_ships.*
import kotlinx.android.synthetic.main.fragment_shoot.*

class ShootFragment : Fragment(), ShipBoardsView.OnTouchListener {
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
        viewModel.game.getCurrPlayer().getShootBoard().selectedCellLiveData.observe(
            viewLifecycleOwner,
            Observer {
                updateSelectedCellUI(it)
            })
        viewModel.game.getCurrPlayer().getShootBoard().cellsLiveData.observe(
            viewLifecycleOwner,
            Observer { updateCells(it) })
        viewModel.game.getCurrPlayer().getMyBoard().selectedCellLiveData.observe(
            viewLifecycleOwner,
            Observer { updateMySelectedCellUI() })

        viewModel.game.getCurrPlayer().getMyBoard().cellsLiveData.observe(
            viewLifecycleOwner,
            Observer { updateMyCells(it) })
        return inflater.inflate(R.layout.fragment_shoot, container, false)
    }

    private fun updateSelectedCellUI(cell: CellPair?) = cell?.let {
        view_shoot_board.updateSelectedCellUI(cell.first, cell.second)
    }

    private fun updateMySelectedCellUI() {
        view_shoot_board.updateSelectedCellUI(-1, -1)
    }

    private fun updatePlayerName(
        name: String
    ) {
        txt_player_name_shoot.text = name
    }

    private fun updateCells(cells: BoardArray?) = cells?.let {
        view_shoot_board.updateCells(cells)
    }

    private fun updateMyCells(cells: BoardArray?) = cells?.let {
        view_my_board.updateCells(cells)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btn_shoot.setOnClickListener {
            val viewModel =
                (activity as? MainActivity)?.getViewModel() ?: ViewModelProvider(this).get(
                    GameViewModel::class.java
                )
            val nextFrag = viewModel.game.step()
            it.findNavController().navigate(nextFrag)
        }
    }

    override fun onCellTouched(row: Int, col: Int) {
        viewModel.game.getCurrPlayer().getShootBoard().updateSelectedCell(row, col)
    }

    override fun onStart() {
        super.onStart()

        view_shoot_board.registerListener(this)
    }
}