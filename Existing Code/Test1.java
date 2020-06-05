import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

class Example{
    int a;
    int b;
}


class NumbersProducer implements Runnable {
    private BlockingQueue<Integer> numbersQueue;
    private final int poisonPill;
    private final int poisonPillPerProducer;
     
    public NumbersProducer(BlockingQueue<Integer> numbersQueue, int poisonPill, int poisonPillPerProducer) {
        this.numbersQueue = numbersQueue;
        this.poisonPill = poisonPill;
        this.poisonPillPerProducer = poisonPillPerProducer;
    }
    public void run() {
        try {
            generateNumbers();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
     
    private void generateNumbers() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            System.out.println("In Producer.");
            System.out.println(Thread.currentThread().getName() + " result: " + i);
            numbersQueue.put(ThreadLocalRandom.current().nextInt(100));
        }
        for (int j = 0; j < poisonPillPerProducer; j++) {
            numbersQueue.put(poisonPill);
        }
     }
}

class NumbersConsumer implements Runnable {
    private BlockingQueue<Integer> queue;
    private final int poisonPill;
     
    public NumbersConsumer(BlockingQueue<Integer> queue, int poisonPill) {
        this.queue = queue;
        this.poisonPill = poisonPill;
    }
    public void run() {
        try {
            while (true) {
                Integer number = queue.take();
                if (number.equals(poisonPill)) {
                    System.out.println("Numbers is equal to poisonPill. So returning.");
                    return;
                }
                System.out.println(Thread.currentThread().getName() + " result: " + number);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class Test1 {
    public static void main(String args[]) throws InterruptedException {
        int BOUND = 10;
        int N_PRODUCERS = 4;
        int N_CONSUMERS = Runtime.getRuntime().availableProcessors();
        int poisonPill = Integer.MAX_VALUE;
        int poisonPillPerProducer = N_CONSUMERS / N_PRODUCERS;
        int mod = N_CONSUMERS % N_PRODUCERS;
         
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(BOUND);
        
        // ThreadPoolExecutor for producer thread
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(N_PRODUCERS); 
        executor.execute(new NumbersProducer(queue, poisonPill, poisonPillPerProducer));

        // for (int i = 1; i < N_PRODUCERS; i++) {
        //     new Thread(new NumbersProducer(queue, poisonPill, poisonPillPerProducer)).start();
        // }
        
        ThreadPoolExecutor executor1 = (ThreadPoolExecutor) Executors.newFixedThreadPool(N_CONSUMERS); 
        executor1.execute(new NumbersConsumer(queue, poisonPill));
        executor.shutdown();
        executor1.shutdown();
 
        // for (int j = 0; j < N_CONSUMERS; j++) {
        //     new Thread(new NumbersConsumer(queue, poisonPill)).start();
        // }
         
        // new Thread(new NumbersProducer(queue, poisonPill, poisonPillPerProducer + mod)).start();

    }

}