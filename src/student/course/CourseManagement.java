package student.course;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.io.*;
import java.nio.file.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

public class CourseManagement extends JFrame {

    // ---------------- palette
    static final Color MAROON      = new Color(0x8A1538);
    static final Color MAROON_DARK = new Color(0x6E0F2B);
    static final Color LINK_BLUE   = new Color(0x2C5AA0);
    static final Color BADGE_BLUE  = new Color(0x1B4F91);
    static final Color BG          = new Color(0xEFF1F5);
    static final Color CARD_BG     = Color.WHITE;
    static final Color BORDER      = new Color(0xE0E3EA);
    static final Color TEXT_MUTED  = new Color(0x6B7280);
    static final Color TRACK       = new Color(0xE4E8F0);
    static final Color STAR_GOLD   = new Color(0xE0A100);

    // Sidebar palette
    static final Color SIDEBAR_BG     = new Color(0x1B1B1F);
    static final Color SIDEBAR_HOVER  = new Color(0x2A2A30);
    static final Color SIDEBAR_TEXT   = new Color(0xC7C9D1);

    static final String MATERIALS_DIR   = "uploads/materials";
    static final String SUBMISSIONS_DIR = "uploads/submissions";

    /** Fallback id used only when this window is opened standalone (see main()) without a logged-in student. */
    static final String CURRENT_STUDENT_ID = "CS2022001";

    /** The id of the student this window was opened for. Set from the constructor argument. */
    private final String studentId;

    /** Display label shown in the UI, built from the real logged-in student's data. */
    private String displayStudentLabel = "Student";

    /** Called when the user clicks "Dashboard" in this window's sidebar, to return to the Student Portal. */
    private final Runnable onBackToDashboard;

    final CourseDAO courseDAO = new CourseDAO();
    final SubmissionDAO submissionDAO = new SubmissionDAO();
    final StudentDAO studentDAO = new StudentDAO();

    StudentDAO.StudentInfo currentStudent;
    List<Course> allCourses = new ArrayList<>();

    JPanel contentPanel;
    CardLayout cardLayout;
    JPanel cardsGrid;
    JTextField searchField;

    /** Standalone/testing entry point - uses the fallback id (see main()). */
    public CourseManagement() {
        this(CURRENT_STUDENT_ID, null);
    }

    /** Opened by the Student Portal without a back-to-dashboard link (rarely used directly). */
    public CourseManagement(String studentId) {
        this(studentId, null);
    }

