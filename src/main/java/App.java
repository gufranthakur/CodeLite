import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import com.formdev.flatlaf.fonts.jetbrains_mono.FlatJetBrainsMonoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import javax.swing.*;
import java.awt.*;

public class App extends JFrame {

    public WelcomeView welcomeView;
    public JSplitPane rootPanel;
    public ProjectView projectView;
    public EditorView editorView;

    public JMenuBar menuBar;
    public JMenu optionsMenu;

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
        optionsMenu = new JMenu("File");

        rootPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, projectView, editorView.getContentPanel());
    }

    public void addComponent() {
        projectView.addComponent();

        menuBar.add(optionsMenu);

        this.add(welcomeView);

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
        app.addComponent();
    }

}
