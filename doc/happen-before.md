
### Happen-Before 规则

1.程序的顺序性规则
2.volatile 变量规则：对一个volatile 变量的写操作，happen-before于后续对这个volatile变量的读操作。
    volatile变量的读写，不能使用CPU缓存，必须从内存中读取或者写入;
    禁止指令重排序。
3.传递性
4.synchronized  sychronized是Java中对管程的实现。管程中的锁在Java里是隐式实现的，monitor
5.线程start()规则， start（）操作 happen-before于 线程B内的任意操作
6.线程join规则，主线程A等待子线程B完成（主线程A通过调用子线程B的join（）方法实现，
    当子线程B完成后（主线程A中join（）方法返回），主线程能够看到子线程的操作（共享变量）。
7.线程中断规则，对线程interrupt()方法的调用先行发生于被中断线程的代码检测到中断事件的发生，
    可以通过Thread.interrupted()方法检测到是否有中断发生。
8.对象终结规则，一个对象的初始化完成（构造函数执行结束）先行发生于它的finalize（）方法的开始。

问题：有一个共享变量abc,在一个线程A里设置了abc=3,有哪些方法可以让其它线程看到abc==3?
    1.volatile修饰
    2.synchronized
    3.A启动后，使用A.join()方法来完成运行，后续线程再启动
    4.A线程设置abc=3后，对一个volatile修饰的变量m进行赋值操作，然后其它线程读取abc之前读取m的值，
        通过volatile的可见性和happen-before传递性规则