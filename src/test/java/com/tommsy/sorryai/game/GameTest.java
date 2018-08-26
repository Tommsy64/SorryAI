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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.tommsy.sorryai.game.dice.DiceRoller;

class GameTest {
    @Test
    void getMaxesWorks() {
        assertAll("getMaxesSingle",
                () -> assertEquals(8, Game.getMaxes(new int[] { 1, 2, 3, 4 })),
                () -> assertEquals(4, Game.getMaxes(new int[] { 1, 2, 5, 4 })),
                () -> assertEquals(2, Game.getMaxes(new int[] { 1, 5, 3, 4 })),
                () -> assertEquals(1, Game.getMaxes(new int[] { 5, 2, 3, 4 })));

        assertAll("getMaxesDouble",
                () -> assertEquals(8 | 4, Game.getMaxes(new int[] { 1, 2, 4, 4 })),
                () -> assertEquals(2 | 4, Game.getMaxes(new int[] { 1, 5, 5, 4 })),
                () -> assertEquals(1 | 2, Game.getMaxes(new int[] { 5, 5, 3, 4 })),
                () -> assertEquals(1 | 8, Game.getMaxes(new int[] { 5, -1, -1, 5 })));

        assertEquals(1 | 2 | 4 | 8, Game.getMaxes(new int[] { 5, 5, 5, 5 }));
    }

    @Test
    void getPlayerByIndexIsCorrect() {
        Game game = new Game(null);
        assertAll("getPlayerByIndex",
                () -> assertEquals(game.player1, game.getPlayerByIndex(1)),
                () -> assertEquals(game.player2, game.getPlayerByIndex(2)),
                () -> assertEquals(game.player3, game.getPlayerByIndex(3)),
                () -> assertEquals(game.player4, game.getPlayerByIndex(4)));

        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> game.getPlayerByIndex(0)),
                () -> assertThrows(IllegalArgumentException.class, () -> game.getPlayerByIndex(5)));
    }

    @Test
    void getNextPlayerIsCorrect() {
        Game game = new Game(null);
        assertAll("getNextPlayer",
                () -> assertEquals(game.player2, game.getNextPlayer(game.player1)),
                () -> assertEquals(game.player3, game.getNextPlayer(game.player2)),
                () -> assertEquals(game.player4, game.getNextPlayer(game.player3)),
                () -> assertEquals(game.player1, game.getNextPlayer(game.player4)));

        assertThrows(IllegalArgumentException.class, () -> game.getNextPlayer(new Player(0)));
    }

    @Test
    void startingPlayerDeterminedCorrectly() {
        int[] index = new int[] { 0 };
        int[][] rolls = new int[1][];
        Game game = new Game(new DiceRoller() {
            public int roll() {
                return rolls[0][index[0]++];
            }
        }, null);

        rolls[0] = new int[] { 1, 2, 3, 4 };
        assertEquals(game.player4, game.determineStartingPlayer());
        index[0] = 0;

        rolls[0] = new int[] { 1, 2, 4, 3 };
        assertEquals(game.player3, game.determineStartingPlayer());
        index[0] = 0;

        rolls[0] = new int[] { 1, 4, 2, 3 };
        assertEquals(game.player2, game.determineStartingPlayer());
        index[0] = 0;

        rolls[0] = new int[] { 4, 1, 2, 3 };
        assertEquals(game.player1, game.determineStartingPlayer());
        index[0] = 0;

        rolls[0] = new int[] { 1, 2, 4, 4, 4, 6 };
        assertEquals(game.player4, game.determineStartingPlayer());
        index[0] = 0;

        rolls[0] = new int[] { 1, 1, 1, 1, 1, 2, 3, 4 };
        assertEquals(game.player4, game.determineStartingPlayer());
        index[0] = 0;

        rolls[0] = new int[] { 1, 1, 1, 1, 1, 6, 3, 4 };
        assertEquals(game.player2, game.determineStartingPlayer());
        index[0] = 0;

        rolls[0] = new int[] { 2, 2, 2, 2, 1, 1, 1, 1, 5, 5, 5, 5, 1, 2, 6, 6, 1, 1, 4, 4, 6, 5 };
        assertEquals(game.player3, game.determineStartingPlayer());
        index[0] = 0;
    }

    @Test
    void playersStartWithFivePieces() {
        Game game = new Game(null);
        assertAll("playerStartWith5Pieces",
                () -> assertEquals(5, game.player1.pieces.length),
                () -> assertEquals(5, game.player2.pieces.length),
                () -> assertEquals(5, game.player3.pieces.length),
                () -> assertEquals(5, game.player4.pieces.length));

    }
}