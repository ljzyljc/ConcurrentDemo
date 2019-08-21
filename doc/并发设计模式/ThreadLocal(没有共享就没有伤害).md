### ThreadLocal:没有共享就没有伤害


源码：

````class Thread {
      // 内部持有 ThreadLocalMap
      ThreadLocal.ThreadLocalMap 
        threadLocals;
    }
    class ThreadLocal<T>{
      public T get() {
        // 首先获取线程持有的
        //ThreadLocalMap
        ThreadLocalMap map =
          Thread.currentThread()
            .threadLocals;
        // 在 ThreadLocalMap 中
        // 查找变量
        Entry e = 
          map.getEntry(this);
        return e.value;  
      }
      static class ThreadLocalMap{
      
      static class Entry extends WeakReference<ThreadLocal<?>> {
                  /** The value associated with this ThreadLocal. */
                  Object value;
      
                  Entry(ThreadLocal<?> k, Object v) {
                      super(k);
                      value = v;
                  }
              }
        // 内部是数组而不是 Map
        Entry[] table;
        // 根据 ThreadLocal 查找 Entry
        Entry getEntry(ThreadLocal key){
          // 省略查找逻辑
        }
        //Entry 定义
        static class Entry extends
        WeakReference<ThreadLocal>{
          Object value;
        }
      }
    }



之前所以这样设置的原因是为了避免内存泄漏，但是在线程池中调用可能还是会导致内存泄漏的，所以需要调用ThreadLocal.remove(),因为虽然key是弱引用，但是value是强引用，

    而且在线程池中的线程是一直存在的，所以可能导致value一直无法回收。

        try{
    
        }finally{
        
        }


InheritableThreadLocal 与继承性

    该类是ThreadLocal的子类（不建议使用）


ThreadLocal  可以用来在高并发场景下替换局部变量（如果每个新建的线程都不同的话），这样可以避免局部变量的频繁创建


上面有些同学说多线程是simpledateformat会打印出一样名称的对象，我刚刚也试了下，的确可以复现，但其实是simpledateformat对象的toString()方法搞得鬼，

该类是继承object类的tostring方法，如下有个hashcode()方法，但该类重写了hashcode方法，在追溯到hashcode方法，pattern.hashcode(),

pattern就是我们的yyyy-MM-dd,这个是一直保持不变的，现在终于真相大白了















