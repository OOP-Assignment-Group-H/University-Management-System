package lecturer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

/**
 * Main window for the Lecturer Portal.
 * Holds the sidebar navigation, the top header bar, and a CardLayout
 * content area that swaps between the different feature panels.
 */
public class LecturerPortal extends JFrame {

    public static final Color MAROON = new Color(123, 17, 19);
    public static final Color MAROON_DARK = new Color(90, 12, 14);
    public static final Color GOLD = new Color(230, 180, 60);
    public static final Color SIDEBAR_BG = new Color(38, 38, 42);
    public static final Color SIDEBAR_TEXT = new Color(210, 210, 215);
    public static final Color PAGE_BG = new Color(244, 245, 247);
    public static final Color CARD_BG = Color.WHITE;
    public static final Color CARD_BORDER = new Color(228, 228, 232);
    public static final Color TEXT_DARK = new Color(35, 35, 40);
    public static final Color TEXT_GREY = new Color(110, 116, 122);

    private final String lecturerName;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(cardLayout);

    private final java.util.List<SidebarButton> navButtons = new java.util.ArrayList<>();
    private JLabel sidebarAvatarLabel;
    private HeaderPanel headerPanel;

    public LecturerPortal() {
        this("Lecturer");
    }

    public LecturerPortal(String lecturerName) {
        this.lecturerName = lecturerName;

        setTitle("University Management System - Lecturer Dashboard");
        setSize(1300, 780);
        setMinimumSize(new Dimension(1050, 650));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(PAGE_BG);

        add(buildSidebar(), BorderLayout.WEST);

        JPanel rightSide = new JPanel(new BorderLayout());
        rightSide.setBackground(PAGE_BG);

        headerPanel = new HeaderPanel("Lecturer Dashboard", "Manage your courses, students and academic records.", lecturerName);
        rightSide.add(headerPanel, BorderLayout.NORTH);

        contentPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        contentPanel.setOpaque(false);

        contentPanel.add(new DashboardHomePanel(lecturerName), "DASHBOARD");
        contentPanel.add(new LecturerProfilePanel(this::updateSidebarPhoto), "PROFILE");
        contentPanel.add(new CoursesPanel(), "COURSES");
        contentPanel.add(new TimeTablePanel(), "TIMETABLE");
        contentPanel.add(new StudentPanel(), "CLASSLIST");
        contentPanel.add(new AssignmentPanel(), "ASSIGNMENTS");
        contentPanel.add(new GradePanel(), "GRADES");
        contentPanel.add(new CalendarPanel(), "CALENDAR");

        rightSide.add(contentPanel, BorderLayout.CENTER);
        add(rightSide, BorderLayout.CENTER);

        showCard("DASHBOARD", navButtons.get(0));
    }

    private JPanel buildSidebar() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(SIDEBAR_BG);
        panel.setPreferredSize(new Dimension(230, 0));
        panel.setBorder(new EmptyBorder(24, 0, 20, 0));

        JLabel facultyLabel = new JLabel("<html><div style='text-align:center;'>Faculty Of<br>Computing &amp;<br>Technology</div></html>");
        facultyLabel.setFont(new Font("Arial", Font.BOLD, 17));
        facultyLabel.setForeground(Color.WHITE);
        facultyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        facultyLabel.setBorder(new EmptyBorder(0, 10, 24, 10));
        panel.add(facultyLabel);

        String[][] items = {
                {"Dashboard", "DASHBOARD"},
                {"Profile", "PROFILE"},
                {"Courses", "COURSES"},
                {"Timetable", "TIMETABLE"},
                {"Class List & Students", "CLASSLIST"},
                {"Assignments & Quizzes", "ASSIGNMENTS"},
                {"Grades", "GRADES"},
                {"Calendar", "CALENDAR"}
        };

        for (String[] item : items) {
            SidebarButton btn = new SidebarButton(item[0]);
            String cardName = item[1];
            btn.addActionListener(e -> showCard(cardName, btn));
            navButtons.add(btn);
            panel.add(btn);
        }

        panel.add(Box.createVerticalGlue());

        SidebarButton logoutBtn = new SidebarButton("Logout");
        logoutBtn.setLogoutStyle(true);
        logoutBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?",
                    "Confirm Logout", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                this.dispose();
            }
        });
        panel.add(logoutBtn);

        return panel;
    }

    private void showCard(String cardName, SidebarButton active) {
        cardLayout.show(contentPanel, cardName);
        for (SidebarButton b : navButtons) {
            b.setActive(b == active);
        }
        if (headerPanel != null) {
            headerPanel.setSectionTitle(active.getText());
        }
    }

    private void updateSidebarPhoto(File file) {
        if (sidebarAvatarLabel != null) {
            sidebarAvatarLabel.setIcon(AvatarUtil.loadCircularIcon(file, 60));
        }
        if (headerPanel != null) {
            headerPanel.updateAvatarPhoto(file);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LecturerPortal("Dr. Alan Reed").setVisible(true));
    }
}