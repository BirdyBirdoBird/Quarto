/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Quarto.Logics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Quarto.Globals;
import Quarto.Utils.Move;

public class GameLogic {

    private byte turns;

    public GameLogic() {
        Globals.logicBoard = new byte[4][4];
        for (byte i = 0; i < 4; i++) {
            for (byte k = 0; k < 4; k++) {
                Globals.emptySquares.add(new Move(i, k));
            }
        } 
        turns = 1;
    }

    public void addPiece(byte row, byte col, byte piece) {
        //System.out.prbyteln(piece);
        Globals.logicBoard[row][col] = piece;
    }

    public boolean isGameOver(boolean isSim) {
        if(!isSim){
            turns++;
        }

        for (byte i = 0; i < 4; i++) {
            if (checkLine(getRow(i)) || checkLine(getColumn(i))) {
                return true;
            }
        }
        return checkLine(getDiagonal(true)) || checkLine(getDiagonal(false));
    }

    public boolean isDraw(){
        return turns == 16;
    }

    public static byte[] getRow(byte row) {
        return Globals.logicBoard[row];
    }

    public static byte[] getColumn(byte col) {
        byte[] column = new byte[4];
        for (byte i = 0; i < 4; i++) {
            column[i] = Globals.logicBoard[i][col];
        }
        return column;
    }

    public static byte[] getDiagonal(boolean main) {
        byte[] diagonal = new byte[4];
        for (byte i = 0; i < 4; i++) {
            diagonal[i] = main ? Globals.logicBoard[i][i] : Globals.logicBoard[i][3 - i];
        }
        return diagonal;
    }

    public static boolean checkLine(byte[] line) {
        if (line[0] == 0) return false;

        byte andBits = (byte) (line[0] - 1);
        byte orBits = (byte) (line[0] - 1);

        for (byte i = 1; i < line.length; i++) {
            if (line[i] == 0) return false;

            byte pieceBits = (byte) (line[i] - 1);
            andBits &= pieceBits;
            orBits |= pieceBits;
        }

        return (andBits | ~orBits & 0b1111) != 0;
    }

    public void removePiece(byte i, byte j){
        Globals.logicBoard[i][j] = 0;
    }

    public static boolean checkLineWithPiece(byte[] line, byte newPieceBits) {
        byte[] lineCopy = Arrays.copyOf(line, 4);

        for (byte i = 0; i < 4; i++) {
            if (lineCopy[i] == 0) {
                lineCopy[i] = (byte) (newPieceBits + 1); // simulate placing the piece (encoded 1–16)
                if (checkLine(lineCopy)) return true;
                lineCopy[i] = 0; // reset
            }
        }

        return false;
    }

    public static Move getWinningMove() {
        byte currentPiece = Globals.logicControl.getFirst();
        byte pieceBits = (byte) (currentPiece - 1);

        for (byte row = 0; row < 4; row++) {
            byte[] line = getRow(row);
            if (canWinInLine(line, pieceBits)) {
                byte col = findEmptyInLine(line);
                return new Move(row, col);
            }

            line = getColumn(row);
            if (canWinInLine(line, pieceBits)) {
                byte col = row;
                byte rowIndex = findEmptyInLine(line);
                return new Move(rowIndex, col);
            }
        }


        byte[] diag1 = getDiagonal(true);
        if (canWinInLine(diag1, pieceBits)) {
            byte idx = findEmptyInLine(diag1);
            return new Move(idx, idx);
        }

        byte[] diag2 = getDiagonal(false);
        if (canWinInLine(diag2, pieceBits)) {
            byte idx = findEmptyInLine(diag2);
            return new Move(idx, (byte) (3 - idx));
        }

        return null;
    }

    private static boolean canWinInLine(byte[] line, byte pieceBits) {
        return checkLineWithPiece(line, pieceBits);
    }

    private static byte findEmptyInLine(byte[] line) {
        for (byte i = 0; i < 4; i++) {
            if (line[i] == 0) return i;
        }
        return -1; // Should never happen if line is winning
    }

    public static List<Byte> getDangerousPieces() {
        List<Byte> dangerous = new ArrayList<>();
        byte[][] board = Globals.logicBoard;
        List<Move> emptySquares = getEmptySquares(board);

        for (byte candidatePiece : Globals.logicControl) {
            for (Move move : emptySquares) {
                byte row = move.getRow();
                byte col = move.getCol();

                // Simulate placing the piece
                board[row][col] = candidatePiece;

                boolean rowWin = GameLogic.checkLine(GameLogic.getRow(row));
                boolean colWin = GameLogic.checkLine(GameLogic.getColumn(col));
                boolean diag1Win = (row == col) && GameLogic.checkLine(GameLogic.getDiagonal(true));
                boolean diag2Win = (row + col == 3) && GameLogic.checkLine(GameLogic.getDiagonal(false));

                board[row][col] = 0; // Undo the simulation

                if (rowWin || colWin || diag1Win || diag2Win) {
                    dangerous.add(candidatePiece);
                    break; // This piece is dangerous — no need to check other positions
                }
            }
        }

        return dangerous;
    }


