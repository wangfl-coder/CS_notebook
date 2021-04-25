package Runnable_ThreadPoolExecutor;

import java.util.Date;

/**
 * 执行一个需要5秒的任务
 */
public class MyRunnable implements Runnable{
    private String command;

    public MyRunnable(String s){
        this.command = s;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "  start.time:" + new Date());
        processCommand();
        System.out.println(Thread.currentThread().getName() + "  end.time:" + new Date());
    }

    public void processCommand() {
        try{
            Thread.sleep(20000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public String toString(){
        return this.command;
    }
}
