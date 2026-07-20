package admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;

public class StudentPanel extends JPanel {


    private static final String[] COLUMNS = {
            "Full Name", "Student ID", "Degree", "Email", "Mobile Number"
    };


    private DefaultTableModel tableModel;
    private JTable table;


    private JLabel rowCountLabel;


    public StudentPanel() {
        setLayout(new BorderLayout(0, 10));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(20, 20, 20, 20));


        buildTableModel();


        add(buildTopBar(),    BorderLayout.NORTH);
        add(buildTable(),     BorderLayout.CENTER);
        add(buildBottomBar(), BorderLayout.SOUTH);
    }

    private JPanel buildTopBar() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(245, 247, 250));

        // Page heading
        JLabel title = new JLabel("Students");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(new Color(35, 35, 40));
        top.add(title, BorderLayout.WEST);

        // Button panel (left side of the right area)
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btnPanel.setBackground(new Color(245, 247, 250));

        JButton btnAdd    = createButton("+ Add New", new Color(123, 17, 19), Color.WHITE);
        JButton btnEdit   = createButton("  Edit",    new Color(60, 60, 70),  Color.WHITE);
        JButton btnDelete = createButton("  Delete",  new Color(180, 40, 40), Color.WHITE);

        btnPanel.add(btnAdd);
        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);
        top.add(btnPanel, BorderLayout.EAST);

        // Wire button clicks
        btnAdd.addActionListener(e    -> openAddDialog());
        btnEdit.addActionListener(e   -> openEditDialog());
        btnDelete.addActionListener(e -> deleteSelected());

        return top;
    }

    private void buildTableModel() {

        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        // Sample data
        tableModel.addRow(new Object[]{"Kumar Sangakkara", "ET/2022/007", "Engineering Technology", "kumars@kln.ac.lk", "0123456789"});
        tableModel.addRow(new Object[]{"Nimal Perera", "ET/2022/012", "Information Technology", "nimalp@kln.ac.lk", "0771234567"});
        tableModel.addRow(new Object[]{"Amali Silva", "ET/2022/019", "Biosystems Technology", "amalis@kln.ac.lk", "0751122334"});

    }

    private JScrollPane buildTable() {
        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setGridColor(new Color(220, 220, 228));
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setSelectionBackground(new Color(248, 220, 220));
        table.setSelectionForeground(new Color(123, 17, 19));


        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(0, 38));
        header.setReorderingAllowed(false);
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setBackground(new Color(123, 17, 19));
                setForeground(Color.WHITE);
                setFont(new Font("Arial", Font.BOLD, 13));
                setBorder(new EmptyBorder(0, 12, 0, 12));
                setHorizontalAlignment(SwingConstants.LEFT);
                return this;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 228)));
        return scroll;
    }

    private JPanel buildBottomBar() {
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(new Color(245, 247, 250));
        bottom.setBorder(new EmptyBorder(8, 0, 0, 0));


        rowCountLabel = new JLabel("Showing " + tableModel.getRowCount() + " student(s)");
        rowCountLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        rowCountLabel.setForeground(new Color(120, 120, 130));
        bottom.add(rowCountLabel, BorderLayout.WEST);


        JButton btnSave = createButton("Save Changes", new Color(123, 17, 19), Color.WHITE);
        btnSave.setPreferredSize(new Dimension(160, 36));
        btnSave.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    this,
                    "Student records saved successfully!",
                    "Saved",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
        bottom.add(btnSave, BorderLayout.EAST);

        return bottom;
    }


    private void openAddDialog() {
        // Create a simple dialog
        JDialog dialog = new JDialog(getParentFrame(), "Add New Student", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // Form fields
        JTextField tfName   = new JTextField();
        JTextField tfID     = new JTextField();
        JTextField tfDegree = new JTextField();
        JTextField tfEmail  = new JTextField();
        JTextField tfMobile = new JTextField();

        // Form panel
        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));
        form.setBorder(new EmptyBorder(20, 20, 10, 20));
        form.add(new JLabel("Full Name:"));   form.add(tfName);
        form.add(new JLabel("Student ID:"));  form.add(tfID);
        form.add(new JLabel("Degree:"));      form.add(tfDegree);
        form.add(new JLabel("Email:"));       form.add(tfEmail);
        form.add(new JLabel("Mobile No:"));   form.add(tfMobile);
        dialog.add(form, BorderLayout.CENTER);

        // Button row
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnCancel = createButton("Cancel", new Color(150, 150, 155), Color.WHITE);
        JButton btnSave   = createButton("Save",   new Color(123, 17, 19),   Color.WHITE);

        btnCancel.addActionListener(e -> dialog.dispose());
        btnSave.addActionListener(e -> {
            // Validation
            if (tfName.getText().trim().isEmpty() || tfID.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                        "Full Name and Student ID are required!",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Add row to table
            tableModel.addRow(new Object[]{
                    tfName.getText().trim(),
                    tfID.getText().trim(),
                    tfDegree.getText().trim(),
                    tfEmail.getText().trim(),
                    tfMobile.getText().trim()
            });
            updateRowCount();
            dialog.dispose();
        });

        btnRow.add(btnCancel);
        btnRow.add(btnSave);
        dialog.add(btnRow, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void openEditDialog() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a student row first, then click Edit.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(getParentFrame(), "Edit Student", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // Pre-fill with existing values
        JTextField tfName   = new JTextField((String) tableModel.getValueAt(row, 0));
        JTextField tfID     = new JTextField((String) tableModel.getValueAt(row, 1));
        JTextField tfDegree = new JTextField((String) tableModel.getValueAt(row, 2));
        JTextField tfEmail  = new JTextField((String) tableModel.getValueAt(row, 3));
        JTextField tfMobile = new JTextField((String) tableModel.getValueAt(row, 4));

        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));
        form.setBorder(new EmptyBorder(20, 20, 10, 20));
        form.add(new JLabel("Full Name:"));   form.add(tfName);
        form.add(new JLabel("Student ID:"));  form.add(tfID);
        form.add(new JLabel("Degree:"));      form.add(tfDegree);
        form.add(new JLabel("Email:"));       form.add(tfEmail);
        form.add(new JLabel("Mobile No:"));   form.add(tfMobile);
        dialog.add(form, BorderLayout.CENTER);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnCancel = createButton("Cancel", new Color(150, 150, 155), Color.WHITE);
        JButton btnUpdate = createButton("Update", new Color(123, 17, 19),   Color.WHITE);

        btnCancel.addActionListener(e -> dialog.dispose());
        btnUpdate.addActionListener(e -> {
            if (tfName.getText().trim().isEmpty() || tfID.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                        "Full Name and Student ID are required!",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Update the selected row
            tableModel.setValueAt(tfName.getText().trim(),   row, 0);
            tableModel.setValueAt(tfID.getText().trim(),     row, 1);
            tableModel.setValueAt(tfDegree.getText().trim(), row, 2);
            tableModel.setValueAt(tfEmail.getText().trim(),  row, 3);
            tableModel.setValueAt(tfMobile.getText().trim(), row, 4);
            dialog.dispose();
        });

        btnRow.add(btnCancel);
        btnRow.add(btnUpdate);
        dialog.add(btnRow, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a student row first, then click Delete.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String name = (String) tableModel.getValueAt(row, 0);
        int choice = JOptionPane.showConfirmDialog(this,
                "Delete student \"" + name + "\"?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            tableModel.removeRow(row);
            updateRowCount();
        }
    }


    private void updateRowCount() {
        rowCountLabel.setText("Showing " + tableModel.getRowCount() + " student(s)");
    }

    private JFrame getParentFrame() {
        Window w = SwingUtilities.getWindowAncestor(this);
        return (w instanceof JFrame) ? (JFrame) w : null;
    }

    private JButton createButton(String text, Color background, Color foreground) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setBackground(background);
        btn.setForeground(foreground);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(120, 36));
        return btn;
    }
}