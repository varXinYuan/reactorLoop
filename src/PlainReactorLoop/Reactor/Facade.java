package PlainReactorLoop.Reactor;

import PlainReactorLoop.Server;
import PlainReactorLoop.SocketInfo;

public class Facade implements Runnable {
    @Override
    public void run() {
        try {
            SocketInfo socketInfo = Server.reactorSocketQueue.take();
            socketInfo.socketChannel.register(Server.selector, socketInfo.socketType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
