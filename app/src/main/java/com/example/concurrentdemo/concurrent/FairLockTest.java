package com.example.concurrentdemo.concurrent;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FairLockTest {

    public static void main(String[] args){

    }


    private Lock lock = new ReentrantLock();  //无参构造器，默认非公平锁

    private Lock lockFair = new ReentrantLock(true); //创建公平锁


}
