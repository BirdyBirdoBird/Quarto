/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Quarto.Utils;

/**
 *
 * @author david
 */
public class Move {
    private final int encodedPiece, row, col;

    public Move(int row, int col,int encodedPiece) {
        this.col = col;
        this.encodedPiece = encodedPiece;
        this.row = row;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getEncodedPiece() {
        return encodedPiece;
    }

  }
