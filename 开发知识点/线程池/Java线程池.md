# 线程的好处

- 降低资源消耗：通过重复利用已创建的线程来降低线程创建和销毁造成的消耗。
- 提高响应速度：当任务到达时，不需要等到线程创建完就能执行任务。
- 提高线程的可管理性：线程时稀缺资源，如果无限的创建线程，不仅会消耗系统的资源，还会降低系统的稳定性，线程池可以统一对线程分配，调优和监控。

# Executor框架

## 简介

- 通过Executor来启动线程比使用Thread的start方法更好，效率高，便于管理，有助于避免this逃逸问题。

## 框架结构（三大组成部分）

- 任务（Runnable/Callable）:执行任务需要实现Runnable或Callable接口，实现接口的任务才能被ThreadPoolExecutor或ScheduledThreadPoolExecutor执行。
- 任务的执行（Executor）：Executor以及继承Executor的ExecutorService的接口。相关接口和类之间的关系如下图所示。![屏幕快照 2021-04-25 下午2.23.42](https://tva1.sinaimg.cn/large/008i3skNly1gpvyzujnfcj30xq0pa13s.jpg)
- 异步计算的结果（Future）：Future接口以及实现接口的实现类FutureTask类都能代表异步计算的结果。当我们把实现Runnable或Callable接口的类给ThreadPoolExecutor或ScheduledThreadPoolExecutor执行。调用submit方法时会返回一个FutureTask对象。

## Executor结构使用示意图

![屏幕快照 2021-04-25 下午2.39.46](https://tva1.sinaimg.cn/large/008i3skNly1gpvzgqg6wwj30vg0mm7cb.jpg)

- 主线程首先创建实现Runnable或者Callable接口的任务对象。
- 把创建的任务对象直接交给ExecutorService执行：execute(Runnable)或者submit(Runnable/Callable)。
- submit()方法返回一个FutureTask对象，该对象实现了Runnable接口，所以也可以创建FutureTask对象直接给ExecutorService执行。
- 主线程可以执行FutureTask.get()方法来等待任务执行完成，也可以执行FutureTask.cancel(booleanmayInterrupIfRunning)来取消此任务的执行。

## ThreadPoolExecutor类简单介绍

## 简介

线程实现类ThreadPoolExecutor是Executor框架最核心的类。

## 类分析

![屏幕快照 2021-04-25 下午3.28.30](https://tva1.sinaimg.cn/large/008i3skNly1gpw0uvgjnij31jc0s0k07.jpg)

## 类参数

- 三个重要参数

  - corePoolSize：核心线程数定义了最小可同时运行的线程数量
  - maximumPoolSize:当前任务队列容量已满，此时可同时运行的线程数为最大线程数
  - workQueue：当新任务来的时候会判断当前线程运行数是否达到核心线程数，如果达到则将新任务加入到队列中。

- 其他常见参数：

  - keepAliveTime：当线程池中线程数大于核心线程数，且没有新任务提交，核心线程外的线程不会立即销毁，超过keepAliveTime后才会被销毁。
  - unit：keepAliveTime的单位
  - threadfactory：executor创建线程的时候会用到
  - handler：饱和策略。

- 利用下图理解各参数：

  ![屏幕快照 2021-04-25 下午4.18.57](https://tva1.sinaimg.cn/large/008i3skNly1gpw2br84dlj31c90u0k5k.jpg)

- 饱和策略：如果当前同时运行线程数达到最大线程数且队列已经放满了任务时，ThreadPoolExecutor定义一些策略：

  - **`ThreadPoolExecutor.AbortPolicy`**：抛出 `RejectedExecutionException`来拒绝新任务的处理。
  - **`ThreadPoolExecutor.CallerRunsPolicy`**：等待线程池执行完有空线程再执行。
  - **`ThreadPoolExecutor.DiscardPolicy`：** 不处理新任务，直接丢弃掉。
  - **`ThreadPoolExecutor.DiscardOldestPolicy`：** 此策略将丢弃最早的未处理的任务请求。

- 创建线程池的方法

  - 推荐使用ThreadPoolExecutor构造函数创建线程池
  - 三种ThreadPoolExecutor：FixedThreadPool；SingleThreadExecutor；CachedThreadPool

## ThreadPoolExecutor使用用例

- 参考包ThreadPoolExecutor

- 线程池原理参考图：![屏幕快照 2021-04-25 下午5.54.21](https://tva1.sinaimg.cn/large/008i3skNly1gpw53dbazdj30xm0esjtb.jpg)

### Runnable vs Callable

- 区别：实现Runnable接口的任务执行不返回结果和抛出异常，Callable则会
- 工具类Executors实现两种`Runnable` 对象和 `Callable` 对象之间的相互转换。`Executors.callable（Runnable task`）或 `Executors.callable（Runnable task，Object resule）`）。

### execute vs submit

- 区别：execute用于执行不需要返回值的任务，所以无法判断任务是否已经执行完；submit用于执行需要返回值的任务，线程池返回一个Future 类型的对象，通过Future对象能够判断任务执行完成。

### shutdown vs shutdownNow

- 区别：shutdown关闭线程池，状态为SHUTDOWN，线程池不会接受新任务，但是队列里任务必须执行完。shutdownNow是关闭线程池，状态变为STOP，且停止当前正在执行的任务，返回带执行任务List。

### isTerminated vs isShutdown

- 区别：isShutdown是调用shutdown后，返回true；isTerminated是调用shutdown后并执行完任务队列后，返回true。

## 常见的线程池详解

### FixedThreadPool

称为可重用固定线程数的线程池,核心线程数和最大线程数为固定大小且相等。执行任务示意图如下。

![屏幕快照 2021-04-25 下午7.52.11](https://tva1.sinaimg.cn/large/008i3skNly1gpw8hnd2i8j318s0u0gun.jpg)

源码：

```java
/**
     * 创建一个可重用固定数量线程的线程池
     */
    public static ExecutorService newFixedThreadPool(int nThreads, ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(nThreads, nThreads,
                                      0L, TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<Runnable>(),
                                      threadFactory);
    }

```



- 不推荐使用原因：
  - 同时运行的线程数只能是核心线程数，且当达到核心线程数时有新任务加入时，将会加入到无界队列中。
  - 由于使用的是无界队列，所以maximumPoolSize是一个无效参数，因为不会存在任务队列填满的情况，corePoolSize和maximumPoolSize被设为同一值。（看源码Executors）
  - keepAliveTime是一个无效参数，原因同2.
  - 运行中的线程池永远不会拒绝新任务，任务多会造成OOM。（内存溢出）

### SingleThreadExecutor

是一个只有一个线程的线程池。执行过程示意图如下。

![屏幕快照 2021-04-25 下午8.07.55](https://tva1.sinaimg.cn/large/008i3skNgy1gpw8xmfl0ej317q0oitfw.jpg)

- 源码：

  ```java
  /**
       *返回只有一个线程的线程池
       */
      public static ExecutorService newSingleThreadExecutor(ThreadFactory threadFactory) {
          return new FinalizableDelegatedExecutorService
              (new ThreadPoolExecutor(1, 1,
                                      0L, TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<Runnable>(),
                                      threadFactory));
      }
     public static ExecutorService newSingleThreadExecutor() {
          return new FinalizableDelegatedExecutorService
              (new ThreadPoolExecutor(1, 1,
                                      0L, TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<Runnable>()));
      }
  ```

- 不推荐原因：使用无界队列，与FixedThreadPool一样。

### CachedThreadPool

是根据需要创建新线程的线程池。执行过程示意图如下。

![屏幕快照 2021-04-25 下午8.14.55](https://tva1.sinaimg.cn/large/008i3skNly1gpw95h4kqcj31840sm11b.jpg)

- 源码：

  ```java
  /**
       * 创建一个线程池，根据需要创建新线程，但会在先前构建的线程可用时重用它。
       */
      public static ExecutorService newCachedThreadPool(ThreadFactory threadFactory) {
          return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                        60L, TimeUnit.SECONDS,
                                        new SynchronousQueue<Runnable>(),
                                        threadFactory);
      }
      public static ExecutorService newCachedThreadPool() {
          return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                        60L, TimeUnit.SECONDS,
                                        new SynchronousQueue<Runnable>());
      }
  
  ```

- 不推荐原因：因为maximumPoolSize是Integer.MAX_VALUE，当队列已满，又有不断的新任务，就会造成大量新建线程，造成OOM。

### ScheduledThreadPoolExecutor

主要用来在给定的延迟后运行任务，或者定期运行任务（任务调度）。在实际项目中基本不会用到，一般会有更好的方案比如quarts。

- 简介：ScheduledThreadPoolExecutor使用的任务队列DelayQueue封装了一个PriorityQueue。PriorityQueue会对队列中任务进行排序，执行所需时间短的放前面先被执行（ScheduledFutureTask的time变量小的）。如果执行所需时间相同则将先提交的放前面先执行（ScheduledFutureTask的squenceNumber小的。

- ScheduledThreadPoolExecutor和Timer的比较（JDK1.5之后，没有理由用Timer任务调度）：

  - `Timer` 对系统时钟的变化敏感，`ScheduledThreadPoolExecutor`不是；
  - `Timer` 只有一个执行线程，因此长时间运行的任务可以延迟其他任务。 `ScheduledThreadPoolExecutor` 可以配置任意数量的线程。 此外，如果你想（通过提供 ThreadFactory），你可以完全控制创建的线程;
  - 在`TimerTask` 中抛出的运行时异常会杀死一个线程，从而导致 `Timer` 死机:-( ...即计划任务将不再运行。`ScheduledThreadExecutor` 不仅捕获运行时异常，还允许您在需要时处理它们（通过重写 `afterExecute` 方法`ThreadPoolExecutor`）。抛出异常的任务将被取消，但其他任务将继续运行。

- 运行过程图：![屏幕快照 2021-04-26 上午10.35.08](https://tva1.sinaimg.cn/large/008i3skNly1gpwxzyezizj30qi0fmtbd.jpg)

- ScheduledThreadPoolExecutor执行分为两个部分：

  - 当ScheduledThreadPoolExecutor调用scheduleAtFixedRate()或者scheduleWithFixedDelay()方法时，将会把实现RunnableScheduledFuture接口的ScheduledFutureTask任务添加到DelayQueue任务队列中。
  - 线程池从任务队列中取任务并执行。

- 实现周期行执行任务，ScheduledThreadPoolExecutor做了一下修改：

  - 使用DelayQueue作为任务队列
  - 获取任务的方式不同
  - 执行周期任务时，会有额外的处理。

- 执行周期性任务过程图

  ![屏幕快照 2021-04-26 上午11.20.38](https://tva1.sinaimg.cn/large/008i3skNly1gpwzb8qywqj30gt0bxtbh.jpg)

  1. 从DelayQueue中获取过期任务DelayQueue.take()，过期任务是time变量大于当前系统时间
  2. 执行任务
  3. 修改任务time，即下次执行的时间
  4. 添加任务到DelayQueue

## 线程池大小确定

线程池设置太小会不够用，如果同一时间内有大量任务就会产生OOM；设置太高会增加上下文切换的成本，上下文切换指的是在不同时间片运行不同线程，因为CPU核数有限，如果线程数量远大于核数CPU就会频繁的在线程间切换，就会增加任务的执行时间。

有一个简单并且适用面比较广的公式：

- **CPU 密集型任务(N+1)：** 这种任务消耗的主要是 CPU 资源，可以将线程数设置为 N（CPU 核心数）+1，比 CPU 核心数多出来的一个线程是为了防止线程偶发的缺页中断，或者其它原因导致的任务暂停而带来的影响。一旦任务暂停，CPU 就会处于空闲状态，而在这种情况下多出来的一个线程就可以充分利用 CPU 的空闲时间。
- **I/O 密集型任务(2N)：** 这种任务应用起来，系统会用大部分的时间来处理 I/O 交互，而线程在处理 I/O 的时间段内不会占用 CPU 来处理，这时就可以将 CPU 交出给其它线程使用。因此在 I/O 密集型任务的应用中，我们可以多配置一些线程，具体的计算方法是 2N。

**如何判断是 CPU 密集任务还是 IO 密集任务？**

CPU 密集型简单理解就是利用 CPU 计算能力的任务比如你在内存中对大量数据进行排序。单凡涉及到网络读取，文件读取这类都是 IO 密集型，这类任务的特点是 CPU 计算耗费时间相比于等待 IO 操作完成的时间来说很少，大部分时间都花在了等待 IO 操作完成上。



# 线程池的使用场景

1. 当有多个互不干扰的耗时任务（任务可以是做一样的事）

2. 不使用线程池，任务执行就会顺序执行，看图。

   ![屏幕快照 2021-04-26 下午2.48.29](https://tva1.sinaimg.cn/large/008i3skNly1gpx5c4cowvj30j9041gll.jpg)

3. 使用线程池，任务可以并行执行，看图。

   ![屏幕快照 2021-04-26 下午2.50.50](https://tva1.sinaimg.cn/large/008i3skNly1gpx5e0817hj30ih08kaae.jpg)



# 线程池最佳实践

- 使用ThreadPoolExecutor构造函数创建线程池。

- 监测线程池运行状态：使用ThreadPoolExecutor的API做一个简陋的监测Demo。printThreadPoolStatus()会每隔一秒打印线程池的线程数、活跃线程数、完成的任务数、积极任务队列中的任务数。见下代码块。

  ```java
  /**
       * 打印线程池的状态
       *
       * @param threadPool 线程池对象
       */
      public static void printThreadPoolStatus(ThreadPoolExecutor threadPool) {
          ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1, createThreadFactory("print-images/thread-pool-status", false));
          scheduledExecutorService.scheduleAtFixedRate(() -> {
              log.info("=========================");
              log.info("ThreadPool Size: [{}]", threadPool.getPoolSize());
              log.info("Active Threads: {}", threadPool.getActiveCount());
              log.info("Number of Tasks : {}", threadPool.getCompletedTaskCount());
              log.info("Number of Tasks in Queue: {}", threadPool.getQueue().size());
              log.info("=========================");
          }, 0, 1, TimeUnit.SECONDS);
      }
  
  ```

- 建议不同类别业务使用不同线程池

  - 为什么要这样做？

  - 答：可能造成死锁。比如线程池核心线程为n，存在父任务数n，父任务包含两个子任务，当其中子任务1完成，父任务占用了核心线程资源，子任务2就会阻塞在任务队列中，但父任务只有在子任务执行完才能执行，所以就产生了死锁。看图更容易理解。

    ![屏幕快照 2021-04-26 下午4.06.59](https://tva1.sinaimg.cn/large/008i3skNly1gpx7l9ypelj30jq0g70tv.jpg)

- 记得给线程池命名：默认创建的线程命名为pool-1-thread-n，不利于定位我们的问题。以下是两种线程命名方式

  - 利用guava的ThreadFactoryBuilder(java8后删除了该类，改成Executors.defaultThreadFactory()创建默认参数的ThreadFactory)：

  ```java
  ThreadFactory threadFactory = new ThreadFactoryBuilder()
                          .setNameFormat(threadNamePrefix + "-%d")
                          .setDaemon(true).build();
  ExecutorService threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MINUTES, workQueue, threadFactory)
  ```

  - 自己实现ThreadFactory(见src目录下MyThreadFactory文件夹，在RunnableDemo测试)

    ```java
    import java.util.concurrent.Executors;
    import java.util.concurrent.ThreadFactory;
    import java.util.concurrent.atomic.AtomicInteger;
    /**
     * 线程工厂，它设置线程名称，有利于我们定位问题。
     */
    public final class NamingThreadFactory implements ThreadFactory {
    
        private final AtomicInteger threadNum = new AtomicInteger();
        private final ThreadFactory delegate;
        private final String name;
    
        /**
         * 创建一个带名字的线程池生产工厂
         */
        public NamingThreadFactory(ThreadFactory delegate, String name) {
            this.delegate = delegate;
            this.name = name; // TODO consider uniquifying this
        }
    
        @Override 
        public Thread newThread(Runnable r) {
            Thread t = delegate.newThread(r);
            t.setName(name + " [#" + threadNum.incrementAndGet() + "]");
            return t;
        }
    
    }
    ```

    



