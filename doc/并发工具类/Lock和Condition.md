### Lock和Condition

1.死锁问题中破坏不可抢占条件，sychroinzed没有办法解决。

三种解决方案：

    1.能够响应中断    void loakInterruptibly() throws InterruptedException;
    2.支持超时        boolean tryLock(long time,TimeUnit unit) throws Interruception
    3.非阻塞地获取锁   boolean tryLock();

2.Lock是如何保证可见性的？ 利用了volatile相关的Happen-before规则，ReentrantLock内部持有一个volatile的成员变量stale,
    获取锁的时候会读写state的值，解锁的时候也会读写state的值


3.什么是可重入锁，指的是线程可以重复获取同一把锁。

  什么是可重入函数，指的是多线程可以同时调用该函数。 可重入函数式线程安全的

4.公平锁与非公平锁

    非公平锁的场景应该是线程释放锁之后，如果来了一个线程获取锁，它不必去排队可以直接获取到，不会入队，获取不到才入队。


5.用锁的最佳实践

    1.永远只在更新对象的成员变量时加锁

    2.永远只在访问可变的成员变量时加锁

    3.永远不再调用其他对象的方法是加锁