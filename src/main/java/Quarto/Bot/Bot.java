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

    /**
     * Public API: pass in the raw board, get back your chosen Move.
     */
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
        QuestionNode<BotLogic,Move> oppWinsQ =
            new QuestionNode<>(
                state -> {
                    // Move m = state.findBestOpportunityMove();
                    Move m = new Move(1, 1, 1);
                    BotLogic.applyMove(Constants.logicBoard , m);
                    return BotLogic.isWin();      // true → bad
                },
                anyMoveNode,                 // if opponent _does_ win → defense
                oppNode                      // else → take the opportunity
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
