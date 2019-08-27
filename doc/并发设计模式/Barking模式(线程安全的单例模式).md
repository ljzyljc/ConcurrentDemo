### Barking模式：再谈线程安全的单例模式

业务逻辑依赖于这个状态变量的状态：当状态满足某个条件时，执行某个逻辑，其本质其实不过就是一个if而已，放到多线程场景里，就是

一种"多线程版本的if"。这种多线程版本的if的应用场景还是很多的，所以也有人把它总结成一种设计模式，叫做**Barking模式**。


Barking模式的经典实现：

````boolean changed=false;
    // 自动存盘操作
    void autoSave(){
      synchronized(this){
        if (!changed) {
          return;
        }
        changed = false;
      }
      // 执行存盘操作
      // 省略且实现
      this.execSave();
    }
    // 编辑操作
    void edit(){
      // 省略编辑逻辑
      ......
      change();
    }
    // 改变状态
    void change(){
      synchronized(this){
        changed = true;
      }
    }


用volatile实现Barking模式：使用volatile的前提是对原子性没有要求。


Barking模式只需要互斥锁能解决，而Guarded Suspension模式则要用到管程这种高级的并发原语。但是从应用的角度看，它们解决的都是"线程安全的if"语义，

不同之处在于，Guarded Suspension模式会等待if条件为真，而Barking模式不会等待。


Barking模式的经典实现是使用互斥锁，你可以使用Java语言内置synchronized,也可以使用SDK提供Lock;如果你对互斥锁的性能不满意，可以尝试采用volatile方案，但要小心。


当然你也可以尝试使用双重检查方案来优化性能。
















