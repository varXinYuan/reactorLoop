package PlainReactorLoop;

import PlainReactorLoop.Handler.HandlerPool;
import PlainReactorLoop.Reactor.Facade;
import PlainReactorLoop.Reactor.Reactor;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.LinkedBlockingQueue;


public class Server {
    // 初始化Acceptor信号
    public static Object acceptMutex = new Object();
    // 初始化ReactorSocket队列
    public static LinkedBlockingQueue<SocketInfo> reactorSocketQueue = new LinkedBlockingQueue<SocketInfo>();
    // 初始化HandlerSocket队列
    public static LinkedBlockingQueue<SocketChannel> handlerSocketQueue = new LinkedBlockingQueue<SocketChannel>();

    // 初始化selector
    public static Selector selector;

    Server() {
        try {
            selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // 运行Reactor
        new Thread(new Reactor()).start();
        // 运行Reactor Facade
        new Thread(new Facade()).start();

        // 运行Acceptor
        new Thread(new Acceptor()).start();

        // 初始化Handler
        new Thread(new HandlerPool()).start();
    }
}
