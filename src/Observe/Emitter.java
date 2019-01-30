package Observe;

public interface Emitter<T> {
    void onNext(T t);

    void onError(Throwable e);

    void onComplete();
}
