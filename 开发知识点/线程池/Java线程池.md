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