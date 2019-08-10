package com.example.concurrentdemo.concurrent;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.concurrent.locks.StampedLock;

/**
 * 比读写锁更快的锁（StampedLock）
 */
public class StampedLockTest {

    public static void main(){




    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void test(){

        final StampedLock sl = new StampedLock();

        //获取/释放悲观读锁示意代码
        long stamp = sl.readLock();
        try{
            //省略相关业务代码
        }finally {
            sl.unlock(stamp);
        }

        //获取/释放写锁示意代码
        long stampd = sl.writeLock();
        try{
            //省略相关业务代码
        }finally {
            sl.unlock(stampd);
        }

    }




}
