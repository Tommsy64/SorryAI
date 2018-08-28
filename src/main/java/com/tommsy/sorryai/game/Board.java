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

/**
 * Handles {@link GamePiece GamePieces} and their movement.
 */
public class Board {
    // The program is not written to allow these values to change
    static final int SIZE = 56;
    static final int TOTAL_PROGRESS = SIZE + 2;
    /**
     * Used to lookup {@linkplain GamePiece GamePieces} by their {@link GamePiece#boardIndex absolute positions}.
     */
    final GamePiece[] board = new GamePiece[SIZE + 1]; // Add extra space for center position

    /**
     * @param player
     * @param amount
     * @return Game pieces that can be moved the specified amount by the player
     */
    GamePiece[] getMoveablePiecesForPlayer(Player player, int amount) {
        ArrayList<GamePiece> moveablePieces = new ArrayList<>(Player.PIECES_PER_PLAYER);

        // Putting pieces on the board
        if (amount == 1 || amount == 6)
            for (int i = 0; i < player.pieces.length; i++)
                if (player.pieces[i].progress == -1 && canPutToStart(player.pieces[i])) {
                    moveablePieces.add(player.pieces[i]);
                    break;
                }

        for (int i = 0; i < player.pieces.length; i++) {
            if (player.pieces[i].progress == -1) // These pieces were checked and added above
                continue;
            // A piece that is in the center and can exit it
            if (player.pieces[i].progress == GamePiece.CENTER_PROGRESS) {
                if (amount == 1 && canExitCenter(player.pieces[i]))
                    moveablePieces.add(player.pieces[i]);
                continue;
            }

            // A piece that can move to the center, must move to the center, and is not blocked on its way to the center
            if (player.pieces[i].canMoveToCenter = canEnterCenter(player.pieces[i], amount) &&
                    (player.pieces[i].mustMoveToCenter = !canMoveByAmount(player.pieces[i], amount)) &&
                    canMoveByAmount(player.pieces[i], amount - 1)) {
                moveablePieces.add(player.pieces[i]);
                continue;
            }
            // Otherwise if a piece doesn't need to or cannot enter the center, just add the piece if it can move the amount
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
     */
    private boolean canExitCenter(GamePiece piece) {
        return !(board[ExitDirection.ONE.position] != null && board[ExitDirection.ONE.position].getPlayer() == piece.getPlayer()) &&
                !(board[ExitDirection.TWO.position] != null && board[ExitDirection.TWO.position].getPlayer() == piece.getPlayer()) &&
                !(board[ExitDirection.THREE.position] != null && board[ExitDirection.THREE.position].getPlayer() == piece.getPlayer()) &&
                !(board[ExitDirection.FOUR.position] != null && board[ExitDirection.FOUR.position].getPlayer() == piece.getPlayer());
    }

    /**
     * Assumes the dice was 1 and that the piece is in the center.
     */
    boolean canExitCenterTo(GamePiece piece, ExitDirection exit) {
        return board[exit.position] == null || board[exit.position].getPlayer() != piece.getPlayer();
    }

    /**
     * Does not check if the piece is blocked by itself before reaching the center.
     */
    boolean canEnterCenter(GamePiece piece, int amount) {
        int centerAmount = piece.progress + amount - 1;
        return (centerAmount == ExitDirection.ONE.position || centerAmount == ExitDirection.TWO.position || centerAmount == ExitDirection.THREE.position
                || centerAmount == ExitDirection.FOUR.position) && !isCenterBlocked(piece);
    }

    /**
     * @return True if the center has a piece with the same player as the specified piece
     */
    private boolean isCenterBlocked(GamePiece piece) {
        return board[board.length - 1] != null && board[board.length - 1].getPlayer() == piece.getPlayer();
    }

    /**
     * Checks if the piece {@linkplain #isOutOfBounds(GamePiece, int) is out of bounds} or {@linkplain #isBlockedBySelf(GamePiece, int) is blocked by self}.
     */
    boolean canMoveByAmount(GamePiece piece, int amount) {
        if (isOutOfBounds(piece, amount) || isBlockedBySelf(piece, amount))
            return false;
        return true;
    }

    /**
     * @return True if the piece would go out of bounds or has not yet been placed on the board.
     */
    private boolean isOutOfBounds(GamePiece piece, int amount) {
        return piece.progress + amount > TOTAL_PROGRESS + Player.PIECES_PER_PLAYER || piece.progress < 0;
    }

    /**
     * Does no out of bounds checking, which is handled by {@linkplain #isOutOfBounds(GamePiece, int)}.
     * <p>
     * Does not try entering center, which is handled by {@linkplain #canEnterCenter(GamePiece, int)}.
     *
     * @param piece
     * @param amount
     * @return
     */
    boolean isBlockedBySelf(GamePiece piece, int amount) {
        final int initialAmount = amount;
        // The amount of progress into the piece's player's safe area.
        int safeAreaAmount = piece.progress + amount - TOTAL_PROGRESS;
        // Handle progress in the safe area
        if (safeAreaAmount > 0) {
            // Subtract safe area progress from the regular progress because the other half of the method handles progress outside the safe area
            amount -= safeAreaAmount;
            Player player = piece.getPlayer();
            for (int i = 0; i < player.pieces.length; i++) {
                // Make sure to check if the player piece isn't the one passed to this method
                if (player.pieces[i] != piece && player.pieces[i].progress > TOTAL_PROGRESS && player.pieces[i].progress <= piece.progress + initialAmount)
                    return true;
            }
        }

        // Check each position in front of the piece to see if it contains a piece belonging to the same player
        for (; amount > 0; amount--) {
            int destination = (piece.boardIndex + amount) % SIZE;
            if (board[destination] != null && board[destination].getPlayer() == piece.getPlayer())
                return true;
        }

        return false;
    }

    /**
     * Puts a piece to its player's {@link Player#startingPosition starting position}.
     * <p>
     * Does not check if the starting position is blocked by the piece's player which is handled by {@link #canPutToStart(GamePiece)}.
     *
     * @return True if a piece was eaten.
     */
    boolean putOnBoard(GamePiece piece) {
        piece.progress = 0;
        return putTo(piece, piece.getPlayer().startingPosition);
    }

    /**
     * Does not check if the piece {@link #canExitCenterTo(GamePiece, ExitDirection)} the specified {@linkplain ExitDirection}.
     *
     * @return True if a piece was eaten.
     */
    boolean exitCenter(GamePiece piece, ExitDirection exitDirection) {
        // Shift absolute position by the starting position of the player. Then, wrap values that go above SIZE or below 0.
        piece.progress = (SIZE + exitDirection.position - piece.getPlayer().startingPosition) % SIZE;
        return putTo(piece, exitDirection.position);
    }

    /**
     * Does not check if the center is blocked, which is handled by {@link #isCenterBlocked(GamePiece)}.
     *
     * @return True if a piece was eaten.
     */
    boolean moveToCenter(GamePiece piece) {
        piece.progress = GamePiece.CENTER_PROGRESS;
        return putTo(piece, board.length - 1);
    }

    /**
     * Does no validity checking of the move.
     *
     * @param amount Amount of positions to move piece
     * @return True if a piece was eaten.
     */
    boolean moveByAmount(GamePiece piece, int amount) {
        piece.progress += amount;
        if (piece.progress > TOTAL_PROGRESS) { // Piece has reached the safe area
            if (piece.boardIndex >= 0) // Piece is no longer on board
                board[piece.boardIndex] = null;
            piece.boardIndex = -1;
            return false;
        }
        int to = (piece.boardIndex + amount) % SIZE; // Modulo by SIZE to wrap around the board array
        return putTo(piece, to);
    }

    /**
     * Does not update progress. Does no validity checking.
     *
     * @param index board index of position to move piece
     * @return True if a piece was eaten.
     */
    boolean putTo(GamePiece piece, int index) {
        if (piece.boardIndex >= 0)
            board[piece.boardIndex] = null;
        GamePiece toPiece = board[index];
        board[index] = piece;
        piece.boardIndex = index;
        if (toPiece != null) {
            toPiece.progress = toPiece.boardIndex = -1;
            if (toPiece.getPlayer() == piece.getPlayer()) // Should never happen.
                throw new IllegalArgumentException("A player ate its own piece!");
            return true;
        }
        return false;
    }

    /**
     * @return True of all the player's pieces are in the safe area.
     */
    boolean hasPlayerWon(Player player) {
        for (int i = 0; i < player.pieces.length; i++)
            if (player.pieces[i].progress <= Board.TOTAL_PROGRESS)
                return false;
        return true;
    }

    /**
     * @return The number of non-null entries in {@link #board}.
     */
    int piecesOnBoard() {
        int pieces = 0;
        for (GamePiece piece : board)
            if (piece != null)
                pieces++;
        return pieces;
    }

    private static final String NO_PIECE_STRING = "░";

    /**
     * Due to not having access to the {@link Player} objects of a {@link Game}, the safe areas are filled with question marks.
     */
    @Override
    public String toString() {
        return toString(null, null, null, null);
    }

    /**
     * An example:
     *
     * <pre>
     *
     *       ╔═════╗
     *       ║░░░░░║
     *       ║3█░█░║
     *       ║░█░█░║
     *       ║░█░█░║
     *  ╔════╝░█░█░╚════╗
     *  ║4░░░░░█░█2░░░░░║
     *  ║3█████████████1║
     *  ║░░░░░░█1█░░░░░░║
     *  ║░█████████████░║
     *  ║░░░░░░█░█░░░░░░║
     *  ╚════╗░█░█░╔════╝
     *       ║░█░█░║
     *       ║░█░█░║
     *       ║░█░█░║
     *       ║░4░░░║
     *       ╚═════╝
     * </pre>
     *
     * @return A human-readable representation of the {@linkplain Board board}.
     */
    public String toString(Player player1, Player player2, Player player3, Player player4) {
        StringBuilder sb = new StringBuilder();

        sb.append("     ╔═════╗     \n     ║");
        boardToStringReverse(sb, 32, 5).append("║     \n     ║");
        boardToString(sb, 33).append("█");
        playerSafeAreaToString(sb, player3, 1).append("█");
        boardToString(sb, 27).append("║     \n     ║");
        boardToString(sb, 34).append("█");
        playerSafeAreaToString(sb, player3, 2).append("█");
        boardToString(sb, 26).append("║     \n     ║");
        boardToString(sb, 35).append("█");
        playerSafeAreaToString(sb, player3, 3).append("█");
        boardToString(sb, 25).append("║     \n╔════╝");

        boardToString(sb, 36).append("█");
        playerSafeAreaToString(sb, player3, 4).append("█");
        boardToString(sb, 24).append("╚════╗\n║");

        boardToStringReverse(sb, 42, 6).append("█");
        playerSafeAreaToString(sb, player3, 5).append("█");
        boardToStringReverse(sb, 23, 6).append("║\n║");

        boardToString(sb, 43).append("█████████████");
        boardToString(sb, 17).append("║\n║");

        boardToString(sb, 44);

        playerSafeAreaToString(sb, player4, 1);
        playerSafeAreaToString(sb, player4, 2);
        playerSafeAreaToString(sb, player4, 3);
        playerSafeAreaToString(sb, player4, 4);
        playerSafeAreaToString(sb, player4, 5).append("█");
        boardToString(sb, board.length - 1).append("█");
        playerSafeAreaToString(sb, player2, 1);
        playerSafeAreaToString(sb, player2, 2);
        playerSafeAreaToString(sb, player2, 3);
        playerSafeAreaToString(sb, player2, 4);
        playerSafeAreaToString(sb, player2, 5);
        boardToString(sb, 16).append("║\n║");

        boardToString(sb, 45).append("█████████████");
        boardToString(sb, 15).append("║\n║");

        boardToString(sb, 46, 6).append("█");
        playerSafeAreaToString(sb, player1, 5).append("█");
        boardToString(sb, 9, 6).append("║\n╚════╗");

        boardToString(sb, 52).append("█");
        playerSafeAreaToString(sb, player1, 4).append("█");
        boardToString(sb, 8).append("╔════╝\n     ║");

        boardToString(sb, 53).append("█");
        playerSafeAreaToString(sb, player1, 3).append("█");
        boardToString(sb, 7).append("║     \n     ║");

        boardToString(sb, 54).append("█");
        playerSafeAreaToString(sb, player1, 2).append("█");
        boardToString(sb, 6).append("║     \n     ║");

        boardToString(sb, 55).append("█");
        playerSafeAreaToString(sb, player1, 1).append("█");
        boardToString(sb, 5).append("║     \n     ║");

        boardToString(sb, 0, 5).append("║     \n     ╚═════╝     ");

        return sb.toString();
    }

    /*
     * String building helper functions.
     */

    private StringBuilder boardToString(StringBuilder sb, int position) {
        GamePiece piece;
        return sb.append((piece = board[position]) == null ? NO_PIECE_STRING : piece.getPlayer().index + 1);
    }

    private StringBuilder boardToString(StringBuilder sb, int start, int length) {
        GamePiece piece;
        for (int i = 0; i < length; i++)
            sb.append((piece = board[start + i]) == null ? NO_PIECE_STRING : piece.getPlayer().index + 1);
        return sb;
    }

    // Only difference is the minus sign in board[start - i]
    private StringBuilder boardToStringReverse(StringBuilder sb, int start, int length) {
        GamePiece piece;
        for (int i = 0; i < length; i++)
            sb.append((piece = board[start - i]) == null ? NO_PIECE_STRING : piece.getPlayer().index + 1);
        return sb;
    }

    private StringBuilder playerSafeAreaToString(StringBuilder sb, Player player, int index) {
        if (player == null)
            return sb.append("?");
        for (int i = 0; i < player.pieces.length; i++)
            if (player.pieces[i].progress > TOTAL_PROGRESS && player.pieces[i].progress - TOTAL_PROGRESS == index)
                return sb.append(player.index + 1);
        return sb.append(NO_PIECE_STRING);
    }
}
