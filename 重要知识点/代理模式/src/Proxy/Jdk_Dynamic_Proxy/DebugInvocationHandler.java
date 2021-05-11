package Proxy.Jdk_Dynamic_Proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class DebugInvocationHandler implements InvocationHandler {
    //代理类中真实对象
    private final Object target;

    public DebugInvocationHandler(Object target){
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //调用方法前，添加自己的操作
        System.out.println("before method " + method.getName());
        Object result = method.invoke(target,args);
        //调用方法后，可以添加自己的操作
        System.out.println("after method " + method.getName());
        return result;
    }
}
