package reflecttest;

public class Man implements Person {

    public int i;

    public Man() {
        this.i = 5;
    }

    @Override
    public void say(String s) {
        System.out.println("我是男的 s = "+s);
    }
}
