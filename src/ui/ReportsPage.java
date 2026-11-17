package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import model.FinanceData;

/**
 * Page 3 — Classic reports: budget progress and spending composition.
 */
public class ReportsPage extends JPanel {
    private final FinanceData data;
    private final JPanel budgetList = new JPanel();
    private final CompositionChart chart = new CompositionChart();
    private final JLabel savingsLabel = new JLabel();
    private final JLabel ratioLabel = new JLabel();

    public ReportsPage(FinanceData data) {
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
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.add(ClassicTheme.heading("Budget & Reports"));
        header.add(Box.createVerticalStrut(6));
        header.add(ClassicTheme.subheading("Compare planned allowances with actual household spending."));
        return header;
    }

    private JPanel buildBody() {
        JPanel body = new JPanel(new BorderLayout(0, 18));
        body.setOpaque(false);

        JPanel summary = new JPanel(new GridLayout(1, 2, 16, 0));
        summary.setOpaque(false);
        summary.add(buildInsightCard("Month's surplus", savingsLabel, "Income remaining after recorded expenses."));
        summary.add(buildInsightCard("Expense ratio", ratioLabel, "Share of income already spent this period."));
        body.add(summary, BorderLayout.NORTH);

        JPanel lower = new JPanel(new GridLayout(1, 2, 18, 0));
        lower.setOpaque(false);
        lower.add(buildBudgetCard());
        lower.add(buildChartCard());
        body.add(lower, BorderLayout.CENTER);
        return body;
    }

    private JPanel buildInsightCard(String title, JLabel valueLabel, String note) {
        JPanel card = ClassicTheme.card();
        card.setLayout(new BorderLayout(0, 10));
        card.add(ClassicTheme.caption(title), BorderLayout.NORTH);

        valueLabel.setFont(ClassicTheme.DISPLAY);
        valueLabel.setForeground(ClassicTheme.NAVY);
        card.add(valueLabel, BorderLayout.CENTER);

        JLabel hint = new JLabel(note);
        hint.setFont(ClassicTheme.SMALL);
        hint.setForeground(ClassicTheme.MUTED);
        card.add(hint, BorderLayout.SOUTH);
        return card;
    }

