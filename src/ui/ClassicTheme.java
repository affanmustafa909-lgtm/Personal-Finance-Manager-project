package ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

/**
 * Classic ledger / private-bank visual language for the finance app.
 */
public final class ClassicTheme {
    public static final Color NAVY = new Color(18, 42, 66);
    public static final Color NAVY_DEEP = new Color(10, 28, 46);
    public static final Color FOREST = new Color(27, 77, 62);
    public static final Color FOREST_SOFT = new Color(214, 232, 224);
    public static final Color GOLD = new Color(184, 148, 74);
    public static final Color GOLD_SOFT = new Color(242, 230, 200);
    public static final Color PAPER = new Color(244, 246, 248);
    public static final Color PANEL = new Color(255, 255, 255);
    public static final Color INK = new Color(28, 36, 44);
    public static final Color MUTED = new Color(98, 110, 122);
    public static final Color RULE = new Color(210, 216, 222);
    public static final Color DANGER = new Color(140, 48, 48);
    public static final Color DANGER_SOFT = new Color(248, 230, 230);
    public static final Color SIDEBAR = new Color(14, 34, 54);

    public static final Font BRAND = new Font("Georgia", Font.BOLD, 28);
    public static final Font DISPLAY = new Font("Georgia", Font.BOLD, 22);
    public static final Font TITLE = new Font("Georgia", Font.BOLD, 18);
    public static final Font BODY = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BODY_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font SMALL_BOLD = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font MONO = new Font("Consolas", Font.PLAIN, 14);

    private static final NumberFormat CURRENCY =
            NumberFormat.getCurrencyInstance(Locale.forLanguageTag("en-PK"));

    private ClassicTheme() {}

    public static String money(double amount) {
        return CURRENCY.format(amount);
    }

    public static Border pagePadding() {
        return new EmptyBorder(28, 32, 28, 32);
    }

    public static Border softCard() {
        return BorderFactory.createCompoundBorder(
                new SoftLineBorder(RULE, 1, 10),
                new EmptyBorder(18, 20, 18, 20)
        );
    }

    public static JLabel heading(String text) {
        JLabel label = new JLabel(text);
        label.setFont(DISPLAY);
        label.setForeground(NAVY);
        return label;
    }

    public static JLabel subheading(String text) {
        JLabel label = new JLabel(text);
        label.setFont(BODY);
        label.setForeground(MUTED);
        return label;
    }

    public static JLabel caption(String text) {
        JLabel label = new JLabel(text.toUpperCase(Locale.ROOT));
        label.setFont(SMALL_BOLD);
        label.setForeground(MUTED);
        return label;
    }

    public static JPanel card() {
        JPanel panel = new JPanel();
        panel.setOpaque(true);
        panel.setBackground(PANEL);
        panel.setBorder(softCard());
        return panel;
    }

    public static JButton primaryButton(String text) {
        JButton button = new FlatButton(text, FOREST, Color.WHITE, GOLD);
        button.setPreferredSize(new Dimension(140, 40));
        return button;
    }

    public static JButton secondaryButton(String text) {
        JButton button = new FlatButton(text, PANEL, NAVY, GOLD);
        button.setPreferredSize(new Dimension(140, 40));
        return button;
    }

    public static JButton navButton(String text, boolean active) {
        Color bg = active ? GOLD : new Color(0, 0, 0, 0);
        Color fg = active ? NAVY_DEEP : new Color(220, 228, 236);
        FlatButton button = new FlatButton(text, bg, fg, GOLD);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setPreferredSize(new Dimension(220, 44));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        button.setBorder(new EmptyBorder(10, 18, 10, 18));
        return button;
    }

    public static void styleField(JTextField field) {
        field.setFont(BODY);
        field.setForeground(INK);
        field.setBackground(PANEL);
        field.setCaretColor(NAVY);
        field.setBorder(BorderFactory.createCompoundBorder(
                new SoftLineBorder(RULE, 1, 6),
                new EmptyBorder(8, 10, 8, 10)
        ));
        field.setPreferredSize(new Dimension(180, 38));
    }

