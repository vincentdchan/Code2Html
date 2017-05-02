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

    private Image fileIcon = new Image("file:///../image/1.jpg");
    private Image javaIcon = new Image("file:///../resources/icons/java.png");
    private Image cLangIcon = new Image("file:///../resources/icons/c.png");
    private Image folderIcon = new Image("file:///../resources/icons/folder.png");
    private Image folderOpenIcon = new Image("file:///../resources/icons/folder-open.png");

    private TreeFileItem treeFileItem;

    public FileTreeControl() {
        super();

        setEditable(false);
        /*
        setCellFactory(new Callback<TreeView<TreeFileItem>, TreeCell<TreeFileItem>>() {
            @Override
            public TreeCell<TreeFileItem> call(TreeView<TreeFileItem> param) {
                MyTreeCellImpl fuck = new MyTreeCellImpl();
                fuck.selectedProperty().addListener((obs, oldVal, newVal) -> {
                    System.out.println("change");
                    System.out.println(newVal);
                });
                return fuck;
            }
        });
        */
        setCellFactory(CheckBoxTreeCell.<TreeFileItem>forTreeView());
    }

    public void setRootFileItem(TreeFileItem treeFileItem) {
        this.treeFileItem = treeFileItem;

        setRoot(new MyTreeItem(treeFileItem));
    }

    private final class MyTreeCellImpl extends CheckBoxTreeCell<TreeFileItem> {

        private TextField textField;

        public MyTreeCellImpl() {
            /*
            selectedProperty().addListener((obs, oldVal, newVal) -> {
                System.out.println("change");
                System.out.println(newVal);
            });
            */
        }

        /*
        @Override
        public void updateItem(TreeFileItem item, boolean empty) {
            super.updateItem(item, empty);
        }
        */

        /*
        @Override
        protected void updateItem(TreeFileItem item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                setText(item.getBaseFile().getName());
                // textField.setText(item.getBaseFile().getName());
                setGraphic(textField);
            }
        }
        */
    }

    public final class MyTreeItem extends CheckBoxTreeItem<TreeFileItem> {

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
