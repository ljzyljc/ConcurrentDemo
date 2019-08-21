### Guarded Suspension模式  等待唤醒机制的规范实现

所谓的Guarded Suspension,直译过来就是保护性的暂停

GuardedObject内部的实现很简单，是管程的一个经典用法，核心是：get()方法通过条件变量的awati()方法实现等待，onChanged（）方法通过条件

变量的singalAll()实现唤醒功能。

```class GuardedObject<T>{
     // 受保护的对象
     T obj;
     final Lock lock = 
       new ReentrantLock();
     final Condition done =
       lock.newCondition();
     final int timeout=1;
     // 获取受保护对象  
     T get(Predicate<T> p) {
       lock.lock();
       try {
         //MESA 管程推荐写法
         while(!p.test(obj)){
           done.await(timeout, 
             TimeUnit.SECONDS);
         }
       }catch(InterruptedException e){
         throw new RuntimeException(e);
       }finally{
         lock.unlock();
       }
       // 返回非空的受保护对象
       return obj;
     }
     // 事件通知方法
     void onChanged(T obj) {
       lock.lock();
       try {
         this.obj = obj;
         done.signalAll();
       } finally {
         lock.unlock();
       }
     }
   }


扩展Guarded Suspension模式，扩展后内部维护一个Map,Map的key是MQ的id,而Value是GuardedObject对象实例，等等，具体细节见专栏。


总结：
    
    Guarded Suspension模式在解决实际问题的时候，往往还是需要扩展的，扩展的方式有很多。
    
    Guarded Suspension模式也常被称作Guarded Wait模式，Spin Lock模式（因为使用了while循环去等待），不过他还有一个更形象的官方名字：
    
    多线程版本的IF,在多线程场景中，等待就变得有意义了，if判断条件的结果是可能发生变化的。




















