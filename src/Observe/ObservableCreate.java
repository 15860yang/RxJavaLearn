package Observe;

class ObservableCreate<T> extends Observable<T> {

    private final ObservableOnSubscribe<T> source;

    ObservableCreate(ObservableOnSubscribe<T> source) {
        this.source = source;
    }

    @Override
    void subscribeActual(Observer<? super T> observer) {
        CreateEmitter<T> emitter = new CreateEmitter<>(observer);

        observer.onSubscribe(emitter);
        try {
            source.subscribe(emitter);
        } catch (Exception e) {
            emitter.onError(e);
        }
    }

    static class CreateEmitter<T> implements Emitter<T>, Disposable {
        private final Observer<? super T> observer;

        CreateEmitter(Observer<? super T> observer) {
            this.observer = observer;
        }

        @Override
        public void onNext(T t) {
            try {
                if (!isDisposed()) {
                    observer.onNext(t);//在这里加上异常处理
                }
            } catch (Exception e) {
                observer.onError(e);
            }
        }

        @Override
        public void onError(Throwable e) {
            if (!isDisposed()) {
                try {
                    observer.onError(e);
                } finally {
                    dispose();
                }
            }

        }

        @Override
        public void onComplete() {
            if (!isDisposed()) {
                try {
                    observer.onComplete();
                } finally {
                    dispose();
                }
            }

        }

        private volatile boolean isDisposed = false;

        @Override
        public void dispose() {
            isDisposed = true;
        }

        @Override
        public boolean isDisposed() {
            return isDisposed;
        }
    }
}
