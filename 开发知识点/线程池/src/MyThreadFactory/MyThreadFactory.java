package MyThreadFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 线程工厂，设置线程名字，有利于定位问题
 */
public final class MyThreadFactory implements ThreadFactory {
    private final AtomicInteger threadNum = new AtomicInteger();
    private final ThreadFactory delegate;
    private String name;

    /**
     * 创建一个带名字的线程工厂
     * @param delegate
     * @param name
     */
    public MyThreadFactory(ThreadFactory delegate,String name){
        this.delegate = delegate;
        this.name = name;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = delegate.newThread(r);
        t.setName(name + " [#" + threadNum.incrementAndGet() + "]");
        return t;
    }
}