    public static List<Byte> getSafePieces() {
        List<Byte> allAvailable = new ArrayList<>(Globals.logicControl);
        List<Byte> dangerous = GameLogic.getDangerousPieces();

        List<Byte> safe = new ArrayList<>();

        for (byte piece : allAvailable) {
            if (!dangerous.contains(piece)) {
                safe.add(piece);
            }
        }

        return safe;
    }

    public static Move getOpportunityMove() {
        byte[][] board = Globals.logicBoard;
        byte myPiece = Globals.logicControl.getFirst();
        List<Move> emptySquares = getEmptySquares(board);

        // Score all moves
        List<Move> sortedMoves = new ArrayList<>(emptySquares);
        sortedMoves.sort((a, b) -> {
            byte scoreB = scoreMultiOpportunities(board, b.getRow(), b.getCol(), myPiece);
            byte scoreA = scoreMultiOpportunities(board, a.getRow(), a.getCol(), myPiece);
            return Byte.compare(scoreB, scoreA); 
        });

        for (Move move : sortedMoves) {
            byte row = move.getRow();
            byte col = move.getCol();

            board[row][col] = myPiece; // Simulate

            List<Byte> remaining = new ArrayList<>(Globals.logicControl);
            remaining.remove(Byte.valueOf(myPiece));

            boolean hasSafePiece = false;

            for (byte piece : remaining) {
                for (Move replyMove : emptySquares) {
                    if (replyMove.equals(move)) continue;

                    byte r = replyMove.getRow();
                    byte c = replyMove.getCol();

                    board[r][c] = piece;

                    boolean win =
                        GameLogic.checkLine(GameLogic.getRow(r)) ||
                        GameLogic.checkLine(GameLogic.getColumn(c)) ||
                        (r == c && GameLogic.checkLine(GameLogic.getDiagonal(true))) ||
                        (r + c == 3 && GameLogic.checkLine(GameLogic.getDiagonal(false)));

                    board[r][c] = 0;

                    if (!win) {
                        hasSafePiece = true;
                        break;
                    }
                }

                if (hasSafePiece) break;
            }

            board[row][col] = 0;

            if (hasSafePiece) return move;
        }

        // If no move leaves a safe piece, just return the best scoring one anyway
        return sortedMoves.isEmpty() ? null : sortedMoves.get(0);
    }

    public static byte scoreMultiOpportunities(byte[][] board, byte row, byte col, byte piece) {
        byte score = 0;

        board[row][col] = piece; // simulate

        byte[][] lines = {
            board[row], // Row
            getColumn(col), // Column
            (row == col) ? getDiagonal(true) : null,
            (row + col == 3) ? getDiagonal(false) : null
        };

        for (byte[] line : lines) {
            if (line == null) continue;

            byte traitCount = countSharedTrait(line);
            if (traitCount >= 2 && traitCount < 4) {
                score += traitCount; // add more for longer sequences
            }
        }

        board[row][col] = 0; // undo
        return score;
    }

    private static byte countSharedTrait(byte[] line) {
        byte andBits = -1;
        byte orNotBits = ~0;
        byte filled = 0;

        for (byte val : line) {
            if (val == 0) continue;
            byte bits = (byte) (val - 1);
            andBits &= bits;
            orNotBits &= ~bits;
            filled++;
        }

        return ((andBits | orNotBits) != 0) ? filled : 0;
    }

    public static byte[] countSharedTraitsIn3AlignedLines(byte row, byte col) {
        byte[][] board = Globals.logicBoard;

        byte[][] lines = {
            board[row],
            GameLogic.getColumn(col),
            (row == col) ? GameLogic.getDiagonal(true) : null,
            (row + col == 3) ? GameLogic.getDiagonal(false) : null
        };

        byte[] traitMatches = new byte[4];

        for (byte[] line : lines) {
            if (line == null) continue;

            byte filled = 0;
            byte andBits = -1;
            byte orNotBits = ~0;

            for (byte val : line) {
                if (val == 0) continue;
                byte bits = (byte) (val - 1);
                andBits &= bits;
                orNotBits &= ~bits;
                filled++;
            }

            if (filled == 3 && ((andBits | orNotBits) & 0b1111) != 0) {
                for (byte bit = 0; bit < 4; bit++) {
                    boolean shared = ((andBits >> bit) & 1) == 1 || ((orNotBits >> bit) & 1) == 1;
                    if (shared) traitMatches[bit]++;
                }
            }
        }

        return traitMatches;
    }

    public static List<Move> getEmptySquares(byte[][] board) {
        return Globals.emptySquares;
    }
}