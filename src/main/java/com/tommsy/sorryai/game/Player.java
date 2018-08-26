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

import lombok.Getter;
import lombok.ToString;

public class Player {
    static final int PIECES_PER_PLAYER = 5;

    final GamePiece[] pieces = new GamePiece[PIECES_PER_PLAYER];
    public final int index, startingPosition;

    Player(int index) {
        this.index = index + 1;
        this.startingPosition = Board.SIZE / 4 * index;
        for (int i = 0; i < pieces.length; i++)
            pieces[i] = new GamePiece();
    }

    @ToString
    public class GamePiece {
        static final int CENTER_PROGRESS = -2;
        @Getter
        int progress = -1;
        int boardIndex = -1;

        @Getter
        boolean canMoveToCenter, mustMoveToCenter;
        public boolean willMoveToCenter;

        public int centerExitPosition;

        public Player getPlayer() {
            return Player.this;
        }
    }
}