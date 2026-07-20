package student;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class SidebarPanel extends JPanel {

    private static final Color SIDEBAR_BG        = new Color(43, 43, 46);
    private static final Color SIDEBAR_HOVER     = new Color(58, 58, 62);
    private static final Color SIDEBAR_SELECTED  = new Color(232, 112, 58);
    private static final Color SIDEBAR_TEXT      = new Color(200, 200, 205);
    private static final Color SIDEBAR_TEXT_SEL  = Color.WHITE;
    private static final Color SIDEBAR_LOGOUT    = new Color(190, 175, 176);
    private static final Font FONT_TITLE   = new Font("SansSerif", Font.BOLD, 20);
    private static final Font FONT_NAV     = new Font("SansSerif", Font.PLAIN, 15);
    private static final Font FONT_NAV_SEL = new Font("SansSerif", Font.BOLD, 15);

    private final Map<String, JPanel> navItems = new LinkedHashMap<>();
    private String selectedKey;
    private final Consumer<String> onSelect;

    public SidebarPanel(String[] keys, String[] labels, String[] icons, String brandText,
                        Consumer<String> onSelect, Runnable onLogout) {
        this.onSelect = onSelect;

        setLayout(new BorderLayout());
        setBackground(SIDEBAR_BG);
        setPreferredSize(new Dimension(240, 0));
        setBorder(new EmptyBorder(0, 0, 0, 0));

        JPanel brand = new JPanel();
        brand.setBackground(SIDEBAR_BG);
        brand.setLayout(new BoxLayout(brand, BoxLayout.Y_AXIS));
        brand.setBorder(new EmptyBorder(24, 20, 24, 20));
        JLabel title = new JLabel(brandText);
        title.setForeground(Color.WHITE);
        title.setFont(FONT_TITLE);
        brand.add(title);

        JPanel itemsPanel = new JPanel();
        itemsPanel.setBackground(SIDEBAR_BG);
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        itemsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        for (int i = 0; i < keys.length; i++) {
            JPanel item = buildNavItem(keys[i], labels[i], icons[i]);
            navItems.put(keys[i], item);
            itemsPanel.add(item);
            itemsPanel.add(Box.createVerticalStrut(8));
        }

        JScrollPane itemsScroll = new JScrollPane(itemsPanel);
        itemsScroll.setBorder(null);
        itemsScroll.setOpaque(false);
        itemsScroll.getViewport().setOpaque(false);
        itemsScroll.getVerticalScrollBar().setUnitIncrement(12);
        itemsScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel logoutWrap = new JPanel(new BorderLayout());
        logoutWrap.setBackground(SIDEBAR_BG);
        logoutWrap.setBorder(new EmptyBorder(14, 24, 24, 20));
        JLabel logout = new JLabel("Logout");
        logout.setForeground(SIDEBAR_LOGOUT);
        logout.setFont(FONT_NAV);
        logout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logout.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (onLogout != null) onLogout.run();
            }
            @Override public void mouseEntered(MouseEvent e) { logout.setForeground(Color.WHITE); }
            @Override public void mouseExited(MouseEvent e) { logout.setForeground(SIDEBAR_LOGOUT); }
        });
        logoutWrap.add(logout, BorderLayout.WEST);

        add(brand, BorderLayout.NORTH);
        add(itemsScroll, BorderLayout.CENTER);
        add(logoutWrap, BorderLayout.SOUTH);

        if (keys.length > 0) {
            select(keys[0]);
        }
    }

    private JPanel buildNavItem(String key, String label, String icon) {
        JPanel item = new JPanel(new BorderLayout());
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        item.setBackground(SIDEBAR_BG);
        item.setBorder(new EmptyBorder(10, 14, 10, 14));
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel text = new JLabel(icon + "   " + label);
        text.setForeground(SIDEBAR_TEXT);
        text.setFont(FONT_NAV);
        item.add(text, BorderLayout.WEST);
        item.putClientProperty("labelRef", text);

        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                select(key);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!key.equals(selectedKey)) {
                    item.setBackground(SIDEBAR_HOVER);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!key.equals(selectedKey)) {
                    item.setBackground(SIDEBAR_BG);
                }
            }
        });

        return item;
    }

    public void select(String key) {
        selectedKey = key;
        for (Map.Entry<String, JPanel> entry : navItems.entrySet()) {
            JPanel panel = entry.getValue();
            JLabel label = (JLabel) panel.getClientProperty("labelRef");
            boolean isSelected = entry.getKey().equals(key);
            panel.setBackground(isSelected ? SIDEBAR_SELECTED : SIDEBAR_BG);
            label.setForeground(isSelected ? SIDEBAR_TEXT_SEL : SIDEBAR_TEXT);
            label.setFont(isSelected ? FONT_NAV_SEL : FONT_NAV);
        }
        if (onSelect != null) {
            onSelect.accept(key);
        }
    }
}