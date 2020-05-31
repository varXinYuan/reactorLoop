package PlainReactorLoop;

import PlainReactorLoop.Handler.HandlerMaster;
import PlainReactorLoop.Reactor.ReactorFacade;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.LinkedBlockingQueue;


public class Server {
    // 初始化Acceptor信号
    public static LinkedBlockingQueue<Integer> acceptSockets = new LinkedBlockingQueue<Integer>();
    // 初始化ReactorSocket队列
    public static LinkedBlockingQueue<SocketInfo> reactorSocketQueue = new LinkedBlockingQueue<SocketInfo>();
    // 初始化HandlerSocket队列
    public static LinkedBlockingQueue<SocketChannel> handlerSocketQueue = new LinkedBlockingQueue<SocketChannel>();

    // 全局selector
    public static Selector selector = null;

    public static void main(String[] args) {
        // 运行Reactor Facade
        new Thread(new ReactorFacade()).start();

        // 运行Acceptor
        new Thread(new Acceptor()).start();

        // 初始化Handler
        new Thread(new HandlerMaster()).start();
    }
}
