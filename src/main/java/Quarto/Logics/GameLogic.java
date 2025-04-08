/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Quarto.Logics;

public class GameLogic {

    private int[][] board;
    private int turns;
    public GameLogic() {
        board = new int[4][4]; 
        turns = 1;
    }

    public void addPiece(int row, int col, int piece) {
        System.out.println(piece);
        board[row][col] = piece;
    }

    public boolean isGameOver() {
        turns++;
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

    private int[] getRow(int row) {
        return board[row];
    }

    private int[] getColumn(int col) {
        int[] column = new int[4];
        for (int i = 0; i < 4; i++) {
            column[i] = board[i][col];
        }
        return column;
    }

    private int[] getDiagonal(boolean main) {
        int[] diagonal = new int[4];
        for (int i = 0; i < 4; i++) {
            diagonal[i] = main ? board[i][i] : board[i][3 - i];
        }
        return diagonal;
    }

    private boolean checkLine(int[] line) {
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
        board[i][j] = 0;
    }
}
