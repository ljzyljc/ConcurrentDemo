### StampedLock:比读写锁更快的锁

在Java1.8这个版本里面，提供了了StampedLock的锁，它的性能就比读写锁还要好。

**StampedLock支持的三种锁模式**

    ReadWriteLock支持两种模式：一种是读锁，一种是写锁。
    
    而StampedLock支持三种模式，分别是：写锁，悲观读锁和乐观读锁。其中写锁，悲观读锁的语义和
    ReadWriteLock的写锁，读锁非常类似，允许多个线程同时获取悲观读锁，但是只允许一个线程获取写锁，
    写锁和悲观读锁是互斥的。
    
    不同的是：StampedLock里的写锁和悲观读锁加锁成功之后，都会返回一个stamp;然后解锁的时候，需要传入这个stamp
    
```final StampedLock sl = new StampedLock();
           
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
           
StampedLock的性能之所以比ReadLock还要好，**其关键是Stamp支持乐观读的方式,乐观读这个操作是无锁的**。  

ReadWriteLock支持多个线程同时读，但是当多个线程同时读的时候，写锁就会被阻塞;

而StampedLock提供的乐观读，是允许一个线程获取写锁的，也就是说不是所有的线程都被阻塞。

```    private static int x,y;
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

上面代码如果执行乐观读操作的期间存在写操作，会把乐观读升级为悲观读锁。否则就需要在一个循环里面反复乐观读，
直到执行乐观读操作的期间没有写操作（只有这个才能保证X和Y的正确性和一致性），但是循环会浪费大量的CPU。


**进一步理解乐观读**

数据库的乐观锁(利用version字段做验证)和StampedLock的乐观锁有异曲同工之妙。


StampedLock使用的注意事项：
    1.**StampedLock的功能仅仅是ReadWriteLock的子集**
    
    2.StampedLock不支持重入
    
    3.StampedLock的悲观读锁，写锁都不支持条件变量
    
    4.使用StampedLock一定不需要调用中断操作，如果需要支持中断功能，一定使用可中断的悲观读锁readLockInterruptily()和写锁writeLockInteruptibly()













    