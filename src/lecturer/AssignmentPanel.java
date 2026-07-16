package lecturer;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;


public class AssignmentPanel extends JPanel {


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
    private static final Color DOC_NAME_TXT    = new Color(30, 41, 59);
    private static final Font FONT_HEADING = new Font("SansSerif", Font.BOLD, 20);
    private static final Font FONT_SUBHEAD = new Font("SansSerif", Font.BOLD, 14);
    private static final Font FONT_BODY    = new Font("SansSerif", Font.PLAIN, 13);
    private static final Font FONT_BUTTON  = new Font("SansSerif", Font.BOLD, 13);


    private static class Assignment {
        String title, course, type, dueDate, status;
        Assignment(String title, String course, String type, String dueDate, String status) {
            this.title = title;
            this.course = course;
            this.type = type;
            this.dueDate = dueDate;
            this.status = status;
        }
    }


    private static final int COL_ATTACHMENT = 5;
    private static final FileNameExtensionFilter DOC_FILTER =
            new FileNameExtensionFilter("PDF or Image files (*.pdf, *.jpg, *.jpeg)", "pdf", "jpg", "jpeg");

    private final DefaultTableModel model;
    private final JTable table;
    private final String[] TYPES = {"Assignment", "Quiz"};
    private final String[] STATUSES = {"Pending", "Open", "Grading", "Completed"};
    private JLabel countLabel;


    private JLabel docSelectedTitleLabel;
    private JLabel docFileNameLabel;
    private JButton docAttachBtn;
    private JButton docViewBtn;
    private JButton docRemoveBtn;

    public AssignmentPanel() {
        setLayout(new BorderLayout(0, 16));
        setBackground(APP_BG);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        model = new DefaultTableModel(new Object[]{"Title", "Course", "Type", "Due Date", "Status", "AttachmentPath"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        styleTable(table);
        model.addTableModelListener(e -> updateCount());

        // Hide the AttachmentPath column from view, keep it in the model as data storage
        table.getColumnModel().removeColumn(table.getColumnModel().getColumn(COL_ATTACHMENT));

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) refreshDocumentCard();
        });

        add(buildHeaderBar(), BorderLayout.NORTH);

        JPanel centerWrap = new JPanel(new BorderLayout(0, 16));
        centerWrap.setBackground(APP_BG);

        JPanel tableCard = card();
        tableCard.setLayout(new BorderLayout());
        tableCard.add(new JScrollPane(table), BorderLayout.CENTER);
        centerWrap.add(tableCard, BorderLayout.CENTER);

        centerWrap.add(buildDocumentCard(), BorderLayout.SOUTH);

        add(centerWrap, BorderLayout.CENTER);
        add(buildFooterBar(), BorderLayout.SOUTH);

