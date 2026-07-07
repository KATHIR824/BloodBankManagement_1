package gui;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    public DashboardFrame() {
        setTitle("Blood Bank Management System - Dashboard");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        JButton donorBtn = new JButton("Manage Donors");
        JButton stockBtn = new JButton("View Blood Stock");
        JButton requestBtn = new JButton("Manage Blood Requests");
        JButton logoutBtn = new JButton("Logout");

        donorBtn.addActionListener(e -> new DonorFrame().setVisible(true));
        stockBtn.addActionListener(e -> new StockFrame().setVisible(true));
        requestBtn.addActionListener(e -> new RequestFrame().setVisible(true));
        logoutBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        panel.add(donorBtn);
        panel.add(stockBtn);
        panel.add(requestBtn);
        panel.add(logoutBtn);

        add(panel);
    }
}
