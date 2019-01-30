package Observe;

public interface ObservableOnSubscribe<T> {
    void subscribe(Emitter<T> emitter);
}
