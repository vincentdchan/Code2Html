package View;

import Model.TreeFileItem;
import javafx.event.EventType;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.io.File;

/**
 * Created by cdzos on 2017/5/2.
 */
public final class FileTreeControl extends TreeView<Model.TreeFileItem> {

    public static int FileIconSize = 16;

    private static Image fileIcon;
    private static Image javaIcon;
    private static Image cLangIcon;
    private static Image folderIcon;
    private static Image folderOpenIcon;

    private TreeFileItem treeFileItem;

    public FileTreeControl() {
        super();

        loadResources();
        setEditable(false);
        setCellFactory(CheckBoxTreeCell.<TreeFileItem>forTreeView());
    }

    /**
     *
     * @return true if expand successfully
     */
    public boolean expandAll() {
        MyTreeItem myTreeItem = (MyTreeItem)getRoot();
        if (myTreeItem != null) {
            expandAll(myTreeItem);
            return true;
        } else {
            return false;
        }
    }

    private void expandAll(MyTreeItem myTreeItem) {
        myTreeItem.setExpanded(true);
        for (TreeItem<TreeFileItem> ti: myTreeItem.getChildren()) {
            expandAll((MyTreeItem)ti);
        }
    }

    /**
     *
     * @return true if collapse successfully
     */
    public boolean collapseAll() {
        MyTreeItem myTreeItem = (MyTreeItem)getRoot();
        if (myTreeItem != null) {
            collapseAll(myTreeItem);
            return true;
        } else {
            return false;
        }
    }

    private void collapseAll(MyTreeItem myTreeItem) {
        myTreeItem.setExpanded(false);
        for (TreeItem<TreeFileItem> ti: myTreeItem.getChildren()) {
            collapseAll((MyTreeItem)ti);
        }
    }

    private void loadResources() {
        fileIcon = new Image(getClass().getResourceAsStream("/image/1.jpg"));
        javaIcon = new Image(getClass().getResourceAsStream("/resources/icons/java.png"));
        cLangIcon = new Image(getClass().getResourceAsStream("/resources/icons/c.png"));
        folderIcon = new Image(getClass().getResourceAsStream("/resources/icons/folder.png"));
        folderOpenIcon = new Image(getClass().getResourceAsStream("/resources/icons/folder-open.png"));
    }

    public void setRootFileItem(TreeFileItem treeFileItem) {
        this.treeFileItem = treeFileItem;

        setRoot(new MyTreeItem(treeFileItem));
    }

    public final static class MyTreeItem extends CheckBoxTreeItem<TreeFileItem> {

        public MyTreeItem(TreeFileItem treeFileItem) {
            super(treeFileItem);

            // let the item changed
            selectedProperty().addListener((obs, oldVal, newVal) -> {
                treeFileItem.setChecked(newVal);
            });

            expandedProperty().addListener((obs, oldVal, newVal) -> {
                if (treeFileItem.isDirectory()) {
                    ImageView iv;
                    if (newVal) {
                        iv = new ImageView(folderOpenIcon);
                    } else {
                        iv = new ImageView(folderIcon);
                    }
                    iv.setFitWidth(FileIconSize);
                    iv.setFitHeight(FileIconSize);
                    setGraphic(iv);
                }
            });

            ImageView iv;
            // = new ImageView(fileIcon);
            String filename = treeFileItem.getBaseFile().getName();
            if (treeFileItem.isDirectory()) {
                iv = new ImageView(folderOpenIcon);
            } else if (filename.endsWith(".java")) {
                iv = new ImageView(javaIcon);
            } else if (filename.endsWith(".c") || filename.endsWith(".h")) {
                iv = new ImageView(cLangIcon);
            } else {
                iv = new ImageView(fileIcon);
            }
            iv.setFitHeight(FileIconSize);
            iv.setFitWidth(FileIconSize);
            setGraphic(iv);

            if (treeFileItem.isDirectory()) {
                setExpanded(true);

                for (TreeFileItem child : treeFileItem.children()) {
                    this.getChildren().add(new MyTreeItem(child));
                }
            }
        }

    }

}
