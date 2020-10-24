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
    private Integer selectorIndex;

    Reactor(Integer selectorIndex) {
        this.selectorIndex = selectorIndex;

        try {
            // 初始化 selector
            Server.selector[selectorIndex] = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void start(Integer processNum) {
        for (Integer selectorIndex = 0; selectorIndex < processNum; selectorIndex++) {
            new Thread(new Reactor(selectorIndex)).start();
        }
    }

    public void run() {
        try {
            logger.info("Reactor" + this.selectorIndex + " 运行中……");
            while (!Thread.interrupted()) {
                // 创建选择器
                Server.selector[selectorIndex].select();
                logger.info("Reactor" + this.selectorIndex + " 处理IO事件……");
                Set<SelectionKey> selected = Server.selector[selectorIndex].selectedKeys();
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
            logger.info("Reactor" + this.selectorIndex + " IO读写事件分发……");
            SocketChannel socketChannel = (SocketChannel) k.channel();
            new Handler(socketChannel).run();
        }
    }
}