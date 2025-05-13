/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Quarto.Logics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import Quarto.Globals;
import Quarto.Utils.Move;
import Quarto.Utils.Utils;

public class GameLogic {

    private int turns;

    public GameLogic() {
        Globals.logicBoard = new int[4][4]; 
        turns = 1;
    }

    public void addPiece(int row, int col, int piece) {
        //System.out.println(piece);
        Globals.logicBoard[row][col] = piece;
    }



    public boolean isGameOver(boolean isSim) {
        if(!isSim){
            turns++;
        }

        for (int i = 0; i < 4; i++) {
            if (checkLine(getRow(i)) || checkLine(getColumn(i))) {
                return true;
            }
        }
        return checkLine(getDiagonal(true)) || checkLine(getDiagonal(false));
    }

    public boolean isDraw(){
        return turns == 16;
    }

    public static int[] getRow(int row) {
        return Globals.logicBoard[row];
    }

    public static int[] getColumn(int col) {
        int[] column = new int[4];
        for (int i = 0; i < 4; i++) {
            column[i] = Globals.logicBoard[i][col];
        }
        return column;
    }

    public static int[] getDiagonal(boolean main) {
        int[] diagonal = new int[4];
        for (int i = 0; i < 4; i++) {
            diagonal[i] = main ? Globals.logicBoard[i][i] : Globals.logicBoard[i][3 - i];
        }
        return diagonal;
    }

    public static boolean checkLine(int[] line) {
        if (line[0] == 0) return false;

        int andBits = line[0] - 1;
        int orBits = line[0] - 1;

        for (int i = 1; i < line.length; i++) {
            if (line[i] == 0) return false;

            int pieceBits = line[i] - 1;
            andBits &= pieceBits;
            orBits |= pieceBits;
        }

        return (andBits | ~orBits & 0b1111) != 0;
    }

    public void removePiece(int i, int j){
        Globals.logicBoard[i][j] = 0;
    }

    public static boolean checkLineWithPiece(int[] line, int newPieceBits) {
        int[] lineCopy = Arrays.copyOf(line, 4);

        for (int i = 0; i < 4; i++) {
            if (lineCopy[i] == 0) {
                lineCopy[i] = newPieceBits + 1; // simulate placing the piece (encoded 1–16)
                if (checkLine(lineCopy)) return true;
                lineCopy[i] = 0; // reset
            }
        }

        return false;
    }

    public static Move getWinningMove() {
        int currentPiece = Globals.logicControl.getFirst();
        int pieceBits = currentPiece - 1;

        for (int row = 0; row < 4; row++) {
            int[] line = getRow(row);
            if (canWinInLine(line, pieceBits)) {
                int col = findEmptyInLine(line);
                return new Move(row, col);
            }

            line = getColumn(row);
            if (canWinInLine(line, pieceBits)) {
                int col = row;
                int rowIndex = findEmptyInLine(line);
                return new Move(rowIndex, col);
            }
        }


        int[] diag1 = getDiagonal(true);
        if (canWinInLine(diag1, pieceBits)) {
            int idx = findEmptyInLine(diag1);
            return new Move(idx, idx);
        }

        int[] diag2 = getDiagonal(false);
        if (canWinInLine(diag2, pieceBits)) {
            int idx = findEmptyInLine(diag2);
            return new Move(idx, 3 - idx);
        }

        return null;
    }

    private static boolean canWinInLine(int[] line, int pieceBits) {
        return checkLineWithPiece(line, pieceBits);
    }

    private static int findEmptyInLine(int[] line) {
        for (int i = 0; i < 4; i++) {
            if (line[i] == 0) return i;
        }
        return -1; // Should never happen if line is winning
    }

