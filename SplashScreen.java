import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JFrame {

    private JProgressBar progressBar;
    private JLabel loadingLabel;

    public SplashScreen() {
        setTitle("Smart Parking System");
        setSize(700, 420);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(20, 44, 57));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("SMART PARKING SYSTEM", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));

        JLabel tagline = new JLabel("Manage parking, revenue and records with confidence", SwingConstants.CENTER);
        tagline.setForeground(new Color(190, 209, 236));
        tagline.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JLabel icon = new JLabel("🚗", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));

        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setOpaque(false);
        centerPanel.add(icon, BorderLayout.NORTH);
        centerPanel.add(title, BorderLayout.CENTER);
        centerPanel.add(tagline, BorderLayout.SOUTH);

        loadingLabel = new JLabel("Getting everything ready...", SwingConstants.CENTER);
        loadingLabel.setForeground(new Color(216, 228, 251));
        loadingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(24, 128, 144));
        progressBar.setBackground(new Color(221, 232, 250));

        JPanel bottomPanel = new JPanel(new BorderLayout(0, 14));
        bottomPanel.setOpaque(false);
        bottomPanel.add(loadingLabel, BorderLayout.NORTH);
        bottomPanel.add(progressBar, BorderLayout.SOUTH);

        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        add(panel);

        startLoading();
    }

    private void startLoading() {
        Timer timer = new Timer(30, null);
        timer.addActionListener(e -> {
            int value = progressBar.getValue();
            progressBar.setValue(value + 1);
            loadingLabel.setText("Loading " + (value + 1) + "%");

            if (value >= 100) {
                timer.stop();
                dispose();
                new LoginFrame().setVisible(true);
            }
        });
        timer.start();
    }
}
