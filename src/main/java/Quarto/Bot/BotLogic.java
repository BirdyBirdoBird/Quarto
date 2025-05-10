package Quarto.Bot;

import Quarto.Utils.Move;
import Quarto.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A flat, rule-based set of predicates and actions for placement and piece-selection.
 * Everything is static – it always reads/writes your global Constants.logicBoard
 * and Constants.currentPieceId.
 */
public class BotLogic {
    private static final Random RND = new Random();

    // ─────────────────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────────────────

    /** Apply Move m to the given board. */
    public void applyMove(Move m) {
        ///
    }

    /** Scan rows, cols, diags for a Quarto win. */
    public boolean isWin(int[][] board) {
        // TODO: implement your 4-in-a-row share-a-property check here
        return false;
    }

    /** All empty positions on the board. */
    private List<Position> getEmptyCells(int[][] board) {
        List<Position> empties = new ArrayList<>();
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                if (board[r][c] == 0) {
                    empties.add(new Position(r, c));
                }
            }
        }
        return empties;
    }

    // Simple pair to hold a row/col
    private class Position {
        final int row, col;
        Position(int r, int c) { this.row = r; this.col = c; }
    }

    // ─────────────────────────────────────────────────────────────────────────────
    // Placement predicates and actions
    // ─────────────────────────────────────────────────────────────────────────────

    /** “Can I win right now by placing the current piece anywhere?” */
    public boolean canWin() {
        return false;
    }

    /** “Place the current piece so I win immediately.” */
    public Move makeWinningMove() {
        return null;
    }

    /** “Can I block the opponent’s immediate win?” */
    public boolean canBlockOpponent() {
        // TODO: you’ll need to know which piece the *opponent* will get next,
        // then simulate exactly like canWin() but for their piece.
        return false;
    }

    /** “Place the current piece to block their win.” */
    public Move makeBlockingMove() {
        // TODO: same loop as makeWinningMove(), but looking for their win-spots
        return pickAnyMove();
    }

    /** “Can I set up a trap (i.e. a move that leaves them no safe reply)?” */
    public boolean canSetUpTrap() {
        return false;
    }

    /** “Place the current piece in a trapping spot.” */
    public Move makeTrappingMove() {
        Move trap = findSafeOpportunityMove();
        return (trap != null) ? trap : pickAnyMove();
    }

    /**
     * “Find me one spot where, after I place, the opponent has *no* winning reply.”
     * Returns null if every placement lets them win.
     */
    public Move findSafeOpportunityMove() {
        return null;
    }

    /** “I’ve run out of smarter ideas—just pick any random empty spot.” */
    public Move pickAnyMove() {
        Random r = new Random();
        int row, col;
        // keep sampling until we hit an empty square (0)
        do {
            // board is 4×4, so indices 0–3
            row = r.nextInt(0,4);
            col = r.nextInt(0,4);
        } while (Constants.logicBoard[row][col] != 0);
    
        System.out.printf("pickAnyMove (%d,%d)%n", row, col);
    
        // use the piece you were given (replace `currentPieceId` with your actual field)
        return new Move(row, col);
    }


    // ─────────────────────────────────────────────────────────────────────────────
    // Piece-selection predicates and actions
    // ─────────────────────────────────────────────────────────────────────────────

    /** “Was my last move a trap?” (you’d set this flag in your manager) */
    public boolean justSetTrap() {
        return false;
    }

    /** “If I just trapped them, hand over a piece that continues pressure.” */
    public int selectTrappingPiece() {
        List<Integer> avail = getAvailablePieces();
        // simple: pick randomly, or implement your own heuristic
        return avail.get(RND.nextInt(avail.size()));
    }

    /** “Otherwise hand over the least‐common piece (LCP).” */
    public int selectLeastCommonPiece() {
        List<Integer> avail = getAvailablePieces();
        // TODO: count attributes among avail, then pick the piece whose bits are least frequent
        return avail.get(0);
    }

    /** “All pieces not yet placed on the board.” */
    public List<Integer> getAvailablePieces() {
        return null;
    }
}

