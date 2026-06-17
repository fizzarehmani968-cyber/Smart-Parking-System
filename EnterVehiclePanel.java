import javax.swing.*;
import java.awt.*;

public class EnterVehiclePanel extends JPanel {

    private JTextField vehicleField;
    private JTextField ownerField;
    private JComboBox<String> typeBox;

    private ParkingManager manager = ParkingManager.getInstance();

    public EnterVehiclePanel() {
        setLayout(new BorderLayout(0, 20));
        setBackground(new Color(245, 247, 252));
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        // Header Section
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Register Vehicle Entry");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(new Color(24, 66, 82));

        JLabel subtitle = new JLabel("Quickly add a new vehicle and assign the next available slot.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitle.setForeground(new Color(95, 110, 135));

        header.add(title, BorderLayout.NORTH);
        header.add(Box.createVerticalStrut(8), BorderLayout.CENTER);
        
        JPanel subtitlePanel = new JPanel();
        subtitlePanel.setOpaque(false);
        subtitlePanel.add(subtitle);
        header.add(subtitlePanel, BorderLayout.SOUTH);

        add(header, BorderLayout.NORTH);

        // Form Section
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 235), 1),
                BorderFactory.createEmptyBorder(24, 24, 24, 24)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 0, 12, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Vehicle Number Field
        JLabel vehicleLabel = new JLabel("Vehicle Number");
        vehicleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        vehicleLabel.setForeground(new Color(84, 102, 126));
        formPanel.add(vehicleLabel, gbc);

        gbc.gridy++;
        vehicleField = new JTextField();
        vehicleField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        vehicleField.setPreferredSize(new Dimension(350, 36));
        vehicleField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        formPanel.add(vehicleField, gbc);

        // Owner Name Field
        gbc.gridy++;
        JLabel ownerLabel = new JLabel("Owner Name");
        ownerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        ownerLabel.setForeground(new Color(84, 102, 126));
        formPanel.add(ownerLabel, gbc);

        gbc.gridy++;
        ownerField = new JTextField();
        ownerField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        ownerField.setPreferredSize(new Dimension(350, 36));
        ownerField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        formPanel.add(ownerField, gbc);

        // Vehicle Type Field
        gbc.gridy++;
        JLabel typeLabel = new JLabel("Vehicle Type");
        typeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        typeLabel.setForeground(new Color(84, 102, 126));
        formPanel.add(typeLabel, gbc);

        gbc.gridy++;
        typeBox = new JComboBox<>(new String[]{"Car", "Bike", "Van"});
        typeBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        typeBox.setBackground(Color.WHITE);
        typeBox.setPreferredSize(new Dimension(350, 36));
        typeBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        formPanel.add(typeBox, gbc);

        // Park Button
        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 0, 0);
        JButton parkBtn = new JButton("Park Vehicle");
        parkBtn.setBackground(new Color(230, 230, 230));
        parkBtn.setForeground(Color.BLACK);
        parkBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        parkBtn.setFocusPainted(false);
        parkBtn.setPreferredSize(new Dimension(350, 44));
        parkBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        parkBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(30, 40, 55), 2),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.WHITE, 2),
                        BorderFactory.createEmptyBorder(10, 15, 10, 15))));
        formPanel.add(parkBtn, gbc);

        add(formPanel, BorderLayout.CENTER);

        parkBtn.addActionListener(e -> parkVehicle());
    }

    private void parkVehicle() {
        String vehicle = vehicleField.getText().trim();
        String owner = ownerField.getText().trim();
        String type = (String) typeBox.getSelectedItem();

        if (vehicle.isEmpty() || owner.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill all fields",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Process entry transaction and prompt completion notification
        String result = manager.parkVehicle(vehicle, owner, type);
        JOptionPane.showMessageDialog(this, result, "Registration Success", JOptionPane.INFORMATION_MESSAGE);
        
        // Reset text boxes safely
        vehicleField.setText("");
        ownerField.setText("");
        
        // Auto-refresh main dashboard statistics counters if active
        Container topFrame = SwingUtilities.getAncestorOfClass(DashboardFrame.class, this);
        if (topFrame instanceof DashboardFrame) {
            ((DashboardFrame) topFrame).refreshDashboard();
        }
    }
}
