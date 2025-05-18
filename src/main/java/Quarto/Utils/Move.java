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
    private final byte row, col;

    public Move(byte row, byte col) {
        this.col = col;
        this.row = row;
    }

    public byte getRow() {
        return row;
    }

    public byte getCol() {
        return col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;                    
        if (!(o instanceof Move)) return false;        // Byte Constants
        Move other = (Move) o;
        return this.row == other.row && this.col == other.col;
    }

    @Override
    public String toString(){
        return "move: " + row  + " " + col;
    }

  }
