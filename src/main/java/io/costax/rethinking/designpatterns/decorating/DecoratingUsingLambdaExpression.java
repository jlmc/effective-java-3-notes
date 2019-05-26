package io.costax.rethinking.designpatterns.decorating;

import java.awt.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class DecoratingUsingLambdaExpression {

    public static void main(String[] args) {
        printIt(new Camera());
        printIt(new Camera(Color::brighter));
        printIt(new Camera(Color::darker));
        printIt(new Camera(Color::brighter, Color::darker));
    }

    private static void printIt(Camera camera) {
        System.out.println(camera.snap(new Color(125, 125, 125)));
    }

}

class Camera {

    private Function<Color, Color> filter;

    Camera(final Function<Color, Color>... filters) {
        /*
        filter = color -> color;

        for (Function<Color, Color> colorColorFunction : filters) {
            this.filter = filter.andThen(colorColorFunction);
        }
         */

        /*
        this.filter = Stream.of(filters)
                .reduce(new Function<Color, Color>() {
                    @Override
                    public Color apply(final Color input) {
                        return input;
                    }
                }, new BinaryOperator<Function<Color, Color>>() {
                    @Override
                    public Function<Color, Color> apply(
                            final Function<Color, Color> aFilter,
                            final Function<Color, Color> result) {
                        return result.andThen(aFilter);
                    }
                });
         */

        this.filter = Stream.of(filters).reduce(Function.identity(), Function::andThen);
    }

    public Color snap(Color input) {
        return filter.apply(input);
    }
}



