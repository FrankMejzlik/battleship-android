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
import com.example.battleship.MainActivity
import com.example.battleship.Player
import com.example.battleship.R
import com.example.battleship.config.BoardArray
import com.example.battleship.config.CellPair
import com.example.battleship.config.Constants
import com.example.battleship.viewModels.GameViewModel
import com.example.battleship.views.ShipBoardsView
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
        view_my_board.updateSelectedCellUI(-1, -1)
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
            if (handleShoot()) {
                // Reset selected cell.
                viewModel.game.getCurrPlayer().getShootBoard().updateSelectedCell(-1, -1)
                val nextFrag = viewModel.game.step()
                it.findNavController().navigate(nextFrag)
            } else
                Toast.makeText(
                    activity, Constants.ERR_MISSING_SHOOT_CELL,
                    Toast.LENGTH_SHORT
                ).show()
        }
    }

    private fun handleShoot(): Boolean {
        val selectedRow =
            viewModel.game.getCurrPlayer().getShootBoard().selectedCellLiveData.value?.first ?: 0
        val selectedCol =
            viewModel.game.getCurrPlayer().getShootBoard().selectedCellLiveData.value?.second ?: 0
        if (selectedRow == -1 || selectedCol == -1)
            return false
        if (viewModel.game.getCurrPlayer() == viewModel.game.player1) {
            updateShootCells(
                selectedRow,
                selectedCol,
                viewModel.game.getCurrPlayer(),
                viewModel.game.player2
            )
        } else {
            updateShootCells(
                selectedRow,
                selectedCol,
                viewModel.game.getCurrPlayer(),
                viewModel.game.player1
            )
        }
        return true
    }

    private fun updateShootCells(
        row: Int,
        col: Int,
        currPlayer: Player,
        otherPlayer: Player
    ) {
        otherPlayer.getMyBoard().updateSelectedCell(row, col)
        otherPlayer.getMyBoard().handleInput(0, Constants.ShipAction.SHOOT)

        val state = when (otherPlayer.getMyBoard().getCell(row, col)?.state) {
            Constants.CellStates.HIT -> Constants.CellStates.HIT
            else -> Constants.CellStates.MISS
        }
        currPlayer.getShootBoard().updateState(row, col, state)
    }

    override fun onCellTouched(row: Int, col: Int) {
        viewModel.game.getCurrPlayer().getShootBoard().updateSelectedCell(row, col)
    }

    override fun onStart() {
        super.onStart()

        view_shoot_board.registerListener(this)
    }
}