    /** Real entry point used by the Student Portal: opens this window for the given logged-in student,
     *  and lets it navigate back to the portal window via onBackToDashboard. */
    public CourseManagement(String studentId, Runnable onBackToDashboard) {
        this.onBackToDashboard = onBackToDashboard;
        this.studentId = (studentId != null && !studentId.trim().isEmpty()) ? studentId : CURRENT_STUDENT_ID;

        setTitle("Student Course Portal");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1560, 900);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG);

        new File(MATERIALS_DIR).mkdirs();
        new File(SUBMISSIONS_DIR).mkdirs();

        try {
            currentStudent = studentDAO.getStudent(this.studentId);
        } catch (SQLException ex) {
            currentStudent = new StudentDAO.StudentInfo(this.studentId, this.studentId, "");
        }
        displayStudentLabel = currentStudent.studentId + " - " + currentStudent.fullName;

        add(buildSidebar(), BorderLayout.WEST);
        add(buildTopHeader(), BorderLayout.NORTH);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(BG);
        contentPanel.add(buildMyCoursesView(), "courses");
        add(contentPanel, BorderLayout.CENTER);

        reloadCourses();
    }


    //  Left sidebar
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(26, 0, 20, 0));

        JLabel brand = new JLabel("<html><span style='color:#D9A5B4;'>&#9679;</span>&nbsp;Student Portal</html>");
        brand.setForeground(Color.WHITE);
        brand.setFont(new Font("SansSerif", Font.BOLD, 18));
        brand.setAlignmentX(Component.LEFT_ALIGNMENT);
        brand.setBorder(BorderFactory.createEmptyBorder(0, 20, 30, 0));
        sidebar.add(brand);

        sidebar.add(sidebarItem("\uD83D\uDCCA  Dashboard", false, this::backToDashboard));
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(sidebarItem("\uD83D\uDCDA  My courses", true, this::showCoursesCard));

        sidebar.add(Box.createVerticalGlue());

        JPanel logoutRow = sidebarItem("\u21AA  Logout", false, null);
        logoutRow.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0x333338)),
                BorderFactory.createEmptyBorder(16, 20, 4, 20)));
        for (MouseListener ml : logoutRow.getMouseListeners()) logoutRow.removeMouseListener(ml);
        logoutRow.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = JOptionPane.showConfirmDialog(CourseManagement.this,
                        "Log out of the Student Portal?", "Logout", JOptionPane.YES_NO_OPTION);
                if (r == JOptionPane.YES_OPTION) System.exit(0);
            }
        });
        sidebar.add(logoutRow);

        return sidebar;
    }

    private JPanel sidebarItem(String text, boolean active, Runnable onClick) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(true);
        row.setBackground(active ? MAROON : SIDEBAR_BG);
        row.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        row.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel lbl = new JLabel(text);
        lbl.setForeground(active ? Color.WHITE : SIDEBAR_TEXT);
        lbl.setFont(new Font("SansSerif", active ? Font.BOLD : Font.PLAIN, 13));
        row.add(lbl, BorderLayout.WEST);

        row.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { if (onClick != null) onClick.run(); }
            public void mouseEntered(MouseEvent e) { if (!active) row.setBackground(SIDEBAR_HOVER); }
            public void mouseExited(MouseEvent e) { if (!active) row.setBackground(SIDEBAR_BG); }
        });
        return row;
    }

    /** Returns to the Student Portal Dashboard window (hides this window, brings the portal to front). */
    private void backToDashboard() {
        if (onBackToDashboard != null) {
            onBackToDashboard.run();
        } else {
            // Opened standalone (no portal window to go back to) - just close this window.
            dispose();
        }
    }


    //  Top header bar
    private JPanel buildTopHeader() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Color.WHITE);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
                BorderFactory.createEmptyBorder(18, 28, 18, 28)));

        JPanel titleBlock = new JPanel();
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        titleBlock.setOpaque(false);

        JLabel title = new JLabel("Welcome To Student Portal");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(new Color(0x1B2440));
        titleBlock.add(title);

        JLabel subtitle = new JLabel("Manage your courses, assignments and grades.");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitle.setForeground(TEXT_MUTED);
        titleBlock.add(subtitle);

        bar.add(titleBlock, BorderLayout.WEST);
        return bar;
    }

    private void showCoursesCard() { cardLayout.show(contentPanel, "courses"); }


    //  "My Courses" grid view
    private JScrollPane buildMyCoursesView() {
        JPanel page = new JPanel();
        page.setLayout(new BoxLayout(page, BoxLayout.Y_AXIS));
        page.setBackground(BG);
        page.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));

        JLabel greeting = new JLabel("Hi, " + displayStudentLabel + "! \uD83D\uDC4B");
        greeting.setFont(new Font("SansSerif", Font.BOLD, 22));
        greeting.setAlignmentX(Component.LEFT_ALIGNMENT);
        page.add(greeting);
        page.add(Box.createVerticalStrut(18));

        JPanel overview = new JPanel();
        overview.setLayout(new BoxLayout(overview, BoxLayout.Y_AXIS));
        overview.setBackground(CARD_BG);
        overview.setAlignmentX(Component.LEFT_ALIGNMENT);
        overview.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1, true), BorderFactory.createEmptyBorder(18, 20, 20, 20)));

        JLabel overviewTitle = new JLabel("Course overview");
        overviewTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        overviewTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        overview.add(overviewTitle);
        overview.add(Box.createVerticalStrut(10));
        overview.add(new JSeparator());
        overview.add(Box.createVerticalStrut(10));

        JPanel filterRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterRow.setOpaque(false);
        filterRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterRow.add(dropdown("Starred"));
        searchField = new JTextField(16);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1, true), BorderFactory.createEmptyBorder(6, 8, 6, 8)));
        searchField.putClientProperty("JTextField.placeholderText", "Search");
        searchField.getDocument().addDocumentListener(new SimpleDocListener(this::renderCards));
        filterRow.add(searchField);
        JComboBox<String> sort = dropdown("Sort by course name");
        sort.addActionListener(e -> renderCards());
        filterRow.add(sort);
        filterRow.add(dropdown("Card"));
        overview.add(filterRow);
        overview.add(Box.createVerticalStrut(16));

        cardsGrid = new JPanel(new GridLayout(0, 3, 24, 24));
        cardsGrid.setOpaque(true);
        cardsGrid.setBackground(CARD_BG);
        cardsGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        overview.add(cardsGrid);

        page.add(overview);

        JScrollPane scroll = new JScrollPane(page);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private JComboBox<String> dropdown(String label) {
        JComboBox<String> box = new JComboBox<>(new String[]{label});
        box.setBorder(BorderFactory.createCompoundBorder(new LineBorder(BORDER, 1, true), BorderFactory.createEmptyBorder(2, 4, 2, 4)));
        return box;
    }

    /** Small helper so a lambda can be used as a DocumentListener without repeating 3 methods everywhere. */
    static class SimpleDocListener implements javax.swing.event.DocumentListener {
        final Runnable action;
        SimpleDocListener(Runnable action) { this.action = action; }
        public void insertUpdate(javax.swing.event.DocumentEvent e) { action.run(); }
        public void removeUpdate(javax.swing.event.DocumentEvent e) { action.run(); }
        public void changedUpdate(javax.swing.event.DocumentEvent e) { action.run(); }
    }

    private void reloadCourses() {
        try {
            allCourses = courseDAO.getEnrolledCourses(this.studentId);
        } catch (SQLException ex) {
            allCourses = new ArrayList<>();
            JOptionPane.showMessageDialog(this,
                    "Could not load courses from the database.\nMake sure MySQL is running.\n" + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        renderCards();
    }

    private void renderCards() {
        cardsGrid.removeAll();
        String query = searchField == null ? "" : searchField.getText().trim().toLowerCase();
        List<Course> filtered = new ArrayList<>();
        for (Course c : allCourses) {
            if (query.isEmpty() || c.name.toLowerCase().contains(query) || c.code.toLowerCase().contains(query)) {
                filtered.add(c);
            }
        }
        filtered.sort(Comparator.comparing(c -> c.name));
        for (Course c : filtered) cardsGrid.add(courseCard(c));
        cardsGrid.revalidate();
        cardsGrid.repaint();
    }

    private static final Color[] BANNER_COLORS = {
            new Color(0xAEE6DC), new Color(0xB7C4DE), new Color(0xD6CFF0),
            new Color(0xE3C7E6), new Color(0xEDE3F5), new Color(0xC9DCEA)
    };

    private static final int CARD_ARC = 40;

    /** Rounds only the top corners — used for the coloured banner strip of a card. */
    static class RoundedTopPanel extends JPanel {
        private final int arc;
        RoundedTopPanel(LayoutManager lm, int arc) { super(lm); this.arc = arc; setOpaque(false); }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            Area area = new Area(new RoundRectangle2D.Float(0, 0, w, h, arc, arc));
            area.add(new Area(new Rectangle2D.Float(0, h / 2f, w, h / 2f)));
            g2.setColor(getBackground());
            g2.fill(area);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Rounds only the bottom corners — used for the white body of a card. */
    static class RoundedBottomPanel extends JPanel {
        private final int arc;
        RoundedBottomPanel(LayoutManager lm, int arc) { super(lm); this.arc = arc; setOpaque(false); }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            Area area = new Area(new RoundRectangle2D.Float(0, 0, w, h, arc, arc));
            area.add(new Area(new Rectangle2D.Float(0, 0, w, h / 2f)));
            g2.setColor(getBackground());
            g2.fill(area);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Rounds all corners — used for the small category badge/pill. */
    static class RoundedBadge extends JLabel {
        private final int arc;
        RoundedBadge(String text, int arc) {
            super(text);
            this.arc = arc;
            setOpaque(false);
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private JPanel courseCard(Course course) {
        JPanel card = new JPanel(new BorderLayout());
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(360, 250));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        Color banner = BANNER_COLORS[Math.abs(course.code.hashCode()) % BANNER_COLORS.length];
        RoundedTopPanel bannerPanel = new RoundedTopPanel(new BorderLayout(), 40);
        bannerPanel.setBackground(banner);
        bannerPanel.setPreferredSize(new Dimension(0, 130));
        RoundedBadge badge = new RoundedBadge("  " + courseCategory(course.code) + "  ", 16);
        badge.setBackground(BADGE_BLUE);
        badge.setForeground(Color.WHITE);
        badge.setFont(new Font("SansSerif", Font.BOLD, 11));
        badge.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        JPanel badgeWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        badgeWrap.setOpaque(false);
        badgeWrap.add(badge);
        bannerPanel.add(badgeWrap, BorderLayout.NORTH);
        card.add(bannerPanel, BorderLayout.NORTH);

        RoundedBottomPanel body = new RoundedBottomPanel(null, 40);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(CARD_BG);
        body.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));

        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);
        //JLabel star = new JLabel("\u2605 ");
        //star.setForeground(STAR_GOLD);
        JLabel titleLbl = new JLabel("<html><u>" + course.code + " - " + course.name + "</u></html>");
        titleLbl.setForeground(LINK_BLUE);
        titleLbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JPanel titleLine = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        titleLine.setOpaque(false);
        //titleLine.add(star);
        titleLine.add(titleLbl);
        titleRow.add(titleLine, BorderLayout.CENTER);
        body.add(titleRow);
        body.add(Box.createVerticalStrut(10));

        if (course.completedPct > 0) {
            JProgressBar bar = new JProgressBar(0, 100);
            bar.setValue(course.completedPct);
            bar.setPreferredSize(new Dimension(0, 8));
            bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 8));
            bar.setForeground(course.completedPct >= 100 ? new Color(0x2E8B57) : LINK_BLUE);
            bar.setBackground(TRACK);
            bar.setBorderPainted(false);
            bar.setAlignmentX(Component.LEFT_ALIGNMENT);
            body.add(bar);
            body.add(Box.createVerticalStrut(4));
            JLabel pct = new JLabel(course.completedPct + "% complete");
            pct.setFont(new Font("SansSerif", Font.PLAIN, 10));
            pct.setForeground(TEXT_MUTED);
            pct.setAlignmentX(Component.LEFT_ALIGNMENT);
            body.add(pct);
        }

        card.add(body, BorderLayout.CENTER);

        MouseAdapter open = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { openCourse(course); }
        };
        card.addMouseListener(open);
        titleLbl.addMouseListener(open);
        bannerPanel.addMouseListener(open);

        return card;
    }

    private String courseCategory(String code) {
        if (code.startsWith("CTEC")) return "CTEC - Computer Technology";
        if (code.startsWith("GTEC")) return "GTEC - General Technology";
        if (code.startsWith("DELT")) return "DELT - Language";
        return code;
    }


    //  Course detail
    private void openCourse(Course course) {
        JPanel detail = buildCourseDetailView(course);
        contentPanel.add(detail, "detail-" + course.id);
        cardLayout.show(contentPanel, "detail-" + course.id);
    }

    private JPanel buildCourseDetailView(Course course) {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(BG);

        JPanel scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setBackground(BG);

        // maroon tab bar
        JPanel tabBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tabBar.setBackground(MAROON);
        tabBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        String[] tabs = {"Course", "Participants", "Grades", "Competencies"};
        for (int i = 0; i < tabs.length; i++) {
            JLabel tab = new JLabel(tabs[i]);
            tab.setForeground(Color.WHITE);
            tab.setFont(new Font("SansSerif", i == 0 ? Font.BOLD : Font.PLAIN, 13));
            tab.setBorder(BorderFactory.createCompoundBorder(
                    i == 0 ? BorderFactory.createMatteBorder(0, 0, 3, 0, Color.WHITE) : BorderFactory.createEmptyBorder(0, 0, 3, 0),
                    BorderFactory.createEmptyBorder(12, 18, 9, 18)));
            tab.setOpaque(true);
            tab.setBackground(i == 0 ? MAROON_DARK : MAROON);
            tabBar.add(tab);
        }
        scrollContent.add(tabBar);

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(BG);
        body.setBorder(BorderFactory.createEmptyBorder(18, 26, 26, 26));

        JLabel back = new JLabel("\u2190 My courses");
        back.setForeground(LINK_BLUE);
        back.setFont(new Font("SansSerif", Font.PLAIN, 12));
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        back.setAlignmentX(Component.LEFT_ALIGNMENT);
        back.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { showCoursesCard(); }
        });
        body.add(back);
        body.add(Box.createVerticalStrut(10));

        JLabel title = new JLabel(course.code + " - " + course.name);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(title);
        body.add(Box.createVerticalStrut(16));

        try {
            List<Material> materials = courseDAO.getMaterialsForCourse(course.id);
            LinkedHashMap<String, List<Material>> sections = CourseDAO.groupBySection(materials);
            for (Map.Entry<String, List<Material>> entry : sections.entrySet()) {
                body.add(accordionSection(entry.getKey(), materialListPanel(entry.getValue())));
                body.add(Box.createVerticalStrut(10));
            }
        } catch (SQLException ex) {
            JLabel err = new JLabel("Could not load course materials: " + ex.getMessage());
            err.setForeground(Color.RED);
            body.add(err);
        }

        // Assignments accordion (submit / list / download)
        JPanel assignmentsPanel = new JPanel();
        assignmentsPanel.setLayout(new BoxLayout(assignmentsPanel, BoxLayout.Y_AXIS));
        assignmentsPanel.setOpaque(false);
        JPanel subListPanel = new JPanel();
        subListPanel.setLayout(new BoxLayout(subListPanel, BoxLayout.Y_AXIS));
        subListPanel.setOpaque(false);
        subListPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        refreshSubmissionList(subListPanel, course.id);
        assignmentsPanel.add(subListPanel);
        assignmentsPanel.add(Box.createVerticalStrut(8));
        JButton submitBtn = new JButton("Submit Assignment");
        submitBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        submitBtn.setBackground(LINK_BLUE);
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFocusPainted(false);
        submitBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        submitBtn.addActionListener(e -> submitAssignment(course, subListPanel));
        assignmentsPanel.add(submitBtn);
        body.add(accordionSection("Assignments", assignmentsPanel));

        scrollContent.add(body);

        JScrollPane scroll = new JScrollPane(scrollContent);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        outer.add(scroll, BorderLayout.CENTER);
        return outer;
    }

    /** A collapsible white card with a chevron header, mimicking Moodle's course-section blocks. */
    private JPanel accordionSection(String title, JComponent content) {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBackground(CARD_BG);
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.setBorder(new LineBorder(BORDER, 1, true));
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(CARD_BG);
        header.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        header.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JLabel chevron = new JLabel("\u25BC  " + title);
        chevron.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.add(chevron, BorderLayout.WEST);
        wrapper.add(header);

        content.setBorder(BorderFactory.createEmptyBorder(0, 16, 14, 16));
        content.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.add(content);

        header.addMouseListener(new MouseAdapter() {
            boolean expanded = true;
            public void mouseClicked(MouseEvent e) {
                expanded = !expanded;
                content.setVisible(expanded);
                chevron.setText((expanded ? "\u25BC  " : "\u25B6  ") + title);
                wrapper.revalidate();
            }
        });
        return wrapper;
    }

    private JPanel materialListPanel(List<Material> items) {
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setOpaque(false);
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yy, HH:mm");
        for (Material m : items) {
            JPanel row = new JPanel(new BorderLayout(8, 0));
            row.setOpaque(false);
            row.setAlignmentX(Component.LEFT_ALIGNMENT);
            row.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xF0F0F0)),
                    BorderFactory.createEmptyBorder(8, 0, 8, 0)));
            row.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
            left.setOpaque(false);
            left.add(new JLabel(iconFor(m.type)));
            JPanel textCol = new JPanel();
            textCol.setLayout(new BoxLayout(textCol, BoxLayout.Y_AXIS));
            textCol.setOpaque(false);
            JLabel nameLbl = new JLabel("<html><u>" + m.title + "</u></html>");
            nameLbl.setForeground(LINK_BLUE);
            nameLbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
            textCol.add(nameLbl);
            if (m.uploadedAt != null && !m.type.equals("Link") && !m.type.equals("Folder")) {
                JLabel dateLbl = new JLabel("Uploaded " + fmt.format(m.uploadedAt));
                dateLbl.setFont(new Font("SansSerif", Font.PLAIN, 10));
                dateLbl.setForeground(TEXT_MUTED);
                textCol.add(dateLbl);
            }
            left.add(textCol);
            row.add(left, BorderLayout.CENTER);

            MouseAdapter click = new MouseAdapter() {
                public void mouseClicked(MouseEvent e) { openMaterial(m); }
            };
            row.addMouseListener(click);
            nameLbl.addMouseListener(click);

            list.add(row);
        }
        if (items.isEmpty()) {
            JLabel none = new JLabel("Nothing here yet.");
            none.setForeground(TEXT_MUTED);
            list.add(none);
        }
        return list;
    }

    private Icon iconFor(String type) {
        Color bg; String glyph;
        switch (type) {
            case "PDF":    bg = new Color(0xE86C60); glyph = "\uD83D\uDCC4"; break;
            case "Video":  bg = new Color(0xE8722E); glyph = "\u25B6"; break;
            case "Folder": bg = new Color(0x2E8B57); glyph = "\uD83D\uDCC1"; break;
            default:       bg = new Color(0x5F7BC7); glyph = "\uD83D\uDD17"; break;
        }
        return new SmallTileIcon(bg, glyph);
    }

    static class SmallTileIcon implements Icon {
        final Color bg; final String glyph;
        SmallTileIcon(Color bg, String glyph) { this.bg = bg; this.glyph = glyph; }
        public int getIconWidth() { return 22; }
        public int getIconHeight() { return 22; }
        public void paintIcon(Component c, Graphics g0, int x, int y) {
            Graphics2D g = (Graphics2D) g0.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(bg);
            g.fillRoundRect(x, y, 22, 22, 5, 5);
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.PLAIN, 11));
            g.drawString(glyph, x + 4, y + 15);
            g.dispose();
        }
    }

    private void openMaterial(Material m) {
        if (m.type.equals("Link")) {
            JOptionPane.showMessageDialog(this, "This would open an external link:\n" + m.filePath,
                    "External Link", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (m.type.equals("Folder")) {
            JOptionPane.showMessageDialog(this, "This would open a folder of resources (demo).",
                    "Folder", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        downloadFile(m.filePath, new File(m.filePath).getName());
    }

    // ==================================================================
    //  Assignment submit / list / download (unchanged logic, MySQL-backed)
    // ==================================================================
    private void refreshSubmissionList(JPanel subListPanel, int courseId) {
        subListPanel.removeAll();
        try {
            List<Submission> subs = submissionDAO.getSubmissions(courseId, this.studentId);
            if (subs.isEmpty()) {
                JLabel none = new JLabel("No submissions yet.");
                none.setFont(new Font("SansSerif", Font.ITALIC, 11));
                none.setForeground(TEXT_MUTED);
                subListPanel.add(none);
            } else {
                SimpleDateFormat fmt = new SimpleDateFormat("dd MMM yyyy, HH:mm");
                for (Submission s : subs) {
                    JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
                    row.setOpaque(false);
                    JLabel info = new JLabel(s.fileName + "  -  submitted " + fmt.format(s.submittedAt));
                    info.setFont(new Font("SansSerif", Font.PLAIN, 11));
                    JButton download = new JButton("Download");
                    download.setFont(new Font("SansSerif", Font.PLAIN, 10));
                    download.setFocusPainted(false);
                    download.addActionListener(e -> downloadFile(s.filePath, s.fileName));
                    row.add(info);
                    row.add(download);
                    subListPanel.add(row);
                }
            }
        } catch (SQLException ex) {
            JLabel err = new JLabel("Could not load submissions: " + ex.getMessage());
            err.setForeground(Color.RED);
            subListPanel.add(err);
        }
        subListPanel.revalidate();
        subListPanel.repaint();
    }

    private void submitAssignment(Course course, JPanel subListPanel) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select assignment file to submit for " + course.code);
        int result = chooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;

        File selected = chooser.getSelectedFile();
        try {
            File destDir = new File(SUBMISSIONS_DIR, String.valueOf(course.id));
            destDir.mkdirs();
            String storedName = System.currentTimeMillis() + "_" + selected.getName();
            File dest = new File(destDir, storedName);
            Files.copy(selected.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);

            submissionDAO.addSubmission(course.id, this.studentId, selected.getName(), dest.getPath());

            JOptionPane.showMessageDialog(this, "Assignment submitted successfully!",
                    "Submission Received", JOptionPane.INFORMATION_MESSAGE);
            refreshSubmissionList(subListPanel, course.id);
        } catch (IOException | SQLException ex) {
            JOptionPane.showMessageDialog(this, "Submission failed: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void downloadFile(String sourcePath, String suggestedName) {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File(suggestedName));
        chooser.setDialogTitle("Save file as");
        int result = chooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;

        try {
            File src = new File(sourcePath);
            if (!src.exists()) {
                JOptionPane.showMessageDialog(this, "Source file not found on server: " + sourcePath,
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Files.copy(src.toPath(), chooser.getSelectedFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
            JOptionPane.showMessageDialog(this, "Downloaded to " + chooser.getSelectedFile().getPath(),
                    "Download Complete", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Download failed: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
            new CourseManagement().setVisible(true);
        });
    }
}

