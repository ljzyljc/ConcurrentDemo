### CompletableFuture 异步编程

创建CompletableFuture对象主要靠下面代码中展示的这4个静态方法

    Runnable接口的run()方法没有返回值，而Supplier接口的get()方法是有返回值的
    
    前面两个方法和后面两个方法的区别在于：后面两个方法可以知道线程池参数。
    
    默认情况下CompletableFuture会使用公共的ForkJoinPool线程池，这个线程池默认创建的线程数是CPU的核数，
    
    如果所有的CompletableFuture共享一个线程池，如果有任务执行一些很慢的I/O操作，就会导致线程池中所有线程都阻塞在I/O操作，
    
    从而造成线程饥饿，进而影响整个系统的性能。**所以需要根据不同的业务类型创建不同的线程池，以避免互相干扰。**
    

    // 使用默认线程池
    static CompletableFuture<Void> 
      runAsync(Runnable runnable)
    static <U> CompletableFuture<U> 
      supplyAsync(Supplier<U> supplier)
    // 可以指定线程池  
    static CompletableFuture<Void> 
      runAsync(Runnable runnable, Executor executor)
    static <U> CompletableFuture<U> 
      supplyAsync(Supplier<U> supplier, Executor executor)  


CompletableFuture类还实现了CompletionStage接口

串行关系    thenApply,thenAccept,thenRun


汇聚关系（AND 和 OR）

    汇聚关系（AND）   thenCombine,thenAcceptBoth,runAfterBoth
    
    汇聚关系（OR）    applyToEither,acceptEither,runAfterEither
    

异常处理：

    CompletionStage exceptionally(fn);
    CompletionStage<R> whenComplete(consumer);
    CompletionStage<R> whenCompleteAsync(consumer);
    CompletionStage<R> handle(fn);
    CompletionStage<R> handleAsync(fn);

















    
    