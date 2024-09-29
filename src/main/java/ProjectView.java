import com.formdev.flatlaf.fonts.inter.FlatInterFont;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ProjectView extends JPanel {

    private final App app;

    /* ------------------------------------------------------------------------
                                        Main root
       -------------------------------------------------------------------------*/

    private DefaultMutableTreeNode root;
    private JTree projectTree;
    private JScrollPane projectScrollPane;

    /* ------------------------------------------------------------------------
                                        Source root
       -------------------------------------------------------------------------*/

    private JPanel sourcePanel;
    public JTree sourceTree;
    public DefaultMutableTreeNode sourceCodeRoot;

    /* ------------------------------------------------------------------------
                                        Data
       -------------------------------------------------------------------------*/

    public ArrayList<CustomNode> projectFiles = new ArrayList<>();
    public ArrayList<CustomNode> codeFiles = new ArrayList<>();
    public String projectPath = null;

    //------------------------------------------------------------------------------//

    public ProjectView(App app) {
        this.app = app;
        this.setPreferredSize(new Dimension(300, 1200));

        setLayout(new BorderLayout());
    }

    public void init() {
        root = new DefaultMutableTreeNode("Project");
        projectTree = new JTree(root);
        projectTree.setFont(new Font(FlatInterFont.FAMILY, Font.PLAIN, 16));
        projectScrollPane = new JScrollPane(projectTree);

        sourcePanel = new JPanel();
        sourcePanel.setLayout(new BorderLayout());

        sourceCodeRoot = new DefaultMutableTreeNode("Source Code");
        sourceTree = new JTree(sourceCodeRoot);
        sourceTree.setFont(new Font(FlatInterFont.FAMILY, Font.PLAIN, 16));
    }

    public void initActionListeners() {
        /* ------------------------------------------------------------------------
                                        Project Tree
       -------------------------------------------------------------------------*/

        projectTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                try {
                    CustomNode node = (CustomNode) projectTree.getLastSelectedPathComponent();
                    setEditorContent(app.editorView, node);
                } catch (NullPointerException pointerException) {
                    System.out.println("No File Selected");
                } catch (ClassCastException classCastException) {
                    System.out.println("Exception");
                }

            }
        });

        /* ------------------------------------------------------------------------
                                        Source Tree
       -------------------------------------------------------------------------*/

        sourceTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    CustomNode node = (CustomNode) sourceTree.getLastSelectedPathComponent();
                    setEditorContent(app.editorView, node);
                } catch (NullPointerException pointerException) {
                    System.out.println("No File Selected");
                } catch (ClassCastException classCastException) {
                    System.out.println("Exception");
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
        sourcePanel.add(sourceTree, BorderLayout.CENTER);
    }

    public void openProject() {

        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = chooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (file.isDirectory()) {
                projectPath = file.getAbsolutePath();
                openDirectory(file);
            } else {
                openFile(file);
            }
        }
    }

    public void openDirectory(File inputFile) {
        openDirectoryRecursive(inputFile, root);
        DefaultTreeModel treeModel = (DefaultTreeModel) projectTree.getModel();
        treeModel.reload();
        System.out.println(projectFiles);
        System.out.println(codeFiles);
        System.out.println(projectPath);

        ArrayList<CustomNode> src = codeFiles;

        for (CustomNode node : src) sourceCodeRoot.add(node.clone());
        DefaultTreeModel sourceTreeModel = (DefaultTreeModel) sourceTree.getModel();
        sourceTreeModel.reload();
    }

    private void openDirectoryRecursive(File inputFile, DefaultMutableTreeNode parentNode) {
        File[] files = inputFile.listFiles();
        if (files != null) {
            for (File file : files) {
                CustomNode node;
                if (file.isDirectory()) {
                    node = new CustomNode(file.getName(), "");
                    openDirectoryRecursive(file, node);
                } else {
                    try {
                        Scanner scanner = new Scanner(file);
                        StringBuilder data = new StringBuilder();
                        while (scanner.hasNextLine()) {
                            data.append(scanner.nextLine()).append("\n");
                        }
                        scanner.close();
                        node = new CustomNode(file.getName(), data.toString());
                        projectFiles.add(node);
                        if (extension(node, ".java") ||
                            extension(node, ".py") ||
                            extension(node, ".c") || extension(node, ".cpp") ||
                            extension(node, ".js"))  {
                            codeFiles.add(node);
                        }
                    } catch (FileNotFoundException e) {
                        System.err.println("File not found: " + file.getAbsolutePath());
                        continue;
                    }
                }
                parentNode.add(node);
            }
        }

    }

    public boolean extension(CustomNode node, String extName) {
        return node.getNodeName().endsWith(extName);
    }

    public void openFile(File file) {
        try {
            Scanner scanner = new Scanner(file);

            String name = file.getName();
            String data = "";

            while (scanner.hasNext()) data = data.concat(scanner.nextLine() + "\n");

            CustomNode node = new CustomNode(name, data);

            root.add(node);
            DefaultTreeModel treeModel = (DefaultTreeModel) projectTree.getModel();
            treeModel.reload();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public JPanel getSourcePanel() {
        return sourcePanel;
    }

    public JTree getProjectTree() {
        return projectTree;
    }

}
