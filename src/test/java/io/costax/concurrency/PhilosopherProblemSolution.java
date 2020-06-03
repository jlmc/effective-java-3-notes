package io.costax.concurrency;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class PhilosopherProblemSolution {

    public static void main(String[] args) {

        Pizza pizza = new Pizza(25);


        final Cutlery cutlery0 = new Cutlery("C0");
        final Cutlery cutlery1 = new Cutlery("C1");
        final Cutlery cutlery2 = new Cutlery("C2");
        final Cutlery cutlery3 = new Cutlery("C3");
        final Cutlery cutlery4 = new Cutlery("C4");

        List<Philosopher> philosophers = List.of(
                new Philosopher("Philosopher-0", cutlery0, cutlery4, pizza),
                new Philosopher("Philosopher-1", cutlery1, cutlery0, pizza),
                new Philosopher("Philosopher-2", cutlery2, cutlery1, pizza),
                new Philosopher("Philosopher-3", cutlery3, cutlery2, pizza),
                new Philosopher("Philosopher-4", cutlery4, cutlery3, pizza));

        ExecutorService executorService = Executors.newFixedThreadPool(philosophers.size());
        CompletableFuture.allOf(philosophers.stream().map(p -> CompletableFuture.supplyAsync(p::call, executorService)).toArray(CompletableFuture[]::new)).join();

        philosophers.forEach(p -> System.out.println(p + " - " + p.getEatsCount()));
        int sum = philosophers.stream().mapToInt(Philosopher::getEatsCount).sum();

        System.out.println("Counted: " + sum);


    }
}

class Pizza {

    private final AtomicInteger numberOfSlices;
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public Pizza(final int numberOfSlices) {
        this.numberOfSlices = new AtomicInteger(numberOfSlices);
    }

    public boolean isEmpty() {
        Lock lock = readWriteLock.readLock();
        lock.lock();
        boolean b = numberOfSlices.get() <= 0;
        lock.unlock();
        return b;
    }

    public int getOneSlice() {
        Lock lock = readWriteLock.writeLock();
        lock.lock();

        int value = 0;
        if (numberOfSlices.get() > 0) {
            numberOfSlices.decrementAndGet();
            value = 1;
        }

        lock.unlock();

        return value;
    }

}

class Philosopher implements Callable<Integer> {

    private final String id;
    private final Cutlery left;
    private final Cutlery right;
    private final Pizza pizza;

    private int eatsCount = 0;

    Philosopher(final String id, final Cutlery left, final Cutlery right, final Pizza pizza) {
        this.id = id;
        this.left = left;
        this.right = right;
        this.pizza = pizza;
    }

    public int getEatsCount() {
        return eatsCount;
    }

    @Override
    public Integer call() {
        try {

            while (!pizza.isEmpty()) {

                think();

                if (left.pickUp(this, "LEFT")) {

                    if (right.pickUp(this, "RIGHT")) {
                        eat();
                        right.putDown(this, "RIGHT");
                    }


                    left.putDown(this, "LEFT");
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return this.eatsCount;
    }


    void think() {
        System.out.println(String.format("Philosopher %s is thinking", this.id));
        System.out.flush();
        Utils.sleep(Utils.MILLIS);
    }

    void eat() {
        System.out.println(String.format("Philosopher %s is eating", this.id));
        System.out.flush();

        int oneSlice = pizza.getOneSlice();
        this.eatsCount += oneSlice;

        Utils.sleep(Utils.MILLIS);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "-" + this.id;
    }


}

class Cutlery {

    // Who I am.
    private final String id;
    // Make sure only one philosopher can have me at any time.
    Lock up = new ReentrantLock();

    public Cutlery(final String id) {
        this.id = id;
    }

    public boolean pickUp(Philosopher philosopher, String where) throws InterruptedException {
        if (up.tryLock(Utils.MILLIS * 2, TimeUnit.MILLISECONDS)) {
            //if (up.tryLock()) {
            System.out.println("The " + philosopher + " picked up " + where + " " + this);
            return true;
        }
        return false;
    }

    public void putDown(Philosopher philosopher, String where) {
        up.unlock();
        System.out.println("The " + philosopher + " put down " + where + " " + id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "-" + id;
    }
}

class Utils {
    public static final int MILLIS = 1_500;

    static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
