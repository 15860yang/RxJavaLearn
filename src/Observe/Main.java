package Observe;

public class Main {
    public static void main(String[] args) {
        try {

            /**
             * 先调用Observable.Create方法创建一个被观察者，当然我们也可以通过new Observable()的方式来创建，
             * 只是通过前者这样传入接口的方式创建的时候我们就可以更灵活的实现自己的逻辑，
             * 这个接口的实现在被传入之后会被包装成一个ObservableCreate对象，这个对象自己又去包装我们传进去的这个被观察者，
             *
             * 然后我们调用Observable(被观察者)的subscribe(订阅)方法传入一个观察者，这个方法将对Observer(观察者)的操作通过抽象方法
             * 让Observable的子类去实现，而这里的子类就是我们在这里实现的ObservableCreate对象，这样就保证了方法的灵活性，我们不需要再去修改
             * 基类就可以实现我们自己的订阅逻辑
             *
             * 与最基本的观察者模式相比，这样的观察者更具有可扩展性
             *
             */
            Observable.create(new ObservableOnSubscribe<String>() { // 创建被观察者
                @Override
                public void subscribe(Emitter<String> emitter) {
                    emitter.onNext("---");
                }
            }).subscribe(new Observer<String>() {   //订阅刚创建的观察者，subscribe方法调用之后就会执行被观察者的subscribe方法，
                @Override                           // 这个方法内部执行了我们自己的逻辑，emitter.onUpdate("1");为什么要这么做呢？
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(String s) {
                    System.out.println(s);
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(Emitter<String> emitter) {
                emitter.onNext("1");
                emitter.onNext("2");
                emitter.onComplete();
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void apply(String x) {
                System.out.println(x);
            }
        });

    }
}
