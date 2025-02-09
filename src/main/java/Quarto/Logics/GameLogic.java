/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Quarto.Logics;

import Quarto.Graphics.Square;

public class GameLogic
{
    private Square[][] board;

    public GameLogic ()
    {
        board = new Square[4][4];
    }

    public void addPiece(int row, int col, Square piece) {
        board[row][col] = piece;
    }

    public boolean isGameOver() {
        for (int i = 0; i < 4; i++) {
            if (checkLine(getRow(i)) || checkLine(getColumn(i))) {
                return true;
            }
        }
        return checkLine(getDiagonal(true)) || checkLine(getDiagonal(false));
    }

    private Square[] getRow(int row) {
        return board[row];
    }

    private Square[] getColumn(int col) {
        Square[] column = new Square[4];
        for (int i = 0; i < 4; i++) {
            column[i] = board[i][col];
        }
        return column;
    }

    private Square[] getDiagonal(boolean main) {
        Square[] diagonal = new Square[4];
        for (int i = 0; i < 4; i++) {
            diagonal[i] = main ? board[i][i] : board[i][3 - i];
        }
        return diagonal;
    }

    private boolean checkLine(Square[] line) {
        if (line[0] == null) return false;
        boolean sameColor = true, sameSize = true, sameShape = true, sameFill = true;
        for (int i = 1; i < line.length; i++) {
            if (line[i] == null) return false;
            sameColor &= (line[i].isRed == line[0].isRed);
            sameSize &= (line[i].isBig == line[0].isBig);
            sameShape &= (line[i].isRound == line[0].isRound);
            sameFill &= (line[i].isHollow == line[0].isHollow);
        }
        return sameColor || sameSize || sameShape || sameFill;
    }
}
