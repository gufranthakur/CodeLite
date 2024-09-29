import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import com.formdev.flatlaf.fonts.jetbrains_mono.FlatJetBrainsMonoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class App extends JFrame {

    public WelcomeView welcomeView;
    public JSplitPane rootPanel;
    public ProjectView projectView;
    public EditorView editorView;

    public JTabbedPane rootTabbedPane;
    public JPanel rightSplitPanel;
    public JPanel toolPanel;

    public JButton openTerminalButton;
    public boolean isRunning = false;
    public JComboBox fileComboBox;
    public String os = System.getProperty("os.name").toLowerCase();
    public ProcessBuilder pb;

    public JMenuBar menuBar;
    public JMenu settingsMenu, themeItem, colorSchemeItem, languageItem;
    public JMenuItem closeProjectItem, newProjectItem, saveProjectItem,
             darkThemeItem, lightThemeItem,
             monokaiItem, eclipseItem, nightItem, redItem, blueItem, purpleItem,
             javaItem, pythonItem, cItem, jsItem,
             runCodeItem,
             exitItem;

    public boolean darkTheme = true;
    public Font editorFont;

    public App() {
        setSize(800, 500);
        setTitle("CodeLite");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
    }

    public void init() {
        editorFont = new Font(FlatJetBrainsMonoFont.FAMILY, Font.PLAIN, 18);

        welcomeView = new WelcomeView(this);

        projectView = new ProjectView(this);
        projectView.init();
        projectView.initActionListeners();

        rightSplitPanel = new JPanel();
        rightSplitPanel.setLayout(new BorderLayout());

        rootTabbedPane = new JTabbedPane();

        toolPanel = new JPanel();
        toolPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        fileComboBox = new JComboBox();

        openTerminalButton = new JButton("Open Terminal");
        openTerminalButton.setFont(new Font(FlatJetBrainsMonoFont.FAMILY, Font.PLAIN, 14));
        openTerminalButton.setBackground(new Color(30, 149, 23));
        openTerminalButton.addActionListener(e -> {
            //If terminal is not open
            if (!isRunning) {

                isRunning = true;
                openTerminalButton.setBackground(new Color(230, 35, 35));
                openTerminalButton.setText("Close Terminal");

                try {
                    if (os.contains("win"))
                         pb = new ProcessBuilder("cmd", "/c", "start", "cmd.exe");
                     else if (os.contains("mac"))
                         pb = new ProcessBuilder("open", "-a", "Terminal");
                     else if (os.contains("nix") || os.contains("nux") || os.contains("bsd"))
                         pb = new ProcessBuilder("x-terminal-emulator");
                     else
                         JOptionPane.showMessageDialog(null, "Unsupported Operating System", "Error", JOptionPane.ERROR_MESSAGE);
                    pb.start();

                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.err.println("Failed to open terminal: " + ex.getMessage());
                } catch (UnsupportedOperationException ex) {
                    System.err.println(ex.getMessage());
                }

            } else {
                isRunning = false;
                openTerminalButton.setBackground(new Color(26, 172, 18));
                openTerminalButton.setText("Run code");
            }

        });

        editorView = new EditorView(this);

        rootTabbedPane.add("Project", projectView);

        rightSplitPanel.add(editorView.getContentPanel(), BorderLayout.CENTER);
        rightSplitPanel.add(toolPanel, BorderLayout.NORTH);

        rootPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, rootTabbedPane, rightSplitPanel);
        menuBar = new JMenuBar();
        settingsMenu = new JMenu("Settings", true);

        newProjectItem = new JMenuItem("Open new Project");
        newProjectItem.addActionListener(e -> {
            projectView.getProjectTree().removeAll();
            projectView.openProject();
        });
        closeProjectItem = new JMenuItem("Close project");
        closeProjectItem.addActionListener(e -> {
            projectView.getProjectTree().removeAll();
            setContentPane(welcomeView);
            this.setSize(800, 500);
            this.setLocationRelativeTo(null);
        });
        saveProjectItem = new JMenuItem("Save project");

        themeItem = new JMenu("Theme");
        darkThemeItem = new JMenuItem("Dark");
        darkThemeItem.addActionListener(e -> {
            try {
                darkTheme = true;
                UIManager.setLookAndFeel(new FlatMacDarkLaf());
                welcomeView.openProjectButton.setBackground(new Color(20, 125, 241));

                SwingUtilities.updateComponentTreeUI(this);
                editorView.setFont(editorFont);
                revalidate();
                repaint();
            } catch (UnsupportedLookAndFeelException ex) {
                throw new RuntimeException(ex);
            }
        });
        lightThemeItem = new JMenuItem("Light");
        lightThemeItem.addActionListener(e -> {
            try {
                darkTheme = false;
                UIManager.setLookAndFeel(new FlatMacLightLaf());
                welcomeView.openProjectButton.setBackground(new Color(12, 182, 41));
                SwingUtilities.updateComponentTreeUI(this);
                editorView.setFont(editorFont);
                revalidate();
                repaint();
            } catch (UnsupportedLookAndFeelException ex) {
                throw new RuntimeException(ex);
            }
        });
        colorSchemeItem = new JMenu("Color scheme");
        monokaiItem = new JMenuItem("Monokai");
        monokaiItem.addActionListener(e -> editorView.setColorScheme("Monokai"));

        eclipseItem = new JMenuItem("Eclipse");
        eclipseItem.addActionListener(e -> editorView.setColorScheme("Eclipse"));

        nightItem = new JMenuItem("Night");
        nightItem.addActionListener(e -> editorView.setColorScheme("Night"));

        redItem = new JMenuItem("Reversal Red");
        redItem.addActionListener(e -> editorView.setColorScheme("Red"));

        blueItem = new JMenuItem("Amplified Blue");
        blueItem.addActionListener(e -> editorView.setColorScheme("Blue"));

        purpleItem = new JMenuItem("Hollow Purple");
        purpleItem.addActionListener(e -> editorView.setColorScheme("Purple"));

        languageItem = new JMenu("Language support");
        javaItem = new JMenuItem("Java");
        javaItem.addActionListener(e -> editorView.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA));
        pythonItem = new JMenuItem("Python");
        pythonItem.addActionListener(e -> editorView.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PYTHON));
        cItem = new JMenuItem("C/C++");
        cItem.addActionListener(e -> editorView.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_C));
        jsItem = new JMenuItem("Javascript");
        jsItem.addActionListener(e -> editorView.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT));

        runCodeItem = new JMenuItem("Run code");
        exitItem = new JMenuItem("Exit CodeLite");
        exitItem.addActionListener(e -> System.exit(0));
    }

    public void addFilesToComboBox() {
        for (CustomNode node : projectView.codeFiles) fileComboBox.addItem(node.getNodeName());
    }

    public void addComponent() {
        projectView.addComponent();

        menuBar.add(settingsMenu);
        settingsMenu.add(newProjectItem);
        settingsMenu.add(closeProjectItem);
        settingsMenu.add(saveProjectItem);

        settingsMenu.addSeparator();
        settingsMenu.add(themeItem);
        settingsMenu.add(colorSchemeItem);
        settingsMenu.addSeparator();
        settingsMenu.add(languageItem);
        settingsMenu.add(runCodeItem);
        settingsMenu.addSeparator();

        themeItem.add(darkThemeItem);
        themeItem.add(lightThemeItem);

        colorSchemeItem.add(monokaiItem);
        colorSchemeItem.add(eclipseItem);
        colorSchemeItem.add(nightItem);
        colorSchemeItem.add(redItem);
        colorSchemeItem.add(blueItem);
        colorSchemeItem.add(purpleItem);

        languageItem.add(javaItem);
        languageItem.add(pythonItem);
        languageItem.add(cItem);
        languageItem.add(jsItem);

        settingsMenu.add(exitItem);

        toolPanel.add(openTerminalButton);
        toolPanel.add(fileComboBox);

        this.add(rootPanel, BorderLayout.CENTER);
        this.add(welcomeView);
        setJMenuBar(menuBar);

        revalidate();
        repaint();

        setVisible(true);
    }

    public void launch() {
        setContentPane(rootPanel);

        this.setExtendedState(MAXIMIZED_BOTH);
        editorView.setColorScheme("Monokai");
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
