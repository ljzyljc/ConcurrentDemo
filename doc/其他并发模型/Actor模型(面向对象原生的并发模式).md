### Actor模型：面向对象原生的并发模型


Actor模型本质上是一种计算模型，基本的计算单元称为Actor，换句话来说，**在Actor模型中，所有的计算都是在Actor中执行的。**

在面向对象编程里面，一切都是对象；在Actor模型里，一切都是Actor,并且Actor之间是完全隔离的，不会共享任何变量。

Akka类库

```
    // 该 Actor 当收到消息 message 后，
    // 会打印 Hello message
    static class HelloActor 
        extends UntypedActor {
      @Override
      public void onReceive(Object message) {
        System.out.println("Hello " + message);
      }
    }
    
    public static void main(String[] args) {
      // 创建 Actor 系统
      ActorSystem system = ActorSystem.create("HelloSystem");
      // 创建 HelloActor
      ActorRef helloActor = 
        system.actorOf(Props.create(HelloActor.class));
      // 发送消息给 HelloActor
      helloActor.tell("Actor", ActorRef.noSender());
    }

```


Actor模型是一种非常简单的计算模型，其中Actor是最基本的计算单元，Actor之间是通过消息进行通信。


https://blog.csdn.net/wang_wbq/article/details/78845804











