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

import java.util.ArrayList;

import com.tommsy.sorryai.game.Player.GamePiece;

public class Board {
    static final int SIZE = 56;
    static final int TOTAL_PROGRESS = SIZE + 3;
    private final GamePiece[] board = new GamePiece[SIZE + 1]; // Add extra space for center

    private static final int CENTER_ENTRANCE_1 = 9, CENTER_ENTRANCE_2 = 23, CENTER_ENTRANCE_3 = 37, CENTER_ENTRANCE_4 = 51;

    GamePiece[] getMoveablePiecesForPlayer(Player player, int amount) {
        ArrayList<GamePiece> moveablePieces = new ArrayList<>(Player.PIECES_PER_PLAYER);

        if (amount == 1 || amount == 6) {
            for (int i = 0; i < player.pieces.length; i++)
                if (player.pieces[i].progress == -1 && canPutToStart(player.pieces[i])) {
                    moveablePieces.add(player.pieces[i]);
                    break;
                }
        }

        for (int i = 0; i < player.pieces.length; i++) {
            if (player.pieces[i].progress == -1)
                continue;
            if (player.pieces[i].progress == GamePiece.CENTER_PROGRESS && amount == 1 && canExitCenter(player.pieces[i])) {
                moveablePieces.add(player.pieces[i]);
                continue;
            }
            if (player.pieces[i].canMoveToCenter = canMoveToCenter(player.pieces[i], amount)) {
                player.pieces[i].mustMoveToCenter = !canMoveByAmount(player.pieces[i], amount);
                amount--;
            }
            if (canMoveByAmount(player.pieces[i], amount))
                moveablePieces.add(player.pieces[i]);
        }

        return moveablePieces.toArray(new GamePiece[moveablePieces.size()]);
    }

    /**
     * Assumes dice roll was 1 or 6.
     *
     * @param piece
     * @return
     */
    private boolean canPutToStart(GamePiece piece) {
        int start = piece.getPlayer().startingPosition;
        if (board[start] != null && board[start].getPlayer() == piece.getPlayer())
            return false;
        return true;
    }

    /**
     * Assumes the dice was 1 and that the piece is in the center.
     *
     * @param piece
     * @return
     */
    private boolean canExitCenter(GamePiece piece) {
        return (board[CENTER_ENTRANCE_1] != null && board[CENTER_ENTRANCE_1].getPlayer() != piece.getPlayer()) ||
                (board[CENTER_ENTRANCE_2] != null && board[CENTER_ENTRANCE_2].getPlayer() != piece.getPlayer()) ||
                (board[CENTER_ENTRANCE_3] != null && board[CENTER_ENTRANCE_3].getPlayer() != piece.getPlayer()) ||
                (board[CENTER_ENTRANCE_4] != null && board[CENTER_ENTRANCE_4].getPlayer() != piece.getPlayer());
    }

    boolean canMoveToCenter(GamePiece piece, int amount) {
        int centerAmount = piece.progress + amount - 1;
        return (centerAmount == CENTER_ENTRANCE_1 || centerAmount == CENTER_ENTRANCE_2 || centerAmount == CENTER_ENTRANCE_3 || centerAmount == CENTER_ENTRANCE_4)
                && !isCenterBlocked(piece);
    }

    private boolean isCenterBlocked(GamePiece piece) {
        return board[board.length - 1] != null && board[board.length - 1].getPlayer() == piece.getPlayer();
    }

    boolean canMoveByAmount(GamePiece piece, int amount) {
        if (isOutOfBounds(piece, amount) || isBlockedBySelf(piece, amount))
            return false;
        return true;
    }

    private boolean isOutOfBounds(GamePiece piece, int amount) {
        return piece.progress + amount > 64;
    }

    /**
     * Does no out of bounds checking. Does not try entering center.
     *
     * @param piece
     * @param amount
     * @return
     */
    private boolean isBlockedBySelf(GamePiece piece, int amount) {
        final int initialAmount = amount;
        int safeAreaAmount = piece.progress + amount - TOTAL_PROGRESS;
        if (safeAreaAmount > 0) {
            amount -= safeAreaAmount;
            Player player = piece.getPlayer();
            for (int i = 0; i < player.pieces.length; i++) {
                if (player.pieces[i].progress > TOTAL_PROGRESS && player.pieces[i].progress <= piece.progress + initialAmount)
                    return true;
            }
        }

        for (; amount > 0; amount--) {
            int destination = (piece.boardIndex + amount) % SIZE;
            if (board[destination] != null && board[destination].getPlayer() == piece.getPlayer())
                return true;
        }

        return false;
    }

    boolean putOnBoard(GamePiece piece) {
        piece.progress = 0;
        return putTo(piece, piece.getPlayer().startingPosition);
    }

    boolean exitCenter(GamePiece piece, int direction) {
        if (direction != CENTER_ENTRANCE_1 || direction != CENTER_ENTRANCE_2 || direction != CENTER_ENTRANCE_3 || direction != CENTER_ENTRANCE_4)
            throw new IllegalArgumentException("Exit direction invalid");
        return putTo(piece, direction);
    }

    boolean moveToCenter(GamePiece piece) {
        piece.progress = GamePiece.CENTER_PROGRESS;
        return putTo(piece, board.length - 1);
    }

