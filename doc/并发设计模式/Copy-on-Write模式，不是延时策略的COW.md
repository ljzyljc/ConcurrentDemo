### Copy-on-Write模式：不是延时策略的COW

1.Copy-on-Write模式的应用领域

    CopyOnWriteArrayList和CopyOnWriteArraySet这个两个Copy-on-Write容器，
    
    通过Copy-on-Write这两个容器实现的读操作是无锁的，将读操作的性能发挥到了极致。
    
    除了Java这个领域，Copy-on-Write在操作系统方面也有广泛的应用：
    
    
    类Unix的操作系统中创建进程的API是fork(),传统的fork()函数会创建父进程的一个完整副本，如果父进程的地址空间现在用到了一个G的内存，
    
    那么fork()子进程的时候要复制父进程整个进程的地址空间（占有1G内存）给子进程，这个过程是很耗时的。
    
    
    而Linux中的fork()函数就聪明的多的，fork()子进程的时候，并不复制整个进程的地址空间，而是让父子进程共享同一个地址空间；只有在
    
    父进程或者子进程需要写入的时候才会复制地址空间，从而使父子进程拥有各自的地址空间。（Copy-on-Write）
    
本质上来讲，父子进程的地址空间以及数据都是要隔离的，使用Copy-on-Write更多的体现是一种**延时策略，只有在真正需要复制的时候才复制，而不是提前复制好**。
  
同时Copy-on-Write还支持按需复制，所以Copy-on-Write在操作系统领域是能够提升性能的。在Java提供的Copy-on-Write容器，是以内存复制为代价的情况下提升读操作的。

这里你会发现，**同样是应用Copy-on-Write,不同的场合，对性能的影响是不同的**。

除了在操作系统领域，Docker容器镜像设计是Copy-on-Write,甚至分布式源码管理系统Git背后的设计思想都有Copy-on-Write

不过，**Copy-on-Write最大的应用领域还是在函数式编程领域**。函数式编程的基础是不可变（Immutability）,所以函数式编程里面所有的修改操作都需要Copy-on-Write来解决。

函数式编程之所以早年没有兴起，性能拖了后退，但是随着硬件性能的提升，性能问题变得慢慢可以接受了。而且Copy-on-Write也是可以按需复制的，

不是都像Java里面的CopyOnWriteArrayList那样笨：整个数组都复制一遍。

CopyOnWriteArrayList和CopyOnWriteArraySet适用于读多写少，数据量不大的情况下。


Copy-on-Write是最简单的并发解决方案，它是如此的简单，以至于Java中的基本数据类型String,Integer,Long都是基于Copy-on-Write实现的。

为什么没有CopyOnWriteLinkedList?

    违背读多写少的场景，复制也不好复制啊，而且链表增加删除也不需要复制啊，就算是并发场景采用锁的方式损耗也不大，而且还可以采用分段式，节点锁。
    
    没有提供CopyOnWriteLinkedList是因为linkedlist的数据结构关系分散到每一个节点里面，对每一个节点的修改都存在竟态条件，需要同步才能保证一致性。
    
    arraylist就不一样，数组天然的拥有前驱后继的结构关系，对列表的增删，因为是copy on wirte，所以只需要cas操作数组对象就能够保证线程安全，
    
    效率上也能接受，更重要的是避免锁竞争带来的上下文切换消耗。有一点需要注意的是CopyOnWriteArrayList在使用上有数据不完整的时间窗口，
    
    要不要考虑需要根据具体场景定夺