package SingleReactorSingleProc;

import java.io.IOException;
import java.nio.channels.*;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 等待事件到来，分发事件处理
 */
class Reactor implements Runnable {
    private static Reactor instance = null;
    private Selector selector;

    private Reactor() {
        // 创建选择器
        try {
            selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Reactor Instance() {
        if (instance == null) {
            instance = new Reactor();
        }

        return instance;
    }

    public static void init() {
        Reactor.Instance();
    }

    public void register(AbstractSelectableChannel socketChannel, int selectionKeyOp) {
        try {
            socketChannel.register(selector, selectionKeyOp);
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        }
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
            e.printStackTrace();
        }
    }

    /**
     * 分发事件
     */
    void dispatch(SelectionKey k) {
        if (k.isAcceptable()) {
            // 若是连接事件，调acceptor处理
            Acceptor.Instance().run();
        } else if (k.isReadable()) {
            // 若是IO读写事件，调handler处理
            SocketChannel socketChannel = (SocketChannel) k.channel();
            new Handler(socketChannel).run();
        }
    }
}
