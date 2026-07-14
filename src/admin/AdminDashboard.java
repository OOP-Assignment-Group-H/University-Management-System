package admin;
import admin.LecturerPanel;
import admin.ProfileDialog;
import admin.StudentPanel;
import admin.CoursePanel;
import admin.DepartmentPanel;
import admin.DegreePanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;


public class AdminDashboard extends JFrame implements ActionListener {

    // Colors
    private static final Color MAROON      = new Color(123, 17, 19);
    private static final Color SIDEBAR_BG  = new Color(55, 55, 60);   // dark sidebar
    private static final Color ACTIVE_BG   = new Color(90, 20, 22);   // active button bg
    private static final Color CONTENT_BG  = new Color(245, 247, 250);
    private static final Color TEXT_DARK   = new Color(35, 35, 40);

    // Content area (panels are swapped inside this)
    private JPanel contentPanel;

    // Logged-in user (needed to rebuild the welcome panel)
    private String username;

    // Sidebar buttons
    private JButton btnDashboard;
    private JButton btnStudents;
    private JButton btnLecturers;
    private JButton btnCourses;
    private JButton btnDepartments;
    private JButton btnDegrees;
    private JButton btnLogout;

    // Header profile button
    private JButton btnProfile;

    //  Track which button is currently active
    private JButton activeBtn = null;

    //  CONSTRUCTOR
    public AdminDashboard(String username) {
        this.username = username;

        setTitle("University Management System - Admin Dashboard");
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(buildSidebar(),  BorderLayout.WEST);
        add(buildMainArea(), BorderLayout.CENTER);

        // Show the welcome panel first, with the Dashboard button highlighted
        showContent(new WelcomePanel(username));
        setActiveButton(btnDashboard);
    }


    //  SIDEBAR

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(SIDEBAR_BG);

        // Logo
        JLabel logo = new JLabel(
                "<html><div style='text-align:center; width:150px;'>"
                        + "Faculty Of Computing &amp; Technology"
                        + "</div></html>"
        );
        logo.setFont(new Font("Arial", Font.BOLD, 20));
        logo.setForeground(Color.WHITE);
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        logo.setBorder(new EmptyBorder(28, 0, 28, 0));
        sidebar.add(logo, BorderLayout.NORTH);

        // Menu buttons
        JPanel menu = new JPanel();
        menu.setBackground(SIDEBAR_BG);
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(new EmptyBorder(10, 12, 10, 12));

        btnDashboard   = makeSidebarBtn("  Dashboard");
        btnStudents    = makeSidebarBtn("  Students");
        btnLecturers   = makeSidebarBtn("  Lecturers");
        btnCourses     = makeSidebarBtn("  Courses");
        btnDepartments = makeSidebarBtn("  Departments");
        btnDegrees     = makeSidebarBtn("  Degrees");

        menu.add(btnDashboard);   menu.add(Box.createVerticalStrut(6));
        menu.add(btnStudents);    menu.add(Box.createVerticalStrut(6));
        menu.add(btnLecturers);   menu.add(Box.createVerticalStrut(6));
        menu.add(btnCourses);     menu.add(Box.createVerticalStrut(6));
        menu.add(btnDepartments); menu.add(Box.createVerticalStrut(6));
        menu.add(btnDegrees);

        sidebar.add(menu, BorderLayout.CENTER);

        // Logout
        btnLogout = makeSidebarBtn("  Logout");
        btnLogout.setForeground(new Color(200, 150, 150));
        JPanel foot = new JPanel(new BorderLayout());
        foot.setBackground(SIDEBAR_BG);
        foot.setBorder(new EmptyBorder(10, 12, 20, 12));
        foot.add(btnLogout, BorderLayout.CENTER);
        sidebar.add(foot, BorderLayout.SOUTH);

