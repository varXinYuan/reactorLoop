package PlainReactorLoop;

import java.nio.channels.spi.AbstractSelectableChannel;

public class SocketInfo {
    public AbstractSelectableChannel socketChannel;
    public int socketType;

    SocketInfo(AbstractSelectableChannel socketChannel, int socketType) {
        this.socketChannel = socketChannel;
        this.socketType = socketType;
    }
}
