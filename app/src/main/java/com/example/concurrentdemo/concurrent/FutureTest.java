package com.example.concurrentdemo.concurrent;

import com.example.concurrentdemo.concurrent.bean.Result;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class FutureTest {

    private static String name = "";
    public static void main(String[] args){
        test();
    }

    public static void test3(){

        FutureTask<Integer> futureTask = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return 100+23;
            }
        });
        Thread thread = new Thread(futureTask);
        thread.start();

        try {
            Integer integer = futureTask.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public static void test2(){

        ExecutorService executor = Executors.newFixedThreadPool(1);

        FutureTask<Integer> futureTask = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return 1+2;
            }
        });

        executor.submit(futureTask);

        try {
            Integer integer = futureTask.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }











    public static void test(){

        ExecutorService executor = Executors.newFixedThreadPool(1);
        //创建Result对象
        Result result = new Result();
        result.setMsg("林世妹");
        //提交任务
        Future<Result> future = executor.submit(new Task(result),result);

        try {
            result = future.get();
            System.out.println("========"+result.getMsg());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static class Task implements Runnable{

        Result result;
        public Task(Result result){
            this.result = result;
        }
        @Override
        public void run() {
            //操作result
            name = "Jackie"+result.getMsg();
            result.setMsg(name);
        }
    }


}
