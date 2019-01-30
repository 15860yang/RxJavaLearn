package Observe;

public class Functions {
    public static final Action EMPTY_ACTION = new Action() {
        @Override
        public void apply() throws Exception {

        }
    };

    public static <T> Consumer<T> emptyConsumer() {
        return new Consumer<T>() {
            @Override
            public void apply(T t) {

            }
        };
    }

}
