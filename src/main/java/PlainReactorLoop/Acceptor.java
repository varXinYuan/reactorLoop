package PlainReactorLoop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 连接事件就绪,处理连接事件
 */
class Acceptor implements Runnable {
    private ServerSocketChannel serverSocketChannel;
    private Logger logger= LoggerFactory.getLogger(Acceptor.class);

    Acceptor() {
        // 初始化服务端连接
        initializeServerSocket();
        logger.info("初始化服务端连接完成……");

        // 注册选择器, 在注册过程中指出该信道可以进行Accept操作
        Server.reactorSocketQueue.add(new SocketInfo(serverSocketChannel, SelectionKey.OP_ACCEPT));
        logger.info("通知Reactor注册服务端连接……");
    }

    public void run() {
        while (true) {
            try {
                Server.acceptSockets.take();
                SocketChannel socketChannel = serverSocketChannel.accept();
                logger.info("接收到客户端连接……");
                if (socketChannel != null) {
                    // 通知Reactor注册选择器, 在注册过程中指出该信道可以进行Read操作
                    socketChannel.configureBlocking(false);
                    Server.reactorSocketQueue.add(new SocketInfo(socketChannel, SelectionKey.OP_READ));
                    logger.info("通知Reactor注册客户端连接...");
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
