package PlainReactorLoop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * 连接事件就绪,处理连接事件
 */
class Acceptor implements Runnable {
    private Logger logger = LoggerFactory.getLogger(Acceptor.class);
    private ServerSocketChannel serverSocketChannel;
    private Selector selector = null;

    Acceptor() {
        try {
            // 初始化selector
            selector = Selector.open();

            // 初始化服务端连接
            initializeServerSocket();
            logger.info("初始化服务端连接完成……");

            // 注册选择器, 在注册过程中指出该信道可以进行Accept操作
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            logger.info("注册服务端连接到Acceptor selector……");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void start() {
        new Thread(new Acceptor()).start();
    }

    public void run() {
        while (true) {
            try {
                selector.select();
                Set<SelectionKey> selected = selector.selectedKeys();
                Iterator<SelectionKey> it = selected.iterator();
                while (it.hasNext()) {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    logger.info("接收到客户端连接……");
                    if (socketChannel != null) {
                        // 通知Reactor注册选择器, 在注册过程中指出该信道可以进行Read操作
                        socketChannel.configureBlocking(false);
                        socketChannel.register(Server.selector, SelectionKey.OP_READ);
                        logger.info("Acceptor注册Socket到Reactor Selector……");
                    }
                    it.remove();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 初始化服务端连接
     */
    private Acceptor initializeServerSocket() {
        try {
            // 打开监听信道
            serverSocketChannel = ServerSocketChannel.open();
            InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8888);
            //与本地端口绑定
            serverSocketChannel.socket().bind(address);
            // 设置为非阻塞模式
            serverSocketChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }
}
