package Observe;

public interface Function<T,R> {

    R apply(T t) throws Exception;
}
