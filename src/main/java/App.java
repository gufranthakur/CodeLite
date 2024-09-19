
import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import com.formdev.flatlaf.fonts.jetbrains_mono.FlatJetBrainsMonoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;
import java.awt.*;

public class App extends JFrame {

    public WelcomeView welcomeView;
    public JSplitPane rootPanel;
    public ProjectView projectView;
    public EditorView editorView;

    public JMenuBar menuBar;
    public JMenu fileMenu;
    public JMenuItem newProject, openProject, closeProject, saveProject;

    public App() {
        setSize(800, 500);
        setTitle("CodeLite");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
    }

    public void init() {
        welcomeView = new WelcomeView(this);

        projectView = new ProjectView(this);
        projectView.init();
        projectView.initActionListeners();

        editorView = new EditorView(this);

        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        newProject = new JMenuItem("New Project");
        openProject = new JMenuItem("Open Project");
        closeProject = new JMenuItem("Close Project");
        saveProject = new JMenuItem("Save Project");

        rootPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, projectView, editorView.getContentPanel());
    }

    public void initActionListeners() {
        openProject.addActionListener(e -> projectView.openProject());
    }

    public void addComponent() {
        projectView.addComponent();

        fileMenu.add(newProject);
        fileMenu.add(openProject);
        fileMenu.add(closeProject);
        fileMenu.add(saveProject);

        menuBar.add(fileMenu);

        this.add(welcomeView);
        //this.add(rootPanel, BorderLayout.CENTER);


        setVisible(true);
    }

    public void launch() {
        this.add(rootPanel, BorderLayout.CENTER);
        this.remove(welcomeView);

        setJMenuBar(menuBar);
        this.revalidate();
        this.repaint();
    }

    public static void main(String[] args) {
        FlatMacDarkLaf.setup();
        FlatJetBrainsMonoFont.install();
        FlatInterFont.install();

        UIManager.put("defaultFont", new Font(FlatInterFont.FAMILY, Font.PLAIN, 13));

        App app = new App();
        app.init();
        app.initActionListeners();
        app.addComponent();
    }

}
