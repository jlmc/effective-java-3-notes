package io.costax.rethinking.designpatterns;

public class RunInOtherThread {

    public static void executeThread() throws InterruptedException {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                System.out.println("Run in other thread !!!!");

                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("exit from the other Thread...");
            }
        });


        thread.start();

        thread.join(3000L);

        System.out.println("--- all done");

        //System.exit(0);
    }

    public static void executeThreadKillingVerbose() throws InterruptedException {

        Thread thread = new Thread(() -> System.out.println("Run in other thread"));

        thread.start();

        thread.join(3000L);

        System.out.println("--- all done");
    }

}
