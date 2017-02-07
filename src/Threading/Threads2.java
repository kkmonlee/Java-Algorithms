package Threading;

/**
 * Created by aa on 07 February 2017.
 */
public class Threads2 {

    public static void main(String[] args) throws InterruptedException {
        // Print information about current thread
        System.out.printf("%s is %salive and in %s state%n and priority %d\n",
                Thread.currentThread().getName(),
                Thread.currentThread().isAlive() ? "" : "not ",
                Thread.currentThread().getState(),
                Thread.currentThread().getPriority());

        // Create a runnable object that prints information about the thread
        Runnable r1 = () -> {
            Thread t = Thread.currentThread();
            System.out.printf("%s is %salive and in %s state%n and priority %d \n",
                    t.getName(),
                    t.isAlive() ? "" : "not ",
                    t.getState(),
                    t.getPriority());
        };

        // Create thread t1 and print information BEFORE calling start()
        Thread t1 = new Thread(r1, "Thread t1");
        System.out.printf("%s is %salive and in %s state%n and priority %d \n",
                t1.getName(),
                t1.isAlive() ? "" : "not ",
                t1.getState(),
                t1.getPriority());
        t1.start();

        // Create thread t2 with only a runnable object and no name
        Thread t2 = new Thread(r1);
        t2.start();

        // Put main thread to sleep for 2 seconds
        Thread.sleep(2000);

        // Change name of thread 2 and print out its info
        t2.setName("Thread t2");
        System.out.printf("%s is %salive and in %s state%n and priority %d \n",
                t2.getName(),
                t2.isAlive() ? "" : "not ",
                t2.getState(),
                t2.getPriority());

        // print info about current thread
        System.out.printf("%s is %salive and in %s state%n and priority %d\n",
                Thread.currentThread().getName(),
                Thread.currentThread().isAlive() ? "" : "not ",
                Thread.currentThread().getState(),
                Thread.currentThread().getPriority());
    }
}
