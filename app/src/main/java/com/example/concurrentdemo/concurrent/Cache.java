package com.example.concurrentdemo.concurrent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Cache<K,V> {

    final Map<K,V> map = new HashMap<>();

    final ReadWriteLock rwl = new ReentrantReadWriteLock();

    //读锁
    final Lock r = rwl.readLock();

    //写锁
    final Lock w = rwl.writeLock();

    //读缓存
    V get(K kev){
        r.lock();
        try{
            return map.get(kev);
        }finally {
            r.unlock();
        }
    }

    //写缓存

    V put(K key,V value){
        w.lock();
        try{
            return map.put(key, value);
        }finally {
            w.unlock();
        }
    }


}
