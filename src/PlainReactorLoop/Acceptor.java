package PlainReactorLoop;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 连接事件就绪,处理连接事件
 */
class Acceptor implements Runnable {
    private ServerSocketChannel serverSocketChannel;

    Acceptor() {
        // 初始化服务端连接
        initializeServerSocket();

        // 注册选择器, 在注册过程中指出该信道可以进行Accept操作
        Server.reactorSocketQueue.add(new SocketInfo(serverSocketChannel, SelectionKey.OP_ACCEPT));
        System.out.println("通知Reactor注册服务端连接……");
    }

    @Override
    public void run() {
        while (true) {
            try {
                Server.acceptMutex.wait();
                SocketChannel socketChannel = serverSocketChannel.accept();
                System.out.println("接收到客户端连接……");
                if (socketChannel != null) {
                    // 通知Reactor注册选择器, 在注册过程中指出该信道可以进行Read操作
                    socketChannel.configureBlocking(false);
                    Server.reactorSocketQueue.add(new SocketInfo(socketChannel, SelectionKey.OP_READ));
                    System.out.println("通知Reactor注册客户端连接...");
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
            System.out.println("初始化服务端连接……");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }
}
