package PlainReactorLoop.Handler;

import PlainReactorLoop.Reactor.ReactorFacade;
import PlainReactorLoop.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HandlerMaster implements Runnable {
    //private static final ExecutorService executor = Executors.newFixedThreadPool(1);
    public static final Logger logger = LoggerFactory.getLogger(HandlerMaster.class);

    public void run() {
        while (!Thread.interrupted()) {
            try {
                SocketChannel socketChannel = Server.handlerSocketQueue.take();
                //executor.submit(new Handler(socketChannel));
                logger.info("处理Socket，执行业务逻辑……");
                new Handler(socketChannel).run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
