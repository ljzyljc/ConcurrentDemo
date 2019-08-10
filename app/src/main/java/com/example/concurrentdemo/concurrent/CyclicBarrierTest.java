package com.example.concurrentdemo.concurrent;

import java.util.Vector;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CyclicBarrierTest {

    static Executor executor = Executors.newFixedThreadPool(1);
    //订单队列
    static Vector<Order> orderVector;
    //派送单队列
    static Vector<PaiOrder> paiOrderVector;

    static int threadOneCount = 0;

    static int threadTwoCount = 0;

    static CyclicBarrier cyclicBarrier = new CyclicBarrier(2, new Runnable() {
        @Override
        public void run() {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    check();
                }
            });
        }
    });

    public static void main(String[] args){
        orderVector = new Vector<>();
        paiOrderVector = new Vector<>();
//        for (int i = 0;i<10;i++){
//            orderVector.add(new Order("订单第  "+i+"   条"));
//            paiOrderVector.add(new PaiOrder("派送订单第  "+i+"   条"));
//        }
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (threadOneCount < 10){
                    threadOneCount++;
                    orderVector.add(new Order("订单"+threadOneCount));
                    try {
                        cyclicBarrier.await();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        thread1.start();

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (threadTwoCount < 10){
                    threadTwoCount++;
                    paiOrderVector.add(new PaiOrder("派送订单"+threadTwoCount));
                    try {
                        cyclicBarrier.await();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread2.start();




    }


    private static void check(){

        Order order = orderVector.remove(0);
        PaiOrder paiOrder = paiOrderVector.remove(0);

        String diff = performCheck(order,paiOrder);

        saveDiff(diff);



    }

    private static String performCheck(Order order,PaiOrder paiOrder){
        String diff = order.orderMsg+"  diff    "+paiOrder.paiOrderMsg;
        return diff;
    }

    private static void saveDiff(String diff){
        System.out.println("差异写入数据库     "+diff);
    }





    private static class Order{

        String orderMsg;
        public Order(String orderMsg){
            this.orderMsg = orderMsg;
        }
    }

    private static class PaiOrder{
        String paiOrderMsg;
        public PaiOrder(String paiOrderMsg){
            this.paiOrderMsg = paiOrderMsg;
        }
    }



}
