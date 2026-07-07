package gui;

import dao.BloodStockDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

public class StockFrame extends JFrame {

    private BloodStockDAO stockDAO = new BloodStockDAO();
    private DefaultTableModel tableModel;

    public StockFrame() {
        setTitle("Blood Stock Levels");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        tableModel = new DefaultTableModel(new String[]{"Blood Group", "Units Available"}, 0);
        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadStock());
        add(refreshBtn, BorderLayout.SOUTH);

        loadStock();
    }

    private void loadStock() {
        tableModel.setRowCount(0);
        Map<String, Integer> stock = stockDAO.getAllStock();
        for (Map.Entry<String, Integer> entry : stock.entrySet()) {
            tableModel.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }
    }
}
