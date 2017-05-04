package Model;

import sun.reflect.generics.tree.Tree;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cdzos on 2017/5/2.
 */
public final class TreeFileItem {

    public static interface PathGetter {
        void get(String name);
    }

    private File baseFile;
    private boolean checked;
    private List<TreeFileItem> children;
    private String[] postfixes;
    private PathGetter getter;

    public TreeFileItem() {
    }

    public TreeFileItem(File file) {
        this.baseFile = file;
    }

    public TreeFileItem(File file, String[] postfixes) {
        this.baseFile = file;
        this.postfixes = postfixes;
    }

    public int countCheckItem() {
        if (isDirectory()) {
            int result = 0;
            for (TreeFileItem child : children()) {
                result += child.countCheckItem();
            }
            return result;
        } else if (checked) {
            return 1;
        } else {
            return 0;
        }
    }

    public boolean hasCheckedItem() {
        if (!isDirectory()) return false;
        for (TreeFileItem child : children()) {
            if (child.isDirectory() && child.hasCheckedItem() ||
                    (!child.isDirectory() && child.isChecked())) {
                return true;
            }
        }
        return false;
    }

    public File getBaseFile() {
        return baseFile;
    }

    public void setBaseFile(File baseFile) {
        this.baseFile = baseFile;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String[] getPostfixes() {
        return postfixes;
    }

    public void setPostfixes(String[] postfixes) {
        this.postfixes = postfixes;
    }

    public boolean isDirectory() {
        return this.baseFile.isDirectory();
    }

    public boolean isValuableDirectory() {
        return isDirectory() && children().length > 0;
    }

    public TreeFileItem[] children() {
        if (children == null) {
            children = new ArrayList<TreeFileItem>();
            try {
                for (File file : baseFile.listFiles()) {
                    if (getter != null) {
                        getter.get(file.getAbsolutePath());
                    }
                    boolean fuck = false;
                    if (postfixes == null ) {
                        fuck = true;
                    } else if (file.isDirectory()) {    // check if directory is empty
                        if (file.getName().length() > 0 && file.getName().charAt(0) == '.') {
                            fuck = false;
                        } else if (file.listFiles().length > 0) {
                            TreeFileItem tf = new TreeFileItem(file, postfixes);
                            tf.setGetter(getter);
                            if (tf.isValuableDirectory()) {
                                children.add(tf);
                            }
                            continue;
                        }
                    } else {
                        for (String postfix: postfixes) {
                            if (file.getName().endsWith(postfix)) {
                                fuck = true;
                                break;
                            }
                        }
                    }
                    if (fuck) {
                        TreeFileItem tf = new TreeFileItem(file, postfixes);
                        tf.setGetter(getter);
                        children.add(tf);
                    }
                }

            } catch (NullPointerException e) {
                System.out.println("Can not list file: " + baseFile.getAbsolutePath());
            }
        }
        return children.toArray(new TreeFileItem[children.size()]);
    }

    public PathGetter getGetter() {
        return getter;
    }

    public void setGetter(PathGetter getter) {
        this.getter = getter;
    }

    @Override
    public String toString() {
        return baseFile.getName();
    }
}
