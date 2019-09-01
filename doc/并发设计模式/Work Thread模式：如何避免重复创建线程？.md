### Work Thread模式：如何避免重复创建线程？

其实就是线程池

线程池的使用过程中，还要注意一种**线程死锁**的场景。如果提交到线程池的任务不是相互独立的，而是有依赖关系的，那么就有可能导致线程死锁。

可能的现象是**应用每运行一段时间偶尔就会处于无响应的状态，监控数据看上去一切都正常，但是实际上已经不能正常工作了。**


示例：
    ```//L1、L2 阶段共用的线程池
       ExecutorService es = Executors.
         newFixedThreadPool(2);
       //L1 阶段的闭锁    
       CountDownLatch l1=new CountDownLatch(2);
       for (int i=0; i<2; i++){
         System.out.println("L1");
         // 执行 L1 阶段任务
         es.execute(()->{
           //L2 阶段的闭锁 
           CountDownLatch l2=new CountDownLatch(2);
           // 执行 L2 阶段子任务
           for (int j=0; j<2; j++){
             es.execute(()->{
               System.out.println("L2");
               l2.countDown();
             });
           }
           // 等待 L2 阶段任务执行完
           l2.await();
           l1.countDown();
         });
       }
       // 等着 L1 阶段任务执行完
       l1.await();
       System.out.println("end");

如上所示，线程池中的两个线程全部都阻塞在l2.await();这行代码上了。

**这种问题通用的解决方案是为不同的任务创建不同的线程池。**比较简单粗暴的方式是直接将线程池的最大线程数调大。

**最后一次强调，提交到线程池中的任务一定是相互独立的，否则就一定要慎重。












