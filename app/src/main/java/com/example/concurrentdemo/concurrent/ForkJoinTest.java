package com.example.concurrentdemo.concurrent;

import android.annotation.TargetApi;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.function.BiConsumer;

public class ForkJoinTest {


    //二分法递归的地将一个文件拆分成更小的文件，直到文件里面只有一行数据。

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void main(String[] args){
        String[] fc = {"hello world",
                "hello me",
                "hello fork",
                "hello join",
                "fork join in world"};
        // 创建 ForkJoin 线程池
        ForkJoinPool fjp =
                new ForkJoinPool(3);
        // 创建任务
        MR mr = new MR(
                fc, 0, fc.length);
        // 启动任务
        Map<String, Long> result =
                fjp.invoke(mr);
        // 输出结果
        result.forEach((k, v)->
                System.out.println(k+":"+v));
    }
    //MR 模拟类
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    static class MR extends
            RecursiveTask<Map<String, Long>> {
        private String[] fc;
        private int start, end;
        // 构造函数
        MR(String[] fc, int fr, int to){
            this.fc = fc;
            this.start = fr;
            this.end = to;
        }
        @Override protected
        Map<String, Long> compute(){
            if (end - start == 1) {
                return calc(fc[start]);
            } else {
                int mid = (start+end)/2;
                MR mr1 = new MR(
                        fc, start, mid);
                mr1.fork();
                MR mr2 = new MR(
                        fc, mid, end);
                // 计算子任务，并返回合并的结果
                return merge(mr2.compute(),
                        mr1.join());    //mr2.compute()和mr1.join()的顺序不可交换，如果join在前面会首先让当前线程阻塞在join()上。当join（）执行完才会执行了
                                        //mr2.compute(),这样的并行度就下来了。
            }
        }
        // 合并结果
        private Map<String, Long> merge(
                Map<String, Long> r1,
                Map<String, Long> r2) {
            Map<String, Long> result =
                    new HashMap<>();
            result.putAll(r1);
            // 合并结果
            r2.forEach((k, v) -> {
                Long c = result.get(k);
                if (c != null)
                    result.put(k, c+v);
                else
                    result.put(k, v);
            });
            return result;
        }
        // 统计单词数量
        private Map<String, Long>
        calc(String line) {
            Map<String, Long> result =
                    new HashMap<>();
            // 分割单词
            String [] words =
                    line.split("\\s+");
            // 统计单词数量
            for (String w : words) {
                Long v = result.get(w);
                if (v != null)
                    result.put(w, v+1);
                else
                    result.put(w, 1L);
            }
            return result;
        }
    }

//    @TargetApi(Build.VERSION_CODES.N)
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public static void main(String[] arsg) {
//
////        ForkJoinPool forkJoinPool = new ForkJoinPool(4);
////
////        Fibonacci fibonacci = new Fibonacci(4);
////
////        Integer result = forkJoinPool.invoke(fibonacci);
////
////        System.out.println("========");
////        System.out.println("========"+result);
//
//        String[] fc = {"hello world", "hello me", "hello fork", "hello join", "fork join in world"};
//
//        ForkJoinPool forkJoinPool = new ForkJoinPool(3);
//
//        MR mr = new MR(fc,0,fc.length);
//
//        Map<String,Long> result = forkJoinPool.invoke(mr);
//
//        result.forEach(new BiConsumer<String, Long>() {
//            @Override
//            public void accept(String s, Long aLong) {
//                System.out.println(s+":"+aLong);
//            }
//        });
//
//
//    }
//
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    static class MR extends RecursiveTask<Map<String, Long>> {
//
//        private String[] fc;
//        private int start, end;
//
//        MR(String[] fc, int fr, int to) {
//            this.fc = fc;
//            this.start = fr;
//            this.end = to;
//
//        }
//
//        @RequiresApi(api = Build.VERSION_CODES.N)
//        @Override
//        protected Map<String, Long> compute() {
//
//            if (end - start == 1) {
//                return calc(fc[start]);
//            } else {
//                int mid = (start+end)/2;
//                MR mr1 = new MR(fc,start,end);
//                mr1.fork();
//
//                MR mr2 = new MR(fc,mid,end);
//
//                //计算子任务，并返回合并的结果
//                return merge(mr2.compute(),mr1.join());
//
//            }
//        }
//
//        @RequiresApi(api = Build.VERSION_CODES.N)
//        private Map<String, Long> merge(Map<String, Long> r1, Map<String, Long> r2) {
//            Map<String, Long> result = new HashMap<>();
//            result.putAll(r1);
//            //合并结果
//
//
//            r2.forEach(new BiConsumer<String, Long>() {
//                @Override
//                public void accept(String k, Long v) {
//                    Long c = result.get(k);
//                    if (c != null) {
//                        result.put(k, c + v);
//                    } else {
//                        result.put(k, v);
//                    }
//                }
//            });
//            return result;
//        }
//
//        private Map<String, Long> calc(String line) {
//
//            Map<String, Long> result = new HashMap<>();
//            //分割单词
//            String[] words = line.split("\\s");
//
//            for (String w : words) {
//                Long v = result.get(w);
//                if (v != null) {
//                    result.put(w, v + 1);
//                } else {
//                    result.put(w, 1l);
//                }
//
//            }
//            return result;
//        }
//
//
//    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    static class Fibonacci extends RecursiveTask<Integer> {


        final int n;

        Fibonacci(int n) {
            this.n = n;
        }

        @Override
        protected Integer compute() {
            if (n <= 1) {
                return n;
            }
            Fibonacci f1 = new Fibonacci(n - 1);
            //创建子任务
            f1.fork();
            Fibonacci f2 = new Fibonacci(n - 2);
            return f2.compute() + f1.join();
        }
    }


}
