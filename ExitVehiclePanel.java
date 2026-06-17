import javax.swing.*;
import java.awt.*;

public class ExitVehiclePanel extends JPanel {

    private JTextField vehicleField;
    private JTextArea resultArea;

    private ParkingManager manager = ParkingManager.getInstance();

    public ExitVehiclePanel() {
        setLayout(new BorderLayout(0, 20));
        setBackground(new Color(245, 247, 252));
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("Process Vehicle Exit");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(26, 48, 89));

        JLabel subtitle = new JLabel("Enter the vehicle number to calculate the fee and release the slot.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitle.setForeground(new Color(96, 112, 139));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(title, BorderLayout.NORTH);
        header.add(subtitle, BorderLayout.SOUTH);

        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);
        content.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(221, 227, 236), 1),
                BorderFactory.createEmptyBorder(22, 22, 22, 22)));
        content.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(14, 14, 14, 14);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel vehicleLabel = new JLabel("Vehicle Number");
        vehicleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        content.add(vehicleLabel, gbc);

        gbc.gridy++;
        vehicleField = new JTextField();
        vehicleField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        vehicleField.setPreferredSize(new Dimension(360, 36));
        content.add(vehicleField, gbc);

        gbc.gridy++;
        JButton exitBtn = new JButton("Exit Vehicle");
        exitBtn.setBackground(new Color(230, 230, 230));
        exitBtn.setForeground(new Color(15, 43, 70));
        exitBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        exitBtn.setFocusPainted(false);
        exitBtn.setPreferredSize(new Dimension(360, 44));
        content.add(exitBtn, gbc);

        resultArea = new JTextArea(8, 1);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setBackground(new Color(249, 250, 252));

        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(221, 227, 236), 1),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));

        add(header, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        exitBtn.addActionListener(e -> exitVehicle());
    }

    private void exitVehicle() {
        String vehicle = vehicleField.getText().trim();
        if (vehicle.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Enter vehicle number",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String result = manager.exitVehicle(vehicle);
        resultArea.append(result + "\n");
        vehicleField.setText("");
    }
}
