package Proxy.Jdk_Dynamic_Proxy;

public class Main {
    public static void main(String[] args) {
        IHello iHello = (IHello) JdkProxyFactory.getProxy(new HelloImpl());
        iHello.sayFuck();
        iHello.sayHello();
    }
}
