package gui;

import dao.DonorDAO;
import model.Donor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DonorFrame extends JFrame {

    private DonorDAO donorDAO = new DonorDAO();

    private JTextField idField, nameField, ageField, contactField, addressField, dateField;
    private JComboBox<String> bloodGroupBox, genderBox;
    private JTable table;
    private DefaultTableModel tableModel;

    public DonorFrame() {
        setTitle("Manage Donors");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));

        add(buildFormPanel(), BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);
        add(buildButtonPanel(), BorderLayout.SOUTH);

        loadDonors();
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 8, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Donor Details"));

        idField = new JTextField();
        idField.setEditable(false);
        nameField = new JTextField();
        ageField = new JTextField();
        genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        bloodGroupBox = new JComboBox<>(new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"});
        contactField = new JTextField();
        addressField = new JTextField();
        dateField = new JTextField("YYYY-MM-DD");

        panel.add(new JLabel("ID:")); panel.add(idField);
        panel.add(new JLabel("Name:")); panel.add(nameField);
        panel.add(new JLabel("Age:")); panel.add(ageField);
        panel.add(new JLabel("Gender:")); panel.add(genderBox);

        panel.add(new JLabel("Blood Group:")); panel.add(bloodGroupBox);
        panel.add(new JLabel("Contact:")); panel.add(contactField);
        panel.add(new JLabel("Address:")); panel.add(addressField);
        panel.add(new JLabel("Last Donation (YYYY-MM-DD):")); panel.add(dateField);

        return panel;
    }

    private JScrollPane buildTablePanel() {
        tableModel = new DefaultTableModel(
                new String[]{"ID", "Name", "Age", "Gender", "Blood Group", "Contact", "Address", "Last Donation"}, 0);
        table = new JTable(tableModel);
        table.getSelectionModel().addListSelectionListener(e -> populateFormFromSelection());
        return new JScrollPane(table);
    }

    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel();

        JButton addBtn = new JButton("Add Donor");
        JButton updateBtn = new JButton("Update Donor");
        JButton deleteBtn = new JButton("Delete Donor");
        JButton clearBtn = new JButton("Clear Form");
        JButton refreshBtn = new JButton("Refresh");

        addBtn.addActionListener(e -> addDonor());
        updateBtn.addActionListener(e -> updateDonor());
        deleteBtn.addActionListener(e -> deleteDonor());
        clearBtn.addActionListener(e -> clearForm());
        refreshBtn.addActionListener(e -> loadDonors());

        panel.add(addBtn);
        panel.add(updateBtn);
        panel.add(deleteBtn);
        panel.add(clearBtn);
        panel.add(refreshBtn);

        return panel;
    }

    private void loadDonors() {
        tableModel.setRowCount(0);
        List<Donor> donors = donorDAO.getAllDonors();
        for (Donor d : donors) {
            tableModel.addRow(new Object[]{
                    d.getDonorId(), d.getName(), d.getAge(), d.getGender(),
                    d.getBloodGroup(), d.getContact(), d.getAddress(), d.getLastDonationDate()
            });
        }
    }

    private void populateFormFromSelection() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        idField.setText(tableModel.getValueAt(row, 0).toString());
        nameField.setText(tableModel.getValueAt(row, 1).toString());
        ageField.setText(tableModel.getValueAt(row, 2).toString());
        genderBox.setSelectedItem(tableModel.getValueAt(row, 3).toString());
        bloodGroupBox.setSelectedItem(tableModel.getValueAt(row, 4).toString());
        contactField.setText(tableModel.getValueAt(row, 5).toString());
        addressField.setText(tableModel.getValueAt(row, 6).toString());
        dateField.setText(tableModel.getValueAt(row, 7).toString());
    }

    private Donor buildDonorFromForm() {
        Donor d = new Donor();
        if (!idField.getText().isEmpty()) {
            d.setDonorId(Integer.parseInt(idField.getText()));
        }
        d.setName(nameField.getText().trim());
        d.setAge(Integer.parseInt(ageField.getText().trim()));
        d.setGender((String) genderBox.getSelectedItem());
        d.setBloodGroup((String) bloodGroupBox.getSelectedItem());
        d.setContact(contactField.getText().trim());
        d.setAddress(addressField.getText().trim());
        d.setLastDonationDate(dateField.getText().trim());
        return d;
    }

    private void addDonor() {
        try {
            Donor d = buildDonorFromForm();
            if (donorDAO.addDonor(d)) {
                JOptionPane.showMessageDialog(this, "Donor added successfully. Blood stock updated (+1 unit).");
                clearForm();
                loadDonors();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add donor.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid age.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateDonor() {
        if (idField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a donor from the table first.");
            return;
        }
        try {
            Donor d = buildDonorFromForm();
            if (donorDAO.updateDonor(d)) {
                JOptionPane.showMessageDialog(this, "Donor updated successfully.");
                clearForm();
                loadDonors();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update donor.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid age.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteDonor() {
        if (idField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a donor from the table first.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this donor?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(idField.getText());
            if (donorDAO.deleteDonor(id)) {
                JOptionPane.showMessageDialog(this, "Donor deleted.");
                clearForm();
                loadDonors();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete donor.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        contactField.setText("");
        addressField.setText("");
        dateField.setText("YYYY-MM-DD");
        genderBox.setSelectedIndex(0);
        bloodGroupBox.setSelectedIndex(0);
        table.clearSelection();
    }
}
