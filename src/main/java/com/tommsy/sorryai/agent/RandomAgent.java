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

import java.util.Random;

import lombok.Getter;

import com.tommsy.sorryai.game.Board;
import com.tommsy.sorryai.game.ExitDirection;
import com.tommsy.sorryai.game.Game;
import com.tommsy.sorryai.game.Player.GamePiece;

/**
 * This agent makes all choices at random.
 */
public class RandomAgent implements Agent {

    private final Random rand;
    @Getter
    private final String name;

    private static int count = 1;

    public RandomAgent() {
        this.rand = new Random();
        this.name = "Random Bot " + count++;
    }

    @Override
    public int processTurn(Game game, Board board, int dice, GamePiece[] moveablePieces) {
        return rand.nextInt(moveablePieces.length);
    }

    @Override
    public boolean moveAfterEating() {
        return rand.nextBoolean();
    }

    @Override
    public boolean moveToCenter() {
        return rand.nextBoolean();
    }

    @Override
    public ExitDirection getExitDirection() {
        return ExitDirection.fromPlayerIndex(rand.nextInt(4));
    }
}
