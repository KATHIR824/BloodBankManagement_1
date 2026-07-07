package dao;

import db.DBConnection;
import model.Donor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DonorDAO {

    // Add a new donor and increase blood stock for that group by 1 unit
    public boolean addDonor(Donor d) {
        String sql = "INSERT INTO donor (name, age, gender, blood_group, contact, address, last_donation_date) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, d.getName());
            ps.setInt(2, d.getAge());
            ps.setString(3, d.getGender());
            ps.setString(4, d.getBloodGroup());
            ps.setString(5, d.getContact());
            ps.setString(6, d.getAddress());
            ps.setString(7, d.getLastDonationDate());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                updateStockOnDonation(con, d.getBloodGroup(), 1);
            }
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update stock when a donation happens (amount can be positive)
    private void updateStockOnDonation(Connection con, String bloodGroup, int units) throws SQLException {
        String sql = "UPDATE blood_stock SET units_available = units_available + ? WHERE blood_group = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, units);
            ps.setString(2, bloodGroup);
            ps.executeUpdate();
        }
    }

    public boolean updateDonor(Donor d) {
        String sql = "UPDATE donor SET name=?, age=?, gender=?, blood_group=?, contact=?, address=?, last_donation_date=? "
                + "WHERE donor_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, d.getName());
            ps.setInt(2, d.getAge());
            ps.setString(3, d.getGender());
            ps.setString(4, d.getBloodGroup());
            ps.setString(5, d.getContact());
            ps.setString(6, d.getAddress());
            ps.setString(7, d.getLastDonationDate());
            ps.setInt(8, d.getDonorId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteDonor(int donorId) {
        String sql = "DELETE FROM donor WHERE donor_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, donorId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Donor> getAllDonors() {
        List<Donor> list = new ArrayList<>();
        String sql = "SELECT * FROM donor";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Donor> searchByBloodGroup(String bloodGroup) {
        List<Donor> list = new ArrayList<>();
        String sql = "SELECT * FROM donor WHERE blood_group = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, bloodGroup);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Donor mapRow(ResultSet rs) throws SQLException {
        Donor d = new Donor();
        d.setDonorId(rs.getInt("donor_id"));
        d.setName(rs.getString("name"));
        d.setAge(rs.getInt("age"));
        d.setGender(rs.getString("gender"));
        d.setBloodGroup(rs.getString("blood_group"));
        d.setContact(rs.getString("contact"));
        d.setAddress(rs.getString("address"));
        Date date = rs.getDate("last_donation_date");
        d.setLastDonationDate(date != null ? date.toString() : "");
        return d;
    }
}
