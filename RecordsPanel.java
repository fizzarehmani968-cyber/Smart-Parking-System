import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class RecordsPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;

    public RecordsPanel() {

        setLayout(new BorderLayout(0, 12));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel title = new JLabel("Parking Records");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(24, 66, 82));
        headerPanel.add(title, BorderLayout.WEST);

        add(headerPanel, BorderLayout.NORTH);

        // Search and buttons panel
        JPanel topPanel = new JPanel(new BorderLayout(12, 0));
        topPanel.setOpaque(false);

        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        JButton refresh = new JButton("Refresh");
        JButton export = new JButton("Export CSV");
        
        refresh.setFont(new Font("Segoe UI", Font.BOLD, 12));
        export.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refresh.setPreferredSize(new Dimension(100, 36));
        export.setPreferredSize(new Dimension(120, 36));

        JPanel rightBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightBox.setOpaque(false);
        rightBox.add(export);
        rightBox.add(refresh);

        topPanel.add(searchField, BorderLayout.CENTER);
        topPanel.add(rightBox, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        String[] columns = {
                "Vehicle",
                "Owner",
                "Type",
                "Slot",
                "Entry",
                "Exit",
                "Fee",
                "Status"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(237, 242, 247));
        table.getTableHeader().setForeground(new Color(24, 66, 82));
        table.setSelectionBackground(new Color(24, 128, 144));
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(220, 225, 235));
        table.setShowGrid(true);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 235), 1));
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        add(scroll, BorderLayout.CENTER);

        refresh.addActionListener(e->loadData());
        export.addActionListener(e -> {
            java.util.List<ParkingRecord> records = ParkingManager.getInstance().getRecords();
            if (records.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No records to export.", "Export CSV", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Save CSV");
            int result = chooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                java.io.File file = chooser.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith(".csv")) {
                    file = new java.io.File(file.getParentFile(), file.getName() + ".csv");
                }
                try (java.io.PrintWriter out = new java.io.PrintWriter(new java.io.FileWriter(file))) {
                    out.println("Vehicle,Owner,Type,Slot,Entry,Exit,Fee,Status");
                    java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    for (ParkingRecord r : records) {
                        String entry = r.getEntryTime() != null ? r.getEntryTime().format(fmt) : "";
                        String exit = r.getExitTime() != null ? r.getExitTime().format(fmt) : "";
                        out.printf("%s,%s,%s,%s,%s,%s,%.2f,%s\n",
                                escapeCsv(r.getVehicleNumber()),
                                escapeCsv(r.getOwnerName()),
                                escapeCsv(r.getVehicleType()),
                                escapeCsv(r.getSlot()),
                                entry,
                                exit,
                                r.getFee(),
                                escapeCsv(r.getStatus())
                        );
                    }
                    JOptionPane.showMessageDialog(this, "CSV exported to: " + file.getAbsolutePath(), "Export CSV", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Failed to export CSV: " + ex.getMessage(), "Export CSV", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {

            public void insertUpdate(javax.swing.event.DocumentEvent e){
                filter();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e){
                filter();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e){
                filter();
            }

        });

        loadData();
    }

    public void refreshData() {
        loadData();
    }

    private void filter(){

        TableRowSorter<DefaultTableModel> sorter =
                new TableRowSorter<>(model);

        table.setRowSorter(sorter);

        String text = searchField.getText();

        if(text.trim().isEmpty()){

            sorter.setRowFilter(null);

        }else{

            sorter.setRowFilter(RowFilter.regexFilter("(?i)"+text));

        }

    }

    private void loadData() {
        model.setRowCount(0);
        
        List<ParkingRecord> records = ParkingManager.getInstance().getRecords();
        
        java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        
        for (ParkingRecord r : records) {
            String entryTime = r.getEntryTime() != null ? r.getEntryTime().format(fmt) : "";
            String exitTime = r.getExitTime() != null ? r.getExitTime().format(fmt) : "";
            
            model.addRow(new Object[]{
                    r.getVehicleNumber(),
                    r.getOwnerName(),
                    r.getVehicleType(),
                    r.getSlot(),
                    entryTime,
                    exitTime,
                    String.format("Rs. %.2f", r.getFee()),
                    r.getStatus()
            });
        }
    }

    private static String escapeCsv(String s) {
        if (s == null) return "";
        if (s.contains(",") || s.contains("\"") || s.contains("\n")) {
            return "\"" + s.replace("\"", "\"\"") + "\"";
        }
        return s;
    }

}