package Quarto.Bot;

import java.util.function.Function;
import java.util.function.Predicate;

public class Rule<S,R> {
    private final Predicate<S> condition;
    private final Function<S,R>  action;

    public Rule(Predicate<S> condition, Function<S,R> action) {
        this.condition = condition;
        this.action    = action;
    }

    public boolean matches(S state)  { return condition.test(state); }
    public R apply(S state)  { return action.apply(state); }
}
