package com.example.battleship.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.battleship.MainActivity
import com.example.battleship.R
import com.example.battleship.views.ShipBoardsView
import com.example.battleship.viewModels.GameViewModel
import com.example.battleship.config.BoardArray
import com.example.battleship.config.CellPair
import com.example.battleship.config.Constants
import kotlinx.android.synthetic.main.fragment_place_ships.*

class PlaceShipsFragment : Fragment(), ShipBoardsView.OnTouchListener {

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
        viewModel.game.getCurrPlayer().getMyBoard().selectedCellLiveData.observe(
            viewLifecycleOwner,
            Observer {
                updateSelectedCellUI(it)
            })
        viewModel.game.getCurrPlayer().getMyBoard().cellsLiveData.observe(
            viewLifecycleOwner,
            Observer { updateCells(it) })
        return inflater.inflate(R.layout.fragment_place_ships, container, false)
    }

    private fun updatePlayerName(
        name: String
    ) {
        txt_place_ships_player_name.text = name
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Set listeners for placing ships buttons.
        val shipsButtons = listOf(btn_five_ship, btn_four_ship, btn_three_ship, btn_two_ship)

        shipsButtons.forEachIndexed { _, button ->
            button.setOnClickListener {
                handlePlaceShip(button)
            }
        }

        // Set listener for rotate ship button.
        btn_rotate_ship.setOnClickListener {
            handleRotateShip()
        }

        // Set listener for erase ship button.
        btn_erase_ship.setOnClickListener {
            handleEraseShips()
        }

        btn_place_ships_ok.setOnClickListener {
            val nextFrag = viewModel.game.step()
            it.findNavController().navigate(nextFrag)
        }
    }

    private fun handleRotateShip() {
        val (_, _, errMsg) = viewModel.game.getCurrPlayer().getMyBoard()
            .handleInput(0, Constants.ShipAction.ROTATE)

        if(errMsg != "") {
            Toast.makeText(
                context,
                errMsg,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun handlePlaceShip(button: Button) {
        val shipSize =
            when (button) {
                btn_five_ship -> 5
                btn_four_ship -> 4
                btn_three_ship -> 3
                btn_two_ship -> 2
                else -> 0
            }

        val (isExceeded, _, errMsg) = viewModel.game.getCurrPlayer().getMyBoard()
            .handleInput(shipSize, Constants.ShipAction.PLACE)
        // Have to revert logic because true means, that the limit is exceeded and
        // therefore the button must be disabled.
        button.isEnabled = !isExceeded

        if(errMsg != "") {
            Toast.makeText(
                context,
                errMsg,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun handleEraseShips() {
        val (isExceeded, shipSize) = viewModel.game.getCurrPlayer().getMyBoard()
            .handleInput(0, Constants.ShipAction.ERASE)

        val shipButton = when (shipSize - 2) {
            0 -> btn_two_ship
            1 -> btn_three_ship
            2 -> btn_four_ship
            3 -> btn_five_ship
            else -> return
        }

        // Have to revert logic because true means, that the limit is exceeded and
        // therefore the button must be disabled.
        shipButton.isEnabled = !isExceeded
    }

    private fun updateCells(cells: BoardArray?) = cells?.let {
        view_board.updateCells(cells)
    }

    private fun updateSelectedCellUI(cell: CellPair?) = cell?.let {
        view_board.updateSelectedCellUI(cell.first, cell.second)
    }

    override fun onCellTouched(row: Int, col: Int) {
        viewModel.game.getCurrPlayer().getMyBoard().updateSelectedCell(row, col)
    }

    override fun onStart() {
        super.onStart()

        view_board.registerListener(this)
    }
}