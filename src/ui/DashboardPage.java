package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import model.FinanceData;
import model.Transaction;

/**
 * Page 1 — Classic ledger home: balance, income/expense tiles, recent activity.
 */
public class DashboardPage extends JPanel {
    private final FinanceData data;
    private final JPanel metricsRow = new JPanel(new GridLayout(1, 3, 16, 0));
    private final JPanel activityList = new JPanel();

    public DashboardPage(FinanceData data) {
        this.data = data;
        setOpaque(true);
        setBackground(ClassicTheme.PAPER);
        setLayout(new BorderLayout(0, 20));
        setBorder(ClassicTheme.pagePadding());

        add(buildHeader(), BorderLayout.NORTH);
        add(buildBody(), BorderLayout.CENTER);
        refresh();
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel titles = new JPanel();
        titles.setOpaque(false);
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));
        titles.add(ClassicTheme.heading("Ledger Overview"));
        titles.add(Box.createVerticalStrut(6));
        titles.add(ClassicTheme.subheading("A quiet summary of your household accounts."));

        JLabel date = new JLabel(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy")));
        date.setFont(ClassicTheme.SMALL);
        date.setForeground(ClassicTheme.MUTED);

        header.add(titles, BorderLayout.WEST);
        header.add(date, BorderLayout.EAST);
        return header;
    }

    private JPanel buildBody() {
        JPanel body = new JPanel(new BorderLayout(0, 20));
        body.setOpaque(false);

        metricsRow.setOpaque(false);
        body.add(metricsRow, BorderLayout.NORTH);

        JPanel lower = new JPanel(new GridLayout(1, 2, 18, 0));
        lower.setOpaque(false);
        lower.add(buildWelcomeCard());
        lower.add(buildActivityCard());
        body.add(lower, BorderLayout.CENTER);
        return body;
    }

    private JPanel buildWelcomeCard() {
        JPanel card = ClassicTheme.card();
        card.setLayout(new BorderLayout(0, 16));

        JLabel brand = new JLabel("Heritage Ledger");
        brand.setFont(ClassicTheme.BRAND);
        brand.setForeground(ClassicTheme.NAVY);

        JLabel line = new JLabel("Private household accounts · Established for calm money habits");
        line.setFont(ClassicTheme.BODY);
        line.setForeground(ClassicTheme.MUTED);

        JPanel copy = new JPanel();
        copy.setOpaque(false);
        copy.setLayout(new BoxLayout(copy, BoxLayout.Y_AXIS));
        copy.add(ClassicTheme.caption("House motto"));
        copy.add(Box.createVerticalStrut(10));
        copy.add(brand);
        copy.add(Box.createVerticalStrut(8));
        copy.add(line);
        copy.add(Box.createVerticalStrut(18));

        JPanel tips = new JPanel(new GridLayout(3, 1, 0, 10));
        tips.setOpaque(false);
        tips.add(tipRow("Record every outflow the day it occurs."));
        tips.add(tipRow("Review category budgets once each week."));
        tips.add(tipRow("Keep one month of expenses in ready cash."));

        card.add(copy, BorderLayout.NORTH);
        card.add(tips, BorderLayout.CENTER);
        card.add(new AccentBar(), BorderLayout.SOUTH);
        return card;
    }

    private JPanel tipRow(String text) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(true);
        row.setBackground(new Color(246, 248, 250));
        row.setBorder(new EmptyBorder(10, 12, 10, 12));

        JLabel mark = new JLabel("▸");
        mark.setForeground(ClassicTheme.GOLD);
        mark.setFont(ClassicTheme.BODY_BOLD);

        JLabel label = new JLabel(text);
        label.setFont(ClassicTheme.BODY);
        label.setForeground(ClassicTheme.INK);

        row.add(mark, BorderLayout.WEST);
        row.add(label, BorderLayout.CENTER);
        return row;
    }

    private JPanel buildActivityCard() {
        JPanel card = ClassicTheme.card();
        card.setLayout(new BorderLayout(0, 12));
        card.add(ClassicTheme.caption("Recent activity"), BorderLayout.NORTH);

        activityList.setOpaque(false);
        activityList.setLayout(new BoxLayout(activityList, BoxLayout.Y_AXIS));

        JScrollPane scroll = new JScrollPane(activityList);
        ClassicTheme.styleScroll(scroll);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    public void refresh() {
        metricsRow.removeAll();
        metricsRow.add(ClassicTheme.metricTile("Current Balance", ClassicTheme.money(data.getBalance()), ClassicTheme.NAVY));
        metricsRow.add(ClassicTheme.metricTile("Total Income", ClassicTheme.money(data.getTotalIncome()), ClassicTheme.FOREST));
        metricsRow.add(ClassicTheme.metricTile("Total Expenses", ClassicTheme.money(data.getTotalExpense()), ClassicTheme.DANGER));
        metricsRow.revalidate();
        metricsRow.repaint();

        activityList.removeAll();
        List<Transaction> list = data.getTransactions();
        int limit = Math.min(6, list.size());
        for (int i = 0; i < limit; i++) {
            activityList.add(activityRow(list.get(i)));
            activityList.add(Box.createVerticalStrut(8));
        }
        activityList.revalidate();
        activityList.repaint();
    }

    private JPanel activityRow(Transaction t) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(true);
        row.setBackground(ClassicTheme.PANEL);
        row.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                new ClassicTheme.SoftLineBorder(ClassicTheme.RULE, 1, 8),
                new EmptyBorder(10, 12, 10, 12)
        ));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        JLabel desc = new JLabel(t.getDescription());
        desc.setFont(ClassicTheme.BODY_BOLD);
        desc.setForeground(ClassicTheme.INK);
        JLabel meta = new JLabel(t.getCategory() + " · " + t.getDate());
        meta.setFont(ClassicTheme.SMALL);
        meta.setForeground(ClassicTheme.MUTED);
        left.add(desc);
        left.add(Box.createVerticalStrut(2));
        left.add(meta);

        JLabel amount = new JLabel((t.isIncome() ? "+" : "−") + ClassicTheme.money(t.getAmount()));
        amount.setFont(new Font("Consolas", Font.BOLD, 14));
        amount.setForeground(t.isIncome() ? ClassicTheme.FOREST : ClassicTheme.DANGER);

        row.add(left, BorderLayout.CENTER);
        row.add(amount, BorderLayout.EAST);
        return row;
    }

    private static class AccentBar extends JPanel {
        AccentBar() {
            setPreferredSize(new Dimension(10, 8));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(ClassicTheme.GOLD);
            g2.fillRoundRect(0, 2, getWidth(), 4, 4, 4);
            g2.dispose();
        }
    }
}
