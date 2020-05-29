package PlainReactorLoop.Handler;

import PlainReactorLoop.Server;

import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HandlerPool implements Runnable {
    private static final ExecutorService executor = Executors.newFixedThreadPool(1);

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                SocketChannel socketChannel = Server.handlerSocketQueue.take();
                executor.submit(new Handler(socketChannel));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
