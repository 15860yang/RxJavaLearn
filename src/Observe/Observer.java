package Observe;

/**
 * 观察者
 *
 * @param <T>
 */
public interface Observer<T> {


    void onSubscribe(Disposable d);

    void onNext(T t);

    void onError(Throwable e);

    void onComplete();
}
