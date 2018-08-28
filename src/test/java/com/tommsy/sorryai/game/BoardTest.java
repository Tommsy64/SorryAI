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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void exitSetsProgressCorrectly() {
        Game game = new Game(null);
        Board board = new Board();

        assertFalse(board.moveToCenter(game.player1.pieces[0]));

        assertFalse(board.exitCenter(game.player1.pieces[0], ExitDirection.fromPlayerIndex(1)));
        assertEquals(51, game.player1.pieces[0].progress);

        assertFalse(board.moveToCenter(game.player2.pieces[0]));
        assertTrue(board.exitCenter(game.player2.pieces[0], ExitDirection.fromPlayerIndex(1)));
        assertEquals(37, game.player2.pieces[0].progress);
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

        game = new Game(null);
        board = new Board();
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
                "║░░░░░░█░█░░░░░2║\n" +
                "╚════╗░█░█░╔════╝\n" +
                "     ║░█░█░║     \n" +
                "     ║░█░█░║     \n" +
                "     ║░█░█░║     \n" +
                "     ║░░░░░║     \n" +
                "     ╚═════╝     ";
        assertFalse(board.putOnBoard(game.player2.pieces[0]));
        assertEquals(expected, board.toString(game.player1, game.player2, game.player3, game.player4));
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
    void getMoveablePiecesSelectsOnlySingleHomePiece() {
        Game game = new Game(null);
        Board board = new Board();
        GamePiece[] pieces = board.getMoveablePiecesForPlayer(game.player1, 1);
        assertEquals(1, pieces.length, "Amount of moveable pieces");

        pieces = board.getMoveablePiecesForPlayer(game.player1, 6);
        assertEquals(1, pieces.length, "Amount of moveable pieces");

        assertAll("Amount of moveable pieces",
                () -> assertEquals(0, board.getMoveablePiecesForPlayer(game.player2, 2).length),
                () -> assertEquals(0, board.getMoveablePiecesForPlayer(game.player3, 3).length),
                () -> assertEquals(0, board.getMoveablePiecesForPlayer(game.player4, 4).length),
                () -> assertEquals(0, board.getMoveablePiecesForPlayer(game.player1, 5).length));
    }

    @Test
    void getMoveablePiecesWorksCorrectly() {
        Game game = new Game(null);
        Board board = new Board();

        assertFalse(board.putOnBoard(game.player1.pieces[0]));
        assertFalse(board.moveByAmount(game.player1.pieces[0], 4));
        assertFalse(board.putOnBoard(game.player1.pieces[1]));
        GamePiece[] pieces = board.getMoveablePiecesForPlayer(game.player1, 1);
        assertEquals(2, pieces.length, "Amount of moveable pieces");
        pieces = board.getMoveablePiecesForPlayer(game.player1, 2);
        assertEquals(2, pieces.length, "Amount of moveable pieces");
        pieces = board.getMoveablePiecesForPlayer(game.player1, 3);
        assertEquals(2, pieces.length, "Amount of moveable pieces");
        pieces = board.getMoveablePiecesForPlayer(game.player1, 4);
        assertEquals(1, pieces.length, "Amount of moveable pieces");
        pieces = board.getMoveablePiecesForPlayer(game.player1, 5);
        assertEquals(1, pieces.length, "Amount of moveable pieces");
        pieces = board.getMoveablePiecesForPlayer(game.player1, 6);
        assertEquals(1, pieces.length, "Amount of moveable pieces");

        assertFalse(board.moveByAmount(game.player1.pieces[1], 1));
        pieces = board.getMoveablePiecesForPlayer(game.player1, 6);
        assertEquals(2, pieces.length, "Amount of moveable pieces");
        pieces = board.getMoveablePiecesForPlayer(game.player1, 1);
        assertEquals(3, pieces.length, "Amount of moveable pieces");

        game = new Game(null);
        board = new Board();

        board.putTo(game.player1.pieces[0], 2);
        game.player1.pieces[0].progress = Board.TOTAL_PROGRESS;
        pieces = board.getMoveablePiecesForPlayer(game.player1, 1);
        assertEquals(2, pieces.length, "Amount of moveable pieces");

        pieces = board.getMoveablePiecesForPlayer(game.player1, 5);
        assertEquals(1, pieces.length, "Amount of moveable pieces");
        board.moveByAmount(game.player1.pieces[0], 1);

        pieces = board.getMoveablePiecesForPlayer(game.player1, 6);
        assertEquals(1, pieces.length, "Amount of moveable pieces");
    }

    @Test
    void movingIntoSafeAreaWorks() {
        Game game = new Game(null);
        Board board = new Board();

        board.putTo(game.player1.pieces[0], 2);
        game.player1.pieces[0].progress = Board.TOTAL_PROGRESS;
        assertFalse(board.moveByAmount(game.player1.pieces[0], 1));
        assertEquals(0, board.piecesOnBoard());

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
                "     ║░█1█░║     \n" +
                "     ║░░░░░║     \n" +
                "     ╚═════╝     ";
        assertEquals(expected, board.toString(game.player1, game.player2, game.player3, game.player4));

        assertFalse(board.moveByAmount(game.player1.pieces[0], 4));
        board.putTo(game.player1.pieces[1], 2);
        game.player1.pieces[1].progress = Board.TOTAL_PROGRESS;
        assertFalse(board.moveByAmount(game.player1.pieces[1], 2));
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
                "║░░░░░░█1█░░░░░░║\n" +
                "╚════╗░█░█░╔════╝\n" +
                "     ║░█░█░║     \n" +
                "     ║░█1█░║     \n" +
                "     ║░█░█░║     \n" +
                "     ║░░░░░║     \n" +
                "     ╚═════╝     ";
        assertEquals(expected, board.toString(game.player1, game.player2, game.player3, game.player4));

        board.putTo(game.player3.pieces[0], 28);
        game.player3.pieces[0].progress = Board.TOTAL_PROGRESS;
        assertFalse(board.moveByAmount(game.player3.pieces[0], 1));
        expected = "     ╔═════╗     \n" +
                "     ║░░░░░║     \n" +
                "     ║░█3█░║     \n" +
                "     ║░█░█░║     \n" +
                "     ║░█░█░║     \n" +
                "╔════╝░█░█░╚════╗\n" +
                "║░░░░░░█░█░░░░░░║\n" +
                "║░█████████████░║\n" +
                "║░░░░░░█░█░░░░░░║\n" +
                "║░█████████████░║\n" +
                "║░░░░░░█1█░░░░░░║\n" +
                "╚════╗░█░█░╔════╝\n" +
                "     ║░█░█░║     \n" +
                "     ║░█1█░║     \n" +
                "     ║░█░█░║     \n" +
                "     ║░░░░░║     \n" +
                "     ╚═════╝     ";
        assertEquals(expected, board.toString(game.player1, game.player2, game.player3, game.player4));

        assertFalse(board.moveByAmount(game.player3.pieces[0], 3));
        expected = "     ╔═════╗     \n" +
                "     ║░░░░░║     \n" +
                "     ║░█░█░║     \n" +
                "     ║░█░█░║     \n" +
                "     ║░█░█░║     \n" +
                "╔════╝░█3█░╚════╗\n" +
                "║░░░░░░█░█░░░░░░║\n" +
                "║░█████████████░║\n" +
                "║░░░░░░█░█░░░░░░║\n" +
                "║░█████████████░║\n" +
                "║░░░░░░█1█░░░░░░║\n" +
                "╚════╗░█░█░╔════╝\n" +
                "     ║░█░█░║     \n" +
                "     ║░█1█░║     \n" +
                "     ║░█░█░║     \n" +
                "     ║░░░░░║     \n" +
                "     ╚═════╝     ";
        assertEquals(expected, board.toString(game.player1, game.player2, game.player3, game.player4));

        game = new Game(null);
        board = new Board();

        board.putTo(game.player2.pieces[0], 16);
        board.putTo(game.player2.pieces[1], 15);
        board.putTo(game.player2.pieces[2], 14);
        board.putTo(game.player2.pieces[3], 13);
        board.putTo(game.player2.pieces[4], 12);
        game.player2.pieces[0].progress = game.player2.pieces[1].progress = game.player2.pieces[2].progress = game.player2.pieces[3].progress = game.player2.pieces[4].progress = Board.TOTAL_PROGRESS;
        assertFalse(board.moveByAmount(game.player2.pieces[0], 5));
        assertFalse(board.moveByAmount(game.player2.pieces[1], 4));
        assertFalse(board.moveByAmount(game.player2.pieces[2], 3));
        assertFalse(board.moveByAmount(game.player2.pieces[3], 2));
        assertFalse(board.moveByAmount(game.player2.pieces[4], 1));
        expected = "     ╔═════╗     \n" +
                "     ║░░░░░║     \n" +
                "     ║░█░█░║     \n" +
                "     ║░█░█░║     \n" +
                "     ║░█░█░║     \n" +
                "╔════╝░█░█░╚════╗\n" +
                "║░░░░░░█░█░░░░░░║\n" +
                "║░█████████████░║\n" +
                "║░░░░░░█░█22222░║\n" +
                "║░█████████████░║\n" +
                "║░░░░░░█░█░░░░░░║\n" +
                "╚════╗░█░█░╔════╝\n" +
                "     ║░█░█░║     \n" +
                "     ║░█░█░║     \n" +
                "     ║░█░█░║     \n" +
                "     ║░░░░░║     \n" +
                "     ╚═════╝     ";
        assertEquals(expected, board.toString(game.player1, game.player2, game.player3, game.player4));
    }

    @Test
    void isBlockedBySelfWorksCorrectly() {
        Game game = new Game(null);
        Board board = new Board();

        assertFalse(board.putTo(game.player3.pieces[0], 34));
        assertFalse(board.putTo(game.player3.pieces[1], 36));

        assertTrue(board.isBlockedBySelf(game.player3.pieces[0], 2));
    }

    @Test
    void movePieceByAmountWorksCorrectly() {
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
                "     ║1░░░░║     \n" +
                "     ╚═════╝     ";
        assertFalse(board.putOnBoard(game.player1.pieces[0]));
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
                "     ║░█░█1║     \n" +
                "     ║░░░░░║     \n" +
                "     ╚═════╝     ";
        assertFalse(board.moveByAmount(game.player1.pieces[0], 5));
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
                "     ║░█░█1║     \n" +
                "     ║░░░░░║     \n" +
                "     ╚═════╝     ";
        assertFalse(board.putOnBoard(game.player2.pieces[0]));
        assertEquals(expected, board.toString(game.player1, game.player2, game.player3, game.player4));

        expected = "     ╔═════╗     \n" +
                "     ║░░░░░║     \n" +
                "     ║░█░█░║     \n" +
                "     ║░█░█░║     \n" +
                "     ║░█░█░║     \n" +
                "╔════╝░█░█░╚════╗\n" +
                "║░░░░░░█░█░░░░2░║\n" +
                "║░█████████████░║\n" +
                "║░░░░░░█░█░░░░░░║\n" +
                "║░█████████████░║\n" +
                "║░░░░░░█░█░░░░░░║\n" +
                "╚════╗░█░█░╔════╝\n" +
                "     ║░█░█░║     \n" +
                "     ║░█░█░║     \n" +
                "     ║░█░█1║     \n" +
                "     ║░░░░░║     \n" +
                "     ╚═════╝     ";
        assertFalse(board.moveByAmount(game.player2.pieces[0], 5));
        assertEquals(2, board.piecesOnBoard(), "Pieces on board");
        assertEquals(expected, board.toString(game.player1, game.player2, game.player3, game.player4));
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

        expected = "     ╔═════╗     \n" +
                "     ║░░░░░║     \n" +
                "     ║░█░█░║     \n" +
                "     ║░█░█░║     \n" +
                "     ║░█░█░║     \n" +
                "╔════╝░█░█░╚════╗\n" +
                "║4░░░░░█░█░░2░░░║\n" +
                "║░█████████████░║\n" +
                "║░░░░░░█░█░░░░░░║\n" +
                "║░█████████████░║\n" +
                "║░░░░░░█░█░░░░░░║\n" +
                "╚════╗░█░█░╔════╝\n" +
                "     ║░█░█░║     \n" +
                "     ║░█░█░║     \n" +
                "     ║1█░█░║     \n" +
                "     ║░░░░░║     \n" +
                "     ╚═════╝     ";
        assertFalse(board.putTo(game.player3.pieces[0], 21));
        assertFalse(board.moveByAmount(game.player2.pieces[0], 6));
        assertEquals(3, board.piecesOnBoard(), "Pieces on board");
        assertTrue(board.moveByAmount(game.player2.pieces[0], 1));
        assertFalse(board.putOnBoard(game.player4.pieces[0]));
        assertEquals(3, board.piecesOnBoard(), "Pieces on board");
        assertEquals(expected, board.toString(game.player1, game.player2, game.player3, game.player4));
    }
}
