package com.example.concurrentdemo.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 如何批量执行异步任务
 */
public class CompletionServiceTest {

    public static void main(String[] args) {

        test2();

    }


    //利用CompletionService实现Dubbo中的Forking Cluster

    //Dubbo中有一种叫做Forking的集群模式，这种集群模式下，支持并行的调用多个查询服务，只要有一个成功返回，整个服务就可以返回了

    public static int test2(){

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        CompletionService<Integer> completionService = new ExecutorCompletionService<>(executorService);

        //用于保存Future对象
        List<Future<Integer>> futures = new ArrayList<>();

        futures.add(completionService.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return 1;
            }
        }));

        futures.add(completionService.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return 2;
            }
        }));

        futures.add(completionService.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return 3;
            }
        }));
        //获取最快返回的任务执行结果
        Integer result = 0;
        try {
            for (int i = 0; i < 3; i++) {
                try {
                    result = completionService.take().get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (result != null) {
                    break;
                }
            }
        }finally {
            for (Future<Integer> f: futures) {
                f.cancel(true);
            }
        }
        System.out.println("================="+result);
        return result;



    }





    //take()和poll()方法的区别

    //CompletionService实现询价系统，CompletionService的实现原理是内部维护了一个阻塞队列，
    // 当任务执行结束就把任务的执行结果加入到阻塞队列中，不同的是CompletionService是把任
    // 务执行结果的Futrue对象加入到阻塞队列中，而上面的实例代码就是把任务最终的执行结果放入了阻塞队列中

    //实现高性能的询价系统

    public static void test1() {

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        CompletionService<Integer> completionService = new ExecutorCompletionService<>(executorService);

        completionService.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return 1;
            }
        });

        completionService.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return 2;
            }
        });

        completionService.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return 3;
            }
        });

        for (int i = 0; i < 3; i++) {
            try {
                Integer integer = completionService.take().get();
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("======"+integer);
                    }
                });

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }


    public static void test() {

        Executor executor = Executors.newFixedThreadPool(6);

        BlockingQueue<Integer> bq = new LinkedBlockingQueue<>();

        executor.execute(new Runnable() {
            @Override
            public void run() {
//                bq.put(f1.get());
            }
        });

        executor.execute(new Runnable() {
            @Override
            public void run() {
//                bq.put(f2.get());
            }
        });

        executor.execute(new Runnable() {
            @Override
            public void run() {
//                bq.put(f3.get());
            }
        });

        //异步保存所有报价
        for (int i = 0; i < 3; i++) {
            try {
                Integer integer = bq.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("======保存数据");
                }
            });
        }


    }


}
