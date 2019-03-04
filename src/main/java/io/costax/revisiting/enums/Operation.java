package io.costax.revisiting.enums;

import java.util.Map;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Operation {

    ADD((x, y) -> x + y, "A"),
    SUBTRACT((x, y) -> x - y, "S"),
    MULTIPLY((x, y) -> x * y, "M"),
    DIVIDE((x, y) -> x / y, "D");

    private static final Map<String, Operation> VALUE_MAP = Stream.of(values())
            .collect(Collectors.collectingAndThen(
                    Collectors.toMap(Operation::toDatabaseValue, Function.identity()),
                    Map::copyOf));
    //Collections::unmodifiableMap));

    private final String databaseValue;
    private final IntBinaryOperator operator;

    Operation(IntBinaryOperator operator, final String databaseValue) {
        this.operator = operator;
        this.databaseValue = databaseValue;
    }

    public static Operation fromString(String databaseValue) {
        final Operation operation = VALUE_MAP.getOrDefault(databaseValue, Operation.ADD);
        return operation;
    }

    public final int apply(int x, int y) {
        return this.operator.applyAsInt(x, y);
    }

    public String toDatabaseValue() {
        return this.databaseValue;
    }


}
