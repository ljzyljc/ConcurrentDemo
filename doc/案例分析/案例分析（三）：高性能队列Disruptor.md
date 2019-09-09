### 高性能队列Disruptor


JavaSDK提供了两个有界队列：ArrayBlockingQueue和LinkedBlockingQueue,它们都是基于ReentrantLock实现的，在高并发场景下，锁的并发效率并不高，更好的替代品是Disruptor


**Disruptor是一款高性能的有界内存队列。** Log4j2、Spring Messaging、HBase、Storm都用到了Disruptor

1.内存分配更加合理，使用RingBuffer数据结构，数组元素在初始化时一次性全部创建，提升缓存命中率；对象循环利用，避免频繁GC

2.能够避免伪共享，提升缓存利用率

3.采用无锁算法，避免频繁加锁，解锁的性能消耗

4.支持批量消费，消费者可以无锁方式消费多个消息。


*在Disruptor中，生产者生产的对象（也就是消费者消费的对象）称为Event，使用Disruptor必须定义Event，例如实例代码的自定义Event是LongEvent;

*构建Disruptor对象除了要指定大小外，还需要传入一个EventFactory,示例代码中传入的是LongEnvent::new;

*消费Disruptor中的Event需要通过handleEventsWith()方法注册一个事件处理器，发布Event则需要通过publishEvent()



```// 自定义 Event
   class LongEvent {
     private long value;
     public void set(long value) {
       this.value = value;
     }
   }
   // 指定 RingBuffer 大小,
   // 必须是 2 的 N 次方
   int bufferSize = 1024;
   
   // 构建 Disruptor
   Disruptor<LongEvent> disruptor 
     = new Disruptor<>(
       LongEvent::new,
       bufferSize,
       DaemonThreadFactory.INSTANCE);
   
   // 注册事件处理器
   disruptor.handleEventsWith(
     (event, sequence, endOfBatch) ->
       System.out.println("E: "+event));
   
   // 启动 Disruptor
   disruptor.start();
   
   // 获取 RingBuffer
   RingBuffer<LongEvent> ringBuffer 
     = disruptor.getRingBuffer();
   // 生产 Event
   ByteBuffer bb = ByteBuffer.allocate(8);
   for (long l = 0; true; l++){
     bb.putLong(0, l);
     // 生产者生产消息
     ringBuffer.publishEvent(
       (event, sequence, buffer) -> 
         event.set(buffer.getLong(0)), bb);
     Thread.sleep(1000);
   }
```

RingBuffer本质上也是数组，首先先了解一下程序的局部性原理。**程序的局部性原理指的是在一段时间内程序的执行会限定在一个局部范围内。**

这里的局部性可以从两个方面来理解，一个是时间局部性，一个是空间局部性。**时间局部性**指的是程序中的某条指令一旦被执行，不久之后这条指令

很可能再次被执行；如果某条数据被访问，不久之后这条数据很可能再次被访问。而空间的局部是指某块内存一旦被访问，不久之后这块内存附近的内存也很可能被访问。

CPU的缓存就利用了程序的局部性原理：CPU从内存中加载数据X时，会将数据X缓存在高速缓存Cache中，实际上CPU缓存X的同时，还缓存了X周围的数据，因为根据

程序具备局部性原理，X周围的数据也很有可能被访问。从另外一个角度来看，如果程序能够很高地体现出局部性原理，也就能更好的利用CPU的缓存，从而提升程序的性能。


Disruptor的设计详见极客时间


ArrayBlockQueue内部有6个元素，这个6个元素都是由生产者线程创建的，由于创建这些元素的时间基本上是离散的，**所以这些元素的内存地址大概率也不是连续的。**

Disruptor内部的RingBuffer也是用数组实现的，但是这个数组中的所有元素在初始化时是一次性全部创建的，所以这些元素的内存地址大概率是连续的。

除此之外，在Disruptor中，生产者线程通过publicEvent()发布Event的时候，并不是创建一个新的Event,而是通过event.set()方法修改Event,也就是说RingBuffer创建

的Event是可以循环利用的，这样还能避免频繁创建，删除Event导致的频繁GC问题。


**如何避免"伪共享"

有一种共享叫做"伪共享（False sharing）"的内存布局就会使Cache失效，那什么是伪共享呢？

伪共享和CPU内部的Cache有关，Cache内部是按照缓存行（Cache Line）管理的，缓存行的大小通常是64个字节；CPU从内存中加载数据X,会同时加载X后面（64-size(X)）个

字节的数据。下面的实例代码出自Java SDK的ArrayBlockingQueue,其内部维护了4个成员变量，分别是队列数组items，出队索引takeIndex,入队索引putIndex以及队列中

的元素总数count.

```
    /** 队列数组 */
   final Object[] items;
   /** 出队索引 */
   int takeIndex;
   /** 入队索引 */
   int putIndex;
   /** 队列中元素总数 */
   int count;
   
```

详见极客时间内容

**简单来讲，伪共享指的是由于共享缓存行导致缓存无效的场景。**


ArrayBlockQueue的入队和出队操作是用锁来保证互斥的，所以入队和出队不会同时发生。

Disruptor的方案，**每个变量独占一个缓存行，不共享缓存行就可以了，具体技术是缓存行填充。**


**Distruptor中的无锁算法**

详见极客时间内容



总结：
    
    Disruptor在优化并发性能方面可谓做到了极致，优化的思路大体是两个方面，一个是利用无锁算法避免锁的争用，另外一个则是将硬件（CPU）的性能发挥到极致。
    

**避免伪共享的注解**

    java8中，@sun.misc.Contended(需要设置JVM参数-XX:-RestrictContened)
































