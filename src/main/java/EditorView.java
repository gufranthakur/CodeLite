import com.formdev.flatlaf.fonts.jetbrains_mono.FlatJetBrainsMonoFont;
import org.fife.ui.autocomplete.*;
import org.fife.ui.rsyntaxtextarea.*;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;


public class EditorView extends RSyntaxTextArea implements ActionListener {

    private final App app;
    private final RTextScrollPane editorScrollPane;

    public EditorView(App app) {
        super();
        this.app = app;

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

        this.setCurrentLineHighlightColor((new Color(30, 30, 30))); // Line highlight
        this.setCaretColor(Color.WHITE);

        editorScrollPane = new RTextScrollPane(this);

    }

    public JScrollPane getContentPanel() {
        return editorScrollPane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

}
