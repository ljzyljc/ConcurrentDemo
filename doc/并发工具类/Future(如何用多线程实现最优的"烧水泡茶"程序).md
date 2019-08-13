### Future 如何用多线程实现最优的"烧水泡茶"程序？

1.Java通过ThreadPoolExecutor的三个submit()方法和一个FutureTask工具类来获得任务执行结果的需求。
    
下面是三个submit方法：

    // 提交 Runnable 任务
    Future<?> 
      submit(Runnable task);
    // 提交 Callable 任务
    <T> Future<T> 
      submit(Callable<T> task);
    // 提交 Runnable 任务及结果引用  
    <T> Future<T> 
      submit(Runnable task, T result);
    
    第三个方法实例：
    
        public static void test(){
    
            ExecutorService executor = Executors.newFixedThreadPool(1);
            //创建Result对象
            Result result = new Result();
            result.setMsg("林世妹");
            //提交任务
            Future<Result> future = executor.submit(new Task(result),result);
    
            try {
                result = future.get();
                System.out.println("========"+result.getMsg());
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    
        }
    
        private static class Task implements Runnable{
    
            Result result;
            public Task(Result result){
                this.result = result;
            }
            @Override
            public void run() {
                //操作result
                name = "Jackie"+result.getMsg();
                result.setMsg(name);
            }
        }
    

Future接口的五个方法： 

    其中两个get()方法都是阻塞式的，调用的线程会被阻塞

    // 取消任务
    boolean cancel(
      boolean mayInterruptIfRunning);
    // 判断任务是否已取消  
    boolean isCancelled();
    // 判断任务是否已结束
    boolean isDone();
    // 获得任务执行结果
    get();
    // 获得任务执行结果，支持超时
    get(long timeout, TimeUnit unit);
   
FutureTask实现了Runnable接口和Future接口，所以可以直接被ThreadPoolExecutor执行，可以可以直接被Thread执行

    ThreadPoolExecutor方式：
    
        public static void test2(){
   
            ExecutorService executor = Executors.newFixedThreadPool(1);
    
            FutureTask<Integer> futureTask = new FutureTask<>(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    return 1+2;
                }
            });
    
            executor.submit(futureTask);
    
            try {
                Integer integer = futureTask.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    
        }
        
    Thread方式：
        
        public static void test3(){
        
                FutureTask<Integer> futureTask = new FutureTask<>(new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        return 100+23;
                    }
                });
                Thread thread = new Thread(futureTask);
                thread.start();
        
                try {
                    Integer integer = futureTask.get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        
            }
            
烧水泡茶程序

    用了两个FutureTask来时实现，一个里面Future的Callable里面传入另一个FutrueTask,ft2.get(),最后ft1.get()