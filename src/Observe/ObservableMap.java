package Observe;

public class ObservableMap<T,U> extends Observable<U> {

    private final ObservableSource<T> source;
    private final Function<? super T,? extends U> function;

    ObservableMap(ObservableSource<T> source, Function<? super T, ? extends U> function) {
        this.source = source;
        this.function = function;
    }

    @Override
    void subscribeActual(Observer<? super U> observer) {
        source.subscribe(new MapObserver<T,U>(observer,function));
    }

    static class MapObserver<T,U> extends BasicObserver<T,U>{

        final Function<? super T,? extends U> mapper;

        public MapObserver(Observer<? super U> actual,Function<? super T, ? extends U> mapper) {
            super(actual);
            this.mapper = mapper;
        }

        @Override
        public void onNext(T t) {
            if(done){
                return;
            }
            try{
                downStream.onNext(mapper.apply(t));
            }catch (Exception e){
                onError(e);
            }
        }
    }
}
