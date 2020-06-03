package PlainReactorLoop.Reactor;

import PlainReactorLoop.Server;
import PlainReactorLoop.SocketInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;

public class ReactorFacade implements Runnable {
    public static final Logger logger = LoggerFactory.getLogger(ReactorFacade.class);
    public static final Integer MAX_THREAD_NUM = 4;

    public void run() {
        // 监听并注册读socket
        try {
            while (!Thread.interrupted()) {
                SocketInfo socketInfo = Server.reactorSocketQueue.take();
                socketInfo.socketChannel.register(Server.selectors[0], socketInfo.socketType);
                logger.info("Reactor注册Socket到Selector……");
                System.out.println("Reactor注册Socket到Selector……");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void init() {
        try {
            // 初始化 selector
            SelectorProvider selectorProvider = SelectorProvider.provider();
            for (Integer i = 0; i < MAX_THREAD_NUM; i++) {
                Server.selectors[i] = selectorProvider.openSelector();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 初始化Facade
        new Thread(new ReactorFacade()).start();
        // 初始化Reactor
        new Thread(new Reactor()).start();
    }
}
