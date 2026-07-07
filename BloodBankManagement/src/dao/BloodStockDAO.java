package dao;

import db.DBConnection;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class BloodStockDAO {

    // Returns a map of blood_group -> units_available
    public Map<String, Integer> getAllStock() {
        Map<String, Integer> stock = new LinkedHashMap<>();
        String sql = "SELECT * FROM blood_stock ORDER BY blood_group";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                stock.put(rs.getString("blood_group"), rs.getInt("units_available"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stock;
    }

    public int getUnits(String bloodGroup) {
        String sql = "SELECT units_available FROM blood_stock WHERE blood_group=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, bloodGroup);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("units_available");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Reduce stock when blood is issued to a recipient. Returns false if not enough stock.
    public boolean issueBlood(String bloodGroup, int units) {
        String checkSql = "SELECT units_available FROM blood_stock WHERE blood_group=?";
        String updateSql = "UPDATE blood_stock SET units_available = units_available - ? WHERE blood_group=?";

        try (Connection con = DBConnection.getConnection()) {
            try (PreparedStatement check = con.prepareStatement(checkSql)) {
                check.setString(1, bloodGroup);
                try (ResultSet rs = check.executeQuery()) {
                    if (rs.next() && rs.getInt("units_available") >= units) {
                        try (PreparedStatement update = con.prepareStatement(updateSql)) {
                            update.setInt(1, units);
                            update.setString(2, bloodGroup);
                            return update.executeUpdate() > 0;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
