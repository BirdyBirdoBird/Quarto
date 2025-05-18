/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Quarto;

import java.util.LinkedList;
import java.util.List;

import Quarto.Utils.GameState;
import Quarto.Utils.Move;

/**
 *
 * @author david
 */
public class Globals {
    public static GameState gameState = GameState.PLAYER_SELECT_MOVE;
    public static boolean botPLaying = false;
    public static byte[][] logicBoard;
    public static LinkedList<Byte> logicControl = new LinkedList<>();
    public static List<Move> emptySquares = new LinkedList<>();
}
