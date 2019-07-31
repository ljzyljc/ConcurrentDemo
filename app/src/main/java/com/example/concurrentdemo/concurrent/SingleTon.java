package com.example.concurrentdemo.concurrent;

public class SingleTon {

    static SingleTon instance;

    static SingleTon getInstance(){
        if (instance == null){
            synchronized (SingleTon.class){
                if (instance == null){
                    instance = new SingleTon();
                }
            }
        }
        return instance;
    }
    /**
     * new 操作
     * 1.分配一块内存M
     * 2.在内存M上初始化Singleton对象
     * 3.然后M的地址赋值给instance变量
     *
     * 由于指令重排序，线程二在第一个判断是否为空时，3已经执行而2未执行，
     * instance不为空，直接返回instance,而此时instance是没有初始化过的，
     * 如果我们这个时候访问instance的成员变量就可能触发空指针异常
     *
     */

}
