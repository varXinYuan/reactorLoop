package SingleReactorSingleProc;

public class Test {
    public static void main(String[] args) throws Exception {
        Reactor.Instance().initAcceptor().run();
    }
}
