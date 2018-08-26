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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.tommsy.sorryai.game.Player.GamePiece;

public class BoardTest {

    @Test
    void putOnBoardThrowsExceptionWhenEatingSelf() {
        Game game = new Game(null);
        Board board = new Board();

        assertFalse(board.putOnBoard(game.player1.pieces[1]));
        assertThrows(IllegalArgumentException.class, () -> board.putOnBoard(game.player1.pieces[0]));
    }

    @Test
    void putOnBoardSetsGamePieceCorrectly() {
        Game game = new Game(null);
        Board board = new Board();

        assertFalse(board.putOnBoard(game.player1.pieces[0]));
        GamePiece piece = game.player1.pieces[0];
        assertEquals(0, piece.progress, "Progress is 0 when newly placed");
        assertEquals(0, piece.boardIndex);

        assertFalse(board.putOnBoard(game.player2.pieces[0]));
        piece = game.player2.pieces[0];
        assertEquals(0, piece.progress, "Progress is 0 when newly placed");
        assertEquals(14, piece.boardIndex);

        assertFalse(board.putOnBoard(game.player3.pieces[0]));
        piece = game.player3.pieces[0];
        assertEquals(0, piece.progress, "Progress is 0 when newly placed");
        assertEquals(28, piece.boardIndex);

        assertFalse(board.putOnBoard(game.player4.pieces[0]));
        piece = game.player4.pieces[0];
        assertEquals(0, piece.progress, "Progress is 0 when newly placed");
        assertEquals(42, piece.boardIndex);
    }

    @Test
    void putToSetsGamePieceCorrectly() {
        Game game = new Game(null);
        Board board = new Board();

        assertFalse(board.putTo(game.player1.pieces[0], 15));
        assertEquals(15, game.player1.pieces[0].boardIndex, "Board index");

        assertFalse(board.putTo(game.player1.pieces[0], 12));
        assertEquals(12, game.player1.pieces[0].boardIndex, "Board index");
    }

    @Test
    void printBoard() {
        Game game = new Game(null);
        Board board = new Board();
        String expected = "     ╔═════╗     \n" +
                "     ║░░░░░║     \n" +
                "     ║░█░█░║     \n" +
                "     ║░█░█░║     \n" +
                "     ║░█░█░║     \n" +
                "╔════╝░█░█░╚════╗\n" +
                "║░░░░░░█░█░░░░░░║\n" +
                "║░█████████████░║\n" +
                "║░░░░░░█░█░░░░░░║\n" +
                "║░█████████████░║\n" +
                "║░░░░░░█░█░░░░░░║\n" +
                "╚════╗░█░█░╔════╝\n" +
                "     ║░█░█░║     \n" +
                "     ║░█░█░║     \n" +
                "     ║░█░█░║     \n" +
                "     ║░░░░░║     \n" +
                "     ╚═════╝     ";
        assertEquals(expected, board.toString(game.player1, game.player2, game.player3, game.player4));

        expected = "     ╔═════╗     \n" +
                "     ║░░░░░║     \n" +
                "     ║░█░█░║     \n" +
                "     ║░█░█░║     \n" +
                "     ║░█░█░║     \n" +
                "╔════╝░█░█░╚════╗\n" +
                "║░░░░░░█░█░░░░░░║\n" +
                "║░█████████████░║\n" +
                "║░░░░░░█░█░░░░░░║\n" +
                "║░█████████████░║\n" +
                "║░░░░░░█░█░░░░░░║\n" +
                "╚════╗░█░█░╔════╝\n" +
                "     ║░█░█░║     \n" +
                "     ║░█░█░║     \n" +
                "     ║░█░█░║     \n" +
                "     ║1░░░░║     \n" +
                "     ╚═════╝     ";
        assertEquals(0, board.piecesOnBoard());
        assertFalse(board.putOnBoard(game.player1.pieces[0]));
        assertEquals(1, board.piecesOnBoard());
        assertEquals(expected, board.toString(game.player1, game.player2, game.player3, game.player4));

        expected = "     ╔═════╗     \n" +
                "     ║░░░░░║     \n" +
                "     ║░█░█░║     \n" +
                "     ║░█░█░║     \n" +
                "     ║░█░█░║     \n" +
                "╔════╝░█░█░╚════╗\n" +
                "║░░░░░░█░█░░░░░░║\n" +
                "║░█████████████░║\n" +
                "║░░░░░░█░█░░░░░░║\n" +
                "║░█████████████░║\n" +
                "║░░░░░░█░█░░░░░2║\n" +
                "╚════╗░█░█░╔════╝\n" +
                "     ║░█░█░║     \n" +
                "     ║░█░█░║     \n" +
                "     ║1█░█░║     \n" +
                "     ║░░░░░║     \n" +
                "     ╚═════╝     ";
        assertFalse(board.putTo(game.player1.pieces[0], 55));
        assertFalse(board.putOnBoard(game.player2.pieces[0]));
        assertEquals(2, board.piecesOnBoard());
        assertEquals(expected, board.toString(game.player1, game.player2, game.player3, game.player4));
    }
}
