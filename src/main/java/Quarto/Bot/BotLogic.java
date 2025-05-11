package Quarto.Bot;

import Quarto.Utils.Move;
import Quarto.Constants;
import Quarto.Graphics.Square;
import Quarto.Logics.GameLogic;

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
    // Placement predicates and actions
    // ─────────────────────────────────────────────────────────────────────────────

    /** “Can I win right now by placing the current piece anywhere?” */
    public boolean canWin() {
        return GameLogic.getWinningMove() != null;
    }

    /** “Place the current piece so I win immediately.” */
    public Move makeWinningMove() {
        return GameLogic.getWinningMove();
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

    public Move mostOpportunities() {
        Move best = GameLogic.getOpportunityMove();
        if (best != null) {
            System.out.println("picking best move");
        }
        return best != null ? best : pickAnyMove();
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

    public int selectLeastCommonPiece() {
        List<Integer> placed = getPlacedPieces();      // Encoded ints: 1–16
        List<Integer> safePieces = getSafePieces();
        System.out.println(getAvailablePieces().toString());

        // Count how common each trait is across placed pieces
        int[] traitCounts = new int[4]; // [Red, Big, Round, Hollow]

        for (int encoded : placed) {
            int bits = encoded - 1;

            if ((bits & 1) != 0) traitCounts[0]++; // Red
            if ((bits & (1 << 1)) != 0) traitCounts[1]++; // Big
            if ((bits & (1 << 2)) == 0) traitCounts[2]++; // Round (inverted)
            if ((bits & (1 << 3)) != 0) traitCounts[3]++; // Hollow
        }

        // Find available piece with *least common* trait combo
        int minScore = Integer.MAX_VALUE;
        int selected = getAvailablePieces().get(0); // fallback

        for (int candidate : safePieces) {
                
            int bits = candidate - 1;
            
            int score = 0;
            if ((bits & 1) != 0) score += traitCounts[0]; // Red
            if ((bits & (1 << 1)) != 0) score += traitCounts[1]; // Big
            if ((bits & (1 << 2)) == 0) score += traitCounts[2]; // Round
            if ((bits & (1 << 3)) != 0) score += traitCounts[3]; // Hollow
            
            if (score < minScore) {
                minScore = score;
                selected = candidate;
            }
        }

        return selected;
    }

    /** “All pieces not yet placed on the board.” */
    public List<Integer> getAvailablePieces() {
        return Constants.logicControl;
    }

    public List<Integer> getPlacedPieces() {
        List<Integer> placed = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int piece = Constants.logicBoard[i][j];
                if (piece != 0) {
                    placed.add(piece);
                }
            }
        }

        return placed;
    }

    private List<Integer> getSafePieces() {
        List<Integer> allAvailable = new ArrayList<>(Constants.logicControl);
        List<Integer> dangerous = GameLogic.getDangerousPieces();

        List<Integer> safe = new ArrayList<>();

        for (int piece : allAvailable) {
            if (!dangerous.contains(piece)) {
                safe.add(piece);
            }
        }

        System.out.println("Safe pieces: " + safe);
        return safe;
    }
}

