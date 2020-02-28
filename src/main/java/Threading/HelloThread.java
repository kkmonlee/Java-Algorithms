package Threading;

/**
 * Created by aa on 07 February 2017.
 */
public class HelloThread extends Thread {

    public void run() {
        System.out.println("Hello from " + Thread.currentThread().getName() +
                            ", created by extending Thread class!");
    }
}
