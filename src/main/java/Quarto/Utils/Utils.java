/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Quarto.Utils;

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

        String color = (piece & 2) != 0 ? "Light" : "Dark";
        String size = (piece & 4) != 0 ? "Big" : "Small";
        String shape = (piece & 8) != 0 ? "Round" : "Square";
        String fill = (piece & 16) != 0 ? "Hollow" : "Solid";

        return color + ", " + size + ", " + shape + ", " + fill;
    }

    public static int encodePiece(Square square){
        int bits = 0;
        if(square.isRed){
            bits += 2;
        }
        if(square.isBig){
            bits += 4;
        }
        if(square.isEmpty){
            bits += 8;
        }
        if(square.isHollow){
            bits += 16;
        }
        return  bits;
    }
}
