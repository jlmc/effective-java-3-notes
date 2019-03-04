package io.costax.revisiting.functional;

import java.util.function.IntUnaryOperator;

public class StrategyBusinessLogic {

    public IntUnaryOperator operator;

    private StrategyBusinessLogic(IntUnaryOperator operator) {
        this.operator = operator;
    }

    static StrategyBusinessLogic of(IntUnaryOperator operator) {
        return new StrategyBusinessLogic(operator);
    }

    public static void main(String[] args) {
        StrategyBusinessLogic.of(x -> x * 2).doIt();
        StrategyBusinessLogic.of(x -> x + 1).doIt();
        StrategyBusinessLogic.of(x -> x - 5).doIt();
        StrategyBusinessLogic.of(x -> x / 78).doIt();
    }

    public void doIt() {

        System.out.println("--- A ");
        System.out.println("--- B ");
        System.out.println("--- -- ");

        System.out.println("---" + this.operator.applyAsInt(110));

        System.out.println("--- -- ");
        System.out.println("--- Y ");
        System.out.println("--- Z ");
    }
}
