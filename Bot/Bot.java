package Quarto.Bot;

import java.util.List;
import Quarto.Utils.Move;

public class Bot {
    private final List<Rule<BotLogic,Move>>    placeRules;
    private final List<Rule<BotLogic,Integer>> selectRules;

    public Bot() {
        // 2) Use explicit <> on the constructor so the compiler knows S=BotLogic, R=Move
        this.placeRules = List.of(
            new Rule<BotLogic,Move>( BotLogic::canWin, BotLogic::makeWinningMove ),
            new Rule<BotLogic,Move>( BotLogic::canBlockOpponent, BotLogic::makeBlockingMove ),
            new Rule<BotLogic,Move>( BotLogic::canSetUpTrap, BotLogic::makeTrappingMove),
            // fallback always matches:
            new Rule<BotLogic,Move>( state -> true, BotLogic::mostOpportunities)
        );

        // 3) Same idea for piece‐selection rules (S=BotLogic, R=Integer piece‐ID)
        this.selectRules = List.of(
            new Rule<BotLogic,Integer>( BotLogic::justSetTrap, BotLogic::selectTrappingPiece),
            new Rule<BotLogic,Integer>( state -> true, BotLogic::selectLeastCommonPiece )
        );
    }

    /** Step 1: pick & return your Move based on the given raw board */
    public Move chooseMove() {
        BotLogic state = new BotLogic();
        for (Rule<BotLogic,Move> rule : placeRules) {
            if (rule.matches(state)) {
                return rule.apply(state);
            }
        }
        throw new IllegalStateException("No placement rule matched");
    }

    /** Step 2: pick & return the next piece‐ID to hand the opponent */
    public int choosePiece() {
        BotLogic state = new BotLogic();
        for (Rule<BotLogic,Integer> rule : selectRules) {
            if (rule.matches(state)) {
                return rule.apply(state);
            }
        }
        throw new IllegalStateException("No selection rule matched");
    }
}