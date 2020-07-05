package PlainReactorLoop;

import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.LinkedBlockingQueue;


public class Server {
    // 全局selector
    public static Selector selector = null;

    public static void main(String[] args) {
        // 运行Reactor
        Reactor.start();

        Acceptor.start();
    }
}
