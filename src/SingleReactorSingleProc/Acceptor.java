package SingleReactorSingleProc;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;

/**
 * 连接事件就绪,处理连接事件
 */
class Acceptor implements Runnable {
    private static Acceptor instance = null;

    private ServerSocketChannel serverSocketChannel;

    private Acceptor() {
        // 初始化服务端连接
        initializeServerSocket();

        // 注册选择器, 在注册过程中指出该信道可以进行Accept操作
        Reactor.Instance().register(serverSocketChannel, SelectionKey.OP_ACCEPT);
        System.out.println("通知Reactor注册服务端连接……");
    }

    public static Acceptor Instance() {
        if (instance == null) {
            instance = new Acceptor();
        }

        return instance;
    }

    @Override
    public void run() {
        try {
            SocketChannel socketChannel = serverSocketChannel.accept();
            System.out.println("接收到客户端连接……");
            if (socketChannel != null) {
                // 通知Reactor注册选择器, 在注册过程中指出该信道可以进行Read操作
                socketChannel.configureBlocking(false);
                Reactor.Instance().register(socketChannel, SelectionKey.OP_READ);
                System.out.println("通知Reactor注册客户端连接...");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void register(AbstractSelectableChannel socketChannel, int selectionKeyOp)

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
