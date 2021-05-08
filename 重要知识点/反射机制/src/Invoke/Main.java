/*
反射基本操作
 */
package Invoke;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchFieldException {
        /**
         * 创建TargetObject类的类对象class和创建TargetObject实例对象targetObject
         */
        Class<?> targetClass = Class.forName("Invoke.TargetObject");
        //Class targetClass = ClassLoader.getSystemClassLoader().loadClass("Invoke.TargetObject");通过类加载器获取类对象
        TargetObject targetObject = (TargetObject) targetClass.newInstance();
        /**
         * 获取类中所有方法
         */
        Method[] methods = targetClass.getDeclaredMethods();
        for (Method method : methods){
            System.out.println(method.getName());
        }
        /**
         * 获得指定方法并调用
         */
        Method publicMethod = targetClass.getDeclaredMethod("publicMethod", String.class);
        publicMethod.invoke(targetObject,"罗萍萍");

        /**
         * 获得指定参数，并修改参数值
         */
        Field field = targetClass.getDeclaredField("value");
        //修改参数值，需要取消对参数的安全检查
        field.setAccessible(true);
        field.set(targetObject,"王飞龙");

        /**
         * 获取private方法，并调用
         */
        Method privateMethod = targetClass.getDeclaredMethod("privateMethod");
        //调用private方法，需要取消安全性检查
        privateMethod.setAccessible(true);
        privateMethod.invoke(targetObject);
    }
}
