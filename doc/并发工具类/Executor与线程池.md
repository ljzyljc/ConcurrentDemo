### Executor与线程池：如何创建正确的线程池？

一般意义上的池化资源申请和释放资源的固有模型

    class XXXPool{
      // 获取池化资源
      XXX acquire() {
      }
      // 释放池化资源
      void release(XXX x){
      }
    }  

**线程池是一种生产者 - 消费者模式**

    为什么线程池没有采用一般意义上的池化资源的设计方法呢？
    
        作者的解释是按照一般意义上的池化资源的设计方式，但是Thread里面没有提供对于的类似execute(Runnable target)这样的公共方法，
        
        所以没有办法直接采用一般意义上的池化资源的设计方法，但是我觉得这不是真正意义上的原因。归根结底还是线程池这种需求实际上是一种生产者消费者的需求

    线程池的使用方是生产者，线程池本身是一种消费者。
    
    
 ThreadPoolExecutor的构造器中RejectedExecutionHandler handler 参数的说明：
    
    通过这个参数可以自定义任务的拒绝策略。如果所有线程池中的所有线程都在忙碌，并且工作队列也满了（前提是工作队列时有界队列），
    
    那么此时提交任务，线程池就会拒绝接收，至于拒绝的策略，通过该参数来指定
    
    1.CallerRunsPolicy:提交任务的线程自己去执行任务。
    
    2.AbortPolicy:默认的拒绝策略，会throws RejectedExecutionException
    
    3.DiscardPolicy:直接丢弃任务，没有任何异常抛出
    
    4.DiscardOldestPolicy:丢弃最老的任务，其实就是把最早进入工作队列的任务丢弃，然后把新任务加入工作队列。
    
    
注意点：线程池中的线程在任务执行的过程中出现运行时异常，会导致执行任务的线程终止，不过最致命的是任务虽然异常了，但是你却获取不到任何通知，
    
这会让你误以为任务执行得很正常，最稳妥和简单的处理方案还是捕获所有的异常并按需处理


    try {
      // 业务逻辑
    } catch (RuntimeException x) {
      // 按需处理
    } catch (Throwable x) {
      // 按需处理
    } 

    
    
    
    
    
    
    
    