    private JPanel buildBudgetCard() {
        JPanel card = ClassicTheme.card();
        card.setLayout(new BorderLayout(0, 12));
        card.add(ClassicTheme.caption("Category allowances"), BorderLayout.NORTH);

        budgetList.setOpaque(false);
        budgetList.setLayout(new BoxLayout(budgetList, BoxLayout.Y_AXIS));

        JScrollPane scroll = new JScrollPane(budgetList);
        ClassicTheme.styleScroll(scroll);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildChartCard() {
        JPanel card = ClassicTheme.card();
        card.setLayout(new BorderLayout(0, 12));
        card.add(ClassicTheme.caption("Spending composition"), BorderLayout.NORTH);
        card.add(chart, BorderLayout.CENTER);

        JLabel note = new JLabel("Bars show each category's share of total expenses.");
        note.setFont(ClassicTheme.SMALL);
        note.setForeground(ClassicTheme.MUTED);
        card.add(note, BorderLayout.SOUTH);
        return card;
    }

    public void refresh() {
        double income = data.getTotalIncome();
        double expense = data.getTotalExpense();
        double surplus = income - expense;
        double ratio = income == 0 ? 0 : (expense / income) * 100.0;

        savingsLabel.setText(ClassicTheme.money(surplus));
        savingsLabel.setForeground(surplus >= 0 ? ClassicTheme.FOREST : ClassicTheme.DANGER);
        ratioLabel.setText(String.format("%.0f%% of income", ratio));

        Map<String, Double> spent = data.getExpenseByCategory();
        Map<String, Double> budgets = data.getBudgets();

        budgetList.removeAll();
        for (Map.Entry<String, Double> entry : budgets.entrySet()) {
            String category = entry.getKey();
            double limit = entry.getValue();
            double used = spent.getOrDefault(category, 0.0);
            budgetList.add(budgetRow(category, used, limit));
            budgetList.add(Box.createVerticalStrut(10));
        }
        budgetList.revalidate();
        budgetList.repaint();

        chart.setData(spent);
        chart.repaint();
    }

    private JPanel budgetRow(String category, double used, double limit) {
        JPanel row = new JPanel(new BorderLayout(0, 6));
        row.setOpaque(true);
        row.setBackground(ClassicTheme.PANEL);
        row.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                new ClassicTheme.SoftLineBorder(ClassicTheme.RULE, 1, 8),
                new EmptyBorder(12, 14, 12, 14)
        ));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 78));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel name = new JLabel(category);
        name.setFont(ClassicTheme.BODY_BOLD);
        name.setForeground(ClassicTheme.INK);
        JLabel figures = new JLabel(ClassicTheme.money(used) + " / " + ClassicTheme.money(limit));
        figures.setFont(ClassicTheme.SMALL);
        figures.setForeground(ClassicTheme.MUTED);
        top.add(name, BorderLayout.WEST);
        top.add(figures, BorderLayout.EAST);

        int percent = limit <= 0 ? 0 : (int) Math.min(100, Math.round((used / limit) * 100));
        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(percent);
        bar.setStringPainted(true);
        bar.setString(percent + "% used");
        bar.setFont(ClassicTheme.SMALL_BOLD);
        bar.setForeground(percent >= 90 ? ClassicTheme.DANGER : ClassicTheme.FOREST);
        bar.setBackground(new Color(236, 240, 244));
        bar.setBorderPainted(false);
        bar.setPreferredSize(new Dimension(100, 18));

        row.add(top, BorderLayout.NORTH);
        row.add(bar, BorderLayout.SOUTH);
        return row;
    }

    private static class CompositionChart extends JPanel {
        private List<Bar> bars = new ArrayList<>();

        CompositionChart() {
            setOpaque(true);
            setBackground(ClassicTheme.PANEL);
            setPreferredSize(new Dimension(320, 280));
        }

        void setData(Map<String, Double> spent) {
            bars = new ArrayList<>();
            double total = spent.values().stream().mapToDouble(Double::doubleValue).sum();
            Color[] palette = {
                    ClassicTheme.NAVY,
                    ClassicTheme.FOREST,
                    ClassicTheme.GOLD,
                    new Color(90, 118, 140),
                    ClassicTheme.DANGER,
                    new Color(70, 96, 80)
            };
            int i = 0;
            for (Map.Entry<String, Double> e : spent.entrySet()) {
                double share = total == 0 ? 0 : e.getValue() / total;
                bars.add(new Bar(e.getKey(), share, palette[i % palette.length]));
                i++;
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int left = 110;
            int right = getWidth() - 24;
            int top = 18;
            int rowH = 34;
            int maxBar = Math.max(40, right - left);

            if (bars.isEmpty()) {
                g2.setColor(ClassicTheme.MUTED);
                g2.setFont(ClassicTheme.BODY);
                g2.drawString("No expenses recorded yet.", 24, 40);
                g2.dispose();
                return;
            }

            for (int i = 0; i < bars.size(); i++) {
                Bar bar = bars.get(i);
                int y = top + i * rowH;
                g2.setFont(ClassicTheme.SMALL_BOLD);
                g2.setColor(ClassicTheme.INK);
                g2.drawString(bar.label, 12, y + 14);

                int w = (int) Math.round(bar.share * maxBar);
                g2.setColor(new Color(236, 240, 244));
                g2.fillRoundRect(left, y, maxBar, 16, 8, 8);
                g2.setColor(bar.color);
                g2.fillRoundRect(left, y, Math.max(8, w), 16, 8, 8);

                g2.setColor(ClassicTheme.MUTED);
                g2.setFont(ClassicTheme.SMALL);
                g2.drawString(String.format("%.0f%%", bar.share * 100), left + maxBar + 8, y + 13);
            }
            g2.dispose();
        }

        private record Bar(String label, double share, Color color) {}
    }
}
