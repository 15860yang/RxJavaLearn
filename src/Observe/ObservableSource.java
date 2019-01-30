package Observe;


/**
 * 被观察者
 *
 * @param <T>
 */
public interface ObservableSource<T> {
    void subscribe(Observer<? super T> observer);
}
