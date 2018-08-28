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

import lombok.RequiredArgsConstructor;

import com.tommsy.sorryai.game.Player.GamePiece;

/**
 * The possible directions a {@link GamePiece} can exit the center of a board to.
 */
@RequiredArgsConstructor
public enum ExitDirection {
    ONE(9), TWO(23), THREE(37), FOUR(51);
    /**
     * The absolute position on the {@link Board} of this exit.
     */
    final int position;

    /**
     * @param player 0, 1, 2, or 3
     * @return The {@linkplain ExitDirection} that is closest to the starting position of the player
     */
    public static ExitDirection fromPlayerIndex(int player) {
        switch (player) {
        case 0:
            return FOUR;
        case 1:
            return ONE;
        case 2:
            return TWO;
        case 3:
            return THREE;
        default:
            throw new IllegalArgumentException("Player must be 0, 1, 2, or 3");
        }
    }
}
