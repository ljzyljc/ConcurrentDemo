### Fork/Join 单机版的MapReduce

Fork/Join的使用：
    
    Fork/Join是一个并行计算的框架，主要就是用来支持分治任务模型的，这个计算框架里的**Fork对应的是分治任务模型里的任务分解，Join对应的是结果合并。
    
    Fork/Join计算框架主要包含两部分，一部分是分治任务的线程池ForkJoinPool,另一部分是任务ForkJoinTask。
    
    这两部分的关系类似于ThreadPoolExecutor和Runnable的关系，都可以理解为提交任务到线程池，只不过分治任务有自己独特类型ForkJoinTask。


    ForkJoinTask是一个抽象类，fork()方法会异步的执行一个子任务，而join()方法会阻塞当前线程来等待子任务的执行结果。
    
    ForkJoinTask有两个子类（都是抽象类）:   RecursiveAction和RecursiveTask,都是用递归的方式来处理分治任务的。
    
    RecursiveAction定义的compute()没有返回值，RecursiveTask定义的compute()有返回值。compute()是抽象类的抽象方法，需要自己实现。
    

ForkJoin的工作原理

    ForkJoinPool本质上也是一个生产者-消费者的实现，**内部有多个任务队列**，当我们通过ForkJolinPool的invoke()或者submit()
    
    方法提交任务时，ForkJoinPool根据一定的路由规则把任务提交到一个任务队列中，如果任务在执行过程中会创建子任务，那么子任务
    
    会提交到工作线程对应的任务队列中。
    
    ForkJoinPool支持一种叫做**任务窃取**的机制。可以窃取其他线程中的任务。
    
    ForkJoinPool中的任务队列采用的是**双端队列**，工作线程正常获取任务和"窃取任务"分别是从任务队列的不同的端消费。这样能避免很多不必要的数据竞争。