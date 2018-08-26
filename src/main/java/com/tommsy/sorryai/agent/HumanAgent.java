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
import com.tommsy.sorryai.game.Player.GamePiece;

public class HumanAgent implements Agent {

    @Override
    public int processTurn(int dice, Board board, GamePiece[] moveablePieces) {

        return 0;
    }

    @Override
    public boolean moveAfterEatting() {
        return false;
    }

    @Override
    public boolean moveToCenter() {
        return false;
    }

}
