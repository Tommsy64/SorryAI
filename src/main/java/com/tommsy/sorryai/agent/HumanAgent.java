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

import static com.tommsy.sorryai.Main.scanner;

import java.util.Arrays;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import com.tommsy.sorryai.game.Board;
import com.tommsy.sorryai.game.Board.ExitDirection;
import com.tommsy.sorryai.game.Game;
import com.tommsy.sorryai.game.Player.GamePiece;

@RequiredArgsConstructor
@ToString
public class HumanAgent implements Agent {

    @Getter
    private final String name;

    @Override
    public int processTurn(Game game, Board board, int dice, GamePiece[] moveablePieces) {
        Arrays.sort(moveablePieces);
        System.out.println(game.drawBoard(board));

        if (moveablePieces.length == 1) {
            System.out.println(name + " rolled a " + dice + ".");
            return 0;
        }

        System.out.println(name + " rolled a " + dice + ". Which piece would you like to move? ");
        System.out.println(Arrays.toString(moveablePieces));

        int selected;
        do {
            selected = scanner.hasNextInt() ? scanner.nextInt() - 1 : -1;
        } while (selected >= moveablePieces.length || selected < 0);
        return selected;
    }

    @Override
    public boolean moveAfterEating() {
        System.out.println("Would you like to move 5 after eating?");
        System.out.println();
        String response;
        boolean booleanResponse;
        do {
            response = scanner.nextLine().toLowerCase();
            booleanResponse = response.startsWith("y") || response.startsWith("t");
            System.out.println("[Debug] Response: " + response + " Interpreted as: " + booleanResponse);
        } while (!response.startsWith("y") && !response.startsWith("t") && !response.startsWith("n") && !response.startsWith("f"));
        return booleanResponse;
    }

    @Override
    public boolean moveToCenter() {
        System.out.println("Would you like to move to the center? ");
        boolean result;
        do {
            String response = scanner.nextLine().toLowerCase();
            if (response.startsWith("y") || response.startsWith("t")) {
                result = true;
                break;
            } else if (response.startsWith("n") || response.startsWith("f")) {
                result = false;
                break;
            }
            System.out.println("Invalid response.");
        } while (true);
        return result;
    }

    @Override
    public ExitDirection getExitDirection() {
        System.out.println("Which direction would you like to exit?");
        int playerDirection;
        do {
            playerDirection = scanner.hasNextInt() ? scanner.nextInt() : -1;
        } while (playerDirection != 1 && playerDirection != 2 && playerDirection != 3 && playerDirection == 4);
        return ExitDirection.fromPlayerIndex(playerDirection);
    }
}
