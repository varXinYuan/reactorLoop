package PlainReactorLoop.Reactor;

import PlainReactorLoop.Server;

import java.io.IOException;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * 等待事件到来，分发事件处理
 */
class Reactor implements Runnable {
    public void run() {
        try {
            while (!Thread.interrupted()) {
                // 创建选择器
                Server.selector.select();
                Set<SelectionKey> selected = Server.selector.selectedKeys();
                Iterator<SelectionKey> it = selected.iterator();
                while (it.hasNext()) {
                    ReactorFacade.logger.info("dispatch key 1");
                    //分发事件处理
                    dispatch(it.next());
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
            ReactorFacade.logger.info("连接事件，通知Acceptor");
            notifyAcceptor();
        } else if (k.isReadable()) {
            // 若是IO读写事件，调handler处理
            SocketChannel socketChannel = (SocketChannel) k.channel();
            ReactorFacade.logger.info("IO读写事件，通知HandlerMaster");
            notifyHandler(socketChannel);
        }
    }

    private void notifyHandler(SocketChannel socketChannel) {
        Server.handlerSocketQueue.add(socketChannel);
    }

    private void notifyAcceptor() {
        Server.acceptSockets.add(1);
    }
}
