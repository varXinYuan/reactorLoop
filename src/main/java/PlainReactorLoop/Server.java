package PlainReactorLoop;

import PlainReactorLoop.Handler.HandlerMaster;
import PlainReactorLoop.Reactor.ReactorFacade;

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
        // 运行Reactor
        ReactorFacade.init();

        // 运行Acceptor
        Acceptor.init();

        // 初始化Handler
        HandlerMaster.init();
    }
}
