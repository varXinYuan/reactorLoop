package SingleReactorSingleProc;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * 处理读写业务逻辑
 */
class Handler implements Runnable {
    private SocketChannel socketChannel;

    Handler(SocketChannel socket) {
        socketChannel = socket;
    }

    @Override
    public void run() {
        System.out.println("处理业务逻辑……");
        try {
            String question = read();
            String resultMsg = process(question);
            write(resultMsg);
        } catch (IOException e) {
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
        System.out.println("接收到来自" + socketChannel.socket().getRemoteSocketAddress() + "的信息:" + receivedString);

        return receivedString;
    }

    private void write(String resultMsg) throws IOException {
        socketChannel.write(ByteBuffer.wrap(resultMsg.getBytes()));

        System.out.println("回复" + socketChannel.socket().getRemoteSocketAddress() + "信息:" + resultMsg);
    }

    /**
     * task 业务处理
     */
    public String process(String question) {
        String res = question.replace("?", ".");
        return res;
    }
}