        return sidebar;
    }


    //  MAIN AREA
    private JPanel buildMainArea() {
        JPanel main = new JPanel(new BorderLayout());

        // Top header bar
        JPanel header = new JPanel(new BorderLayout());
        header.setPreferredSize(new Dimension(0, 72));
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 228)));

        // Left: title + subtitle
        JPanel hLeft = new JPanel();
        hLeft.setBackground(Color.WHITE);
        hLeft.setLayout(new BoxLayout(hLeft, BoxLayout.Y_AXIS));
        hLeft.setBorder(new EmptyBorder(12, 28, 12, 0));

        JLabel hTitle = new JLabel("Welcome To Admin Dashboard");
        hTitle.setFont(new Font("Arial", Font.BOLD, 21));
        hTitle.setForeground(TEXT_DARK);

        JLabel hSub = new JLabel("Manage university records and system settings.");
        hSub.setFont(new Font("Arial", Font.PLAIN, 12));
        hSub.setForeground(new Color(140, 140, 150));

        hLeft.add(hTitle);
        hLeft.add(hSub);
        header.add(hLeft, BorderLayout.WEST);

        // Right:
        JPanel hRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 16));
        hRight.setBackground(Color.WHITE);

        JLabel bell = new JLabel("\uD83D\uDD14");
        bell.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        bell.setCursor(new Cursor(Cursor.HAND_CURSOR));
        hRight.add(bell);

        // Profile circle button – painted with Graphics2D
        btnProfile = new JButton("A") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(MAROON);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 16));
                FontMetrics fm = g2.getFontMetrics();
                String t = getText();
                g2.drawString(t,
                        (getWidth()  - fm.stringWidth(t)) / 2,
                        (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
                g2.dispose();
            }
        };
        btnProfile.setPreferredSize(new Dimension(40, 40));
        btnProfile.setFocusPainted(false);
        btnProfile.setBorderPainted(false);
        btnProfile.setContentAreaFilled(false);
        btnProfile.setOpaque(false);
        btnProfile.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnProfile.setToolTipText("View / Edit Profile");
        btnProfile.addActionListener(this);
        hRight.add(btnProfile);

        header.add(hRight, BorderLayout.EAST);
        main.add(header, BorderLayout.NORTH);

        // Content area – panels are swapped in here
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(CONTENT_BG);
        main.add(contentPanel, BorderLayout.CENTER);

        return main;
    }


    private void showContent(JPanel panel) {
        contentPanel.removeAll();              // 1. clear old panel
        contentPanel.add(panel, BorderLayout.CENTER); // 2. add new panel
        contentPanel.revalidate();             // 3. recalculate layout
        contentPanel.repaint();               // 4. redraw
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Profile icon
        if (e.getSource() == btnProfile) {
            new ProfileDialog(this).setVisible(true);
        }

        // Logout
        else if (e.getSource() == btnLogout) {
            int ok = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to log out?", "Logout",
                    JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION) {
                // new LoginFrame().setVisible(true);   ← put your login screen here
                dispose();
            }
        }




        // Dashboard - WelcomePanel
        else if (e.getSource() == btnDashboard) {
            showContent(new WelcomePanel(username));
            setActiveButton(btnDashboard);
        }

        // Students -StudentPanel
        else if (e.getSource() == btnStudents) {
            showContent(new StudentPanel());
            setActiveButton(btnStudents);
        }

        // Lecturers -LecturerPanel
        else if (e.getSource() == btnLecturers) {
            showContent(new LecturerPanel());
            setActiveButton(btnLecturers);
        }

        // Courses - CoursePanel
        else if (e.getSource() == btnCourses) {
            showContent(new CoursePanel());
            setActiveButton(btnCourses);
        }

        // Departments - DepartmentPanel
        else if (e.getSource() == btnDepartments) {
            showContent(new DepartmentPanel());
            setActiveButton(btnDepartments);
        }

        // Degrees - DegreePanel
        else if (e.getSource() == btnDegrees) {
            showContent(new DegreePanel());
            setActiveButton(btnDegrees);
        }
    }

    private void setActiveButton(JButton clicked) {
        // Reset the previously active button
        if (activeBtn != null) {
            activeBtn.setBackground(SIDEBAR_BG);
            activeBtn.setForeground(Color.LIGHT_GRAY);
            activeBtn.setOpaque(false);
        }
        // Highlight the new active button
        clicked.setBackground(ACTIVE_BG);
        clicked.setForeground(Color.WHITE);
        clicked.setOpaque(true);
        activeBtn = clicked;
    }

    private JButton makeSidebarBtn(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setForeground(Color.LIGHT_GRAY);
        btn.setBackground(SIDEBAR_BG);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);           // transparent until active
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btn.setPreferredSize(new Dimension(196, 44));
        btn.addActionListener(this);    // all buttons handled in actionPerformed()

        // Hover effect
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                if (btn != activeBtn) {
                    btn.setBackground(new Color(75, 25, 27));
                    btn.setOpaque(true);
                }
            }
            @Override public void mouseExited(MouseEvent e) {
                if (btn != activeBtn) {
                    btn.setBackground(SIDEBAR_BG);
                    btn.setOpaque(false);
                }
            }
        });
        return btn;
    }


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new AdminDashboard("AdminUser").setVisible(true));
    }
}