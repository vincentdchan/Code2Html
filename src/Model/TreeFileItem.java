package Model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cdzos on 2017/5/2.
 */
public final class TreeFileItem {

    private File baseFile;
    private boolean checked;
    private List<TreeFileItem> children;
    private String[] postfixes;

    public TreeFileItem() {
    }

    public TreeFileItem(File file) {
        this.baseFile = file;
    }

    public TreeFileItem(File file, String[] postfixes) {
        this.baseFile = file;
        this.postfixes = postfixes;
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
            for (File file : baseFile.listFiles()) {
                boolean fuck = false;
                if (postfixes == null ) {
                    fuck = true;
                } else if (file.isDirectory()) {    // check if directory is empty
                    if (file.getName().length() > 0 && file.getName().charAt(0) == '.') {
                        fuck = false;
                    } else if (file.listFiles().length > 0) {
                        TreeFileItem tf = new TreeFileItem(file, postfixes);
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
                    children.add(new TreeFileItem(file, postfixes));
                }
            }
        }
        return children.toArray(new TreeFileItem[children.size()]);
    }

    @Override
    public String toString() {
        return baseFile.getName();
    }
}
