/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Quarto.Bot;

import Quarto.Bot.DecisionGraph.ActionNode;
import Quarto.Bot.DecisionGraph.DecisionNode;
import Quarto.Bot.DecisionGraph.QuestionNode;
import Quarto.Constants;
import Quarto.Utils.Move;

/**
 *
 * @author david
 */
/**
 * A Quarto‐specific bot built on top of your DecisionGraph.
 * T = BotLogic (wraps int[][] board)
 * R = Move
 */
public class Bot {
    private final DecisionGraph<BotLogic,Move> graph;

    public Bot() {
        this.graph = new DecisionGraph<>(buildGraph());
    }

    public Move chooseMove() {
        BotLogic state = new BotLogic();
        return graph.decide(state);
    }

    /**
     * canWin? → winNode
     *  else canBlock? → blockNode
     *   else canSetUpTrap? → trapNode
     *    else oppWins? → anyMoveNode : oppNode
     */
    private static DecisionNode<BotLogic,Move> buildGraph() {
        // 1) Action leaves, bound to instance methods of BotLogic
        ActionNode<BotLogic,Move> winNode = new ActionNode<>(BotLogic::makeWinningMove);
        ActionNode<BotLogic,Move> blockNode = new ActionNode<>(BotLogic::makeBlockingMove);
        ActionNode<BotLogic,Move> trapNode = new ActionNode<>(BotLogic::makeTrappingMove);
        ActionNode<BotLogic,Move> oppNode = new ActionNode<>(BotLogic::findBestOpportunityMove);
        ActionNode<BotLogic,Move> anyMoveNode = new ActionNode<>(BotLogic::pickAnyMove);

        // 2) “Does opponent win if I play the opportunity move?” question
        QuestionNode<BotLogic,Move> oppWinsQ = new QuestionNode<>(
            state -> {
                // “pieceId” is the piece you’re forcing the bot to place right now:
                int pieceId = Constants.currentPieceId;
        
                // Try every empty cell:
                for (int r = 0; r < Constants.logicBoard.length; r++) {
                    for (int c = 0; c < Constants.logicBoard[r].length; c++) {
                        if (Constants.logicBoard[r][c] != 0) continue;
        
                        // simulate placing here
                        Move test = new Move(r, c, pieceId);
                        int[][] copy = BotLogic.getBoardCopy();     // deep‐clone global board
                        BotLogic.applyMove(copy, test);
        
                        // now ask “will opponent win on that new board?”
                        // assume doesOpponentWinAfter(Move) uses Constants.logicBoard internally,
                        // so we call the overload that takes an explicit board:
                        boolean oppWins = BotLogic.doesOpponentWinAfter(copy, test);
        
                        if (!oppWins) {
                            // found at least one placement that *doesn't* let them win
                            return false;
                        }
                    }
                }
        
                // if we get here, *every* empty cell gives them a winning reply
                return true;
            },
            anyMoveNode,  // if true → opponent always wins → fall back
            oppNode       // if false → we found a safe “opportunity” placement
        );

        // 3) Trap check
        QuestionNode<BotLogic,Move> trapQ =
            new QuestionNode<>( BotLogic::canSetUpTrap, trapNode, oppWinsQ );

        // 4) Block check
        QuestionNode<BotLogic,Move> blockQ =
            new QuestionNode<>( BotLogic::canBlockOpponent, blockNode, trapQ );

        // 5) Top-level win check
        return new QuestionNode<>( BotLogic::canWin, winNode, blockQ );
    }
}
