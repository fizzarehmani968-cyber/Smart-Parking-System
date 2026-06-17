import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {

    private JLabel totalLabel;
    private JLabel occupiedLabel;
    private JLabel availableLabel;
    private JLabel revenueLabel;
    private ParkingLotDisplay parkingLotDisplay;

    private final ParkingManager manager = ParkingManager.getInstance();

    public DashboardPanel(String username) {

        setLayout(new BorderLayout(0, 20));
        setBackground(new Color(245, 247, 252));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Header
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        JLabel title = new JLabel("Welcome Back, " + username);
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(new Color(24, 66, 82));

        JLabel subtitle = new JLabel("Review parking performance and occupancy at a glance.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitle.setForeground(new Color(95, 110, 135));

        header.add(title);
        header.add(Box.createVerticalStrut(5));
        header.add(subtitle);

        add(header, BorderLayout.NORTH);

        // Main content with stats and parking lot
        JPanel mainContent = new JPanel(new GridLayout(1, 2, 20, 20));
        mainContent.setOpaque(false);

        // Left side - Stats Panel
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        statsPanel.setOpaque(false);

        totalLabel = createValueLabel();
        occupiedLabel = createValueLabel();
        availableLabel = createValueLabel();
        revenueLabel = createValueLabel();

        statsPanel.add(createStatCard("Total Slots", totalLabel, new Color(21, 136, 159)));
        statsPanel.add(createStatCard("Occupied Slots", occupiedLabel, new Color(16, 185, 129)));
        statsPanel.add(createStatCard("Available Slots", availableLabel, new Color(234, 88, 12)));
        statsPanel.add(createStatCard("Total Revenue", revenueLabel, new Color(168, 85, 247)));

        // Right side - Parking Lot Display
        parkingLotDisplay = new ParkingLotDisplay(manager);
        JScrollPane parkingScroll = new JScrollPane(parkingLotDisplay,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        parkingScroll.setBorder(null);
        parkingScroll.getVerticalScrollBar().setUnitIncrement(16);
        parkingScroll.getHorizontalScrollBar().setUnitIncrement(16);

        JPanel parkingPanel = new JPanel(new BorderLayout());
        parkingPanel.setBackground(Color.WHITE);
        parkingPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 235), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JLabel parkingTitle = new JLabel("Parking Lot Status");
        parkingTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        parkingTitle.setForeground(new Color(84, 102, 126));

        parkingPanel.add(parkingTitle, BorderLayout.NORTH);
        parkingPanel.add(parkingScroll, BorderLayout.CENTER);

        mainContent.add(statsPanel);
        mainContent.add(parkingPanel);

        add(mainContent, BorderLayout.CENTER);

        refreshStats();
    }

    private JLabel createValueLabel() {

        JLabel label = new JLabel("0");
        label.setFont(new Font("Segoe UI", Font.BOLD, 34));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        return label;
    }

    private JPanel createStatCard(String title,
                                  JLabel valueLabel,
                                  Color accent) {

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(220, 225, 235), 1),
                BorderFactory.createEmptyBorder(
                        20, 20, 20, 20)));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titleLabel.setForeground(new Color(84, 102, 126));

        JSeparator separator = new JSeparator();

        valueLabel.setForeground(accent);

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(separator);
        card.add(Box.createVerticalGlue());
        card.add(Box.createVerticalStrut(15));
        card.add(valueLabel);

        return card;
    }

    public void refreshStats() {

        totalLabel.setText(
                String.valueOf(manager.getTotalSlots()));

        occupiedLabel.setText(
                String.valueOf(manager.getOccupiedSlots()));

        availableLabel.setText(
                String.valueOf(manager.getAvailableSlots()));

        revenueLabel.setText(
                "Rs. " +
                String.format("%.2f",
                        manager.getRevenue()));
        
        if (parkingLotDisplay != null) {
            parkingLotDisplay.refresh();
        }
        repaint();
    }

    // Inner class for parking lot visual display
    private static class ParkingLotDisplay extends JPanel {
        private final ParkingManager manager;
        private static final int SLOT_SIZE = 40;
        private static final int SLOT_GAP = 8;

        public ParkingLotDisplay(ParkingManager manager) {
            this.manager = manager;
            setBackground(new Color(249, 250, 252));
            int totalSlots = manager.getTotalSlots();
            int slotsPerRow = 5;
            int rows = (totalSlots + slotsPerRow - 1) / slotsPerRow;
            int height = rows * (SLOT_SIZE + SLOT_GAP) + 80;
            int width = slotsPerRow * (SLOT_SIZE + SLOT_GAP) + 40;
            setPreferredSize(new Dimension(width, height));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int totalSlots = manager.getTotalSlots();
            int slotsPerRow = 5;
            int rows = (totalSlots + slotsPerRow - 1) / slotsPerRow;

            int startX = (getWidth() - (slotsPerRow * (SLOT_SIZE + SLOT_GAP))) / 2;
            int startY = 15;

            for (int i = 1; i <= totalSlots; i++) {
                boolean isOccupied = manager.isSlotOccupied(i);
                int row = (i - 1) / slotsPerRow;
                int col = (i - 1) % slotsPerRow;

                int x = startX + col * (SLOT_SIZE + SLOT_GAP);
                int y = startY + row * (SLOT_SIZE + SLOT_GAP);

                // Draw slot background
                if (isOccupied) {
                    g2.setColor(new Color(239, 68, 68)); // Red for occupied
                } else {
                    g2.setColor(new Color(34, 197, 94)); // Green for available
                }
                g2.fillRoundRect(x, y, SLOT_SIZE, SLOT_SIZE, 6, 6);

                // Draw slot border
                g2.setColor(new Color(100, 116, 139));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(x, y, SLOT_SIZE, SLOT_SIZE, 6, 6);

                // Draw slot number
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
                String slotNum = String.valueOf(i);
                FontMetrics fm = g2.getFontMetrics();
                int textX = x + (SLOT_SIZE - fm.stringWidth(slotNum)) / 2;
                int textY = y + ((SLOT_SIZE - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(slotNum, textX, textY);
            }

            // Draw legend
            int legendY = startY + rows * (SLOT_SIZE + SLOT_GAP) + 15;
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            g2.setColor(new Color(100, 116, 139));

            // Green square
            g2.setColor(new Color(34, 197, 94));
            g2.fillRect(10, legendY, 12, 12);
            g2.setColor(new Color(100, 116, 139));
            g2.drawString("Available", 25, legendY + 10);

            // Red square
            g2.setColor(new Color(239, 68, 68));
            g2.fillRect(130, legendY, 12, 12);
            g2.setColor(new Color(100, 116, 139));
            g2.drawString("Occupied", 145, legendY + 10);
        }

        public void refresh() {
            repaint();
        }
    }
}