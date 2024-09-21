import com.formdev.flatlaf.fonts.jetbrains_mono.FlatJetBrainsMonoFont;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class WelcomeView extends JPanel implements ComponentListener {

    private final JLabel titleLabel;
    private final JLabel mottoLabel;
    private final JButton openProjectButton;

    int titleWidth = 400, titleHeight = 200, mottoWidth = 400, mottoHeight = 100;

    public WelcomeView(App app) {

        this.addComponentListener(this);
        this.setBounds(0, 0, app.getWidth(), app.getHeight());
        this.setLayout(null);

        titleLabel = new JLabel("{CodeLite}");
        titleLabel.setFont(new Font(FlatJetBrainsMonoFont.FAMILY, Font.BOLD, 52));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        mottoLabel = new JLabel("Code editing simplified");
        mottoLabel.setFont(new Font(FlatJetBrainsMonoFont.FAMILY, Font.PLAIN, 24));
        mottoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        openProjectButton = new JButton("Open Project");
        openProjectButton.setFont(new Font(FlatJetBrainsMonoFont.FAMILY, Font.PLAIN, 18));
        openProjectButton.setBackground(new Color(12, 100, 181));
        openProjectButton.addActionListener(e -> {
            app.projectView.openProject();
            app.launch();
        });

        this.add(titleLabel);
        this.add(mottoLabel);
        this.add(openProjectButton);
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;

        GradientPaint gradient = new GradientPaint(0, 0, new Color(25, 25, 25), getWidth(), getHeight(), new Color(30, 30, 30));
        g2D.setPaint(gradient);
        g2D.fillRect(0, 0, getWidth(), getHeight());
    }

    @Override
    public void componentResized(ComponentEvent e) {
        if (titleLabel != null && mottoLabel!=null && openProjectButton!= null) {
            titleLabel.setBounds(getWidth() / 2 - titleWidth / 2,
                    (getHeight() / 2 - titleHeight / 2) - 50,
                       titleWidth, titleHeight);
            mottoLabel.setBounds(getWidth() / 2 - mottoWidth / 2,
                    titleLabel.getY() + titleHeight / 2,
                       mottoWidth, mottoHeight);
            openProjectButton.setBounds(getWidth() / 2 - titleWidth / 4,
                    mottoLabel.getY() + mottoHeight,
                    titleWidth / 2, mottoHeight / 2);
        }
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
}
