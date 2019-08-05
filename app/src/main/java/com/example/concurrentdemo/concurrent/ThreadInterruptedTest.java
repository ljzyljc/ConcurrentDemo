package com.example.concurrentdemo.concurrent;

public class ThreadInterruptedTest {


    public static void main(String[] args){

    }

    private static void test(){

        Thread thread = Thread.currentThread();
        while (true){
            if (thread.isInterrupted()){
                break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();  //TODO:中断异常被抛出后中断标识也会被清除，所有需要重新中断一下
                e.printStackTrace();
            }


        }


    }


}
