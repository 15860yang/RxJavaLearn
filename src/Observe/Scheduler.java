package Observe;

public interface Scheduler {
    void submit(Runnable runnable);
    void remove(Runnable runnable);
    void shutdown();
}
