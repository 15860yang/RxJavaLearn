package Observe;

public abstract class BasicObserver<T,R> implements Observer<T>,Disposable {
    //上游传过来的Disposable
    private Disposable upStream;

    //下游接收的观察者对象
    final Observer<? super R> downStream;

    boolean done;

    public BasicObserver(Observer<? super R> downStream) {
        this.downStream = downStream;
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.upStream = d;
        downStream.onSubscribe(this);
    }

    @Override
    public void onError(Throwable e) {
        if(done){
            return;
        }
        done = true;
        downStream.onError(e);
    }

    @Override
    public void onComplete() {
        if(done){
            return;
        }
        done = true;
        downStream.onComplete();
    }

    @Override
    public void dispose() {
        upStream.dispose();
    }

    @Override
    public boolean isDisposed() {
        return upStream.isDisposed();
    }
}
