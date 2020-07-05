package PlainReactorLoop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * 等待事件到来，分发事件处理
 */
class Reactor implements Runnable {
    public static final Logger logger = LoggerFactory.getLogger(Reactor.class);

    Reactor() {
        try {
            // 初始化 selector
            Server.selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void start() {
        new Thread(new Reactor()).start();
    }

    public void run() {
        try {
            logger.info("Reactor 运行中……");
            while (!Thread.interrupted()) {
                // 创建选择器
                Server.selector.select();
                logger.info("Reactor 处理IO事件……");
                Set<SelectionKey> selected = Server.selector.selectedKeys();
                Iterator<SelectionKey> it = selected.iterator();
                while (it.hasNext()) {
                    dispatch(it.next());
                    it.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 分发事件处理
     */
    void dispatch(SelectionKey k) {
        if (k.isReadable()) {
            // 若是IO读写事件，调handler处理
            logger.info("IO读写事件，处理Socket……");
            SocketChannel socketChannel = (SocketChannel) k.channel();
            new Handler(socketChannel).run();
        }
    }
}
