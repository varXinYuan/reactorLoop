package PlainReactorLoop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Random;

/**
 * 连接事件就绪,处理连接事件
 */
class Acceptor implements Runnable {
    private Logger logger = LoggerFactory.getLogger(Acceptor.class);

    private ServerSocketChannel serverSocketChannel;

    Acceptor() {
        // 初始化服务端连接
        initServerSocket();
    }

    public static void start() {
        new Thread(new Acceptor()).start();
    }

    public void run() {
        while (!Thread.interrupted()) {
            try {
                SocketChannel socketChannel = serverSocketChannel.accept();
                logger.info("接收到客户端连接……");
                if (socketChannel != null) {
                    register(socketChannel);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 初始化服务端连接
     */
    private Acceptor initServerSocket() {
        try {
            // 打开监听信道
            serverSocketChannel = ServerSocketChannel.open();
            InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8888);
            // 与本地端口绑定
            serverSocketChannel.socket().bind(address);
            // 设置为非阻塞模式
            //serverSocketChannel.configureBlocking(false);
            logger.info("初始化服务端连接完成……");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * 通知Reactor注册选择器, 在注册过程中指出该信道可以进行Read操作
     */
    private void register(SocketChannel socketChannel) {
        try {
            socketChannel.configureBlocking(false);
            // 唤起selector以防锁未释放
            int selectorIndex = new Random().nextInt(Server.processNum);
            Server.selector[selectorIndex].wakeup();
            socketChannel.register(Server.selector[selectorIndex], SelectionKey.OP_READ);
            logger.info("Acceptor注册Socket到Reactor Selector" + selectorIndex + "……");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
