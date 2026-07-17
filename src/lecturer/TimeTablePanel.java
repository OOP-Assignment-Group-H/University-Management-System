package lecturer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Weekly timetable grid for the lecturer's classes.
 */
public class TimeTablePanel extends JPanel {

    public TimeTablePanel() {
        setLayout(new BorderLayout(0, 16));
        setOpaque(false);

        JLabel heading = new JLabel("Weekly Timetable");
        heading.setFont(new Font("Arial", Font.BOLD, 20));
        heading.setForeground(LecturerPortal.TEXT_DARK);
        add(heading, BorderLayout.NORTH);

        add(buildTable(), BorderLayout.CENTER);
    }

    private JScrollPane buildTable() {
        String[] columns = {"Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        Object[][] data = {
                {"8:30 - 10:00", "CS3201\nLH 1", "", "CS4102\nLH 3", "", "CS3201\nLH 1"},
                {"10:15 - 11:45", "", "CS3305\nLab 2", "", "CS4210\nLH 2", ""},
                {"1:00 - 2:30", "CS4210\nLH 2", "", "CS3305\nLab 2", "", ""},
                {"2:45 - 4:15", "", "CS4102\nLH 3", "", "", "Consultation"},
        };

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(56);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(250, 250, 250));
        table.setGridColor(LecturerPortal.CARD_BORDER);
        table.setDefaultRenderer(Object.class, new MultilineCellRenderer());
        table.getColumnModel().getColumn(0).setPreferredWidth(110);

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

    /** Renders "\n"-separated cell text as centered, multi-line HTML. */
    private static class MultilineCellRenderer extends javax.swing.table.DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            String text = value == null ? "" : value.toString().replace("\n", "<br>");
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            label.setText("<html><div style='text-align:center;'>" + text + "</div></html>");
            label.setHorizontalAlignment(SwingConstants.CENTER);
            return label;
        }
    }
}