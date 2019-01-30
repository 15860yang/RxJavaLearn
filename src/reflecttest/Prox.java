package reflecttest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class Prox implements InvocationHandler {

    private Object person;

    public Prox(Object person) {
        this.person = person;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {



        method.invoke(person,args);



        return null;
    }
}
