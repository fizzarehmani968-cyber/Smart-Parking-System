import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Properties;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox showPassword;
    private JButton loginButton;
    private JButton registerButton;
    //private JToggleButton fullPageToggle;
    private java.util.Map<String, String> accounts = new java.util.HashMap<>();
    private final Properties props = new Properties();
    private final File accountsFile = new File(System.getProperty("user.home"), "smart_parking_accounts.properties");

    public LoginFrame() {
        setTitle("Smart Parking System");
        setSize(1080, 700);
        setMinimumSize(new Dimension(1080, 700));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        accounts.put("admin", "admin");
        loadAccounts();
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(new Color(240, 244, 250));
        setContentPane(content);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));

        JLabel headerTitle = new JLabel("Smart Parking Login");
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerTitle.setForeground(new Color(27, 70, 99));


        headerPanel.add(headerTitle, BorderLayout.WEST);
        //headerPanel.add(fullPageToggle, BorderLayout.EAST);
        content.add(headerPanel, BorderLayout.NORTH);

        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, new Color(16, 75, 92), 0, getHeight(), new Color(22, 42, 55));
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        leftPanel.setPreferredSize(new Dimension(420, 0));
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createEmptyBorder(40, 30, 40, 30));

        JLabel brandIcon = new JLabel("🚗", SwingConstants.CENTER);
        brandIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 72));
        brandIcon.setForeground(Color.WHITE);

        JLabel brandTitle = new JLabel("<html><center>SMART PARKING<br>MANAGEMENT</center></html>", SwingConstants.CENTER);
        brandTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        brandTitle.setForeground(Color.WHITE);

        JLabel brandDesc = new JLabel("<html><center>Secure entry. Faster exits.<br>Smarter vehicle and revenue control.</center></html>", SwingConstants.CENTER);
        brandDesc.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        brandDesc.setForeground(new Color(209, 224, 247));

        JPanel brandText = new JPanel(new BorderLayout(0, 18));
        brandText.setOpaque(false);
        brandText.add(brandTitle, BorderLayout.NORTH);
        brandText.add(brandDesc, BorderLayout.CENTER);

        JPanel featurePanel = new JPanel(new GridLayout(3, 1, 0, 14));
        featurePanel.setOpaque(false);
        featurePanel.add(createFeatureLabel("Fast login", "Quick access with a modern interface"));
        featurePanel.add(createFeatureLabel("Create account", "Register in seconds and start managing"));
        featurePanel.add(createFeatureLabel("Detailed reports", "Review parking activity and revenue"));

        leftPanel.add(brandIcon, BorderLayout.NORTH);
        leftPanel.add(brandText, BorderLayout.CENTER);
        leftPanel.add(featurePanel, BorderLayout.SOUTH);

        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                BorderFactory.createEmptyBorder(32, 32, 32, 32)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        JLabel loginTitle = new JLabel("Welcome Back");
        loginTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        loginTitle.setForeground(new Color(24, 72, 87));
        rightPanel.add(loginTitle, gbc);

        gbc.gridy++;
        JLabel loginSubtitle = new JLabel("Sign in to your Smart Parking dashboard");
        loginSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loginSubtitle.setForeground(new Color(102, 120, 148));
        rightPanel.add(loginSubtitle, gbc);

        // spacer row (no remembered-user shown)
        gbc.gridy++;
        rightPanel.add(Box.createVerticalStrut(6), gbc);

        gbc.gridy++;
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(new Color(70, 90, 115));
        rightPanel.add(userLabel, gbc);

        gbc.gridy++;
        usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setBackground(new Color(245, 249, 253));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(214, 222, 231)),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        usernameField.setPreferredSize(new Dimension(360, 44));
        rightPanel.add(usernameField, gbc);

        gbc.gridy++;
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passLabel.setForeground(new Color(70, 90, 115));
        rightPanel.add(passLabel, gbc);

        gbc.gridy++;
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBackground(new Color(245, 249, 253));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(214, 222, 231)),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        passwordField.setPreferredSize(new Dimension(360, 44));
        rightPanel.add(passwordField, gbc);

        gbc.gridy++;
        showPassword = new JCheckBox("Show password");
        showPassword.setBackground(Color.WHITE);
        showPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        showPassword.setForeground(new Color(103, 123, 147));
        rightPanel.add(showPassword, gbc);

        showPassword.addActionListener(e -> passwordField.setEchoChar(showPassword.isSelected() ? (char) 0 : '*'));

        gbc.gridy++;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 14, 0));
        buttonPanel.setOpaque(false);

        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(19, 88, 122));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setFocusPainted(false);
        loginButton.setOpaque(true);
        loginButton.setBorderPainted(false);
        loginButton.setPreferredSize(new Dimension(170, 46));

        registerButton = new JButton("Create Account");
        registerButton.setBackground(new Color(245, 250, 255));
        registerButton.setForeground(new Color(22, 85, 102));
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerButton.setFocusPainted(false);
        registerButton.setOpaque(true);
        registerButton.setBorder(BorderFactory.createLineBorder(new Color(22, 85, 102), 1));
        registerButton.setPreferredSize(new Dimension(170, 46));

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        rightPanel.add(buttonPanel, gbc);

        gbc.gridy++;
        JLabel helpLabel = new JLabel("New here? Create an account to begin managing parking.");
        helpLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        helpLabel.setForeground(new Color(109, 129, 152));
        rightPanel.add(helpLabel, gbc);

        content.add(leftPanel, BorderLayout.WEST);
        content.add(rightPanel, BorderLayout.CENTER);

        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> showCreateAccountDialog());
        // no visible remembered-user label (accounts saved silently)
    }

    private JLabel createFeatureLabel(String heading, String description) {
        JLabel label = new JLabel("<html><b>" + heading + "</b><br><span style='font-size:11px;color:#d9e3f6;'>" + description + "</span></html>");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(Color.WHITE);
        return label;
    }

    private void showCreateAccountDialog() {
        JDialog dialog = new JDialog(this, "Create Account", true);
        dialog.setSize(420, 420);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Create Your Account");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setBounds(30, 20, 340, 30);

        JLabel nameLabel = new JLabel("Username");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        nameLabel.setForeground(new Color(69, 88, 119));
        nameLabel.setBounds(30, 70, 340, 22);

        JTextField nameField = new JTextField();
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameField.setBounds(30, 95, 340, 42);
        nameField.setBackground(new Color(245, 249, 253));
        nameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(214, 222, 231)),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        passwordLabel.setForeground(new Color(69, 88, 119));
        passwordLabel.setBounds(30, 155, 340, 22);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBounds(30, 180, 340, 42);
        passwordField.setBackground(new Color(245, 249, 253));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(214, 222, 231)),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));

        JLabel confirmLabel = new JLabel("Confirm Password");
        confirmLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        confirmLabel.setForeground(new Color(69, 88, 119));
        confirmLabel.setBounds(30, 235, 340, 22);

        JPasswordField confirmField = new JPasswordField();
        confirmField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        confirmField.setBounds(30, 260, 340, 42);
        confirmField.setBackground(new Color(245, 249, 253));
        confirmField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(214, 222, 231)),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));

        JButton createButton = new JButton("Create Account");
        createButton.setBackground(new Color(24, 104, 130));
        createButton.setForeground(Color.WHITE);
        createButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        createButton.setBounds(30, 325, 340, 45);
        createButton.setFocusPainted(false);
        createButton.setOpaque(true);
        createButton.setBorderPainted(false);

        createButton.addActionListener(e -> {
            String username = nameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String confirm = new String(confirmField.getPassword()).trim();
            String key = username.toLowerCase();

            if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please complete all fields.", "Missing Data", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!password.equals(confirm)) {
                JOptionPane.showMessageDialog(dialog, "Passwords do not match.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (accounts.containsKey(key)) {
                JOptionPane.showMessageDialog(dialog, "You already have an account. Please login.", "Duplicate User", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // store hashed password
            try {
                byte[] salt = generateSalt();
                String hash = hashPassword(password.toCharArray(), salt);
                String stored = Base64.getEncoder().encodeToString(salt) + ":" + hash;
                accounts.put(key, stored);
                props.setProperty(key, stored);
                saveAccounts();
            } catch (Exception ex) {
                // fallback to plain storage if hashing fails
                accounts.put(key, password);
                props.setProperty(key, password);
                saveAccounts();
            }
            JOptionPane.showMessageDialog(dialog, "Account created successfully! You can now login.", "Success", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });

        panel.add(title);
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(confirmLabel);
        panel.add(confirmField);
        panel.add(createButton);

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    private void handleLogin() {
    String username = usernameField.getText().trim();
    String password = new String(passwordField.getPassword()).trim();
    String key = username.toLowerCase();

    if (username.isEmpty() || password.isEmpty()) {
        JOptionPane.showMessageDialog(this,
                "Please fill all fields",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        return;
    }

    String storedPassword = accounts.get(key);

    boolean ok = false;
    if (storedPassword != null) {
        // stored format: base64(salt):hash OR legacy plain
        if (storedPassword.contains(":")) {
            try {
                String[] parts = storedPassword.split(":", 2);
                byte[] salt = Base64.getDecoder().decode(parts[0]);
                String expectedHash = parts[1];
                String gotHash = hashPassword(password.toCharArray(), salt);
                ok = expectedHash.equals(gotHash);
            } catch (Exception ex) {
                ok = false;
            }
        } else {
            // legacy plaintext
            ok = storedPassword.equals(password);
            if (ok) {
                // migrate to hashed
                try {
                    byte[] salt = generateSalt();
                    String hash = hashPassword(password.toCharArray(), salt);
                    String stored = Base64.getEncoder().encodeToString(salt) + ":" + hash;
                    accounts.put(key, stored);
                    props.setProperty(key, stored);
                    saveAccounts();
                } catch (Exception ignore) {}
            }
        }
    }

    if (ok) {
        dispose();
        new DashboardFrame(username).setVisible(true);
    } else {
        JOptionPane.showMessageDialog(this,
                "Invalid credentials",
                "Login Failed",
                JOptionPane.ERROR_MESSAGE);
    }
}
       

    private void loadAccounts() {
        if (accountsFile.exists()) {
            try (FileInputStream in = new FileInputStream(accountsFile)) {
                props.load(in);
                for (String name : props.stringPropertyNames()) {
                    if (name.startsWith("__")) continue;
                    accounts.put(name, props.getProperty(name));
                }
            } catch (IOException e) {
                // ignore, we'll continue with default account
            }
        } else {
            // persist the default admin
            try {
                byte[] salt = generateSalt();
                String hash = hashPassword("admin".toCharArray(), salt);
                props.setProperty("admin", Base64.getEncoder().encodeToString(salt) + ":" + hash);
            } catch (Exception e) {
                props.setProperty("admin", "admin");
            }
            saveAccounts();
        }
    }

    private void saveAccounts() {
        try (FileOutputStream out = new FileOutputStream(accountsFile)) {
            for (java.util.Map.Entry<String, String> e : accounts.entrySet()) {
                props.setProperty(e.getKey(), e.getValue());
            }
            props.store(out, "Smart Parking accounts (username=password)");
        } catch (IOException e) {
            // could log
        }
    }

    // --------- Password hashing helpers (PBKDF2) ---------
    private static byte[] generateSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    private static String hashPassword(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 65536;
        int keyLength = 256;
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(hash);
    }

   
}
