import com.formdev.flatlaf.fonts.inter.FlatInterFont;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class ProjectView extends JPanel {

    private final App app;

    public CustomNode root;
    private JTree projectTree;
    private JScrollPane projectScrollPane;
    public String directoryPath;

    public ArrayList<CustomNode> projectFiles = new ArrayList<>();
    public String projectPath = null;

    private static Icon folderIcon;

    private JPopupMenu popupMenu;

    public ProjectView(App app) {
        this.app = app;
        this.setPreferredSize(new Dimension(300, 1200));

        setLayout(new BorderLayout());
    }

    public void init() {
        root = new CustomNode("Project", null, projectPath);
        projectTree = new JTree(root);
        projectTree.setFont(new Font(FlatInterFont.FAMILY, Font.PLAIN, 16));
        projectScrollPane = new JScrollPane(projectTree);

        folderIcon = new ImageIcon(Objects.requireNonNull
                (ProjectView.class.getResource("/icons/folder_icon_24.png")));
        refreshTree();
        createPopupMenu();
    }

    public void initActionListeners() {
        projectTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                    try {
                        CustomNode node = (CustomNode) projectTree.getLastSelectedPathComponent();
                        if (!node.isDirectory) {
                            setEditorContent(app.editorView, node);
                            app.saveFileButton.setEnabled(true);
                            CustomNode parentNode = (CustomNode) node.getParent();
                            app.currentFileParentPath = parentNode.getFilePath();

                        } else {
                            app.saveFileButton.setEnabled(false);
                        }
                    } catch (NullPointerException pointerException) {
                        System.out.println("No File Selected");
                    } catch (ClassCastException classCastException) {
                        System.out.println("Exception");
                    }
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int row = projectTree.getClosestRowForLocation(e.getX(), e.getY());
                    projectTree.setSelectionRow(row);
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }

        });

    }

    public void setEditorContent(EditorView editorView, CustomNode node) {
        editorView.setText(node.getContent());
        app.setTitle("CodeLite - " + node.getNodeName());
    }

    public void addComponent() {
        this.add(projectScrollPane, BorderLayout.CENTER);
    }

    public void openProject() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = chooser.showOpenDialog(null);
        File file = chooser.getSelectedFile();
        directoryPath = file.getAbsolutePath();

        if (result == JFileChooser.APPROVE_OPTION) {
            projectPath = file.getAbsolutePath();
            openDirectory(file);
        }
    }

    public void openDirectory(File inputFile) {
        openDirectoryRecursive(inputFile, root);
        DefaultTreeModel treeModel = (DefaultTreeModel) projectTree.getModel();
        treeModel.reload();
    }

    private void openDirectoryRecursive(File inputFile, DefaultMutableTreeNode parentNode) {
        File[] files = inputFile.listFiles();
        if (files != null) {
            for (File file : files) {
                CustomNode node;
                if (file.isDirectory()) {
                    node = new CustomNode(file.getName(), "", file.getAbsolutePath());
                    node.isDirectory = true;
                    openDirectoryRecursive(file, node);
                } else {
                    try {
                        Scanner scanner = new Scanner(file);
                        StringBuilder data = new StringBuilder();
                        while (scanner.hasNextLine()) {
                            data.append(scanner.nextLine()).append("\n");
                        }
                        scanner.close();
                        node = new CustomNode(file.getName(), data.toString(), file.getAbsolutePath());
                        node.isDirectory = false;
                        projectFiles.add(node);

                    } catch (FileNotFoundException e) {
                        System.err.println("File not found: " + file.getAbsolutePath());
                        continue;
                    }
                }
                parentNode.add(node);
            }
        }

    }

    public void saveFile() {
        CustomNode node = (CustomNode) projectTree.getLastSelectedPathComponent();
        if (node == null) {
            JOptionPane.showMessageDialog(null, "No file selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        node.setContent(app.editorView.getText());

        File savedFile = new File(node.getFilePath());
        try (FileWriter writer = new FileWriter(savedFile)) {
            writer.write(node.getContent());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error saving file: " + savedFile.getName() + "\n" + e.getMessage(),
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createPopupMenu() {
        popupMenu = new JPopupMenu();

        JMenuItem addFileItem = new JMenuItem("New File");
        JMenuItem addFolderItem = new JMenuItem("New Folder");
        JMenuItem deleteItem = new JMenuItem("Delete");
        JMenuItem renameItem = new JMenuItem("Rename");

        addFileItem.addActionListener(e -> createFile(false));
        addFolderItem.addActionListener(e -> createFile(true));
        deleteItem.addActionListener(e -> deleteFile());
        renameItem.addActionListener(e -> renameFile());

        popupMenu.add(addFileItem);
        popupMenu.add(addFolderItem);
        popupMenu.add(deleteItem);
        popupMenu.add(renameItem);
    }

    private void createFile(boolean isDirectory) {
        CustomNode selectedNode = (CustomNode) projectTree.getLastSelectedPathComponent();
        File parentFile;
        CustomNode newNode;

        if (!isDirectory) {
            String fileName = JOptionPane.showInputDialog(null, "Enter file name");
            try {
                parentFile = new File(selectedNode.getFilePath());
            } catch (NullPointerException e) {
                parentFile = new File(projectPath);
            }

            File newFile = new File(parentFile.getAbsolutePath(), fileName);
            newNode = new CustomNode(fileName, "", newFile.getAbsolutePath());

            if (parentFile.isDirectory()) selectedNode.add(newNode);
            else root.add(newNode);
        } else {
            String folderName = JOptionPane.showInputDialog(null, "Enter folder name");
            try {
                parentFile = new File(selectedNode.getFilePath());
            } catch (NullPointerException e) {
                parentFile = new File(projectPath);
            }

            File newFolder = new File(parentFile.getAbsolutePath(), folderName);
            boolean folderCreated = newFolder.mkdir();
            if (!folderCreated) JOptionPane.showMessageDialog(null, "Unable to create folder",
                    "Error", JOptionPane.ERROR_MESSAGE);
            else {
                newNode = new CustomNode(folderName, null, newFolder.getAbsolutePath());

                if (parentFile.isDirectory()) selectedNode.add(newNode);
                else root.add(newNode);
            }
        }
        refreshTree();
    }

    private void deleteFile() {
        CustomNode selectedNode = (CustomNode) projectTree.getLastSelectedPathComponent();
        try {
            File selectedFile = new File(selectedNode.getFilePath());
            if (selectedFile.exists()) {
                boolean deleted;
                if (selectedFile.isDirectory()) deleted = deleteDirectory(selectedFile);
                else deleted = selectedFile.delete();


                if (deleted) {
                    DefaultTreeModel model = (DefaultTreeModel) projectTree.getModel();
                    model.removeNodeFromParent(selectedNode);
                    model.reload();
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Could not delete the file/folder",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, "Cannot delete opened project", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private boolean deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        return directory.delete();
    }

    private void renameFile() {
        CustomNode selectedNode = (CustomNode) projectTree.getLastSelectedPathComponent();

        if (selectedNode.isDirectory) JOptionPane.showMessageDialog(null, "Cannot rename folders", "Error", JOptionPane.ERROR_MESSAGE);
        else {

        String newName = JOptionPane.showInputDialog(null, "Enter new name", selectedNode.getNodeName());
        if (newName == null || newName.trim().isEmpty()) {
            return;
        }

        File currentFile = new File(selectedNode.getFilePath());
        File parentFile = currentFile.getParentFile();
        File newFile = new File(parentFile, newName);

        if (currentFile.renameTo(newFile)) {

            selectedNode.setNodeName(newName);
            selectedNode.setFilePath(newFile.getAbsolutePath());


            DefaultTreeModel model = (DefaultTreeModel) projectTree.getModel();
            model.nodeChanged(selectedNode);

            refreshTree();
        } else {
            JOptionPane.showMessageDialog(null,
                    "Could not rename the file",
                    "Rename Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        }
    }

    public void refreshTree() {
        DefaultTreeModel model = (DefaultTreeModel) projectTree.getModel();
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) projectTree.getCellRenderer();
        renderer.setClosedIcon(folderIcon);
        renderer.setOpenIcon(folderIcon);
        model.reload();
    }

    public JTree getProjectTree() {
        return projectTree;
    }

    public CustomNode getSelectedCustomNode() {
        return (CustomNode) projectTree.getLastSelectedPathComponent();
    }

}
