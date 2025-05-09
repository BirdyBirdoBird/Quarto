/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Quarto.Bot;

import java.util.function.Function;
import java.util.function.Predicate;

import Quarto.Bot.DecisionGraph.DecisionNode;

public class DecisionGraph<T, R> {

    /**
     * Each node in the decision graph implements this interface.
     */
    public interface DecisionNode<T, R> {
        R decide(T state);
    }

    /**
     * A QuestionNode asks a yes/no question (Predicate<T>).
     * If 'true', it goes to ifTrue node; if 'false', it goes to ifFalse node.
     */
    public static class QuestionNode<T, R> implements DecisionNode<T, R> {
        private final Predicate<T> question;
        private final DecisionNode<T, R> ifTrue;
        private final DecisionNode<T, R> ifFalse;

        public QuestionNode(
            Predicate<T> question,
            DecisionNode<T, R> ifTrue,
            DecisionNode<T, R> ifFalse
        ) {
            this.question = question;
            this.ifTrue = ifTrue;
            this.ifFalse = ifFalse;
        }

        @Override
        public R decide(T state) {
            if (question.test(state)) {
                return ifTrue.decide(state);
            } else {
                return ifFalse.decide(state);
            }
        }
    }

    /**
     * An ActionNode directly returns an R (the 'action')
     * by applying a Function<T, R> on the state.
     */
    public static class ActionNode<T, R> implements DecisionNode<T, R> {
        private final Function<T, R> action;

        public ActionNode(Function<T, R> action) {
            this.action = action;
        }

        @Override
        public R decide(T state) {
            return action.apply(state);
        }
    }

    private final DecisionNode<T, R> rootNode;

    /**
     * Build the graph with a "root node."
     */
    public DecisionGraph(DecisionNode<T, R> rootNode) {
        this.rootNode = rootNode;
    }

    /**
     * Start the decision process at the root node.
     */
    public R decide(T state) {
        return rootNode.decide(state);
    }
}
