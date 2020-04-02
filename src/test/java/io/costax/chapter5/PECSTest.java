package io.costax.chapter5;

import io.costax.xtra.App;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Based on above reasoning and examples, let’s summarize our learning in bullet points.
 *
 * <ol>
 *    <li>Use the <? extends T> wildcard if you need to retrieve (GET) object of type T from a collection.</li>
 *    <li>Use the <? super T> wildcard if you need to put (ADD) objects of type T in a collection.</li>
 *    <li>If you need to satisfy both things, well, don’t use any wildcard. As simple as it is.</li>
 *    <li>In short, remember the term PECS. Producer extends Consumer super. Really easy to remember.</li>
 * </ol>
 * That’s all for simple yet complex concept in generics in java. Let me know of your thoughts via comments.
 */
@DisplayName("Java Generics Wildcards - PECS")
public class PECSTest {


    //@formatter:off
    static class Fruit {}
    static class Apple extends Fruit { }
    static class Banana extends Fruit { }
    static class AsianApple extends Apple {}
    //@formatter:on

    @Nested
    @DisplayName("Understanding <? extends T> Produce Extends")
    class UnderstandingProducerExtends {

        private void printBasket(List<? extends Fruit> basket) {
            System.out.println("-------- Busket ----------");

            int i = 1;

            for (final Fruit o : basket) {
                System.out.println(i + "- " + o.getClass().getSimpleName());
                i++;
            }
        }

        /**
         * This is the first part of PECS i.e. PE (Producer extends). To more relate it to real life terms, let’s use an analogy of a basket of fruits (i.e. collection of fruits).
         * When we pick a fruit from basket, then we want to be sure that we are taking out only fruit only and nothing else; so that we can write generic code like this example.
         * <p>
         * <p>
         * It ensures that whatever it comes out from basket is definitely going to be a fruit; so you iterate over it and simply cast it a Fruit.
         * Now in last two lines, I tried to add an Apple and then a Fruit in basket, but compiler didn’t allowed me. Why?
         * <p>
         * The reason is pretty simple, if we think about it; the <? extends Fruit> wildcard tells the compiler that we’re dealing with a subtype of the type Fruit,
         * but we cannot know which fruit as there may be multiple subtypes. Since there’s no way to tell, and we need to guarantee type safety (invariance), you won’t be allowed to put anything inside such a structure.
         * <p>
         * On the other hand, since we know that whichever type it might be, it will be a subtype of Fruit, we can get data out of the structure with the guarantee that it will be a Fruit.
         * <p>
         * <p>
         * we are taking elements out of collection “List<? extends Fruit> basket“; so here this basket is actually producing the elements i.e. fruits.
         * In simple words, when you want to ONLY retrieve elements out of a collection, treat it as a producer and use “? extends T>” syntax.
         * “Producer extends” now should make more sense to you.
         */
        @Test
        public void test() {
            //List of apples
            List<Apple> apples = new ArrayList<>();
            Apple apple1 = new Apple();
            Apple apple2 = new Apple();
            apples.add(apple1);
            apples.add(apple2);

            //We can assign a list of apples to a basket of fruits;
            //because apple is subtype of fruit
            List<? extends Fruit> basket = apples;

            //Here we know that in basket there is nothing but fruit only
            printBasket(basket);

            // we are not allowed to add any thing to the busked
            //basket.add(new Apple()); //Compile time error
            //basket.add(new Banana()); //Compile time error
            //basket.add(new Fruit()); //Compile time error


            basket.remove(apple1);

            printBasket(basket);

            Fruit fruit = basket.get(0);
            System.out.println("++++ " + fruit);

        }

        /**
         * Summary:
         * We can not add any kid instance to the basket.
         * we can remove instances from to the basket.
         * We are able to get instance of type Fruit from the basket: Fruit fruit = basket.get(0);
         */

    }


    @Nested
    @DisplayName("Understanding <? super T> - Consumer Super")
    class UnderstandingConsumerSuper {

        /**
         * We are able to add apple and even Asian apple inside basket, but we are not able to add Fruit (super type of apple) to basket. Why?
         * <p>
         * Reason is that basket is a <b>reference to a List of something that is a supertype of Apple.</b>
         * Again, <b>we cannot know which supertype it is</b>,
         * but we know that Apple and any of its subtypes (which are subtype of Fruit) can be added to be without problem (you can always add a subtype in collection of supertype).
         * So, now we can add any type of Apple inside basket.
         * </p>
         * <p>
         * What about getting data out of such a type?
         * It turns out that you <b> the only thing you can get out of it will be Object instances </b>:
         * since we cannot know which supertype it is, the compiler can only guarantee that it will be a reference to an Object,
         * since Object is the supertype of any Java type.
         * </p>
         * <p>
         * we are putting elements inside collection “List<? super Apple> basket“;
         * so here this basket is actually consuming the elements i.e. apples.
         * In simple words, when you want to ONLY add elements inside a collection,
         * treat it as a consumer and use “? super T>” syntax. Now, “Consumer super” also should make more sense to you.
         * </p>
         */
        @Test
        void test() {

            //List of bananas
            List<Apple> apples = new ArrayList<>();
            apples.add(new Apple());

            //We can assign a list of Apple to a basket of Apple
            List<? super Apple> basket = apples;

            Apple apple1 = new Apple();
            AsianApple asianApple1 = new AsianApple();
            basket.add(apple1);      //Successful
            basket.add(asianApple1);  //Successful
            basket.add(new AsianApple());  //Successful
            //basket.add(new Fruit());      //Compile time error

            printBasketA(basket);

            basket.remove(apple1);
            basket.remove(asianApple1);

            printBasketA(basket);


            Object object = basket.get(0);
            System.out.println("-- " + object);
        }

        private void printBasketA(final List<? super Apple> basket) {

            System.out.println("-------- Busket ----------");

            int i = 1;

            for (final Object o : basket) {
                System.out.println(i + "- " + o.getClass().getSimpleName());
                i++;
            }
        }

        /**
         * Summary:
         * We can add any kid instance of Apple to the basket.
         * we can remove instances from to the basket.
         * We can only get instance of type Object from the basket: Object object = basket.get(0);
         */
    }


    @Nested
    class CopyOf {

        @Test
        void name() {
            List<Apple> basket = new ArrayList<>();
            basket.add(new Apple());
            basket.add(new AsianApple());
            basket.add(new AsianApple());

            ArrayList<Apple> otherBasket = new ArrayList<>();
            copy(otherBasket, basket);

            for (Object o : otherBasket) {
                System.out.println("======" + o.getClass());
            }
        }

        <T> void copy(List<? super T> target, List<? extends T> source) {
            for (int i = 0; i < source.size(); i++) {
                T element = source.get(i);
                target.add(element);
            }

        }
    }


}
