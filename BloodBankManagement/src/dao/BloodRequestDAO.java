package dao;

import db.DBConnection;
import model.BloodRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BloodRequestDAO {

    public boolean addRequest(BloodRequest r) {
        String sql = "INSERT INTO blood_request (patient_name, blood_group, units_required, contact, request_date, status) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, r.getPatientName());
            ps.setString(2, r.getBloodGroup());
            ps.setInt(3, r.getUnitsRequired());
            ps.setString(4, r.getContact());
            ps.setString(5, r.getRequestDate());
            ps.setString(6, r.getStatus());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateStatus(int requestId, String status) {
        String sql = "UPDATE blood_request SET status=? WHERE request_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, requestId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<BloodRequest> getAllRequests() {
        List<BloodRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM blood_request ORDER BY request_id DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                BloodRequest r = new BloodRequest();
                r.setRequestId(rs.getInt("request_id"));
                r.setPatientName(rs.getString("patient_name"));
                r.setBloodGroup(rs.getString("blood_group"));
                r.setUnitsRequired(rs.getInt("units_required"));
                r.setContact(rs.getString("contact"));
                Date date = rs.getDate("request_date");
                r.setRequestDate(date != null ? date.toString() : "");
                r.setStatus(rs.getString("status"));
                list.add(r);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
