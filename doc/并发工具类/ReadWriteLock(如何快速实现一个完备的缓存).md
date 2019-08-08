### ReadWriteLock(如何快速实现一个完备的缓存)

**读写锁**

    所有的读写锁都遵循以下三个基本原则：
    
       1.运行多个线程同时读共享变量
       2.只允许一个线程写共享变量
       3.如果一个线程正在执行写操作，此时禁止线程读共享变量
       
ReentrantReadWriteLock锁的升级是不允许的，降级是可以的


读写锁类似于ReadWriteLock,也支持公平模式和非公平模式，但是需要注意的是，那就是只有写锁支持条件变量，

读锁是不要条件变量的，读锁调用newCondition()会抛出UnsupportedOperationException异常。