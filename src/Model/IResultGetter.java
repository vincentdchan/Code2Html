package Model;

/**
 * Created by duzhong on 17-3-11.
 *
 * Notice that do not save different content in the same file,
 * the getter will be called in another thread.
 *
 */
public interface IResultGetter {

    void getResult(String result);

}
