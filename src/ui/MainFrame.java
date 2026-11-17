package ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.UIManager;

import model.FinanceData;

/**
 * Shell window with classic private-bank sidebar and three pages.
 */
public class MainFrame extends JFrame {
    public static final String PAGE_DASHBOARD = "dashboard";
    public static final String PAGE_TRANSACTIONS = "transactions";
    public static final String PAGE_REPORTS = "reports";

    private final CardLayout cards = new CardLayout();
    private final JPanel content = new JPanel(cards);
    private final FinanceData data = new FinanceData();

    private final DashboardPage dashboardPage;
    private final TransactionsPage transactionsPage;
    private final ReportsPage reportsPage;

    private JButton navDashboard;
    private JButton navTransactions;
    private JButton navReports;
    private String current = PAGE_DASHBOARD;

    public MainFrame() {
        super("Heritage Ledger — Personal Finance Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 700));
        setSize(1200, 760);
        setLocationRelativeTo(null);

        dashboardPage = new DashboardPage(data);
        transactionsPage = new TransactionsPage(data);
        reportsPage = new ReportsPage(data);

        content.setOpaque(true);
        content.setBackground(ClassicTheme.PAPER);
        content.add(dashboardPage, PAGE_DASHBOARD);
        content.add(transactionsPage, PAGE_TRANSACTIONS);
        content.add(reportsPage, PAGE_REPORTS);

        transactionsPage.addPropertyChangeListener("ledgerChanged", evt -> {
            dashboardPage.refresh();
            reportsPage.refresh();
        });

        JPanel root = new JPanel(new BorderLayout());
        root.add(buildSidebar(), BorderLayout.WEST);
        root.add(content, BorderLayout.CENTER);
        setContentPane(root);

        showPage(PAGE_DASHBOARD);
    }

    private JPanel buildSidebar() {
        JPanel side = new JPanel(new BorderLayout());
        side.setPreferredSize(new Dimension(250, 10));
        side.setBackground(ClassicTheme.SIDEBAR);
        side.setBorder(new EmptyBorder(28, 18, 24, 18));

        JPanel brandBlock = new JPanel();
        brandBlock.setOpaque(false);
        brandBlock.setLayout(new BoxLayout(brandBlock, BoxLayout.Y_AXIS));

        JLabel mark = new JLabel("HL");
        mark.setFont(ClassicTheme.BRAND);
        mark.setForeground(ClassicTheme.GOLD);
        mark.setAlignmentX(LEFT_ALIGNMENT);

        JLabel brand = new JLabel("Heritage Ledger");
        brand.setFont(ClassicTheme.TITLE);
        brand.setForeground(Color.WHITE);
        brand.setAlignmentX(LEFT_ALIGNMENT);

        JLabel tag = new JLabel("Classic household accounts");
        tag.setFont(ClassicTheme.SMALL);
        tag.setForeground(new Color(170, 186, 200));
        tag.setAlignmentX(LEFT_ALIGNMENT);

        brandBlock.add(mark);
        brandBlock.add(Box.createVerticalStrut(6));
        brandBlock.add(brand);
        brandBlock.add(Box.createVerticalStrut(4));
        brandBlock.add(tag);
        brandBlock.add(Box.createVerticalStrut(18));
        brandBlock.add(new GoldRule());

        JPanel nav = new JPanel();
        nav.setOpaque(false);
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        nav.setBorder(new EmptyBorder(22, 0, 0, 0));

        navDashboard = ClassicTheme.navButton("1  Dashboard", true);
        navTransactions = ClassicTheme.navButton("2  Transactions", false);
        navReports = ClassicTheme.navButton("3  Budget & Reports", false);

        navDashboard.addActionListener(e -> showPage(PAGE_DASHBOARD));
        navTransactions.addActionListener(e -> showPage(PAGE_TRANSACTIONS));
        navReports.addActionListener(e -> showPage(PAGE_REPORTS));

        nav.add(navDashboard);
        nav.add(Box.createVerticalStrut(8));
        nav.add(navTransactions);
        nav.add(Box.createVerticalStrut(8));
        nav.add(navReports);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        footer.setOpaque(false);
        JLabel foot = new JLabel("Three pages · Java Swing");
        foot.setFont(ClassicTheme.SMALL);
        foot.setForeground(new Color(140, 158, 174));
        footer.add(foot);

        side.add(brandBlock, BorderLayout.NORTH);
        side.add(nav, BorderLayout.CENTER);
        side.add(footer, BorderLayout.SOUTH);
        return side;
    }

    private void showPage(String page) {
        current = page;
        cards.show(content, page);
        restyleNav();
        if (PAGE_DASHBOARD.equals(page)) {
            dashboardPage.refresh();
        } else if (PAGE_TRANSACTIONS.equals(page)) {
            transactionsPage.refresh();
        } else if (PAGE_REPORTS.equals(page)) {
            reportsPage.refresh();
        }
    }

    private void restyleNav() {
        JPanel nav = (JPanel) navDashboard.getParent();
        nav.removeAll();
        navDashboard = ClassicTheme.navButton("1  Dashboard", PAGE_DASHBOARD.equals(current));
        navTransactions = ClassicTheme.navButton("2  Transactions", PAGE_TRANSACTIONS.equals(current));
        navReports = ClassicTheme.navButton("3  Budget & Reports", PAGE_REPORTS.equals(current));
        navDashboard.addActionListener(e -> showPage(PAGE_DASHBOARD));
        navTransactions.addActionListener(e -> showPage(PAGE_TRANSACTIONS));
        navReports.addActionListener(e -> showPage(PAGE_REPORTS));
        nav.add(navDashboard);
        nav.add(Box.createVerticalStrut(8));
        nav.add(navTransactions);
        nav.add(Box.createVerticalStrut(8));
        nav.add(navReports);
        nav.revalidate();
        nav.repaint();
    }

    private static class GoldRule extends JPanel {
        GoldRule() {
            setOpaque(false);
            setPreferredSize(new Dimension(200, 10));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 10));
            setAlignmentX(LEFT_ALIGNMENT);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(ClassicTheme.GOLD);
            g2.fill(new RoundRectangle2D.Float(0, 3, Math.min(72, getWidth()), 3, 3, 3));
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Fall back to default L&F; classic theme still paints over controls.
            }
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
