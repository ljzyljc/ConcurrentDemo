package com.example.concurrentdemo.concurrent;

import android.annotation.TargetApi;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForkJoinTest {


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void main(String[] arsg){

        ForkJoinPool forkJoinPool = new ForkJoinPool(4);

        Fibonacci fibonacci = new Fibonacci(4);

        Integer result = forkJoinPool.invoke(fibonacci);

        System.out.println("========");
        System.out.println("========"+result);

    }




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    static class Fibonacci extends RecursiveTask<Integer>{


        final int n;
        Fibonacci(int n){
            this.n = n;
        }
        @Override
        protected Integer compute() {
            if (n <= 1){
                return n;
            }
            Fibonacci f1 = new Fibonacci(n -1);
            //创建子任务
              f1.fork();
            Fibonacci f2 = new Fibonacci(n -2);
            return f2.compute() + f1.join();
        }
    }


}
