import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private final ParkingManager manager = ParkingManager.getInstance();
    private String currentUser;
    private DashboardPanel dashboardPanel;
    private JButton activeMenuButton;

    // Called by Enter/Exit panels after each transaction to keep stats live
    public void refreshDashboard() {
        if (dashboardPanel != null) {
            dashboardPanel.refreshStats();
        }
    }

    public DashboardFrame(String username) {
        this.currentUser = username;
        setTitle("Smart Parking System");
        setSize(1260, 760);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- TOP BAR SETUP ---
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(24, 77, 91));
        topBar.setPreferredSize(new Dimension(0, 72));
        topBar.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 24));

        JLabel title = new JLabel("SMART PARKING SYSTEM");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI Semibold", Font.BOLD, 22));

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12)); 
        userPanel.setOpaque(false);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setContentAreaFilled(false);
        btnLogout.setOpaque(true);
        btnLogout.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogout.setHorizontalAlignment(SwingConstants.LEFT); 
        btnLogout.setBackground(new Color(230, 230, 230)); 
        btnLogout.setForeground(new Color(15, 43, 70)); // Match the new deep navy style             
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 16)); 

        btnLogout.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(30, 40, 55), 2), 
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.WHITE, 2),    
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)   
                )
        ));

        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        userPanel.add(btnLogout);

        topBar.add(title, BorderLayout.WEST);
        topBar.add(userPanel, BorderLayout.EAST);

        // --- SIDEBAR SETUP ---
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(31, 41, 55));
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(24, 16, 24, 16));

        JLabel navHeader = new JLabel("MENU");
        navHeader.setForeground(new Color(192, 211, 255));
        navHeader.setFont(new Font("Segoe UI", Font.BOLD, 20));
        navHeader.setHorizontalAlignment(SwingConstants.CENTER);
        navHeader.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(navHeader);
        sidebar.add(Box.createVerticalStrut(16));

        JButton btnDashboard = createMenuButton("Dashboard");
        JButton btnEnter = createMenuButton("Enter Vehicle");
        JButton btnExit = createMenuButton("Exit Vehicle");
        JButton btnRecords = createMenuButton("Records");
        JButton btnReports = createMenuButton("Reports");
        JButton btnSettings = createMenuButton("Settings");

        sidebar.add(btnDashboard);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnEnter);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnExit);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnRecords);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnReports);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnSettings);
        sidebar.add(Box.createVerticalGlue());

        // --- PANEL CARDS SETUP ---
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(new Color(245, 247, 252));
        
        dashboardPanel = new DashboardPanel(currentUser);
        EnterVehiclePanel enterPanel = new EnterVehiclePanel();
        ExitVehiclePanel exitPanel = new ExitVehiclePanel();
        RecordsPanel recordsPanel = new RecordsPanel();
        JPanel reportsPanel = createReportsPanel();
        JPanel settingsPanel = createPlaceholderPanel("Settings coming soon.");

        mainPanel.add(dashboardPanel, "dashboard");
        mainPanel.add(enterPanel, "enter");
        mainPanel.add(exitPanel, "exit");
        mainPanel.add(recordsPanel, "records");
        mainPanel.add(reportsPanel, "reports");
        mainPanel.add(settingsPanel, "settings");

        // --- BUTTON ACTION LISTENERS ---
        btnDashboard.addActionListener(e -> { dashboardPanel.refreshStats(); setActiveMenu(btnDashboard); cardLayout.show(mainPanel, "dashboard"); });
        btnEnter.addActionListener(e -> { setActiveMenu(btnEnter); cardLayout.show(mainPanel, "enter"); });
        btnExit.addActionListener(e -> { setActiveMenu(btnExit); cardLayout.show(mainPanel, "exit"); });
        btnRecords.addActionListener(e -> { recordsPanel.refreshData(); setActiveMenu(btnRecords); cardLayout.show(mainPanel, "records"); });
        btnReports.addActionListener(e -> { setActiveMenu(btnReports); cardLayout.show(mainPanel, "reports"); });
        btnSettings.addActionListener(e -> { setActiveMenu(btnSettings); cardLayout.show(mainPanel, "settings"); });

        // --- APPLICATION VIEW ASSEMBLY ---
        add(topBar, BorderLayout.NORTH);
        add(sidebar, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);

        // Highlight Dashboard on System Launch
        setActiveMenu(btnDashboard);
        cardLayout.show(mainPanel, "dashboard");
    } 

    // --- HELPER METHOD TO GENERATE FLAT BUTTON BASE STYLE ---
    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBackground(new Color(230, 230, 230));
        button.setForeground(new Color(15, 43, 70)); // Text changed to your exact deep navy blue heading tone
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setMaximumSize(new Dimension(210, 45)); 
        
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(30, 40, 55), 2),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.WHITE, 2),
                        BorderFactory.createEmptyBorder(10, 15, 10, 15))));
        
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // --- MENUS ENGINE HOOK ---
    private void setActiveMenu(JButton btn) {
        if (activeMenuButton != null) {
            // Restore styling for the previously active button when deselected
            activeMenuButton.setContentAreaFilled(false);
            activeMenuButton.setOpaque(true);
            activeMenuButton.setBackground(new Color(230, 230, 230));
            activeMenuButton.setForeground(new Color(15, 43, 70)); // Text restored to matching deep navy blue
            activeMenuButton.setMaximumSize(new Dimension(210, 45)); 
            activeMenuButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(30, 40, 55), 2), 
                    BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(Color.WHITE, 2),
                            BorderFactory.createEmptyBorder(10, 15, 10, 15))));
        }
        
        activeMenuButton = btn;
        
        if (btn != null) {
            // Apply blue highlighted theme for the currently active button selection
            btn.setContentAreaFilled(false);
            btn.setOpaque(true);
            btn.setBackground(new Color(230, 230, 230)); // Bright blue highlight for active selection
            btn.setForeground(new Color(15, 43,70));// White text contrast remains on dark background selection
            btn.setMaximumSize(new Dimension(210, 45)); 
            btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(30, 40, 55), 2),
                    BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(Color.WHITE, 2),
                            BorderFactory.createEmptyBorder(10, 15, 10, 15))));
        }
    }

    // --- PLACEHOLDER RENDER HOOKS ---
    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(new Color(245, 247, 252));
        panel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("Reports & Insights");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(new Color(24, 66, 82));

        JLabel description = new JLabel("Current parking metrics and revenue overview.");
        description.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        description.setForeground(new Color(95, 110, 135));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(title, BorderLayout.NORTH);
        header.add(description, BorderLayout.SOUTH);

        JPanel stats = new JPanel(new GridLayout(2, 2, 18, 18));
        stats.setOpaque(false);
        stats.add(createReportCard("Total Slots", String.valueOf(manager.getTotalSlots()), new Color(21, 136, 159)));
        stats.add(createReportCard("Occupied Slots", String.valueOf(manager.getOccupiedSlots()), new Color(24, 128, 144)));
        stats.add(createReportCard("Available Slots", String.valueOf(manager.getAvailableSlots()), new Color(234, 88, 12)));
        stats.add(createReportCard("Total Revenue", "Rs. " + String.format("%.2f", manager.getRevenue()), new Color(168, 85, 247)));

        panel.add(header, BorderLayout.NORTH);
        panel.add(stats, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createReportCard(String titleText, String valueText, Color accent) {
        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(221, 227, 237), 1),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)));

        JLabel title = new JLabel(titleText);
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(new Color(84, 102, 126));

        JLabel value = new JLabel(valueText, SwingConstants.CENTER);
        value.setFont(new Font("Segoe UI", Font.BOLD, 28));
        value.setForeground(accent);

        card.add(title, BorderLayout.NORTH);
        card.add(value, BorderLayout.CENTER);
        return card;
    }

    private JPanel createPlaceholderPanel(String msg) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 247, 252));
        
        JLabel label = new JLabel(msg);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        label.setForeground(new Color(92, 108, 132));
        
        panel.add(label);
        return panel;
    }
}
