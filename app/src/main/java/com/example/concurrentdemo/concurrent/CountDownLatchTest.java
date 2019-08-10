package com.example.concurrentdemo.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CountDownLatchTest {


    static Executor executor = Executors.newFixedThreadPool(2);
    static CountDownLatch countDownLatch = new CountDownLatch(2);

    public static void main(String[] args){

        while (true){
            //查询未对账订单
            executor.execute(new Runnable() {
                @Override
                public void run() {

                    System.out.println("查询未对账订单");
                    countDownLatch.countDown();
                }
            });

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("查询派送单");
                    countDownLatch.countDown();
                }
            });

            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("执行对账操作");
            System.out.println("差异写入差异库");
            break;


        }


    }




}
