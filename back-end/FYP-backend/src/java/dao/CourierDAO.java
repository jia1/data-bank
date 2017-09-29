/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import database.ConnectionManager;
import entity.Courier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Huiyan
 */
public class CourierDAO {

    public String addCourier(Courier courier) throws SQLException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "INSERT INTO COURIER VALUES (?,?)";
        if (getCourierByName(courier.getCourierName()) != null) {

            try {

                conn = ConnectionManager.getConnection();
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, courier.getCourierName());
                stmt.setString(2, courier.getTrackingUrl());
                rs = stmt.executeQuery();

            } finally {
                ConnectionManager.close(conn, stmt, rs);
            }
        } else {
            return "Courier already exist";
        }

        return "Success";
    }

    public Courier getCourierByName(String courierName) throws SQLException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Courier result = null;

        String sql = "SELECT * FROM COURIER WHERE COURIER_NAME = ?";

        try {

            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, courierName);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String trackingURL = rs.getString("TRACKING_URL");
                result = new Courier(courierName, trackingURL);
            }

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return result;

    }

}
