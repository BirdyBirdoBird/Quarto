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
    private final int pieceId, row, col;
    // ctor + getters

    public Move(int row, int col, int pieceId) {
        this.col = col;
        this.pieceId = pieceId;
        this.row = row;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getPieceId() {
        return pieceId;
    }

  }
