package Test;

/**
 * Created by cdzos on 2017/5/13.
 */
public abstract class Test {

    private int totalInstances;
    private int passInstances;

    public abstract void run();

    public int getTotalInstances() {
        return totalInstances;
    }

    protected void setTotalInstances(int totalInstances) {
        this.totalInstances = totalInstances;
    }

    public int getPassInstances() {
        return passInstances;
    }

    protected void setPassInstances(int passInstances) {
        this.passInstances = passInstances;
    }

    protected void Assert(boolean value) {
        if (!value) throw new AssertionError();
    }

}
