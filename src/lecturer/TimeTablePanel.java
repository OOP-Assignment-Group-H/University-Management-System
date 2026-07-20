package lecturer;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;


public class TimeTablePanel extends JPanel {

    private static final String[] DAYS = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    private static final String[] TIME_SLOTS = {
            "8am - 10am",
            "10am - 12pm",
            "12pm - 1pm (Interval)",
            "1pm - 3pm",
            "3pm - 5pm"
    };
    private static final String[] COURSE_OPTIONS = {
            "Elective", "CS401 Algorithms", "CS420 AI (Lab A)", "MA310 Stats", "(Empty)"
    };

    private static class Slot {
        String course;
        String room;

        Slot(String course, String room) {
            this.course = course;
            this.room = room;
        }
    }

    private final Map<String, Slot> schedule = new LinkedHashMap<>();
    private final Map<String, JButton> cellButtons = new LinkedHashMap<>();
    private JLabel statusLabel;
    private int changesPending = 0;

    public TimeTablePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        JLabel header = new JLabel("WEEKLY LECTURE TIME TABLE");
        header.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(header, BorderLayout.NORTH);

        seedData();
        add(buildGrid(), BorderLayout.CENTER);

        statusLabel = new JLabel(buildStatusText());
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        add(statusLabel, BorderLayout.SOUTH);
    }

    private void seedData() {
        // 10am - 12pm slot (was 9am - 11am)
        schedule.put(key("Monday", TIME_SLOTS[1]), new Slot("CS401 Algorithms", "LH101"));
        schedule.put(key("Tuesday", TIME_SLOTS[1]), new Slot("CS420 AI (Lab A)", "Lab A"));
        schedule.put(key("Wednesday", TIME_SLOTS[1]), new Slot("CS420 (LaH01)", "LaH01"));

        // 1pm - 3pm slot (unchanged)
        schedule.put(key("Monday", TIME_SLOTS[3]), new Slot("MA310 Stats", "LH105"));
        schedule.put(key("Tuesday", TIME_SLOTS[3]), new Slot("Elective", "LH203"));
        schedule.put(key("Wednesday", TIME_SLOTS[3]), new Slot("MA310", "LH105"));
    }

    private String key(String day, String timeSlot) {
        return day + "|" + timeSlot;
    }

    private JPanel buildGrid() {
        JPanel grid = new JPanel(new GridLayout(TIME_SLOTS.length + 1, DAYS.length + 1, 4, 4));
        grid.setBackground(Color.WHITE);

        Color headerMaroon = new Color(0x80, 0x20, 0x24);

        JLabel cornerLabel = new JLabel("");
        cornerLabel.setOpaque(true);
        cornerLabel.setBackground(headerMaroon);
        grid.add(cornerLabel);

        for (String day : DAYS) {
            JLabel dayLabel = new JLabel(day, SwingConstants.CENTER);
            dayLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
            dayLabel.setOpaque(true);
            dayLabel.setBackground(headerMaroon);
            dayLabel.setForeground(Color.WHITE);
            dayLabel.setBorder(BorderFactory.createEmptyBorder(6, 4, 6, 4));
            grid.add(dayLabel);
        }

        for (String timeSlot : TIME_SLOTS) {
            boolean isInterval = timeSlot.contains("Interval");

            JLabel timeLabel = new JLabel(timeSlot, SwingConstants.CENTER);
            timeLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
            timeLabel.setOpaque(true);
            timeLabel.setBackground(headerMaroon);
            timeLabel.setForeground(Color.WHITE);
            timeLabel.setBorder(BorderFactory.createEmptyBorder(6, 4, 6, 4));
            grid.add(timeLabel);

            for (String day : DAYS) {
                String k = key(day, timeSlot);

                if (isInterval) {
                    // Interval row is not editable / not a schedulable slot
                    JLabel intervalCell = new JLabel("Interval", SwingConstants.CENTER);
                    intervalCell.setOpaque(true);
                    intervalCell.setBackground(new Color(230, 230, 230));
                    intervalCell.setFont(new Font("SansSerif", Font.ITALIC, 11));
                    grid.add(intervalCell);
                    continue;
                }

                Slot slot = schedule.get(k);
                JButton cell = new JButton(slot == null ? "+ Add" : "<html><center>" + slot.course
                        + "<br>(" + slot.room + ")</center></html>");
                cell.setBackground(slot == null ? Color.WHITE : new Color(255, 240, 225));
                if (slot == null) {
                    cell.setForeground(headerMaroon);
                    cell.setBorder(BorderFactory.createLineBorder(headerMaroon, 2));
                }
                cell.setFocusPainted(false);
                cell.setFont(new Font("SansSerif", Font.PLAIN, 11));
                final String dayFinal = day;
                final String timeFinal = timeSlot;
                cell.addActionListener(e -> openEditDialog(dayFinal, timeFinal));
                cellButtons.put(k, cell);
                grid.add(cell);
            }
        }
        return grid;
    }

    private void openEditDialog(String day, String timeSlot) {
        String k = key(day, timeSlot);
        Slot existing = schedule.get(k);

        JComboBox<String> courseBox = new JComboBox<>(COURSE_OPTIONS);
        if (existing != null) {
            courseBox.setSelectedItem(existing.course);
        }
        JTextField roomField = new JTextField(existing != null ? existing.room : "", 15);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.add(new JLabel("Edit Timetable Entry (" + day + " " + timeSlot + ")"));
        form.add(Box.createRigidArea(new Dimension(0, 8)));
        form.add(new JLabel("Course"));
        form.add(courseBox);
        form.add(Box.createRigidArea(new Dimension(0, 8)));
        form.add(new JLabel("Room"));
        form.add(roomField);

        JButton moveBtn = new JButton("Move slot");
        JButton deleteBtn = new JButton("Delete slot");
        deleteBtn.setBackground(new Color(210, 60, 50));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setOpaque(true);

        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionRow.add(moveBtn);
        actionRow.add(deleteBtn);
        form.add(Box.createRigidArea(new Dimension(0, 8)));
        form.add(actionRow);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Edit Timetable Entry", true);
        dialog.setLayout(new BorderLayout(10, 10));
        ((JComponent) dialog.getContentPane()).setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        dialog.add(form, BorderLayout.CENTER);

        JPanel bottomButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        bottomButtons.add(cancelBtn);
        bottomButtons.add(saveBtn);
        dialog.add(bottomButtons, BorderLayout.SOUTH);

        saveBtn.addActionListener(e -> {
            String course = (String) courseBox.getSelectedItem();
            String room = roomField.getText().trim();
            schedule.put(k, new Slot(course, room));
            changesPending++;
            refreshCell(k, day, timeSlot);
            updateStatus();
            dialog.dispose();
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        deleteBtn.addActionListener(e -> {
            schedule.remove(k);
            changesPending++;
            refreshCell(k, day, timeSlot);
            updateStatus();
            dialog.dispose();
        });

        moveBtn.addActionListener(e -> {

            String[] movableSlots = java.util.Arrays.stream(TIME_SLOTS)
                    .filter(t -> !t.contains("Interval"))
                    .toArray(String[]::new);

            String targetDay = (String) JOptionPane.showInputDialog(dialog, "Move to day:",
                    "Move slot", JOptionPane.PLAIN_MESSAGE, null, DAYS, day);
            if (targetDay == null) return;
            String targetTime = (String) JOptionPane.showInputDialog(dialog, "Move to time:",
                    "Move slot", JOptionPane.PLAIN_MESSAGE, null, movableSlots, timeSlot);
            if (targetTime == null) return;

            Slot moving = schedule.remove(k);
            if (moving == null) {
                moving = new Slot((String) courseBox.getSelectedItem(), roomField.getText().trim());
            }
            String newKey = key(targetDay, targetTime);
            schedule.put(newKey, moving);
            changesPending++;
            refreshCell(k, day, timeSlot);
            refreshCell(newKey, targetDay, targetTime);
            updateStatus();
            dialog.dispose();
        });

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void refreshCell(String k, String day, String timeSlot) {
        JButton cell = cellButtons.get(k);
        if (cell == null) return; // interval slots have no button
        Slot slot = schedule.get(k);
        cell.setText(slot == null ? "+ Add" : "<html><center>" + slot.course
                + "<br>(" + slot.room + ")</center></html>");
        cell.setBackground(slot == null ? Color.WHITE : new Color(255, 240, 225));
        if (slot == null) {
            cell.setForeground(new Color(0x80, 0x20, 0x24));
            cell.setBorder(BorderFactory.createLineBorder(new Color(0x80, 0x20, 0x24), 2));
        } else {
            cell.setForeground(Color.BLACK);
            cell.setBorder(UIManager.getBorder("Button.border"));
        }
    }

    private String buildStatusText() {
        return "<html>Total Lectures: " + schedule.size()
                + " &nbsp;&nbsp; Changes Pending: " + changesPending + "</html>";
    }

    private void updateStatus() {
        statusLabel.setText(buildStatusText());
    }
}