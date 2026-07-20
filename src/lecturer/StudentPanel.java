package lecturer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;


public class StudentPanel extends JPanel {


    private static final Color APP_BG          = new Color(243, 244, 246);
    private static final Color CARD_BG         = Color.WHITE;
    private static final Color CARD_BORDER     = new Color(224, 224, 224);
    private static final Color PRIMARY_BTN     = new Color(128, 32, 36);
    private static final Color PRIMARY_BTN_TXT = Color.WHITE;
    private static final Color DANGER_BTN      = new Color(196, 57, 58);
    private static final Color DANGER_BTN_TXT  = Color.WHITE;
    private static final Color SUCCESS_BTN     = new Color(73, 80, 87);
    private static final Color SUCCESS_BTN_TXT = Color.WHITE;
    private static final Color TABLE_HEADER_BG = new Color(128, 32, 36);
    private static final Color TABLE_HEADER_TXT= Color.WHITE;
    private static final Color TABLE_GRID      = new Color(230, 230, 230);
    private static final Color TABLE_ALT_ROW   = new Color(250, 243, 244);
    private static final Color FOOTER_TXT      = new Color(110, 110, 115);
    private static final Font FONT_HEADING = new Font("SansSerif", Font.BOLD, 20);
    private static final Font FONT_BODY    = new Font("SansSerif", Font.PLAIN, 13);
    private static final Font FONT_BUTTON  = new Font("SansSerif", Font.BOLD, 13);


    private static class Student {
        String id, name, course, email, phone;
        Student(String id, String name, String course, String email, String phone) {
            this.id = id;
            this.name = name;
            this.course = course;
            this.email = email;
            this.phone = phone;
        }
    }

    private final DefaultTableModel model;
    private final JTable table;
    private JLabel countLabel;

    public StudentPanel() {
        setLayout(new BorderLayout(0, 16));
        setBackground(APP_BG);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        model = new DefaultTableModel(new Object[]{"Student ID", "Name", "Course", "Email", "Phone"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        styleTable(table);
        model.addTableModelListener(e -> updateCount());

        add(buildHeaderBar(), BorderLayout.NORTH);

        JPanel card = card();
        card.setLayout(new BorderLayout());
        card.add(new JScrollPane(table), BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);

        add(buildFooterBar(), BorderLayout.SOUTH);

        loadSampleData();
        updateCount();
    }

    private JPanel buildHeaderBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(APP_BG);
        bar.add(sectionTitle("Student Details"), BorderLayout.WEST);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttons.setBackground(APP_BG);

        JButton add = styledButton("+ Add New", PRIMARY_BTN, PRIMARY_BTN_TXT);
        JButton edit = styledButton("Edit", SUCCESS_BTN, SUCCESS_BTN_TXT);
        JButton remove = styledButton("Delete", DANGER_BTN, DANGER_BTN_TXT);

        add.addActionListener(e -> openForm(null));
        edit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Please select a student row first.");
                return;
            }
            Student s = new Student(
                    (String) model.getValueAt(row, 0),
                    (String) model.getValueAt(row, 1),
                    (String) model.getValueAt(row, 2),
                    (String) model.getValueAt(row, 3),
                    (String) model.getValueAt(row, 4));
            openForm(s);
        });
        remove.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Please select a student row first.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Remove this student?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                model.removeRow(row);
            }
        });

        buttons.add(add);
        buttons.add(edit);
        buttons.add(remove);
        bar.add(buttons, BorderLayout.EAST);
        return bar;
    }


    private JPanel buildFooterBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(APP_BG);

        countLabel = new JLabel();
        countLabel.setFont(FONT_BODY);
        countLabel.setForeground(FOOTER_TXT);
        bar.add(countLabel, BorderLayout.WEST);

        JButton save = styledButton("Save Changes", PRIMARY_BTN, PRIMARY_BTN_TXT);
        save.addActionListener(e -> JOptionPane.showMessageDialog(this, "Changes saved successfully.", "Saved", JOptionPane.INFORMATION_MESSAGE));
        bar.add(save, BorderLayout.EAST);
        return bar;
    }

    private void updateCount() {
        countLabel.setText("Showing " + model.getRowCount() + " student(s)");
    }

    private void openForm(Student existing) {
        JTextField idField = new JTextField(existing != null ? existing.id : "", 15);
        JTextField nameField = new JTextField(existing != null ? existing.name : "", 15);
        JTextField courseField = new JTextField(existing != null ? existing.course : "", 15);
        JTextField emailField = new JTextField(existing != null ? existing.email : "", 15);
        JTextField phoneField = new JTextField(existing != null ? existing.phone : "", 15);
        idField.setEditable(existing == null);

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Student ID:"));
        form.add(idField);
        form.add(new JLabel("Name:"));
        form.add(nameField);
        form.add(new JLabel("Course:"));
        form.add(courseField);
        form.add(new JLabel("Email:"));
        form.add(emailField);
        form.add(new JLabel("Phone:"));
        form.add(phoneField);

        int result = JOptionPane.showConfirmDialog(this, form,
                existing == null ? "Add Student" : "Edit Student",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            if (idField.getText().trim().isEmpty() || nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Student ID and Name are required.");
                return;
            }
            Object[] row = {idField.getText(), nameField.getText(), courseField.getText(),
                    emailField.getText(), phoneField.getText()};
            if (existing == null) {
                model.addRow(row);
            } else {
                int selected = table.getSelectedRow();
                for (int c = 0; c < row.length; c++) model.setValueAt(row[c], selected, c);
            }
        }
    }

    private void loadSampleData() {
        model.addRow(new Object[]{"CT/2023/11", "Nimal Perera", "CTEC22055", "nimal@stu.kln.edu", "071-1234567"});
        model.addRow(new Object[]{"CT/2023/12", "Kavya Silva", "DELT22054", "kavya@stu.kln.edu", "077-2345678"});
        model.addRow(new Object[]{"CT/2023/13", "Ruwan Fernando", "CTEC21033", "ruwan@stu.kln.edu", "070-3456789"});
    }



    private JButton styledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(FONT_BUTTON);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        return btn;
    }

    private void styleTable(JTable t) {
        t.setRowHeight(28);
        t.setFont(FONT_BODY);
        t.setGridColor(TABLE_GRID);
        t.setShowGrid(true);
        t.setSelectionBackground(new Color(219, 234, 254));
        t.setSelectionForeground(Color.BLACK);
        t.setFillsViewportHeight(true);

        JTableHeader header = t.getTableHeader();
        header.setBackground(TABLE_HEADER_BG);
        header.setForeground(TABLE_HEADER_TXT);
        header.setFont(FONT_HEADING.deriveFont(Font.BOLD, 13f));
        header.setPreferredSize(new Dimension(0, 36));
        header.setOpaque(true);
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, column);
                label.setOpaque(true);
                label.setBackground(TABLE_HEADER_BG);
                label.setForeground(TABLE_HEADER_TXT);
                label.setFont(FONT_HEADING.deriveFont(Font.BOLD, 13f));
                label.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
                return label;
            }
        });

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : TABLE_ALT_ROW);
                }
                return c;
            }
        };
        for (int i = 0; i < t.getColumnCount(); i++) {
            t.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }

    private JLabel sectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_HEADING);
        label.setForeground(new Color(30, 41, 59));
        return label;
    }

    private JPanel card() {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CARD_BORDER, 1),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        return panel;
    }
}
