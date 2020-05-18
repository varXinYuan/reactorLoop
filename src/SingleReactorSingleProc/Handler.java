package SingleReactorSingleProc;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * 处理读写业务逻辑
 */
class Handler implements Runnable {
    public static final int READING = 0, WRITING = 1;
    int state;
    private final SelectionKey selectionKey;

    Handler(SelectionKey key) {
        this.state = READING;
        this.selectionKey = key;
    }

    @Override
    public void run() {
        System.out.println("处理业务逻辑……");
        if (state == READING) {
            try {
                read();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (state == WRITING) {
            write();
        }
    }

    private void read() throws IOException {
        SocketChannel socket = (SocketChannel) this.selectionKey.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bytesRead = socket.read(buffer);
        if (bytesRead > 0) {
            buffer.flip();
            // 将字节转化为为UTF-16的字符串
            String receivedString = Charset.forName("utf-8").newDecoder().decode(buffer).toString();
            // 控制台打印出来
            System.out.println("接收到来自" + socket.socket().getRemoteSocketAddress() + "的信息:" + receivedString);

            //socket.close();
            //下一步处理写事件
            //this.selectionKey.interestOps(SelectionKey.OP_WRITE);
            //this.state = WRITING;
        }
    }

    private void write() {
        process();
        //下一步处理读事件
        this.selectionKey.interestOps(SelectionKey.OP_READ);
        this.state = READING;
    }

    /**
     * task 业务处理
     */
    public void process() {
        //do something
    }
}

