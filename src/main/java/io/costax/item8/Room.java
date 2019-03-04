package io.costax.item8;


// TODO
public class Room implements AutoCloseable {
    @Override
    public void close() throws Exception {

    }
    /*

    private static Cleaner cleaner;

    private static class State implements Runnable {
        int numJunkPipes;

        State (int numJunkPipes) {
            this.numJunkPipes = numJunkPipes;
        }

        @Override
        public void run() {
            System.out.println("Cleaning The ROOM");
            numJunkPipes = 0;
        }
    }

    private final State state;

    public Room (int numJunkPiles) {
        this.state = new State(numJunkPiles);
        cleaner = Cleaner.create(this, this.state);
    }


    @Override
    public void close() throws Exception {
        cleaner.clean();
    }

    public static void main(String[] args) {
        try ( Room room = new Room(7)) {
            System.out.println("Goodbye");
        } catch (Exception e) {
            System.gc();
            //System.exit();
            e.printStackTrace();
        }
    }
    */
}
