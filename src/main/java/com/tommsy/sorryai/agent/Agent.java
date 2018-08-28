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

package com.tommsy.sorryai.agent;

import com.tommsy.sorryai.game.Board;
import com.tommsy.sorryai.game.ExitDirection;
import com.tommsy.sorryai.game.Game;
import com.tommsy.sorryai.game.Player;
import com.tommsy.sorryai.game.Player.GamePiece;

/**
 * Represents an Agent that can control a {@link Player} and its {@link GamePiece GamePieces} in a {@link Game}.
 */
public interface Agent {

    /**
     * Called each time a {@link Player} must make a choice of what {@link GamePiece} to move on a turn.
     *
     * @param game
     * @param board
     * @param dice
     * @param moveablePieces Possible pieces that can be moved this turn
     * @return The index of the GamePiece in the moveablePieces array which should be moved this turn.
     */
    // TODO: Don't pass game and board every single time
    public int processTurn(Game game, Board board, int dice, GamePiece[] moveablePieces);

    /**
     * @return Whether or not the last turn's piece should move 5 positions after having ate another piece.
     */
    public boolean moveAfterEating();

    /**
     * @return Whether or not the last turn's piece should move into the center.
     */
    public boolean moveToCenter();

    /**
     * @return The direction to exit if last turn's piece is exiting the center.
     */
    public ExitDirection getExitDirection();

    /**
     * @return The name of this agent.
     */
    public String getName();
}
