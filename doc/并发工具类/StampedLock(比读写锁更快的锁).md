### StampedLock:比读写锁更快的锁

在Java1.8这个版本里面，提供了了StampedLock的锁，它的性能就比读写锁还要好。

**StampedLock支持的三种锁模式**

    ReadWriteLock支持两种模式：一种是读锁，一种是写锁。
    
    而StampedLock支持三种模式，分别是：写锁，悲观读锁和乐观读锁。其中写锁，悲观读锁的语义和
    ReadWriteLock的写锁，读锁非常类似，允许多个线程同时获取悲观读锁，但是只允许一个线程获取写锁，
    写锁和悲观读锁是互斥的。
    
    不同的是：StampedLock里的写锁和悲观读锁加锁成功之后，都会返回一个stamp;然后解锁的时候，需要传入这个stamp