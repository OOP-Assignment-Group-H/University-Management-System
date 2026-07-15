package admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;


public class DegreePanel extends JPanel {

    // Theme colours
    private static final Color MAROON     = new Color(123, 17,  19);
    private static final Color BG         = new Color(245, 247, 250);
    private static final Color WHITE      = Color.WHITE;
    private static final Color TEXT_DARK  = new Color(35,  35,  40);
    private static final Color TEXT_MUTED = new Color(120, 120, 130);
    private static final Color BORDER_CLR = new Color(220, 220, 228);
    private static final Color ROW_ALT    = new Color(253, 248, 248);

    // Table columns
    private static final String[] COLUMNS = {
            "Degree Name", "Duration (Years)", "Department", "No. of Students"
    };

    private DefaultTableModel tableModel;
    private JTable            table;
    private JLabel            countLabel;

    public DegreePanel() {
        setLayout(new BorderLayout(0, 0));
        setBackground(BG);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // STEP 1: build table model
        buildTable();

        // STEP 2: top bar with action buttons
        add(buildTopBar(), BorderLayout.NORTH);

        // STEP 3: table inside scroll pane
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_CLR, 1));
        scroll.getViewport().setBackground(WHITE);
        add(scroll, BorderLayout.CENTER);

        // STEP 4: bottom bar
        add(buildBottomBar(), BorderLayout.SOUTH);
    }

    private void buildTable() {
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        // Sample degree data
        tableModel.addRow(new Object[]{"BSc (Hons) Software Engineering", "4", "Software Engineering", "120"});
        tableModel.addRow(new Object[]{"BSc (Hons) Computer Science",      "4", "Computer Science",      "95"});


        // Build JTable
        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setForeground(TEXT_DARK);
        table.setBackground(WHITE);
        table.setRowHeight(42);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setGridColor(BORDER_CLR);
        table.setSelectionBackground(new Color(248, 220, 220));
        table.setSelectionForeground(MAROON);
        table.setFocusable(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);

        // Alternating row colours
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setBorder(new EmptyBorder(0, 12, 0, 12));
                if (sel) {
                    setBackground(new Color(248, 220, 220));
                    setForeground(MAROON);
                } else {
                    setBackground(row % 2 == 0 ? WHITE : ROW_ALT);
                    setForeground(TEXT_DARK);
                }
                return this;
            }
        });

        // Header style
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(0, 44));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setBackground(MAROON);
                setForeground(WHITE);
                setFont(new Font("Arial", Font.BOLD, 13));
                setBorder(new EmptyBorder(0, 12, 0, 12));
                setHorizontalAlignment(SwingConstants.LEFT);
                return this;
            }
        });
    }

    private JPanel buildTopBar() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(BG);
        top.setBorder(new EmptyBorder(0, 0, 16, 0));

        // Left: heading
        JLabel heading = new JLabel("Degrees");
        heading.setFont(new Font("Arial", Font.BOLD, 26));
        heading.setForeground(TEXT_DARK);
        top.add(heading, BorderLayout.WEST);

        // Right: action buttons
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnRow.setBackground(BG);

        JButton btnAdd    = makeButton("+ Add New", MAROON,                  WHITE);
        JButton btnEdit   = makeButton("  Edit",    new Color(70, 70, 80),   WHITE);
        JButton btnDelete = makeButton("  Delete",  new Color(180, 40, 40),  WHITE);

        btnRow.add(btnAdd);
        btnRow.add(btnEdit);
        btnRow.add(btnDelete);
        top.add(btnRow, BorderLayout.EAST);

        // Wire actions – tableModel is guaranteed non-null here
        btnAdd.addActionListener(e    -> openAddDialog());
        btnEdit.addActionListener(e   -> openEditDialog());
        btnDelete.addActionListener(e -> deleteSelected());

        return top;
    }

    private JPanel buildBottomBar() {
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(BG);
        bottom.setBorder(new EmptyBorder(8, 0, 0, 0));

        // Left: row count
        countLabel = new JLabel();
        countLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        countLabel.setForeground(TEXT_MUTED);
        refreshCount();
        bottom.add(countLabel, BorderLayout.WEST);

        // Right: Save Changes button
        JButton btnSave = makeButton("Save Changes", MAROON, WHITE);
        btnSave.setPreferredSize(new Dimension(160, 36));
        btnSave.addActionListener(e ->
                JOptionPane.showMessageDialog(
                        this,
                        "Degree records saved successfully!",
                        "Saved",
                        JOptionPane.INFORMATION_MESSAGE
                )
        );
        bottom.add(btnSave, BorderLayout.EAST);

        return bottom;
    }

    private void openAddDialog() {
        JDialog dlg = makeDialog("Add New Degree", 420, 440);

        JTextField tfName     = inputField("");
        JTextField tfDuration = inputField("");
        JTextField tfDept     = inputField("");
        JTextField tfStudents = inputField("");

        dlg.add(buildForm(
                new String[]{"Degree Name *", "Duration (Years) *", "Department *", "No. of Students"},
                new JTextField[]{tfName, tfDuration, tfDept, tfStudents}
        ), BorderLayout.CENTER);

        dlg.add(makeFooter(dlg, "Save", () -> {
            if (tfName.getText().trim().isEmpty() || tfDuration.getText().trim().isEmpty()
                    || tfDept.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dlg,
                        "Degree Name, Duration and Department are required.", "Validation",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            // ✅ Add new row to the degree table
            tableModel.addRow(new Object[]{
                    tfName.getText().trim(),
                    tfDuration.getText().trim(),
                    tfDept.getText().trim(),
                    tfStudents.getText().trim()
            });
            refreshCount();
            dlg.dispose();
        }), BorderLayout.SOUTH);

        dlg.setVisible(true);
    }

    private void openEditDialog() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please click on a degree row first, then click Edit.",
                    "No Row Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog dlg = makeDialog("Edit Degree", 420, 440);

        // Pre-fill with selected row values
        JTextField tfName     = inputField((String) tableModel.getValueAt(row, 0));
        JTextField tfDuration = inputField((String) tableModel.getValueAt(row, 1));
        JTextField tfDept     = inputField((String) tableModel.getValueAt(row, 2));
        JTextField tfStudents = inputField((String) tableModel.getValueAt(row, 3));

        dlg.add(buildForm(
                new String[]{"Degree Name *", "Duration (Years) *", "Department *", "No. of Students"},
                new JTextField[]{tfName, tfDuration, tfDept, tfStudents}
        ), BorderLayout.CENTER);

        dlg.add(makeFooter(dlg, "Update", () -> {
            if (tfName.getText().trim().isEmpty() || tfDuration.getText().trim().isEmpty()
                    || tfDept.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dlg,
                        "Degree Name, Duration and Department are required.", "Validation",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            // ✅ Update the existing row
            tableModel.setValueAt(tfName.getText().trim(),     row, 0);
            tableModel.setValueAt(tfDuration.getText().trim(), row, 1);
            tableModel.setValueAt(tfDept.getText().trim(),     row, 2);
            tableModel.setValueAt(tfStudents.getText().trim(), row, 3);
            dlg.dispose();
        }), BorderLayout.SOUTH);

        dlg.setVisible(true);
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please click on a degree row first, then click Delete.",
                    "No Row Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String name = (String) tableModel.getValueAt(row, 0);
        int choice = JOptionPane.showConfirmDialog(this,
                "Delete degree \"" + name + "\"?  This cannot be undone.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            // ✅ Remove the selected row
            tableModel.removeRow(row);
            refreshCount();
        }
    }


    private void refreshCount() {
        if (countLabel != null)
            countLabel.setText("Showing " + tableModel.getRowCount() + " degree(s)");
    }


    private JTextField inputField(String value) {
        JTextField tf = new JTextField(value);
        tf.setFont(new Font("Arial", Font.PLAIN, 14));
        tf.setForeground(TEXT_DARK);
        tf.setBackground(new Color(250, 250, 252));
        tf.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_CLR, 1, true),
                new EmptyBorder(6, 10, 6, 10)));
        return tf;
    }

    private JPanel buildForm(String[] labels, JTextField[] fields) {
        JPanel form = new JPanel();
        form.setBackground(WHITE);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(20, 24, 10, 24));

        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(new Font("Arial", Font.BOLD, 12));
            lbl.setForeground(TEXT_MUTED);
            lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

            fields[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            fields[i].setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

            form.add(lbl);
            form.add(Box.createVerticalStrut(4));
            form.add(fields[i]);
            form.add(Box.createVerticalStrut(12));
        }
        return form;
    }

    private JDialog makeDialog(String title, int width, int height) {
        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dlg;
        if (owner instanceof JFrame) {
            dlg = new JDialog((JFrame) owner, title, true);
        } else {
            dlg = new JDialog();
            dlg.setTitle(title);
            dlg.setModal(true);
        }
        dlg.setSize(width, height);
        dlg.setResizable(false);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());
        dlg.getContentPane().setBackground(WHITE);

        // Maroon header bar
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(MAROON);
        bar.setBorder(new EmptyBorder(14, 20, 14, 20));
        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Arial", Font.BOLD, 16));
        titleLbl.setForeground(WHITE);
        bar.add(titleLbl, BorderLayout.WEST);
        dlg.add(bar, BorderLayout.NORTH);

        return dlg;
    }

    private JPanel makeFooter(JDialog dlg, String actionLabel, Runnable onAction) {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        footer.setBackground(new Color(248, 248, 250));
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_CLR));

        JButton btnCancel = makeButton("Cancel", new Color(150, 150, 155), WHITE);
        JButton btnOK     = makeButton(actionLabel, MAROON, WHITE);

        btnCancel.addActionListener(e -> dlg.dispose());
        btnOK.addActionListener(e     -> onAction.run());

        footer.add(btnCancel);
        footer.add(btnOK);
        return footer;
    }

    private JButton makeButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(120, 38));

        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(bg.darker()); }
            @Override public void mouseExited (MouseEvent e) { btn.setBackground(bg); }
        });
        return btn;
    }
}