    /**
     * Does no validity checking of move.
     *
     * @param from Index of piece
     * @param amount Amount of positions to move piece
     * @return Whether or not a piece was eaten.
     */
    boolean moveByAmount(GamePiece piece, int amount) {
        piece.progress += amount;
        if (piece.progress > TOTAL_PROGRESS) // Piece has reached the safe area
            return false;
        int to = (piece.boardIndex + amount) % SIZE;
        return putTo(piece, to);
    }

    /**
     * Does not update progress.
     *
     * @param piece
     * @param to
     * @return Whether or not a piece was eaten.
     */
    boolean putTo(GamePiece piece, int to) {
        if (piece.boardIndex >= 0)
            board[piece.boardIndex] = null;
        GamePiece toPiece = board[to];
        board[to] = piece;
        piece.boardIndex = to;
        if (toPiece != null) {
            toPiece.progress = toPiece.boardIndex = -1;
            if (toPiece.getPlayer() == piece.getPlayer())
                throw new IllegalArgumentException("A player ate its own piece!");
            return true;
        }
        return false;
    }

    boolean hasPlayerWon(Player player) {
        for (int i = 0; i < player.pieces.length; i++)
            if (player.pieces[i].progress <= Board.TOTAL_PROGRESS)
                return false;
        return true;
    }

    int piecesOnBoard() {
        int pieces = 0;
        for (GamePiece piece : board)
            if (piece != null)
                pieces++;
        return pieces;
    }

    private static final String NO_PIECE_STRING = "░";

    @Override
    public String toString() {
        return toString(null, null, null, null);
    }

    public String toString(Player player1, Player player2, Player player3, Player player4) {
        StringBuilder sb = new StringBuilder();

        sb.append("     ╔═════╗     \n     ║");
        boardToStringReverse(sb, 32, 5).append("║     \n     ║");
        boardToString(sb, 33).append("█");
        playerHomeToString(sb, player3, 1).append("█");
        boardToString(sb, 27).append("║     \n     ║");
        boardToString(sb, 34).append("█");
        playerHomeToString(sb, player3, 2).append("█");
        boardToString(sb, 26).append("║     \n     ║");
        boardToString(sb, 35).append("█");
        playerHomeToString(sb, player3, 3).append("█");
        boardToString(sb, 25).append("║     \n╔════╝");

        boardToString(sb, 36).append("█");
        playerHomeToString(sb, player3, 4).append("█");
        boardToString(sb, 26).append("╚════╗\n║");

        boardToStringReverse(sb, 42, 6).append("█");
        playerHomeToString(sb, player3, 5).append("█");
        boardToStringReverse(sb, 25, 6).append("║\n║");

        boardToString(sb, 43).append("█████████████");
        boardToString(sb, 17).append("║\n║");

        boardToString(sb, 44);
        playerHomeToString(sb, player4).append("█");
        boardToString(sb, board.length - 1).append("█");
        playerHomeToString(sb, player2);
        boardToString(sb, 16).append("║\n║");

        boardToString(sb, 45).append("█████████████");
        boardToString(sb, 15).append("║\n║");

        boardToString(sb, 46, 6).append("█");
        playerHomeToString(sb, player1, 5).append("█");
        boardToString(sb, 9, 6).append("║\n╚════╗");

        boardToString(sb, 52).append("█");
        playerHomeToString(sb, player1, 4).append("█");
        boardToString(sb, 8).append("╔════╝\n     ║");

        boardToString(sb, 53).append("█");
        playerHomeToString(sb, player1, 3).append("█");
        boardToString(sb, 7).append("║     \n     ║");

        boardToString(sb, 54).append("█");
        playerHomeToString(sb, player1, 2).append("█");
        boardToString(sb, 6).append("║     \n     ║");

        boardToString(sb, 55).append("█");
        playerHomeToString(sb, player1, 1).append("█");
        boardToString(sb, 5).append("║     \n     ║");

        boardToString(sb, 0, 5).append("║     \n     ╚═════╝     ");

        return sb.toString();
    }

    private StringBuilder boardToString(StringBuilder sb, int position) {
        GamePiece piece;
        return sb.append((piece = board[position]) == null ? NO_PIECE_STRING : piece.getPlayer().index);
    }

    private StringBuilder boardToString(StringBuilder sb, int start, int length) {
        GamePiece piece;
        for (int i = 0; i < length; i++)
            sb.append((piece = board[start + i]) == null ? NO_PIECE_STRING : piece.getPlayer().index);
        return sb;
    }

    private StringBuilder boardToStringReverse(StringBuilder sb, int start, int length) {
        GamePiece piece;
        for (int i = length; i > 0; i--)
            sb.append((piece = board[start + i]) == null ? NO_PIECE_STRING : piece.getPlayer().index);
        return sb;
    }

    private StringBuilder playerHomeToString(StringBuilder sb, Player player, int index) {
        if (player == null)
            return sb.append("?");
        for (int i = 0; i < player.pieces.length; i++)
            if (player.pieces[i].progress > TOTAL_PROGRESS && TOTAL_PROGRESS - player.pieces[i].progress == index)
                return sb.append(player.index);
        return sb.append(NO_PIECE_STRING);
    }

    private StringBuilder playerHomeToString(StringBuilder sb, Player player) {
        if (player == null)
            return sb.append("?");
        for (int i = 0; i < player.pieces.length; i++)
            if (player.pieces[i].progress > TOTAL_PROGRESS && TOTAL_PROGRESS - player.pieces[i].progress == i + 1)
                sb.append(player.index);
            else
                sb.append(NO_PIECE_STRING);
        return sb;
    }
}
