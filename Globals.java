/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Quarto;

import java.util.LinkedList;

import Quarto.Utils.GameState;

/**
 *
 * @author david
 */
public class Globals {
    public static GameState gameState = GameState.PLAYER_SELECT_MOVE;
    public static boolean botPLaying = false;
    public static int[][] logicBoard;
    public static LinkedList<Integer> logicControl = new LinkedList<>();
}
