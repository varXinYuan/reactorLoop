package SingleReactorSingleProc;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 等待事件到来，分发事件处理
 */
class Reactor implements Runnable {
    private Selector selector;

    Reactor() throws IOException {
        // 创建选择器
        selector = Selector.open();

        // 初始化服务端socket
        ServerSocketChannel serverSocketChannel = initializeServer();

        // 注册选择器, 在注册过程中指出该信道可以进行Accept操作
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("注册服务端连接至selector……");
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                // 创建选择器
                selector.select();
                Set selected = selector.selectedKeys();
                Iterator it = selected.iterator();
                while (it.hasNext()) {
                    System.out.println("dispatch key 1");
                    //分发事件处理
                    dispatch((SelectionKey) (it.next()));
                    it.remove();
                }
            }
        } catch (IOException e) {
            //do something
            e.printStackTrace();
        }
    }

    /**
     * 初始化服务端连接
     */
    public ServerSocketChannel initializeServer() throws IOException {
        // 打开监听信道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8888);
        //与本地端口绑定
        serverSocketChannel.socket().bind(address);
        // 设置为非阻塞模式
        serverSocketChannel.configureBlocking(false);
        System.out.println("初始化服务端连接……");

        return serverSocketChannel;
    }

    /**
     * 分发事件
     */
    void dispatch(SelectionKey k) {
        // 若是连接事件获取是acceptor
        // 若是IO读写事件获取是handler
        if (k.isAcceptable()) {
            new Acceptor(k).run();
        } else if (k.isReadable()) {
            //new Handler(k).run();
            new Thread(new Handler(k)).start();
        }
    }
}
