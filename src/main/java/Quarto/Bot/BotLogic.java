/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Quarto.Bot;

import java.util.Random;

import Quarto.Utils.Move;

/**
 *
 * @author david
 */
public class BotLogic {

    public static void applyMove(int[][] board, Move m) {
        board[m.getRow()][m.getCol()] = m.getPieceId();
    }

    /** Check whether the given board is in a winning state. */
    public static boolean isWin() {
        // TODO: your row/col/diag‐checking logic here
        return true;
    }

    /** “Can I win right now by placing any available piece?” */
    public boolean canWin() {
        // scan all legal moves, simulate, and see if isWin
        return false;
    }

    /** “If I don’t win, can I at least block their immediate win?” */
    public boolean canBlockOpponent() {
        return false;
    }

    /** “If I can’t block, can I set up a trap?” */
    public boolean canSetUpTrap() {
        return false;
    }

    /** “If I play this move, will the opponent then have a winning reply?” */
    public boolean doesOpponentWinAfter(Move m) {
        return false;
    }


    // ─────────────── Action factories ───────────────────

    /** Return a new Move that immediately wins, or null if none. */
    public Move makeWinningMove() {
        // iterate board & available pieces → simulate → return the winning Move
        return null;
    }

    /** Return a new Move that blocks the opponent’s win. */
    public Move makeBlockingMove() {
        return null;
    }

    /** Return a new Move that sets up a trap. */
    public Move makeTrappingMove() {
        return null;
    }

    /** Return the best “opportunity” move (but might let them win afterward). */
    public Move findBestOpportunityMove() {
        return null;
    }

    /** Return any legal Move (fallback). */
    public Move pickAnyMove() {
        Random r = new Random();
        return new Move(r.nextInt(0,4), 0, r.nextInt(0,4));
    }
}
