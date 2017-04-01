package Model;

/**
 * Created by ZMYang on 2017/4/1.
 */
public class Getter implements IResultGetter {
    @Override
    public void getResult(String result) {
        System.out.println(result);
    }
}
