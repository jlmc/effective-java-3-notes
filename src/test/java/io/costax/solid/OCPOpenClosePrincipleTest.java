package io.costax.solid;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Optional.ofNullable;

@DisplayName("OCP - Open Close Principle")
class OCPOpenClosePrincipleTest {

    private static Engine engineFactory(final Engine original) {
        try {
            if (original == null) return null;

            Class<? extends Engine> aClass = original.getClass();

            // this return only the fields from the top class, not the fields from the super class
            //Field[] declaredFields = aClass.getDeclaredFields();

            // to get all the inherited fields we need to navigate recursively in the hierarchy
            final List<Field> fields = new ArrayList<>();
            List<Field> allFields = getAllFields(fields, aClass);

            Constructor<? extends Engine> constructor = aClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            Engine target = constructor.newInstance();

            // IMPORTANT: we need to copy all the properties. to be able to remove all the copy constructors
            for (final Field field : allFields) {
                field.setAccessible(true);
                Object value = field.get(original);
                field.set(target, value);
            }

            return target;

        } catch (InstantiationException |
                IllegalAccessException |
                IllegalArgumentException |
                InvocationTargetException |
                NoSuchMethodException | SecurityException e) {

            throw new RuntimeException(e);
        }
    }

    public static List<Field> getAllFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));

        if (type.getSuperclass() != null) {
            getAllFields(fields, type.getSuperclass());
        }

        return fields;
    }

    @Test
    void test() {
        Car car1 = new Car(2020, new TurboEngine("V1", 456));
        Car car2 = new Car(car1);

        System.out.println(car1);
        System.out.println(car2);

        Car car3 = new Car(2019, new EclecticEngine("V5", 2019, "Mercedes"));
        Car car4 = new Car(car3);

        System.out.println(car3);
        System.out.println(car4);

        Assertions.assertNotSame(car1, car2);
        Assertions.assertNotSame(car1.engine, car2.engine);
    }

    static class Engine {
        protected String version;

        //@formatter:off
        private Engine() {}
        protected Engine(String version) { this.version = version;}
        //@formatter:on

        @Override
        public String toString() {
            return getClass().getSimpleName() + ":" + version + ":" + hashCode();
        }

        public final Engine copy() {
            return engineFactory(this);
        }
    }

    static class TurboEngine extends Engine {
        private int power;

        private TurboEngine() {
        }

        public TurboEngine(final String version, int power) {
            super(version);
            this.power = power;
        }

      /*  protected TurboEngine(final TurboEngine other) {
            super(other.version);
            this.power = other.power;
        }*/

        @Override
        public String toString() {
            return super.toString() + " : " + power;
        }
    }

    static class EclecticEngine extends Engine {
        private double autonomy;
        private String brand;

        private EclecticEngine() {
        }

        public EclecticEngine(final String version, final double autonomy, String brand) {
            super(version);
            this.autonomy = autonomy;
            this.brand = brand;
        }
    }

    static class Car {
        private final int year;
        private final Engine engine;

        Car(final int theYear, final Engine theEngine) {
            this.year = theYear;
            this.engine = theEngine;
        }

        public Car(final Car other) {
            this.year = other.year;
            // we don't what to use the same engine in two cars, so we have to create a deep copy
            //this.engine = new Engine(other.engine);


            //IMPORTANT: This is a violation of the OCP, for each new Engine Type we will need to add a new if statement :(
            /*if (other.engine instanceof TurboEngine) {
                this.engine = new TurboEngine();
            } else {
                this.engine = new Engine(other.engine);
            }*/

            // IMPORTANT: To fix the previous problem we can use a little of the reflection api
            this.engine = ofNullable(other.engine).map(Engine::copy).orElse(null);
        }


        @Override
        public String toString() {
            return getClass().getSimpleName() + " : " + year + ":" + engine.toString();
        }
    }
}