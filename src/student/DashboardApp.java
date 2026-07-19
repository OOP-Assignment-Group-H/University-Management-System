package student;

import DB.DBConnection;
import lecturer.StudentPanel;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardApp extends JFrame {

    static final Color NAVY = new Color(0x1B2440);
    static final Color NAVY_ACTIVE = new Color(0x2A3558);
    static final Color ORANGE = new Color(0xE8703A);
    static final Color BG = new Color(0xF3F5F9);
    static final Color CARD_BG = Color.WHITE;
    static final Color TABLE_ORANGE = new Color(0xFCE9DD);
    static final Color BORDER = new Color(0xE0E3EA);
    static final Color TEXT_MUTED = new Color(0x8A93A6);

    private static final Color TOPBAR_BG = Color.WHITE;
    private static final Color TOPBAR_BORDER = new Color(226, 226, 226);
    private static final Color TITLE_TXT = new Color(33, 37, 41);
    private static final Color SUBTITLE_TXT = new Color(120, 120, 125);
    private static final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 22);
    private static final Font FONT_BODY = new Font("SansSerif", Font.PLAIN, 13);

    private static final String KEY_DASHBOARD = "DASHBOARD";
    private static final String KEY_COURSES = "COURSES";

    private final String userId;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(cardLayout);

    // Fields
    private student.AvatarButton avatarButton;
    private JLabel profileName, profileStudentId, profileEmail, profileDob, profileDegree, profileStatus;

    // Global models so we can update them after loading data
    private DefaultTableModel timetableModel;
    private DefaultTableModel courseModel;

    private String cachedFullName = "Loading...";
    private String cachedStudentId = "";
    private String cachedEmail = "";
    private String cachedDob = "";
    private String cachedDegree = "";
    private String cachedStatus = "";

    public DashboardApp(String userId) {
        this.userId = userId;

        setTitle("Student Portal Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1350, 760);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        contentPanel.add(buildDashboardPanel(), KEY_DASHBOARD);
        //contentPanel.add(new student.Courses(), KEY_COURSES);

        String[] keys = {KEY_DASHBOARD, KEY_COURSES};
        String[] labels = {"Dashboard", "Courses"};
        String[] icons = {"\uD83C\uDFE0", "\uD83D\uDCD8"};

        student.SidebarPanel sidebar = new student.SidebarPanel(keys, labels, icons, "DASHBOARD",
                this::showSection, this::confirmLogout);

        add(buildTopBar(), BorderLayout.NORTH);
        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // Fetch user data first, which will trigger the timetable/course loads
        loadStudentDetails();
    }

    private void showSection(String key) { cardLayout.show(contentPanel, key); }
    private void confirmLogout() { System.exit(0); }

    private JPanel buildTopBar() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(TOPBAR_BG);
        top.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, TOPBAR_BORDER),
                new EmptyBorder(16, 28, 16, 24)
        ));

        JPanel titleBlock = new JPanel();
        titleBlock.setOpaque(false);
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Student Portal Dashboard");
        title.setForeground(TITLE_TXT); title.setFont(FONT_TITLE);
        JLabel subtitle = new JLabel("View your profile, timetable and course progress.");
        subtitle.setForeground(SUBTITLE_TXT); subtitle.setFont(FONT_BODY);
        titleBlock.add(title); titleBlock.add(Box.createVerticalStrut(4)); titleBlock.add(subtitle);

        JPanel rightSide = new JPanel(new FlowLayout(FlowLayout.RIGHT, 18, 0));
        rightSide.setOpaque(false);
        avatarButton = new student.AvatarButton("..", ORANGE);
        avatarButton.addActionListener(e -> showProfileDialog());
        rightSide.add(avatarButton);

        top.add(titleBlock, BorderLayout.WEST);
        top.add(rightSide, BorderLayout.EAST);
        return top;
    }

    private void showProfileDialog() {
        JDialog dialog = new JDialog(this, "My Profile", true);
        dialog.setSize(420, 560);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // Header Panel
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(NAVY);
        header.setBorder(new EmptyBorder(20, 24, 20, 24));

        JLabel h1 = new JLabel("My Profile");
        h1.setFont(new Font("SansSerif", Font.BOLD, 22));
        h1.setForeground(Color.WHITE);
        h1.setAlignmentX(Component.CENTER_ALIGNMENT);

        student.AvatarButton bigAvatar = new student.AvatarButton(initialsFrom(cachedFullName), ORANGE);
        bigAvatar.setPreferredSize(new Dimension(70, 70));
        bigAvatar.setMaximumSize(new Dimension(70, 70));
        bigAvatar.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(h1);
        header.add(Box.createVerticalStrut(16));
        header.add(bigAvatar);

        // Body Panel
        JPanel body = new JPanel();
        body.setBackground(Color.WHITE);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(20, 24, 20, 24));

        // Populate fields
        body.add(profileField("Full Name", cachedFullName));
        body.add(profileField("Student ID", cachedStudentId));
        body.add(profileField("Email", cachedEmail));
        body.add(profileField("Date of Birth", cachedDob));
        body.add(profileField("Degree Program", cachedDegree));
        body.add(profileField("Status", cachedStatus));

        dialog.add(header, BorderLayout.NORTH);
        dialog.add(new JScrollPane(body), BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private JPanel profileField(String label, String value) {
        JPanel wrap = new JPanel();
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setBackground(Color.WHITE);
        wrap.setBorder(new EmptyBorder(0, 0, 12, 0));
        JLabel l = new JLabel(label);
        l.setForeground(new Color(120, 120, 125));
        JTextField f = new JTextField(value);
        f.setEditable(false);
        wrap.add(l); wrap.add(f);
        return wrap;
    }

    private String initialsFrom(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) return "??";
        String[] parts = fullName.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(2, parts.length); i++) sb.append(Character.toUpperCase(parts[i].charAt(0)));
        return sb.toString();
    }

    private JPanel buildDashboardPanel() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);
        main.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        JPanel grid = new JPanel(new GridLayout(2, 2, 20, 20));
        grid.setOpaque(false);
        grid.add(profileCard());
        grid.add(quickLinksCard());
        grid.add(timetableCard());
        grid.add(coursesSummaryCard());
        main.add(grid, BorderLayout.CENTER);
        return main;
    }

    private JPanel card(String title) {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(CARD_BG);
        outer.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1, true),
                BorderFactory.createEmptyBorder(16, 18, 16, 18)));
        JLabel header = new JLabel(title);
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        outer.add(header, BorderLayout.NORTH);
        return outer;
    }

    private JPanel profileCard() {
        JPanel outer = card("\uD83D\uDC64  PROFILE DETAILS");
        JPanel body = new JPanel(new BorderLayout(15, 0));
        body.setOpaque(false);
        body.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        profileName = new JLabel("Loading...");
        profileName.setFont(new Font("SansSerif", Font.BOLD, 18));
        profileName.setForeground(new Color(0x1B2440));
        info.add(profileName);
        info.add(Box.createVerticalStrut(15));

        profileStudentId = new JLabel("Student ID: ");
        profileStudentId.setFont(new Font("SansSerif", Font.BOLD, 13));
        info.add(profileStudentId);

        profileEmail = new JLabel("Email: ");
        profileEmail.setFont(new Font("SansSerif", Font.PLAIN, 13));
        info.add(profileEmail);

        profileDob = new JLabel("DOB: ");
        profileDob.setFont(new Font("SansSerif", Font.PLAIN, 13));
        info.add(profileDob);

        profileDegree = new JLabel(" ");
        profileDegree.setFont(new Font("SansSerif", Font.PLAIN, 13));
        info.add(profileDegree);

        profileStatus = new JLabel("Status : ");
        profileStatus.setFont(new Font("SansSerif", Font.BOLD, 15));
        profileStatus.setForeground(new Color(0, 150, 0));
        info.add(profileStatus);

        body.add(info, BorderLayout.CENTER);
        outer.add(body, BorderLayout.CENTER);

        return outer;
    }

    private JPanel quickLinksCard() {
        JPanel outer = card("\uD83D\uDD17  QUICK LINKS");
        JPanel list = new JPanel();
        list.setOpaque(false);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        String[][] links = {
                {"\uD83D\uDCDA Library", "https://lib.kln.ac.lk/"},
                {"\uD83D\uDCBB IT Support", "https://ithelp.kln.ac.lk/"},
                {"\uD83D\uDDFA UOK News", "https://news.kln.ac.lk/"}
        };

        for (String[] linkData : links) {
            JLabel l = new JLabel(linkData[0]);
            l.setFont(new Font("SansSerif", Font.PLAIN, 13));
            l.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));
            l.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            l.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    openWebpage(linkData[1]);
                }
            });

            list.add(l);
        }
        outer.add(list, BorderLayout.CENTER);
        return outer;
    }

    private void openWebpage(String urlString) {
        try {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                desktop.browse(new java.net.URI(urlString));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Could not open link: " + e.getMessage());
        }
    }

    private JPanel timetableCard() {
        JPanel outer = card("\uD83D\uDDD3 WEEKLY TIMETABLE");
        String[] cols = {"Time", "Mon", "Tue", "Wed", "Thu", "Fri"};

        timetableModel = new DefaultTableModel(cols, 0);
        // Initialize empty rows
        String[] times = {"08:00 - 08.55", "09:00 - 09.55", "10:00 - 10.55", "11:00 - 11.55", "12:00 - 12.55", "13:00 - 13.55", "14:00 - 14.55", "15:00 - 15.55", "16:00 - 16.55"};
        for (String time : times) timetableModel.addRow(new Object[]{time, "", "", "", "", ""});

        JTable table = new JTable(timetableModel) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table.setRowHeight(40);
        table.setShowGrid(true);
        table.setGridColor(BORDER);

        // Renderer to highlight occupied slots
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column > 0 && value != null && !value.toString().trim().isEmpty()) {
                    c.setBackground(TABLE_ORANGE);
                } else {
                    c.setBackground(Color.WHITE);
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(null); // Clean look
        outer.add(scrollPane, BorderLayout.CENTER);
        return outer;
    }

    private void loadTimetableData() {
        if (cachedStudentId == null || cachedStudentId.isEmpty()) return;

        String sql = "SELECT time_slot, day_of_week, course_code FROM timetable " +
                "WHERE batch = (SELECT batch FROM students WHERE student_id = ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cachedStudentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String time = rs.getString("time_slot").trim();
                    // 1. Convert to lowercase so it matches your getColumnForDay() cases
                    String day = rs.getString("day_of_week").trim().toLowerCase();
                    String course = rs.getString("course_code");

                    for (int i = 0; i < timetableModel.getRowCount(); i++) {
                        if (timetableModel.getValueAt(i, 0).toString().trim().equalsIgnoreCase(time)) {
                            int col = getColumnForDay(day);
                            if (col != -1) {
                                timetableModel.setValueAt(course, i, col);
                            }
                        }
                    }
                }
                // 2. Refresh the table UI
                timetableModel.fireTableDataChanged();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Helper to keep code clean
    private int getColumnForDay(String day) {
        switch (day.toLowerCase()) {
            case "mon": return 1;
            case "tue": return 2;
            case "wed": return 3;
            case "thu": return 4;
            case "fri": return 5;
            default: return -1;
        }
    }

    private JPanel coursesSummaryCard() {
        JPanel outer = card("\uD83D\uDCD7  COURSES ENROLLED & GRADES");
        String[] cols = {"Course", "Name", "Grade", "GPA"};
        courseModel = new DefaultTableModel(cols, 0); // Class-level model
        JTable table = new JTable(courseModel) { public boolean isCellEditable(int r, int c) { return false; } };
        outer.add(new JScrollPane(table), BorderLayout.CENTER);
        return outer;
    }

    private void loadCoursesData() {
        if (cachedStudentId.isEmpty()) return;
        String sql = "SELECT course_code, course_name, grade, gpa FROM courses_enrolled WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cachedStudentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    courseModel.addRow(new Object[]{rs.getString("course_code"), rs.getString("course_name"), rs.getString("grade"), rs.getString("gpa")});
                }
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    private void loadStudentDetails() {
        String sql = "SELECT s.full_name, s.student_id, s.email, s.dob, s.degree_program, s.status " +
                "FROM students s JOIN users u ON s.student_id = u.student_id WHERE u.username = ? OR s.student_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId); ps.setString(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cachedFullName = rs.getString("full_name");
                    cachedStudentId = rs.getString("student_id");
                    cachedEmail = rs.getString("email");
                    cachedDob = (rs.getDate("dob") != null) ? rs.getDate("dob").toString() : "N/A";
                    cachedDegree = rs.getString("degree_program");
                    cachedStatus = rs.getString("status");

                    // Update UI
                    profileName.setText(cachedFullName);
                    profileStudentId.setText("Student ID: " + cachedStudentId);
                    profileEmail.setText("Email: " + cachedEmail);
                    profileDob.setText("DOB: " + cachedDob);
                    profileDegree.setText(cachedDegree);
                    profileStatus.setText("Status: " + cachedStatus);
                    avatarButton.setInitials(initialsFrom(cachedFullName));

                    // Now that data is loaded, populate tables
                    loadTimetableData();
                    loadCoursesData();
                }
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
    }
}
