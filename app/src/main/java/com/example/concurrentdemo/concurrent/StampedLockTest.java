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


    private static int x,y;
    @RequiresApi(api = Build.VERSION_CODES.N)
    //计算到原点的距离
    public static double distanceFromOrigin(){
        final StampedLock sl = new StampedLock();

        //乐观读
        long stamp = sl.tryOptimisticRead();
        //读入局部变量
        //读的过程数据可能被修改
        int currX = x;
        int currY = y;

        //判断执行读操作期间，是否存在些操作，如果存在，则sl.validate返回false
        if (!sl.validate(stamp)){
            //升级为悲观读锁
            stamp = sl.readLock();
            try{
                currX = x;
                currY = y;
            }finally {
                //释放悲观读锁
                sl.unlock(stamp);
            }
        }
        return Math.sqrt(currX*currX+currY*currY);
    }






}
