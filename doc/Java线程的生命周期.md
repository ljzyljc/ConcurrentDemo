###Java线程的生命周期

------

1.通用的线程生命周期
    
    初始状态：线程已经被创建，但是还不允许分配CPU执行。编程语言特有的，仅仅是在编程语言层面被创建，而在操作系统层面，真正的线程还没有被创建。
    
    可运行状态
    
    运行状态
    
    休眠状态：运行状态的线程如果调用一个阻塞的API（例如以阻塞方式读文件）或者等待某个事件（例如条件变量），那么线程的状态就会转到休眠状态，
    同时释放CPU使用权。
    
    终止状态
    
2.Java线程中线程共有6种状态

    1.NEW(初始化状态)
    
    2.RUNNABLE(可运行/运行状态)
    
    3.BLOCKED(阻塞状态)
    
    4.WAITING(无限时等待)
    
    5.TIMED_WAITING(有限时等待)
    
    6.TERMINATED(终止状态)
    
3.线程的状态转换
    
    1.RUNNABLE与BLOCKED的状态转换：只有一种场景会触发这种转换，就是线程等待synchroinzed的隐式锁。线程调用阻塞式API时，
    在操作系统层面，线程是会转换到休眠状态的；但是在JVM层面，Java线程的状态是不会发生变化。**JVM层面并不关心操作系统调度相关的状态**，
    因为在JVM看来，等待CPU使用权（操作系统层面此时处于可执行状态）与等待I/O(操作系统层面此时处于休眠状态)没有区别，都是等待某个资源，所以都归入RUNNABLE状态。
    
    2.RUNNABLE与WAITING的状态转换
        Object.wait()
        Thread.join()
        LockSupport.park()
    
    3.RUNNABLE与TIMED_WAITING的状态转换：
        Thread.sleep(long mills)
        Object.wait(long timeout)
        Thread.join(long mills)
        LockSupport.parkNanos(Object blocker,long deadline)
        LockSupport.parkUntil(long deadline)
    
    4.从NEW到RUNNABLE状态
    
    5.RUNNABLE到TERMINATED状态
    
4.stop()和interrupt()方法的主要区别
    
    stop()方法会真的杀死线程，不给线程喘息的机会，有可能导致锁无法释放（synchroinzed隐式锁会被释放）。suspend()和resume（）方法也不建议使用
    
    interrupt()方法仅仅是通知线程，线程有机会执行一些后续操作，同时也可以无视这个通知。被interrupt的线程，
    有两种方式受到通知：异常和主动监测。
    
    异常：
    1.线程A处于WAITING，TIMED_WAITING状态是，如果其他线程调用线程A的interrupt()方法，会使线程A返回到RUNNABLE状态，
    同时线程A的代码会触发InterruptedException异常。该异常的触发条件就是其他线程调用了该线程的interrupt（）方法。
    
    2.当线程A处于RUNNABLE状态时，并且阻塞在java.nio.chnenls.InterruptibleChannel上时，如果其他线程调用了线程A的interrupt()方法，
    线程A会触发java.nio.channels.ClosedByInterruptException 这个异常；而阻塞在java.nio.channels.Selector 上时，如果其他线程调用
    线程A的interrupt()方法，线程A的java.nio.channels.Selector 会立即返回。
    
    主动监测：isInterrupted()方法
    
    
    
    
    
    
    
    
    