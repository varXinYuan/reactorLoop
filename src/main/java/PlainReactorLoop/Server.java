package PlainReactorLoop;

import java.nio.channels.Selector;


public class Server {
    // 读取配置内的进程数
    public static Integer processNum = 4;

    // 全局selector初始化
    public static Selector[] selector = new Selector[Server.processNum];

    public static void main(String[] args) {
        // 运行Reactor
        Reactor.start(Server.processNum);

        // 运行Acceptor
        Acceptor.start();
    }
}