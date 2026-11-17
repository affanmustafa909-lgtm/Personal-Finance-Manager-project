package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import model.FinanceData;
import model.Transaction;

/**
 * Page 2 — Classic register: post new entries and review the full ledger.
 */
public class TransactionsPage extends JPanel {
    private final FinanceData data;
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JTextField descriptionField = new JTextField();
    private final JTextField amountField = new JTextField();
    private final JComboBox<String> typeBox = new JComboBox<>(new String[]{"Expense", "Income"});
    private final JComboBox<String> categoryBox = new JComboBox<>();

    public TransactionsPage(FinanceData data) {
        this.data = data;
        setOpaque(true);
        setBackground(ClassicTheme.PAPER);
        setLayout(new BorderLayout(0, 20));
        setBorder(ClassicTheme.pagePadding());

        tableModel = new DefaultTableModel(new String[]{"Date", "Description", "Category", "Type", "Amount"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        ClassicTheme.styleTable(table);
        table.getColumnModel().getColumn(4).setCellRenderer(new AmountRenderer());

        categoryBox.setModel(new DefaultComboBoxModel<>(new String[]{
                "Food", "Utilities", "Transport", "Entertainment", "Health", "Education", "Income", "Other"
        }));
        categoryBox.setFont(ClassicTheme.BODY);
        typeBox.setFont(ClassicTheme.BODY);
        ClassicTheme.styleField(descriptionField);
        ClassicTheme.styleField(amountField);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildBody(), BorderLayout.CENTER);
        refresh();
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.add(ClassicTheme.heading("Transaction Register"));
        header.add(Box.createVerticalStrut(6));
        header.add(ClassicTheme.subheading("Post income and expenses into the household ledger."));
        return header;
    }

    private JPanel buildBody() {
        JPanel body = new JPanel(new BorderLayout(0, 18));
        body.setOpaque(false);
        body.add(buildFormCard(), BorderLayout.NORTH);
        body.add(buildTableCard(), BorderLayout.CENTER);
        return body;
    }

    private JPanel buildFormCard() {
        JPanel card = ClassicTheme.card();
        card.setLayout(new BorderLayout(0, 14));
        card.add(ClassicTheme.caption("New entry"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 4, 4, 12);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;

        addField(form, g, 0, "Description", descriptionField);
        addField(form, g, 1, "Amount", amountField);
        addField(form, g, 2, "Type", typeBox);
        addField(form, g, 3, "Category", categoryBox);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actions.setOpaque(false);
        JButton post = ClassicTheme.primaryButton("Post Entry");
        JButton clear = ClassicTheme.secondaryButton("Clear");
        post.addActionListener(e -> postEntry());
        clear.addActionListener(e -> clearForm());
        actions.add(post);
        actions.add(clear);

        card.add(form, BorderLayout.CENTER);
        card.add(actions, BorderLayout.SOUTH);
        return card;
    }

    private void addField(JPanel form, GridBagConstraints g, int col, String label, java.awt.Component field) {
        g.gridx = col;
        g.gridy = 0;
        g.weightx = 0;
        JLabel caption = ClassicTheme.caption(label);
        form.add(caption, g);
        g.gridy = 1;
        g.weightx = 1;
        field.setPreferredSize(new Dimension(160, 38));
        form.add(field, g);
    }

    private JPanel buildTableCard() {
        JPanel card = ClassicTheme.card();
        card.setLayout(new BorderLayout(0, 12));
        card.add(ClassicTheme.caption("Full ledger"), BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(table);
        ClassicTheme.styleScroll(scroll);
        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    private void postEntry() {
        String description = descriptionField.getText().trim();
        String amountText = amountField.getText().trim().replace(",", "");
        String type = (String) typeBox.getSelectedItem();
        String category = (String) categoryBox.getSelectedItem();

        if (description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a description.", "Missing field", JOptionPane.WARNING_MESSAGE);
            return;
        }
        double amount;
        try {
            amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid amount greater than zero.", "Invalid amount", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if ("Income".equals(type)) {
            category = "Income";
        }

        data.add(description, category, type, amount, LocalDate.now());
        clearForm();
        refresh();
        firePropertyChange("ledgerChanged", false, true);
    }

    private void clearForm() {
        descriptionField.setText("");
        amountField.setText("");
        typeBox.setSelectedIndex(0);
        categoryBox.setSelectedIndex(0);
    }

    public void refresh() {
        tableModel.setRowCount(0);
        for (Transaction t : data.getTransactions()) {
            tableModel.addRow(new Object[]{
                    t.getDate().toString(),
                    t.getDescription(),
                    t.getCategory(),
                    t.getType(),
                    t
            });
        }
    }

    private static class AmountRenderer extends DefaultTableCellRenderer {
        @Override
        public java.awt.Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            label.setBorder(new EmptyBorder(0, 12, 0, 12));
            label.setHorizontalAlignment(RIGHT);
            if (value instanceof Transaction t) {
                label.setText((t.isIncome() ? "+" : "−") + ClassicTheme.money(t.getAmount()));
                if (!isSelected) {
                    label.setForeground(t.isIncome() ? ClassicTheme.FOREST : ClassicTheme.DANGER);
                    label.setBackground(ClassicTheme.PANEL);
                }
            }
            return label;
        }
    }
}
