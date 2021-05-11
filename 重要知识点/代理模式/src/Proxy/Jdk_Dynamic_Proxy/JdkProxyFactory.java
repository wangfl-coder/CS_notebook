package Proxy.Jdk_Dynamic_Proxy;

import java.lang.reflect.Proxy;

/*
    代理对象的工厂类
 */
public class JdkProxyFactory {
    public static Object getProxy(Object target){
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new DebugInvocationHandler(target));
    }
}
