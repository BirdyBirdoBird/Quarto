package Quarto.Bot;

import Quarto.Utils.Move;
import Quarto.Globals;
import Quarto.Graphics.Square;
import Quarto.Logics.GameLogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * A flat, rule-based set of predicates and actions for placement and piece-selection.
 * Everything is static – it always reads/writes your global Constants.logicBoard
 * and Constants.currentPieceId.
 */
public class BotLogic {
    private boolean justTrapped = false;

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
        int myPiece = Globals.logicControl.getFirst();

        // Simulate removing the piece I'm about to play
        LinkedList<Integer> remaining = new LinkedList<>(Globals.logicControl);
        remaining.remove(Integer.valueOf(myPiece));

        // Now check: are there any safe pieces left?
        List<Integer> dangerous = GameLogic.getDangerousPieces();

        for (int piece : remaining) {
            if (!dangerous.contains(piece)) {
                System.out.println("I can safely give this to the opponent");
                return false; // I can safely give this to the opponent
            }
        }
        System.out.println("HAVE TO BLOCK");
        return true; // All remaining pieces are dangerous — I must block
    }


    /** “Place the current piece to block their win.” */
    public Move makeBlockingMove() {
        int[][] board = Globals.logicBoard;
        int myPiece = Globals.logicControl.getFirst();
        List<Integer> dangerous = GameLogic.getDangerousPieces();

        // Get all empty squares
        List<Move> emptySquares = new ArrayList<>();
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                if (board[row][col] == 0) {
                    emptySquares.add(new Move(row, col));
                }
            }
        }

        // Now check only those empty positions
        for (Move move : emptySquares) {
            int row = move.getRow();
            int col = move.getCol();

            // Simulate each dangerous piece at this position
            for (int threatPiece : dangerous) {
                board[row][col] = threatPiece;

                boolean win =
                    GameLogic.checkLine(GameLogic.getRow(row)) ||
                    GameLogic.checkLine(GameLogic.getColumn(col)) ||
                    (row == col && GameLogic.checkLine(GameLogic.getDiagonal(true))) ||
                    (row + col == 3 && GameLogic.checkLine(GameLogic.getDiagonal(false)));

                board[row][col] = 0;

                if (win) {
                    return new Move(row, col);
                }
            }
        }

        return pickAnyMove();
    }

    /** “Can I set up a trap (i.e. a move that leaves them no safe reply)?” */
    public boolean canSetUpTrap() {
        int[][] board = Globals.logicBoard;
        int myPiece = Globals.logicControl.getFirst();

        // Precompute: for each trait (bit 0–3), how many remaining pieces do NOT have that trait
        Map<Integer, Integer> safePieceCountPerTrait = new HashMap<>();
        for (int bit = 0; bit < 4; bit++) safePieceCountPerTrait.put(bit, 0);

        List<Integer> remaining = new ArrayList<>(Globals.logicControl);
        remaining.remove(Integer.valueOf(myPiece));

        for (int piece : remaining) {
            int bits = piece - 1;
            for (int bit = 0; bit < 4; bit++) {
                if (((bits >> bit) & 1) == 0) {
                    safePieceCountPerTrait.put(bit, safePieceCountPerTrait.get(bit) + 1);
                }
            }
        }

        // Scan each empty square
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                if (board[row][col] != 0) continue;

                board[row][col] = myPiece;

                int[] traitMatches = GameLogic.countSharedTraitsIn3AlignedLines(row, col);

                board[row][col] = 0; // Undo

                for (int bit = 0; bit < 4; bit++) {
                    if (traitMatches[bit] >= 2 && safePieceCountPerTrait.get(bit) > 0) {
                        return true; // Valid trap found
                    }
                }
            }
        }
        System.out.println("Can't Trap");
        return false;
    }

    /** “Place the current piece in a trapping spot.” */
    public Move makeTrappingMove() {
        int[][] board = Globals.logicBoard;
        int myPiece = Globals.logicControl.getFirst();

        // Recompute safe trait availability
        Map<Integer, Integer> safeTraitCount = new HashMap<>();
        for (int bit = 0; bit < 4; bit++) safeTraitCount.put(bit, 0);

        List<Integer> remaining = new ArrayList<>(Globals.logicControl);
        remaining.remove(Integer.valueOf(myPiece));

        for (int piece : remaining) {
            int bits = piece - 1;
            for (int bit = 0; bit < 4; bit++) {
                if (((bits >> bit) & 1) == 0) {
                    safeTraitCount.put(bit, safeTraitCount.get(bit) + 1);
                }
            }
        }

        // Now search for the first move that forms a trap
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                if (board[row][col] != 0) continue;

                board[row][col] = myPiece;
                int[] traitMatches = GameLogic.countSharedTraitsIn3AlignedLines(row, col);
                board[row][col] = 0;

                for (int bit = 0; bit < 4; bit++) {
                    if (traitMatches[bit] >= 2 && safeTraitCount.get(bit) > 0) {
                        justTrapped = true;
                        return new Move(row, col);
                    }
                }
            }
        }

        // Should never happen since canSetUpTrap() was true
        throw new IllegalStateException("Expected a trap move but none found.");
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
        } while (Globals.logicBoard[row][col] != 0);
    
        System.out.printf("picking Any Move (%d,%d)%n", row, col);
    
        // use the piece you were given (replace `currentPieceId` with your actual field)
        return new Move(row, col);
    }


    // ─────────────────────────────────────────────────────────────────────────────
    // Piece-selection predicates and actions
    // ─────────────────────────────────────────────────────────────────────────────

    /** “Was my last move a trap?” (you’d set this flag in your manager) */
    public boolean justSetTrap() {
        boolean result = justTrapped;
        justTrapped = false; // reset after one use
        return result;
    }

    public Integer selectTrappingPiece() {
        List<Integer> available = getAvailablePieces(); // from Constants.logicControl

        // For each bit (trait), count how many pieces have it
        int[] traitCount = new int[4];
        for (int piece : available) {
            int bits = piece - 1;
            for (int bit = 0; bit < 4; bit++) {
                if (((bits >> bit) & 1) == 1) {
                    traitCount[bit]++;
                }
            }
        }

        // Find the most common trait (likely the trap trait)
        int trapBit = 0;
        int max = 0;
        for (int bit = 0; bit < 4; bit++) {
            if (traitCount[bit] > max) {
                max = traitCount[bit];
                trapBit = bit;
            }
        }

        // Pick the first piece that does NOT have the trap trait
        for (int piece : available) {
            int bits = piece - 1;
            if (((bits >> trapBit) & 1) == 0) {
                return piece; // safe to give
            }
        }

        // If no such piece exists, pick any (we're forced)
        return available.get(0);
    }

    public int selectLeastCommonPiece() {
        List<Integer> placed = getPlacedPieces();      // Encoded ints: 1–16
        List<Integer> safePieces = GameLogic.getSafePieces();
        System.out.println(getAvailablePieces().toString());
        System.out.println(safePieces);

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
        return Globals.logicControl;
    }

    private List<Integer> getPlacedPieces() {
        List<Integer> placed = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int piece = Globals.logicBoard[i][j];
                if (piece != 0) {
                    placed.add(piece);
                }
            }
        }

        return placed;
    }
}

