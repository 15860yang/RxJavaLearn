package Observe;

import java.util.LinkedList;
import java.util.Queue;

public final class ObservableObserveOn<T> extends Observable<T> {

    private final ObservableSource<T> source;
    private final Scheduler schedule;

    ObservableObserveOn(ObservableSource<T> source, Scheduler schedule) {
        this.source = source;
        this.schedule = schedule;
    }

    @Override
    void subscribeActual(Observer<? super T> observer) {
        source.subscribe(new ObserveOnObserver<>(observer, schedule));
    }


    static final class ObserveOnObserver<T> implements Observer<T>, Disposable, Runnable {

        private final Observer<? super T> downstream;
        private final Scheduler scheduler;
        private Disposable upstream;
        private volatile boolean done;
        private volatile boolean disposed;
        private Queue<T> queue = new LinkedList<>();
        private Throwable error;

        public ObserveOnObserver(Observer<? super T> downstream, Scheduler scheduler) {
            this.downstream = downstream;
            this.scheduler = scheduler;
        }

        @Override
        public void dispose() {
            if (!disposed) {
                disposed = true;
                upstream.dispose();
                scheduler.remove(this);
                queue.clear();
            }
        }

        @Override
        public boolean isDisposed() {
            return disposed;
        }

        @Override
        public void onSubscribe(Disposable d) {
            upstream = d;
            downstream.onSubscribe(this);
        }

        @Override
        public void onNext(T t) {
            if(done)return;
            queue.offer(t);
            schedule();
        }

        // 提交任务
        void schedule() {
            scheduler.submit(this);
        }

        @Override
        public void onError(Throwable t) {
            if (done) return;
            done = true;
            error = t;
            schedule();
        }

        @Override
        public void onComplete() {
            if (done) return;
            done = true;
            schedule();
        }

        // 检查事件是否已中断,并作出相应的反馈
        boolean checkTerminated(boolean d, boolean empty, Observer<? super T> a) {
            if (disposed) {
                queue.clear();
                return true;
            }
            if (d) {
                Throwable e = error;
                if (e != null) {
                    disposed = true;
                    queue.clear();
                    a.onError(e);
                    scheduler.remove(this);
                    return true;
                } else if (empty) {
                    disposed = true;
                    a.onComplete();
                    scheduler.remove(this);
                    return true;
                }
            }
            return false;
        }

        @Override
        public void run() {
            final Queue<T>            q = queue;
            final Observer<? super T> a = downstream;
            for (; ; ) {
                if (checkTerminated(done, q.isEmpty(), a)) return;
                for (; ; ) {
                    boolean d = done;
                    T       v;
                    try {
                        v = q.poll();
                    } catch (Throwable ex) {
                        disposed = true;
                        upstream.dispose();
                        q.clear();
                        a.onError(ex);
                        scheduler.remove(this);
                        return;
                    }
                    boolean empty = v == null;
                    if (checkTerminated(d, empty, a)) return;
                    if (empty) break;
                    a.onNext(v);
                }
            }
        }
    }
}
