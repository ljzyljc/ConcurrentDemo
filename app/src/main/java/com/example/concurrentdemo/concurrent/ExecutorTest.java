package com.example.concurrentdemo.concurrent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ExecutorTest {

    private static AtomicInteger count = new AtomicInteger(0);
    static CustomRejectedExecutionHandler handler = new CustomRejectedExecutionHandler();
    public static void main(String[] args){

        test();

    }


    private static void test(){

        ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 3,
                60, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread t = new Thread(r);
                        String threadName = ExecutorTest.class.getSimpleName() + count.addAndGet(1);
                        t.setName(threadName);
                        return t;
                    }
                }, handler);

        for (int i = 0;i<10;i++){
            System.out.println("提交第" + i + "个任务!");
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getId()+"======开始");

                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println(Thread.currentThread().getId()+"======结束");
                }
            });
            System.out.println("提交第" + i + "个任务成功!");
        }



    }


    private static class CustomRejectedExecutionHandler implements RejectedExecutionHandler{

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            try {
                executor.getQueue().put(r);   //核心改造点，offer改成put会发生阻塞，offer会直接返回false
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }









}
