package com.example.battleship

import com.example.battleship.config.Constants

class Game() {

    var player1 = Player()
        private set
    var player2 = Player()
        private set

    private var _state = Constants.GameStates.INIT

    // 1 - player 1 wins
    // 0 - no one wins
    // -1 - player 2 wins
    private fun detectWin(): Int {
        val boardSize = player2.myBoard.cellsLiveData.value?.size ?: 0

        if (getCurrPlayer() == player1) {
            for (i in 0 until boardSize) {
                for (j in 0 until boardSize) {
                    if (player2.myBoard.getCell(i, j)?.state == Constants.CellStates.SHIP)
                        return 0
                }
            }
            return 1
        } else {
            for (i in 0 until boardSize) {
                for (j in 0 until boardSize) {
                    if (player1.myBoard.getCell(i, j)?.state == Constants.CellStates.SHIP)
                        return 0
                }
            }
            return -1
        }
    }

    fun getCurrPlayer(): Player {
        return when (_state) {
            Constants.GameStates.INIT -> player1
            Constants.GameStates.INPUT_NAMES -> player1
            Constants.GameStates.P1_PLACE_SWITCH -> player1
            Constants.GameStates.P1_PLACE -> player1
            Constants.GameStates.P2_PLACE_SWITCH -> player2
            Constants.GameStates.P2_PLACE -> player2
            Constants.GameStates.P1_SWITCH -> player1
            Constants.GameStates.P1_SHOOT -> player1
            Constants.GameStates.P1_RES -> player1
            Constants.GameStates.P2_SWITCH -> player2
            Constants.GameStates.P2_SHOOT -> player2
            Constants.GameStates.P2_RES -> player2
            Constants.GameStates.P1_WIN -> player1
            Constants.GameStates.P2_WIN -> player2
        }
    }

    fun step(): Int {
        val oldState = _state
        update()
        return when (oldState) {
            Constants.GameStates.INIT -> R.id.action_mainFragment_to_setPlayersFragment
            Constants.GameStates.INPUT_NAMES -> R.id.action_setPlayersFragment_to_middleScreenFragment
            Constants.GameStates.P1_PLACE_SWITCH -> R.id.action_middleScreenFragment_to_placeShipsFragment
            Constants.GameStates.P1_PLACE -> R.id.action_placeShipsFragment_to_middleScreenFragment
            Constants.GameStates.P2_PLACE_SWITCH -> R.id.action_middleScreenFragment_to_placeShipsFragment
            Constants.GameStates.P2_PLACE -> R.id.action_placeShipsFragment_to_middleScreenFragment
            Constants.GameStates.P1_SWITCH -> R.id.action_middleScreenFragment_to_shootFragment
            Constants.GameStates.P1_SHOOT ->
                if (_state == Constants.GameStates.P1_RES) R.id.action_shootFragment_to_resultFragment
                else R.id.action_shootFragment_to_scoreboardFragment
            Constants.GameStates.P1_RES -> R.id.action_resultFragment_to_middleScreenFragment
            Constants.GameStates.P2_SWITCH -> R.id.action_middleScreenFragment_to_shootFragment
            Constants.GameStates.P2_SHOOT ->
                if (_state == Constants.GameStates.P2_RES) R.id.action_shootFragment_to_resultFragment
                else R.id.action_shootFragment_to_scoreboardFragment
            Constants.GameStates.P2_RES -> R.id.action_resultFragment_to_middleScreenFragment
            Constants.GameStates.P1_WIN -> R.id.action_scoreboardFragment_to_mainFragment
            Constants.GameStates.P2_WIN -> R.id.action_scoreboardFragment_to_mainFragment
        }
    }

    private fun update() {
        _state = when (_state) {
            Constants.GameStates.INIT -> Constants.GameStates.INPUT_NAMES
            Constants.GameStates.INPUT_NAMES -> Constants.GameStates.P1_PLACE_SWITCH
            Constants.GameStates.P1_PLACE_SWITCH -> Constants.GameStates.P1_PLACE
            Constants.GameStates.P1_PLACE -> Constants.GameStates.P2_PLACE_SWITCH
            Constants.GameStates.P2_PLACE_SWITCH -> Constants.GameStates.P2_PLACE
            Constants.GameStates.P2_PLACE -> Constants.GameStates.P1_SWITCH
            Constants.GameStates.P1_SWITCH -> Constants.GameStates.P1_SHOOT
            Constants.GameStates.P1_SHOOT -> if (detectWin() == 1) Constants.GameStates.P1_WIN
            else Constants.GameStates.P1_RES
            Constants.GameStates.P1_RES -> Constants.GameStates.P2_SWITCH
            Constants.GameStates.P2_SWITCH -> Constants.GameStates.P2_SHOOT
            Constants.GameStates.P2_SHOOT -> if (detectWin() == -1) Constants.GameStates.P2_WIN
            else Constants.GameStates.P2_RES
            Constants.GameStates.P2_RES -> Constants.GameStates.P1_SWITCH
            Constants.GameStates.P1_WIN -> Constants.GameStates.INIT
            Constants.GameStates.P2_WIN -> Constants.GameStates.INIT
        }
    }
}