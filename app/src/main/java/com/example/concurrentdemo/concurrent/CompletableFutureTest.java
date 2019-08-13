package com.example.concurrentdemo.concurrent;

import android.annotation.TargetApi;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 异步编程
 */
public class CompletableFutureTest {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void main(String[] args){

        test1();

    }

    //异步编程真的给力啊
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void test1(){

        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 7/0;
            }
        }).thenApply(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                return integer * 10;
            }
        }).exceptionally(new Function<Throwable, Integer>() {
            @Override
            public Integer apply(Throwable throwable) {
                System.out.println("=========="+throwable.getMessage());
                return 0;
            }
        });


    }





    @TargetApi(Build.VERSION_CODES.N)
    public static void test(){

        //任务1  洗水壶 -> 烧开水
        CompletableFuture<Void> f1 = CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                System.out.println("T1:洗水壶...");
                try {
                    TimeUnit.SECONDS.sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        //任务2：洗茶壶 -> 洗茶杯 -> 拿茶叶
        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(()->{

            try {
                System.out.println("T2:洗茶壶...");
                TimeUnit.SECONDS.sleep(1);

                System.out.println("T2:洗茶杯...");
                TimeUnit.SECONDS.sleep(1);

                System.out.println("T2:拿茶叶...");
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "龙井";
        });

        //任务3：任务1和任务2 完成后执行：泡茶

        CompletableFuture<String> f3 = f1.thenCombine(f2, new BiFunction<Void, String, String>() {
            @Override
            public String apply(Void aVoid, String s) {
                System.out.println("T1:拿到茶叶..."+s);
                System.out.println("T1:泡茶...");
                return "上茶："+s;

            }
        });

        //等待任务3执行结果
        System.out.println(f3.join());



    }


}
