package PlainReactorLoop;

public class Server {
    public static void main(String[] args) {
        // 初始化Reactor
        Reactor.init();
        // 初始化Acceptor
        Acceptor.init();

        // 启动
        Reactor.Instance().run();
    }
}
