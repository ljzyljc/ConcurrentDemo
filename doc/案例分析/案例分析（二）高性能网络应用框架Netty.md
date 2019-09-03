### 高性能网络应用框架Netty

利用非租塞式API就能够实现一个线程处理多个连接了。现在普遍都是采用Reactor模式，包括Netty的实现


Synchronous Event Demultiplexer可以理解为操作系统提供的I/O多路复用API，例如POSIX标准里的select()以及Linux里面的epoll()


Netty中的线程模型

Netty中最核心的概念是事件循环，其实就是Reactor模式中的Reactor,负责监听网络事件调用并调用事件处理器进行处理。