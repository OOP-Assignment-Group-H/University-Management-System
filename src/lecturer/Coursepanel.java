package lecturer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Shows the list of courses the lecturer is assigned to teach.
 */
public class Coursepanel extends JPanel {

    public Coursepanel() {
        setLayout(new BorderLayout(0, 16));
        setOpaque(false);

        JLabel heading = new JLabel("My Courses");
        heading.setFont(new Font("Arial", Font.BOLD, 20));
        heading.setForeground(LecturerPortal.TEXT_DARK);
        add(heading, BorderLayout.NORTH);

        add(buildTable(), BorderLayout.CENTER);
    }

    private JScrollPane buildTable() {
        String[] columns = {"Course Code", "Course Name", "Semester", "Credits", "Enrolled Students"};
        Object[][] data = {
                {"CS3201", "Object Oriented Programming", "Semester 3", 3, 58},
                {"CS3305", "Database Management Systems", "Semester 3", 3, 62},
                {"CS4102", "Software Engineering", "Semester 4", 4, 47},
                {"CS4210", "Artificial Intelligence", "Semester 4", 3, 45},
        };

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(34);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(250, 250, 250));
        table.setSelectionBackground(new Color(240, 220, 220));
        table.setGridColor(LecturerPortal.CARD_BORDER);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(LecturerPortal.CARD_BG);
        wrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LecturerPortal.CARD_BORDER, 1, true),
                new EmptyBorder(4, 4, 4, 4)));
        wrapper.add(table.getTableHeader(), BorderLayout.NORTH);
        wrapper.add(table, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(wrapper);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        return scrollPane;
    }
}