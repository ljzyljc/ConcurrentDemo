package com.example.concurrentdemo.concurrent;

import java.util.concurrent.atomic.AtomicInteger;

public class AddCountTest {

    private static long count = 0;
    private static AtomicInteger atomicInteger = new AtomicInteger(0);
    private void add10K(){
        int index = 0;
        while (index++ < 10000){
            count += 1;
            atomicInteger.addAndGet(1);
        }
    }

    public static void calc(){
        final AddCountTest test = new AddCountTest();

        Thread threadOne = new Thread(new Runnable() {
            @Override
            public void run() {
                test.add10K();
            }
        });
        Thread threadTwo = new Thread(new Runnable() {
            @Override
            public void run() {
                test.add10K();
            }
        });

        threadOne.start();
        threadTwo.start();

        try {
            threadOne.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            threadTwo.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args){

        calc();
        System.out.println("======="+count);  //10000 ~ 20000 之间
        System.out.println("================="+atomicInteger.get()); //20000
    }

    //count = 0 写入各自的CPU缓存中

    /**
     * count += 1 至少需要三条指令
     *
     * 1.首先，需要把变量count从内存加载到CPU的寄存器
     * 2.在寄存器中中执行 +1操作
     * 3.最后，将结果写入内存（缓存机制导致写入的是CPU缓存而不是内存）
     *
     */


}
