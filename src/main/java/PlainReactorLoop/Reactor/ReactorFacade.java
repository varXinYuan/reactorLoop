package PlainReactorLoop.Reactor;

import PlainReactorLoop.Server;
import PlainReactorLoop.SocketInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.Selector;

public class ReactorFacade implements Runnable {
    public static final Logger logger = LoggerFactory.getLogger(ReactorFacade.class);

    public void run() {
        // 监听并注册读socket
        logger.info("ReactorFacade Run");
        try {
            while (true) {
                SocketInfo socketInfo = Server.reactorSocketQueue.take();
                socketInfo.socketChannel.register(Server.selector, socketInfo.socketType);
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
            Server.selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 初始化Facade
        new Thread(new ReactorFacade()).start();
        // 初始化Reactor
        new Thread(new Reactor()).start();
    }
}