    public static List<Integer> getDangerousPieces() {
        List<Integer> dangerous = new ArrayList<>();
        int[][] board = Globals.logicBoard;
        List<Move> emptySquares = getEmptySquares(board);

        for (int candidatePiece : Globals.logicControl) {
            for (Move move : emptySquares) {
                int row = move.getRow();
                int col = move.getCol();

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


    public static List<Integer> getSafePieces() {
        List<Integer> allAvailable = new ArrayList<>(Globals.logicControl);
        List<Integer> dangerous = GameLogic.getDangerousPieces();

        List<Integer> safe = new ArrayList<>();

        for (int piece : allAvailable) {
            if (!dangerous.contains(piece)) {
                safe.add(piece);
            }
        }

        return safe;
    }

    public static Move getOpportunityMove() {
        int[][] board = Globals.logicBoard;
        int myPiece = Globals.logicControl.getFirst();
        List<Move> emptySquares = getEmptySquares(board);

        // Score all moves
        List<Move> sortedMoves = new ArrayList<>(emptySquares);
        sortedMoves.sort((a, b) -> {
            int scoreB = scoreMultiOpportunities(board, b.getRow(), b.getCol(), myPiece);
            int scoreA = scoreMultiOpportunities(board, a.getRow(), a.getCol(), myPiece);
            return Integer.compare(scoreB, scoreA); // descending order
        });

        for (Move move : sortedMoves) {
            int row = move.getRow();
            int col = move.getCol();

            board[row][col] = myPiece; // Simulate

            List<Integer> remaining = new ArrayList<>(Globals.logicControl);
            remaining.remove(Integer.valueOf(myPiece));

            boolean hasSafePiece = false;

            for (int piece : remaining) {
                for (Move replyMove : emptySquares) {
                    if (replyMove.equals(move)) continue;

                    int r = replyMove.getRow();
                    int c = replyMove.getCol();

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

    public static int scoreMultiOpportunities(int[][] board, int row, int col, int piece) {
        int score = 0;

        board[row][col] = piece; // simulate

        int[][] lines = {
            board[row],                             // Row
            getColumn(col),                  // Column
            (row == col) ? getDiagonal(true) : null,
            (row + col == 3) ? getDiagonal(false) : null
        };

        for (int[] line : lines) {
            if (line == null) continue;

            int traitCount = countSharedTrait(line);
            if (traitCount >= 2 && traitCount < 4) {
                score += traitCount; // add more for longer sequences
            }
        }

        board[row][col] = 0; // undo
        return score;
    }

    private static int countSharedTrait(int[] line) {
        int andBits = -1;
        int orNotBits = ~0;
        int filled = 0;

        for (int val : line) {
            if (val == 0) continue;
            int bits = val - 1;
            andBits &= bits;
            orNotBits &= ~bits;
            filled++;
        }

        return ((andBits | orNotBits) != 0) ? filled : 0;
    }

    public static int[] countSharedTraitsIn3AlignedLines(int row, int col) {
        int[][] board = Globals.logicBoard;

        int[][] lines = {
            board[row],
            GameLogic.getColumn(col),
            (row == col) ? GameLogic.getDiagonal(true) : null,
            (row + col == 3) ? GameLogic.getDiagonal(false) : null
        };

        int[] traitMatches = new int[4];

        for (int[] line : lines) {
            if (line == null) continue;

            int filled = 0;
            int andBits = -1;
            int orNotBits = ~0;

            for (int val : line) {
                if (val == 0) continue;
                int bits = val - 1;
                andBits &= bits;
                orNotBits &= ~bits;
                filled++;
            }

            if (filled == 3 && ((andBits | orNotBits) & 0b1111) != 0) {
                for (int bit = 0; bit < 4; bit++) {
                    boolean shared = ((andBits >> bit) & 1) == 1 || ((orNotBits >> bit) & 1) == 1;
                    if (shared) traitMatches[bit]++;
                }
            }
        }

        return traitMatches;
    }

    public static List<Move> getEmptySquares(int[][] board) {
        List<Move> emptySquares = new ArrayList<>();
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                if (board[row][col] == 0) {
                    emptySquares.add(new Move(row, col));
                }
            }
        }
        return emptySquares;
    }
}