package gui;

import dao.BloodRequestDAO;
import dao.BloodStockDAO;
import model.BloodRequest;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class RequestFrame extends JFrame {

    private BloodRequestDAO requestDAO = new BloodRequestDAO();
    private BloodStockDAO stockDAO = new BloodStockDAO();

    private JTextField patientField, unitsField, contactField;
    private JComboBox<String> bloodGroupBox;
    private JTable table;
    private DefaultTableModel tableModel;

    public RequestFrame() {
        setTitle("Manage Blood Requests");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(buildFormPanel(), BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);
        add(buildButtonPanel(), BorderLayout.SOUTH);

        loadRequests();
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 6, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("New Blood Request"));

        patientField = new JTextField();
        bloodGroupBox = new JComboBox<>(new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"});
        unitsField = new JTextField();
        contactField = new JTextField();

        panel.add(new JLabel("Patient Name:")); panel.add(patientField);
        panel.add(new JLabel("Blood Group:")); panel.add(bloodGroupBox);
        panel.add(new JLabel("Units Required:")); panel.add(unitsField);
        panel.add(new JLabel("Contact:")); panel.add(contactField);

        return panel;
    }

    private JScrollPane buildTablePanel() {
        tableModel = new DefaultTableModel(
                new String[]{"ID", "Patient", "Blood Group", "Units", "Contact", "Date", "Status"}, 0);
        table = new JTable(tableModel);
        return new JScrollPane(table);
    }

    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel();

        JButton submitBtn = new JButton("Submit Request");
        JButton approveBtn = new JButton("Approve & Issue Blood");
        JButton rejectBtn = new JButton("Reject");
        JButton refreshBtn = new JButton("Refresh");

        submitBtn.addActionListener(e -> submitRequest());
        approveBtn.addActionListener(e -> approveRequest());
        rejectBtn.addActionListener(e -> rejectRequest());
        refreshBtn.addActionListener(e -> loadRequests());

        panel.add(submitBtn);
        panel.add(approveBtn);
        panel.add(rejectBtn);
        panel.add(refreshBtn);

        return panel;
    }

    private void loadRequests() {
        tableModel.setRowCount(0);
        List<BloodRequest> requests = requestDAO.getAllRequests();
        for (BloodRequest r : requests) {
            tableModel.addRow(new Object[]{
                    r.getRequestId(), r.getPatientName(), r.getBloodGroup(),
                    r.getUnitsRequired(), r.getContact(), r.getRequestDate(), r.getStatus()
            });
        }
    }

    private void submitRequest() {
        try {
            BloodRequest r = new BloodRequest();
            r.setPatientName(patientField.getText().trim());
            r.setBloodGroup((String) bloodGroupBox.getSelectedItem());
            r.setUnitsRequired(Integer.parseInt(unitsField.getText().trim()));
            r.setContact(contactField.getText().trim());
            r.setRequestDate(LocalDate.now().toString());
            r.setStatus("Pending");

            if (requestDAO.addRequest(r)) {
                JOptionPane.showMessageDialog(this, "Request submitted.");
                patientField.setText("");
                unitsField.setText("");
                contactField.setText("");
                loadRequests();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to submit request.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number of units.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void approveRequest() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a request from the table first.");
            return;
        }
        int requestId = (int) tableModel.getValueAt(row, 0);
        String bloodGroup = (String) tableModel.getValueAt(row, 2);
        int units = (int) tableModel.getValueAt(row, 3);

        if (stockDAO.issueBlood(bloodGroup, units)) {
            requestDAO.updateStatus(requestId, "Approved");
            JOptionPane.showMessageDialog(this, "Request approved. Stock updated.");
        } else {
            JOptionPane.showMessageDialog(this, "Not enough stock for " + bloodGroup + ".", "Insufficient Stock", JOptionPane.WARNING_MESSAGE);
        }
        loadRequests();
    }

    private void rejectRequest() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a request from the table first.");
            return;
        }
        int requestId = (int) tableModel.getValueAt(row, 0);
        requestDAO.updateStatus(requestId, "Rejected");
        loadRequests();
    }
}
