package Runnable_ThreadPoolExecutor;



import MyThreadFactory.MyThreadFactory;

import java.util.concurrent.*;

public class RunnableDemo {

    private static final int CORE_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 10;
    private static final int QUEUE_CAPACITY = 100;
    private static final Long KEEP_ALIVE_TIME = 1L;



//    public static void printThreadPoolStatus(ThreadPoolExecutor threadPool){
//        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1,threadPool.getThreadFactory());
//        scheduledExecutorService.scheduleAtFixedRate(() -> {
//
//            System.out.println("=========================");
//            System.out.println("ThreadPool Size:" + threadPool.getPoolSize());
//            System.out.println("Active Threads: {}" + threadPool.getActiveCount());
//            System.out.println("Number of Tasks : {}" + threadPool.getCompletedTaskCount());
//            System.out.println("Number of Tasks in Queue: {}" + threadPool.getQueue().size());
//            System.out.println("=========================");
//        },0,1,TimeUnit.SECONDS);
//        //System.out.println(threadPool.isShutdown());
//
//    }

    public static void main(String[] args) {
        //1。利用guava的ThreadFactoryBuilder创建自定义线程池名和线程名的线程工厂
        //java8后删除了该类，只能通过Executors.defaultThreadFactory()创建默认参数的线程工厂
//      ThreadFactory threadFactory = Executors.defaultThreadFactory()

        //2。实现ThreadFactory接口，创建自定义名字的ThreadFactory
        MyThreadFactory myThreadFactory = new MyThreadFactory(Executors.defaultThreadFactory(),"wfl");

        // 使用ThreadPoolExecutor构造方法创建线程池
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(QUEUE_CAPACITY),
                myThreadFactory,
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        //printThreadPoolStatus(executor);
        for (int i=0;i<10;i++){
            //创建MyRunnable对象
            MyRunnable worker = new MyRunnable(""+i);
            //执行Runnable
            executor.execute(worker);
        }
        //终止线程池
        executor.shutdown();
        while (!executor.isTerminated()){
        }
        System.out.println("Finished all threads");
    }

}
