package Test;

import Test.TokenizerTest.StringStreamTest;
import Test.TokenizerTest.TokenizerTest;

/**
 * Created by cdzos on 2017/5/13.
 */
public class Main {

    public static void main(String args[]) {
        int total_test = 0;
        int pass_test = 0;

        Test tkTest = new TokenizerTest();
        tkTest.run();

        Test ssTest = new StringStreamTest();
        ssTest.run();

        total_test += ssTest.getTotalInstances();
        pass_test += ssTest.getPassInstances();

        System.out.println("Total pass test: " + pass_test + "/" + total_test);
    }

}
