package com.example.battleship.activities.placeShips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.battleship.R
import com.example.battleship.views.ShipBoardsView
import com.example.battleship.viewModels.GameViewModel
import com.example.battleship.viewModels.GameViewModelFactory
import com.example.battleship.utils.BoardArray
import com.example.battleship.utils.CellPair
import com.example.battleship.config.Constants
import kotlinx.android.synthetic.main.fragment_middle_screen.*
import kotlinx.android.synthetic.main.fragment_place_ships.*
import java.io.Serializable

class PlaceShipsFragment : Fragment(), ShipBoardsView.OnTouchListener {

    private lateinit var viewModel: GameViewModel
    private lateinit var viewModelFactory: GameViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val playerID = arguments?.getSerializable(Constants.KEY_PLAYER_ID) as Constants.Indices
        val state = Constants.GameStates.PL_PLACE

        viewModelFactory = GameViewModelFactory(activity?.application, state, playerID)
        viewModel = ViewModelProvider(this, viewModelFactory).get(GameViewModel::class.java)

        viewModel.game.currPlayer?.getName()?.observe(viewLifecycleOwner, Observer {
            updatePlayerName(it)
        })
        viewModel.game.currPlayer?.getBoard()?.selectedCellLiveData?.observe(
            viewLifecycleOwner,
            Observer {
                updateSelectedCellUI(it)
            })
        viewModel.game.currPlayer?.getBoard()?.cellsLiveData?.observe(
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
                val shipSize =
                    when (button) {
                        btn_five_ship -> 5
                        btn_four_ship -> 4
                        btn_three_ship -> 3
                        btn_two_ship -> 2
                        else -> 0
                    }
                viewModel.game.currPlayer?.getBoard()
                    ?.handleInput(it, shipSize, Constants.ShipAction.PLACE)
            }
        }

        // Set listener for rotate ship button.
        btn_rotate_ship.setOnClickListener {
            viewModel.game.currPlayer?.getBoard()?.handleInput(view, 0, Constants.ShipAction.ROTATE)
        }

        // Set listener for erase ship button.
        btn_erase_ship.setOnClickListener {
            viewModel.game.currPlayer?.getBoard()?.handleInput(view, 0, Constants.ShipAction.ERASE)
        }
    }

    private fun updateCells(cells: BoardArray?) = cells?.let {
        view_board.updateCells(cells)
    }

    private fun updateSelectedCellUI(cell: CellPair?) = cell?.let {
        view_board.updateSelectedCellUI(cell.first, cell.second)
        if (cell != Pair(-1, -1)) {
            Toast.makeText(
                activity,
                "chosen cell is: " + viewModel.game.currPlayer?.getBoard()?.selectedCellLiveData?.value?.first + ", " + viewModel.game.currPlayer?.getBoard()?.selectedCellLiveData?.value?.second,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCellTouched(row: Int, col: Int) {
        viewModel.game.currPlayer?.getBoard()?.updateSelectedCell(row, col)
    }

    override fun onStart() {
        super.onStart()

        view_board.registerListener(this)

        // Creating buttons for ship board.
//        var index = 0
//        for (i in 0..9) {
//            for (j in 0..9) {
//                var button = Button(context)
//                button.id = index
//                button.layoutParams = LinearLayout.LayoutParams(ConstraintLayout.LayoutParams(3,3))
//                button.text = index.toString()
//                //button.minHeight = 0
//                //button.minWidth = 0
//                gl_board.addView(button)
//                ++index
//            }
//        }

    }

    companion object {

        // Factory method to create fragment instance. Framework requires empty default constructor.
        @JvmStatic
        fun newInstance(id: Serializable?): PlaceShipsFragment {
            val fragment = PlaceShipsFragment()
            fragment.arguments = Bundle().apply {
                putSerializable(Constants.KEY_PLAYER_ID, id)
            }
            return fragment
        }
    }
}