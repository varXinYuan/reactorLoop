package PlainReactorLoop.Reactor;

import PlainReactorLoop.Server;
import PlainReactorLoop.SocketInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.Selector;

public class ReactorFacade implements Runnable {
    public static final Logger logger = LoggerFactory.getLogger(ReactorFacade.class);

    public ReactorFacade() {
        try {
            // 初始化 selector
            Server.selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        // 初始化Reactor
        new Thread(new Reactor()).start();

        // 监听并注册读socket
        try {
            SocketInfo socketInfo = Server.reactorSocketQueue.take();
            socketInfo.socketChannel.register(Server.selector, socketInfo.socketType);
            logger.info("Reactor注册Socket到Selector……");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
