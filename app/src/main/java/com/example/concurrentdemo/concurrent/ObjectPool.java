package com.example.concurrentdemo.concurrent;

import androidx.arch.core.util.Function;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.Semaphore;

/**
 * 使用Semaphore来实现一个限流器
 * @param <T>
 * @param <R>
 */

public class ObjectPool<T, R> {


    public static void main(String[] args){

        ObjectPool<Long,String> pool = new ObjectPool<Long, String>(10,2l);
        try {
            pool.exec(new Function<Long, String>() {
                @Override
                public String apply(Long input) {
                    System.out.println(input);
                    return input.toString();
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }



    final List<T> pool;
    //用信号量实现限流器
    final Semaphore sem;

    ObjectPool(int size, T t) {
        //需要使用线程安全的vector,因为信号量支持多个线程进入临界区，执行list的add和remove方法可能是多线程并发执行
        pool = new Vector<T>() {
        };
        for (int i = 0; i < size; i++) {
            pool.add(t);
        }
        sem = new Semaphore(size);
    }


    //利用对象池的对象，调用func
    R exec(Function<T, R> function) throws InterruptedException {
        T t = null;
        sem.acquire();
        try {
            t = pool.remove(0);
            return function.apply(t);
        } finally {
            pool.add(t);
            sem.release();
        }
    }


}