        loadSampleData();
        updateCount();
        refreshDocumentCard();
    }


    private JPanel buildHeaderBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(APP_BG);
        bar.add(sectionTitle("Assignments & Quizzes"), BorderLayout.WEST);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttons.setBackground(APP_BG);

        JButton add = styledButton("+ Add New", PRIMARY_BTN, PRIMARY_BTN_TXT);
        JButton edit = styledButton("Edit", SUCCESS_BTN, SUCCESS_BTN_TXT);
        JButton remove = styledButton("Delete", DANGER_BTN, DANGER_BTN_TXT);

        add.addActionListener(e -> openForm(null));
        edit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Please select a row first.");
                return;
            }
            Assignment a = new Assignment(
                    (String) model.getValueAt(row, 0),
                    (String) model.getValueAt(row, 1),
                    (String) model.getValueAt(row, 2),
                    (String) model.getValueAt(row, 3),
                    (String) model.getValueAt(row, 4));
            openForm(a);
        });
        remove.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Please select a row first.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Remove this item?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                model.removeRow(row);
                refreshDocumentCard();
            }
        });

        buttons.add(add); buttons.add(edit); buttons.add(remove);
        bar.add(buttons, BorderLayout.EAST);
        return bar;
    }


    private JPanel buildDocumentCard() {
        JPanel outer = card();
        outer.setLayout(new BorderLayout(0, 10));

        JLabel heading = new JLabel("Attached Document");
        heading.setFont(FONT_SUBHEAD);
        heading.setForeground(new Color(30, 41, 59));
        outer.add(heading, BorderLayout.NORTH);

        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setBackground(CARD_BG);

        JPanel infoBox = new JPanel();
        infoBox.setLayout(new BoxLayout(infoBox, BoxLayout.Y_AXIS));
        infoBox.setBackground(CARD_BG);

        docSelectedTitleLabel = new JLabel("No assignment selected");
        docSelectedTitleLabel.setFont(FONT_BODY);
        docSelectedTitleLabel.setForeground(FOOTER_TXT);

        docFileNameLabel = new JLabel("\u2014");
        docFileNameLabel.setFont(FONT_BODY.deriveFont(Font.BOLD));
        docFileNameLabel.setForeground(DOC_NAME_TXT);
        docFileNameLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        docFileNameLabel.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { viewSelectedDocument(); }
        });

        infoBox.add(docSelectedTitleLabel);
        infoBox.add(Box.createVerticalStrut(4));
        infoBox.add(docFileNameLabel);

        row.add(infoBox, BorderLayout.CENTER);

        JPanel docButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        docButtons.setBackground(CARD_BG);

        docAttachBtn = styledButton("Attach / Change", PRIMARY_BTN, PRIMARY_BTN_TXT);
        docViewBtn = styledButton("View", SUCCESS_BTN, SUCCESS_BTN_TXT);
        docRemoveBtn = styledButton("Remove", DANGER_BTN, DANGER_BTN_TXT);

        docAttachBtn.addActionListener(e -> attachDocumentToSelected());
        docViewBtn.addActionListener(e -> viewSelectedDocument());
        docRemoveBtn.addActionListener(e -> removeDocumentFromSelected());

        docButtons.add(docAttachBtn);
        docButtons.add(docViewBtn);
        docButtons.add(docRemoveBtn);

        row.add(docButtons, BorderLayout.EAST);
        outer.add(row, BorderLayout.CENTER);

        return outer;
    }

    private void attachDocumentToSelected() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an assignment/quiz row first.");
            return;
        }
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select PDF or Image document");
        chooser.setFileFilter(DOC_FILTER);
        chooser.setAcceptAllFileFilterUsed(false);

        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            model.setValueAt(selectedFile.getAbsolutePath(), selectedRow, COL_ATTACHMENT);
            refreshDocumentCard();
        }
    }

    private void viewSelectedDocument() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) return;
        String path = (String) model.getValueAt(selectedRow, COL_ATTACHMENT);
        if (path == null || path.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No document attached to this item yet.");
            return;
        }
        File file = new File(path);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "The attached file could not be found:\n" + path,
                    "File not found", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            } else {
                JOptionPane.showMessageDialog(this, "Opening files is not supported on this system.");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Could not open the file:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeDocumentFromSelected() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an assignment/quiz row first.");
            return;
        }
        String path = (String) model.getValueAt(selectedRow, COL_ATTACHMENT);
        if (path == null || path.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "There is no document attached to remove.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Remove the attached document from this item?",
                "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            model.setValueAt("", selectedRow, COL_ATTACHMENT);
            refreshDocumentCard();
        }
    }

    private void refreshDocumentCard() {
        int selectedRow = table.getSelectedRow();
        boolean hasSelection = selectedRow >= 0;

        docAttachBtn.setEnabled(hasSelection);
        docViewBtn.setEnabled(hasSelection);
        docRemoveBtn.setEnabled(hasSelection);

        if (!hasSelection) {
            docSelectedTitleLabel.setText("No assignment selected");
            docFileNameLabel.setText("\u2014");
            return;
        }

        String title = (String) model.getValueAt(selectedRow, 0);
        String course = (String) model.getValueAt(selectedRow, 1);
        docSelectedTitleLabel.setText(title + "  \u2022  " + course);

        String path = (String) model.getValueAt(selectedRow, COL_ATTACHMENT);
        if (path == null || path.trim().isEmpty()) {
            docFileNameLabel.setText("No document attached");
            docFileNameLabel.setForeground(FOOTER_TXT);
        } else {
            docFileNameLabel.setText(new File(path).getName() + "  (click to open)");
            docFileNameLabel.setForeground(new Color(37, 99, 235));
        }
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
        countLabel.setText("Showing " + model.getRowCount() + " item(s)");
    }

    private void openForm(Assignment existing) {
        JTextField titleField = new JTextField(existing != null ? existing.title : "", 15);
        JTextField courseField = new JTextField(existing != null ? existing.course : "", 15);
        JComboBox<String> typeBox = new JComboBox<>(TYPES);
        JTextField dueField = new JTextField(existing != null ? existing.dueDate : "", 15);
        JComboBox<String> statusBox = new JComboBox<>(STATUSES);

        if (existing != null) {
            typeBox.setSelectedItem(existing.type);
            statusBox.setSelectedItem(existing.status);
        }

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("Title:"));
        form.add(titleField);
        form.add(new JLabel("Course:"));
        form.add(courseField);
        form.add(new JLabel("Type:"));
        form.add(typeBox);
        form.add(new JLabel("Due Date:"));
        form.add(dueField);
        form.add(new JLabel("Status:"));
        form.add(statusBox);

        int result = JOptionPane.showConfirmDialog(this, form,
                existing == null ? "Add Assignment/Quiz" : "Edit Assignment/Quiz",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            if (titleField.getText().trim().isEmpty() || courseField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Title and Course are required.");
                return;
            }
            if (existing == null) {
                // New rows start with no attached document (empty AttachmentPath)
                Object[] row = {titleField.getText(), courseField.getText(), typeBox.getSelectedItem(),
                        dueField.getText(), statusBox.getSelectedItem(), ""};
                model.addRow(row);
            } else {
                int selected = table.getSelectedRow();
                model.setValueAt(titleField.getText(), selected, 0);
                model.setValueAt(courseField.getText(), selected, 1);
                model.setValueAt(typeBox.getSelectedItem(), selected, 2);
                model.setValueAt(dueField.getText(), selected, 3);
                model.setValueAt(statusBox.getSelectedItem(), selected, 4);
                // Attachment (column 5) is left untouched here; it is managed via the Document card
                refreshDocumentCard();
            }
        }
    }

    private void loadSampleData() {
        model.addRow(new Object[]{"Mid Exam", "CTEC22055", "Assignment", "2026-07-20", "Grading", ""});
        model.addRow(new Object[]{"Quiz 3", "DELT22054", "Quiz", "2026-07-18", "Pending", ""});
        model.addRow(new Object[]{"Project ", "CTEC22033", "Assignment", "2026-07-25", "Open", ""});
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