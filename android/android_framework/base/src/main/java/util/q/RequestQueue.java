package util.q;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class RequestQueue<T extends Comparable> {
    private final BlockingQueue<T> priorityQueue = new PriorityBlockingQueue<>();

    private final Executor requestExecutor;
    private final RequestHandler<T> requestHandler;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    private final int poolSize;

    public RequestQueue(int poolSize, RequestHandler<T> requestHandler) {
        this.requestHandler = requestHandler;
        this.poolSize = poolSize;
        this.requestExecutor = Executors.newFixedThreadPool(poolSize);
    }

    public void start() {
        isRunning.set(true);
        for (int i = 0; i < poolSize; i++) {
            requestExecutor.execute(new RequestConsumer());
        }
    }

    public void stop() {
        isRunning.set(false);
    }

    public void add(T request) {
        priorityQueue.add(request);
    }

    private class RequestConsumer implements Runnable {
        @Override
        public void run() {
            while (isRunning.get()) {
                try {
                    T request = priorityQueue.take();
                    requestHandler.handleRequest(request);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }
}
