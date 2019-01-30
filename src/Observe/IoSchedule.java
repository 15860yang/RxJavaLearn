package Observe;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class IoSchedule implements Scheduler {

    private final ExecutorService executor = Executors.newCachedThreadPool();

    private final Map<Runnable, Future> futureMap = new ConcurrentHashMap<>();

    @Override
    public void submit(Runnable runnable) {
        Future future = futureMap.get(runnable);
        if(future != null && !future.isDone()) return;
        if(executor.isShutdown()) return;
        futureMap.put(runnable,executor.submit(runnable));
    }

    @Override
    public void remove(Runnable runnable) {
        Future future = futureMap.get(runnable);
        if(future == null)return;
        try{
            future.cancel(true);
        }catch (Exception e){

        }finally {
            futureMap.remove(future);
        }
    }

    @Override
    public void shutdown() {
        if(!executor.isShutdown()){
            executor.shutdown();
        }
    }
}
