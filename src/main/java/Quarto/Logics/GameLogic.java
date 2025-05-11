/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Quarto.Logics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Quarto.Constants;
import Quarto.Utils.Move;
import Quarto.Utils.Utils;

public class GameLogic {

    private int turns;
    private static int[][] simulatedBoard;

    public GameLogic() {
        Constants.logicBoard = new int[4][4]; 
        simulatedBoard = new int[4][4];
        turns = 1;
    }

    public void addPiece(int row, int col, int piece) {
        //System.out.println(piece);
        Constants.logicBoard[row][col] = piece;
    }

    public static void addSimulatedPiece(int row, int col, int piece){
        simulatedBoard = Utils.getBoardCopy();
        simulatedBoard[row][col] = piece;
    }

    public int[][] getSimulatedBoard() {
        return simulatedBoard;
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
        return Constants.logicBoard[row];
    }

    public static int[] getColumn(int col) {
        int[] column = new int[4];
        for (int i = 0; i < 4; i++) {
            column[i] = Constants.logicBoard[i][col];
        }
        return column;
    }

    public static int[] getDiagonal(boolean main) {
        int[] diagonal = new int[4];
        for (int i = 0; i < 4; i++) {
            diagonal[i] = main ? Constants.logicBoard[i][i] : Constants.logicBoard[i][3 - i];
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
        Constants.logicBoard[i][j] = 0;
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
        int currentPiece = Constants.logicControl.getFirst();
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
        int[][] board = Constants.logicBoard;

        for (int candidatePiece : Constants.logicControl) {
            for (int row = 0; row < 4; row++) {
                for (int col = 0; col < 4; col++) {
                    if (board[row][col] == 0) {
                        // Simulate
                        board[row][col] = candidatePiece;

                        boolean rowWin = GameLogic.checkLine(GameLogic.getRow(row));
                        boolean colWin = GameLogic.checkLine(GameLogic.getColumn(col));
                        boolean diag1Win = (row == col) && GameLogic.checkLine(GameLogic.getDiagonal(true));
                        boolean diag2Win = (row + col == 3) && GameLogic.checkLine(GameLogic.getDiagonal(false));

                        board[row][col] = 0; // undo

                        if (rowWin || colWin || diag1Win || diag2Win) {
                            dangerous.add(candidatePiece);
                            break; // No need to check more positions for this piece
                        }
                    }
                }
            }
        }

        return dangerous;
    }



    public static Move getOpportunityMove() {
        int[][] board = Constants.logicBoard;
        int myPiece = Constants.logicControl.getFirst();

        Move bestMove = null;
        int bestScore = -1;

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                if (board[row][col] == 0) {
                    int score = scoreMultiOpportunities(board, row, col, myPiece);
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new Move(row, col);
                    }
                }
            }
        }

        return bestMove;
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
}