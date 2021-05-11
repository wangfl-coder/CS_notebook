package Proxy.Jdk_Dynamic_Proxy;

public class HelloImpl implements IHello{

    @Override
    public void sayHello() {
        System.out.println("hello");
    }

    @Override
    public void sayFuck() {
        System.out.println("Fuck");
    }
}
