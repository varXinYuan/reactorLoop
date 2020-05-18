package SingleReactorSingleProc;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 连接事件就绪,处理连接事件
 */
class Acceptor implements Runnable {
    private SelectionKey selectionKey;

    Acceptor(SelectionKey key) {
        selectionKey = key;
    }

    @Override
    public void run() {
        try {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
            SocketChannel socketChannel = serverSocketChannel.accept();
            System.out.println("接收到客户端连接……");
            if (socketChannel != null) {
                // 注册选择器, 在注册过程中指出该信道可以进行Read操作
                Selector selector = selectionKey.selector();
                socketChannel.configureBlocking(false);
                socketChannel.register(selector, SelectionKey.OP_READ);
                System.out.println("注册客户端连接至selector……");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
