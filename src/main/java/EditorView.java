import com.formdev.flatlaf.fonts.jetbrains_mono.FlatJetBrainsMonoFont;
import org.fife.ui.rsyntaxtextarea.*;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;


public class EditorView extends RSyntaxTextArea implements KeyListener{

    private final RTextScrollPane editorScrollPane;

    InputStream monokaiStream, eclipseStream, nightStream, redStream, blueStream, purpleStream;
    Theme monokaiTheme, eclipseTheme, nightTheme, redTheme, blueTheme, purpleTheme;
    private final App app;

    public EditorView(App app) {
        super();
        this.app = app;
        this.addKeyListener(this);

        monokaiStream = EditorView.class.getResourceAsStream("/themes/monokai.xml");
        eclipseStream = EditorView.class.getResourceAsStream("/themes/eclipse.xml");
        nightStream = EditorView.class.getResourceAsStream("/themes/night.xml");
        redStream = EditorView.class.getResourceAsStream("/themes/red.xml");
        blueStream = EditorView.class.getResourceAsStream("/themes/blue.xml");
        purpleStream = EditorView.class.getResourceAsStream("/themes/purple.xml");

        try {
            monokaiTheme = Theme.load(monokaiStream);
            eclipseTheme = Theme.load(eclipseStream);
            nightTheme = Theme.load(nightStream);
            redTheme = Theme.load(redStream);
            blueTheme = Theme.load(blueStream);
            purpleTheme = Theme.load(purpleStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        this.setCodeFoldingEnabled(true);
        this.setRoundedSelectionEdges(true);

        editorScrollPane = new RTextScrollPane(this);
    }

    public void setColorScheme(String colorScheme) {
        switch (colorScheme) {
            case "Monokai" : monokaiTheme.apply(this);
                break;
            case "Eclipse" : eclipseTheme.apply(this);
                break;
            case "Night" : nightTheme.apply(this);
                break;
            case "Red" : redTheme.apply(this);
                break;
            case "Blue" : blueTheme.apply(this);
                break;
            case "Purple" : purpleTheme.apply(this);
                break;
        }
        this.setFont(app.editorFont);
    }


    public JScrollPane getContentPanel() {
        return editorScrollPane;
    }

    private int fontSize = 12;
    private static final int MIN_FONT_SIZE = 8;
    private static final int MAX_FONT_SIZE = 72;

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.isControlDown()) {
            if (e.getKeyCode() == KeyEvent.VK_PLUS || e.getKeyCode() == KeyEvent.VK_EQUALS) {
                if (fontSize < MAX_FONT_SIZE) {
                    fontSize++;
                }
            } else if (e.getKeyCode() == KeyEvent.VK_MINUS) {
                if (fontSize > MIN_FONT_SIZE) {
                    fontSize--;
                }
            }
            this.setFont(new Font(FlatJetBrainsMonoFont.FAMILY, Font.PLAIN, fontSize));
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

}
