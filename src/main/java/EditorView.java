import org.fife.ui.rsyntaxtextarea.*;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;


public class EditorView extends RSyntaxTextArea{

    private final RTextScrollPane editorScrollPane;

    InputStream monokaiStream, eclipseStream, nightStream, redStream, blueStream, purpleStream;
    Theme monokaiTheme, eclipseTheme, nightTheme, redTheme, blueTheme, purpleTheme;
    private final App app;

    public EditorView(App app) {
        super();
        this.app = app;

        monokaiStream = EditorView.class.getResourceAsStream("/monokai.xml");
        eclipseStream = EditorView.class.getResourceAsStream("/eclipse.xml");
        nightStream = EditorView.class.getResourceAsStream("/night.xml");
        redStream = EditorView.class.getResourceAsStream("/red.xml");
        blueStream = EditorView.class.getResourceAsStream("/blue.xml");
        purpleStream = EditorView.class.getResourceAsStream("/purple.xml");

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
            case "Monokai" : {
                monokaiTheme.apply(this);
                if (app.darkTheme) {
                    this.setBackground(app.getBackground().darker());
                    editorScrollPane.setBackground(app.getBackground().darker().darker());
                }
            }
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

}
