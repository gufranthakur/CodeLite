import com.formdev.flatlaf.fonts.jetbrains_mono.FlatJetBrainsMonoFont;
import org.fife.ui.rsyntaxtextarea.*;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;


public class EditorView extends RSyntaxTextArea{

    private final RTextScrollPane editorScrollPane;

    public EditorView(App app) {
        super();

        InputStream themeStream = EditorView.class.getResourceAsStream("/monokai.xml");

        try {
            Theme theme = Theme.load(themeStream);
            theme.apply(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.setBackground(app.getBackground().darker());
        this.setFont(new Font(FlatJetBrainsMonoFont.FAMILY, Font.PLAIN, 18));
        this.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        this.setCodeFoldingEnabled(true);
        this.setForeground(Color.WHITE);
        this.setRoundedSelectionEdges(true);

        this.setCurrentLineHighlightColor((new Color(30, 30, 30)));
        this.setCaretColor(Color.WHITE);

        editorScrollPane = new RTextScrollPane(this);

    }

    public JScrollPane getContentPanel() {
        return editorScrollPane;
    }

}
