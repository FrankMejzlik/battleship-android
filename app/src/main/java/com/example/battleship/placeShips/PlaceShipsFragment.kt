package com.example.battleship.placeShips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.battleship.R
import com.example.battleship.shipBoardsViewModel.ShipBoardsView
import com.example.battleship.shipBoardsViewModel.ShipBoardsViewModel
import com.example.battleship.shipBoardsViewModel.ShipBoardsViewModelFactory
import com.example.battleship.utils.BoardArray
import com.example.battleship.utils.CellPair
import com.example.battleship.utils.Constants
import kotlinx.android.synthetic.main.fragment_place_ships.*
import java.io.Serializable

class PlaceShipsFragment : Fragment(), ShipBoardsView.OnTouchListener {

    private lateinit var viewModel: ShipBoardsViewModel
    private lateinit var viewModelFactory: ShipBoardsViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModelFactory = ShipBoardsViewModelFactory(activity?.application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ShipBoardsViewModel::class.java)
        viewModel.shipBoard.selectedCellLiveData.observe(viewLifecycleOwner, Observer {
            updateSelectedCellUI(it)
        })
        viewModel.shipBoard.cellsLiveData.observe(viewLifecycleOwner, Observer { updateCells(it)})
        return inflater.inflate(R.layout.fragment_place_ships, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val shipsButtons = listOf(btn_five_ship, btn_four_ship, btn_three_ship, btn_two_ship)

        shipsButtons.forEachIndexed { _, button ->
            button.setOnClickListener {
                viewModel.shipBoard.handleInput(Constants.CellStates.SHIP)
            }
        }
    }

    private fun updateCells(cells: BoardArray?) = cells?.let {
        view_board.updateCells(cells)
    }

    private fun updateSelectedCellUI(cell: CellPair?) = cell?.let {
        view_board.updateSelectedCellUI(cell.first, cell.second)
        if(cell != Pair(-1,-1)) {
            Toast.makeText(
                activity,
                "chosen cell is: " + viewModel.shipBoard.selectedCellLiveData.value?.first + ", " + viewModel.shipBoard.selectedCellLiveData.value?.second,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCellTouched(row: Int, col: Int) {
        viewModel.shipBoard.updateSelectedCell(row, col)
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