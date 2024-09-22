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
    public JMenu settingsMenu, themeItem, colorSchemeItem, languageItem;
    public JMenuItem closeProjectItem, newProjectItem, saveProjectItem,
             darkThemeItem, lightThemeItem,
             monokaiItem, eclipseItem, nightItem, redItem, blueItem, purpleItem,
             javaItem, pythonItem, cItem, jsItem,
             runCodeItem,
             exitItem;

    public static boolean darkTheme = true;
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

        editorView = new EditorView(this);

        rootPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, projectView, editorView.getContentPanel());

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
                welcomeView.openProjectButton.setBackground(new Color(8, 166, 33));
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
        pythonItem = new JMenuItem("Python");
        cItem = new JMenuItem("C/C++");
        jsItem = new JMenuItem("Javascript");

        runCodeItem = new JMenuItem("Run code");
        exitItem = new JMenuItem("Exit CodeLite");
        exitItem.addActionListener(e -> System.exit(0));

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
