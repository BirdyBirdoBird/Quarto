/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Quarto.Utils;

import Quarto.Graphics.BoardFrame;
import Quarto.Graphics.Square;

/**
 *
 * @author david
 */
public class Utils {
    public static String decodePiece(int piece) {
        if (piece == 0) {
            return "Empty";
        }
    
        int bits = piece - 1;
    
        String color = (bits & 1) != 0 ? "Red" : "Blue";
        String size  = (bits & 2) != 0 ? "Big" : "Small";
        String shape = (bits & 4) != 0 ? "Round" : "Square";
        String fill  = (bits & 8) != 0 ? "Hollow" : "Solid";
    
        return color + ", " + size + ", " + shape + ", " + fill;
    }
    

    public static int encodePiece(Square square){
        int bits = 0;
        if (square.isRed)    bits |= 1;   // bit 0
        if (square.isBig)    bits |= 1 << 1; // bit 1
        if (!square.isRound)  bits |= 1 << 2; // bit 2
        if (square.isHollow) bits |= 1 << 3; // bit 3
        return bits + 1; // Shift range to 1–16 instead of 0–15
    }
    
    public static Square decodePieceAsSquare(int pieceCode, int row, int col, BoardFrame frame) {
        Square square = new Square(row, col, frame, false);

        if (pieceCode == 0) {
            return square; // Leave it empty
        }

        int bits = pieceCode - 1;

        boolean isRed = (bits & 1) != 0;
        boolean isBig = (bits & 2) != 0;
        boolean isRound = (bits & 4) != 0;
        boolean isHollow = (bits & 8) != 0;

        square.addPiece(isRed, isHollow, isBig, isRound);
        return square;
    }
}
