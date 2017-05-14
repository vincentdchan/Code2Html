package Test.TokenizerTest;

import Model.StringStream;
import Test.Test;

import java.util.regex.Pattern;

/**
 * Created by cdzos on 2017/5/14.
 */
public class StringStreamTest extends Test {

    public StringStreamTest() {
        setTotalInstances(2);
    }

    @Override
    public void run() {
        System.out.println("Testing StringStream");
        test1();
        test2();
        System.out.println("StringStream test: " + getPassInstances() + "/" + getTotalInstances());
    }

    private void test1() {
        final String testStr = "I am the king of the world";
        final StringStream ss = new StringStream(testStr);

        assert ss.isFirstChar();

        ss.moveForward();
        assert ss.getChar() == ' ';

        String popS = ss.popString();
        assert popS.equals("I");

        setPassInstances(getPassInstances() + 1);
    }

    private void test2() {
        final String testStr = "test,abc";
        final StringStream ss = new StringStream(testStr);
        Pattern testP = Pattern.compile("^test");
        Pattern abcP = Pattern.compile("^,abc");

        assert ss.test(testP);

        ss.moveForward(4);

        assert ss.swallow(abcP);
        assert ss.popString().equals(testStr);
        assert !ss.isLastChar();
        assert ss.reachEnd();

        setPassInstances(getPassInstances() + 1);
    }

}
