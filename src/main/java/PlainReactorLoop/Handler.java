package PlainReactorLoop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * 处理读写业务逻辑
 */
class Handler implements Runnable {
    private SocketChannel socketChannel;
    private static final Logger logger = LoggerFactory.getLogger(Handler.class);

    public Handler(SocketChannel socket) {
        socketChannel = socket;
    }

    public void run() {
        try {
            logger.info("处理业务逻辑……");
            String question = read();
            String resultMsg = process(question);
            write(resultMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String read() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bytesRead = socketChannel.read(buffer);
        if (bytesRead <= 0) {
            throw new IOException("客户端数据为空");
        }

        buffer.flip();
        // 将字节转化为为UTF-16的字符串
        String receivedString = Charset.forName("utf-8").newDecoder().decode(buffer).toString();
        // 控制台打印
        logger.info("接收到来自" + socketChannel.socket().getRemoteSocketAddress() + "的信息:" + receivedString);

        return receivedString;
    }

    private void write(String resultMsg) throws IOException {
        socketChannel.write(ByteBuffer.wrap(resultMsg.getBytes()));

        logger.info("回复" + socketChannel.socket().getRemoteSocketAddress() + "信息:" + resultMsg);
    }

    /**
     * task 业务处理
     */
    public String process(String question) {
        return question.replace("?", ".");
    }
}