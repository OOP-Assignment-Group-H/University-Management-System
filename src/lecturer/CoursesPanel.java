package lecturer;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class CoursesPanel extends JPanel {

    private static class CourseRow {
        String course;
        String status;

        CourseRow(String course, String status) {
            this.course = course;
            this.status = status;
        }
    }

    private final List<CourseRow> courses = new ArrayList<>();
    private static final String[] COLUMNS = {"Course", "Status"};

    private CoursesTableModel model;
    private JTable table;

    public CoursesPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        seedData();

        JPanel headerRow = new JPanel(new BorderLayout());
        headerRow.setBackground(Color.WHITE);

        JLabel header = new JLabel(" COURSE PROGRESS");
        header.setFont(new Font("SansSerif", Font.BOLD, 16));
        headerRow.add(header, BorderLayout.WEST);

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttonRow.setBackground(Color.WHITE);

        JButton deleteCourseBtn = new JButton("Delete Course");
        deleteCourseBtn.addActionListener(e -> deleteSelectedCourse());
        buttonRow.add(deleteCourseBtn);

        JButton addCourseBtn = new JButton("+ Add New Course");
        addCourseBtn.addActionListener(e -> openAddCourseDialog());
        buttonRow.add(addCourseBtn);

        headerRow.add(buttonRow, BorderLayout.EAST);

        add(headerRow, BorderLayout.NORTH);

        model = new CoursesTableModel();
        table = new JTable(model) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(0xFF, 0xEF, 0xEE));
                }
                return c;
            }
        };
        table.setRowHeight(36);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("SansSerif", Font.BOLD, 13));
        tableHeader.setBackground(new Color(0x80, 0x20, 0x24));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setPreferredSize(new Dimension(tableHeader.getPreferredSize().width, 36));
        ((DefaultTableCellRenderer) tableHeader.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.LEFT);

        table.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void seedData() {
        courses.add(new CourseRow("CS401", "Active"));
        courses.add(new CourseRow("MA310", "Applied Statistics"));
        courses.add(new CourseRow("CS420", "Artificial Intelligence"));
        courses.add(new CourseRow("CS450", "Software Engineering"));
        courses.add(new CourseRow("HUM202", "Ethics"));
        courses.add(new CourseRow("CS499", "In Progress"));
    }

    private void openAddCourseDialog() {
        CourseRow blank = new CourseRow("", "");
        CourseRow result = showCourseFormDialog("Add New Course", blank);
        if (result != null) {
            courses.add(result);
            model.fireTableDataChanged();
        }
    }

    private void deleteSelectedCourse() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a course to delete first.");
            return;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);
        String courseName = courses.get(modelRow).course;

        int confirm = JOptionPane.showConfirmDialog(this,
                "Remove " + courseName + " from the course list?",
                "Delete Course", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            courses.remove(modelRow);
            model.fireTableDataChanged();
        }
    }


    private CourseRow showCourseFormDialog(String title, CourseRow prefill) {
        JTextField courseField = new JTextField(prefill.course, 18);
        JTextField statusField = new JTextField(prefill.status, 18);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.add(labeledField("Course code", courseField));
        form.add(Box.createRigidArea(new Dimension(0, 6)));
        form.add(labeledField("Status", statusField));

        int result = JOptionPane.showConfirmDialog(this, form, title,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) {
            return null;
        }

        String course = courseField.getText().trim();
        if (course.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Course code cannot be empty.");
            return null;
        }
        return new CourseRow(course, statusField.getText().trim());
    }

    private JPanel labeledField(String label, JComponent field) {
        JPanel rowPanel = new JPanel(new BorderLayout(5, 2));
        rowPanel.add(new JLabel(label), BorderLayout.NORTH);
        rowPanel.add(field, BorderLayout.CENTER);
        return rowPanel;
    }

    private class CoursesTableModel extends AbstractTableModel {
        @Override
        public int getRowCount() {
            return courses.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMNS.length;
        }

        @Override
        public String getColumnName(int col) {
            return COLUMNS[col];
        }

        @Override
        public Object getValueAt(int row, int col) {
            CourseRow r = courses.get(row);
            switch (col) {
                case 0: return r.course;
                default: return r.status;
            }
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }
    }
}