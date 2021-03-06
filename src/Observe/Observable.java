package Observe;

public abstract class Observable<T> implements ObservableSource<T> {

    public static <T> Observable<T> create(ObservableOnSubscribe<T> source){
        return new ObservableCreate<>(source);
    }

    // 真正进行事件分发处理的方法
    abstract void subscribeActual(Observer<? super T> observer);

    @Override
    public void subscribe(Observer<? super T> observer) {
        subscribeActual(observer);
    }

    public <R> Observable<R> map(Function<? super T,? extends R> mapper){
        return new ObservableMap<>(this,mapper);
    }

    // 线程调度操作符
    public final Observable<T> observeOn(Scheduler scheduler) {
        return new ObservableObserveOn<>(this, scheduler);
    }

    // 接受4个lambda表达式的方法参数
    public Disposable subscribe(Consumer<? super T> next, Consumer<? super Throwable> error,
                                Action complete, Consumer<? super Disposable> onSubscribe) {
        LambdaObserver<T> lambdaObserver = new LambdaObserver<>(next, error, complete, onSubscribe);
        subscribe(lambdaObserver);
        return lambdaObserver;
    }

    // 接受3个lambda表达式的方法参数
    public Disposable subscribe(Consumer<? super T> next, Consumer<? super Throwable> error, Action complete) {
        return subscribe(next, error, complete, Functions.emptyConsumer());
    }
    // 接受2个lambda表达式的方法参数
    public Disposable subscribe(Consumer<? super T> next, Consumer<? super Throwable> error) {
        return subscribe(next, error, Functions.EMPTY_ACTION,Functions.emptyConsumer());
    }
    // 接受1个lambda表达式的方法参数
    public Disposable subscribe(Consumer<? super T> next) {
        return subscribe(next, Functions.emptyConsumer(), Functions.EMPTY_ACTION,Functions.emptyConsumer());
    }
}