    public static void styleTable(JTable table) {
        table.setFont(BODY);
        table.setForeground(INK);
        table.setRowHeight(38);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setGridColor(RULE);
        table.setSelectionBackground(FOREST_SOFT);
        table.setSelectionForeground(NAVY);
        table.setFillsViewportHeight(true);
        table.setBackground(PANEL);
        table.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setResizingAllowed(true);
        header.setFont(SMALL_BOLD);
        header.setForeground(MUTED);
        header.setBackground(PAPER);
        header.setPreferredSize(new Dimension(100, 40));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, RULE));

        DefaultTableCellRenderer left = new DefaultTableCellRenderer();
        left.setBorder(new EmptyBorder(0, 12, 0, 12));
        left.setBackground(PANEL);
        table.setDefaultRenderer(Object.class, left);
    }

    public static void styleScroll(JScrollPane scroll) {
        scroll.setBorder(new SoftLineBorder(RULE, 1, 8));
        scroll.getViewport().setBackground(PANEL);
        scroll.getVerticalScrollBar().setUI(new ThinScrollBarUI());
        scroll.getHorizontalScrollBar().setUI(new ThinScrollBarUI());
    }

    public static JPanel metricTile(String label, String value, Color accent) {
        JPanel tile = new JPanel();
        tile.setLayout(new java.awt.BorderLayout(0, 8));
        tile.setOpaque(true);
        tile.setBackground(PANEL);
        tile.setBorder(BorderFactory.createCompoundBorder(
                new SoftLineBorder(RULE, 1, 10),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 4, 0, 0, accent),
                        new EmptyBorder(16, 18, 16, 18)
                )
        ));

        JLabel caption = caption(label);
        JLabel amount = new JLabel(value);
        amount.setFont(TITLE);
        amount.setForeground(NAVY);
        tile.add(caption, java.awt.BorderLayout.NORTH);
        tile.add(amount, java.awt.BorderLayout.CENTER);
        return tile;
    }

    /** Soft rounded line border. */
    public static class SoftLineBorder extends AbstractBorder {
        private final Color color;
        private final int thickness;
        private final int radius;

        public SoftLineBorder(Color color, int thickness, int radius) {
            this.color = color;
            this.thickness = thickness;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.draw(new RoundRectangle2D.Float(
                    x + 0.5f, y + 0.5f,
                    width - 1f, height - 1f,
                    radius, radius
            ));
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(thickness, thickness, thickness, thickness);
        }
    }

    private static class FlatButton extends JButton {
        private final Color bg;
        private final Color fg;
        private final Color hoverBorder;

        FlatButton(String text, Color bg, Color fg, Color hoverBorder) {
            super(text);
            this.bg = bg;
            this.fg = fg;
            this.hoverBorder = hoverBorder;
            setFont(BODY_BOLD);
            setForeground(fg);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setBorder(new EmptyBorder(10, 16, 10, 16));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color fill = bg.getAlpha() == 0 ? new Color(255, 255, 255, getModel().isRollover() ? 28 : 0) : bg;
            if (getModel().isPressed() && bg.getAlpha() != 0) {
                fill = fill.darker();
            }
            g2.setColor(fill);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
            if (getModel().isRollover()) {
                g2.setColor(hoverBorder);
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1f, getHeight() - 1f, 8, 8));
            } else if (bg.getAlpha() != 0 && !bg.equals(FOREST)) {
                g2.setColor(RULE);
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1f, getHeight() - 1f, 8, 8));
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class ThinScrollBarUI extends BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            thumbColor = new Color(180, 190, 200);
            trackColor = PAPER;
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return zeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return zeroButton();
        }

        private JButton zeroButton() {
            JButton b = new JButton();
            b.setPreferredSize(new Dimension(0, 0));
            b.setMinimumSize(new Dimension(0, 0));
            b.setMaximumSize(new Dimension(0, 0));
            return b;
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, java.awt.Rectangle thumbBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(thumbColor);
            g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2,
                    thumbBounds.width - 4, thumbBounds.height - 4, 8, 8);
            g2.dispose();
        }
    }
}
