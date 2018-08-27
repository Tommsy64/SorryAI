/*
* Sorry AI
* Copyright (C) 2018  Tommsy64
*
* Sorry AI is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* Sorry AI is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with Sorry AI.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.tommsy.sorryai.game;

import java.util.Arrays;

import com.tommsy.sorryai.agent.Agent;
import com.tommsy.sorryai.agent.AgentHandler;
import com.tommsy.sorryai.game.Board.ExitDirection;
import com.tommsy.sorryai.game.Player.GamePiece;
import com.tommsy.sorryai.game.dice.DiceRoller;
import com.tommsy.sorryai.game.dice.RandomDiceRoller;

public class Game {
    final Player player1, player2, player3, player4;
    private final DiceRoller dice;

    AgentHandler agentHandler;

    public Game(AgentHandler agentHandler) {
        this(new RandomDiceRoller(), agentHandler);
    }

    public Game(DiceRoller dice, AgentHandler agentHandler) {
        this.player1 = new Player(0);
        this.player2 = new Player(1);
        this.player3 = new Player(2);
        this.player4 = new Player(3);

        this.dice = dice;
        this.agentHandler = agentHandler;
    }

    public void runGame() {
        runGame(new Board());
    }

    void runGame(Board board) {
        if (agentHandler == null)
            throw new IllegalStateException("Cannot run game without an agent handler.");
        Player currentPlayer = determineStartingPlayer();
        boolean gameOver = false;
        int roll;
        do {
            roll = dice.roll();
            GamePiece[] moveablePieces = board.getMoveablePiecesForPlayer(currentPlayer, roll);
            Agent agent = agentHandler.getAgent(currentPlayer.index);
            System.out.println("Start of " + agent.getName() + "'s turn. Dice: " + roll);
            if (moveablePieces.length != 0) {
                System.out.println("__________________________________________________________________________");
                int selectedPieceIndex = agent.processTurn(this, board, roll, moveablePieces);
                GamePiece selectedPiece = moveablePieces[selectedPieceIndex];
                if (selectedPiece.progress == -1) {
                    if (board.putOnBoard(selectedPiece))
                        processEating(agent, board, selectedPiece);
                } else if (selectedPiece.progress == GamePiece.CENTER_PROGRESS) {
                    ExitDirection exitDirection;
                    do {
                        exitDirection = agent.getExitDirection();
                    } while (!board.canExitCenterTo(selectedPiece, exitDirection));

                    if (board.exitCenter(selectedPiece, exitDirection))
                        processEating(agent, board, selectedPiece);
                } else if (selectedPiece.canMoveToCenter) {
                    System.out.println("[Debug] Can move to center");
                    if (selectedPiece.mustMoveToCenter)
                        board.moveToCenter(selectedPiece);
                    else if (agent.moveToCenter())
                        board.moveToCenter(selectedPiece);
                    else if (board.moveByAmount(selectedPiece, roll))
                        processEating(agent, board, selectedPiece);
                } else {
                    if (board.moveByAmount(selectedPiece, roll))
                        processEating(agent, board, selectedPiece);
                }

                System.out.println(drawBoard(board));
                System.out.println("=========================================================================");
                System.out.print("End of turn\n\n\n\n\n\n\n\n\n\n");

                if (selectedPiece.progress > Board.TOTAL_PROGRESS) {
                    gameOver = board.hasPlayerWon(currentPlayer);
                    System.out.println(agent.getName() + " has won! (Player " + (currentPlayer.index + 1) + ")");

                    System.out.println(Arrays.toString(currentPlayer.pieces));
                    continue;
                }
            } else {
                System.out.println("No possible moves.");
            }
            if (roll == 6) // Player gets to roll again on a 6.
                continue;
            currentPlayer = getNextPlayer(currentPlayer);
        } while (!gameOver);
    }

    public String drawBoard(Board board) {
        return board.toString(player1, player2, player3, player4);
    }

    private void processEating(Agent agent, Board board, GamePiece piece) {
        boolean atePiece = true;
        while (atePiece && agent.moveAfterEating()) {
            if (board.canMoveToCenter(piece, 5) && board.canMoveByAmount(piece, 4)) {
                piece.canMoveToCenter = true;
                if (piece.mustMoveToCenter = !board.canMoveByAmount(piece, 5) || agent.moveToCenter()) {
                    board.moveToCenter(piece);
                    break;
                }
            }
            if (board.canMoveByAmount(piece, 5))
                atePiece = board.moveByAmount(piece, 5);
        }
    }

    Player determineStartingPlayer() {
        int[] rolls = { dice.roll(), dice.roll(), dice.roll(), dice.roll() };
        int max;
        while (Integer.bitCount(max = getMaxes(rolls)) != 1)
            for (int i = 0; i < rolls.length; i++)
                rolls[i] = (max & (1 << i)) > 0 ? dice.roll() : -1;
        return getPlayerByIndex(Integer.numberOfTrailingZeros(max) + 1);
    }

    static int getMaxes(int[] rolls) {
        int maxVal = rolls[0];
        int bitmap = 1;
        if (rolls[1] > maxVal) {
            bitmap = 2;
            maxVal = rolls[1];
        } else if (rolls[1] == maxVal)
            bitmap |= 2;

        if (rolls[2] > maxVal) {
            bitmap = 4;
            maxVal = rolls[2];
        } else if (rolls[2] == maxVal)
            bitmap |= 4;

        if (rolls[3] > maxVal) {
            bitmap = 8;
            maxVal = rolls[3];
        } else if (rolls[3] == maxVal)
            bitmap |= 8;
        return bitmap;
    }

    Player getPlayerByIndex(int i) {
        switch (i) {
        case 1:
            return player1;
        case 2:
            return player2;
        case 3:
            return player3;
        case 4:
            return player4;
        default:
            throw new IllegalArgumentException("Specified player index is not 1, 2, 3, or 4.");
        }
    }

    Player getNextPlayer(Player currentPlayer) {
        if (currentPlayer == player1)
            return player2;
        else if (currentPlayer == player2)
            return player3;
        else if (currentPlayer == player3)
            return player4;
        else if (currentPlayer == player4)
            return player1;
        else
            throw new IllegalArgumentException("Invalid player.");
    }
}
