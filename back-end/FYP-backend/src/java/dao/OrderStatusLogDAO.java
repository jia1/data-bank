/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

/**
 *
 * @author Huiyan
 */
import database.ConnectionManager;
import entity.OrderStatus;
import entity.OrderStatusLog;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.Timestamp;

public class OrderStatusLogDAO {

    public OrderStatusLog[] getOrderStatusByOrderId(int orderId) throws SQLException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<OrderStatusLog> orderStatusLogs = new ArrayList<>();

        String sql = "SELECT ol.*, os.STATUS_NAME FROM mydb.order_status_log ol LEFT OUTER JOIN mydb.order_status os ON ol.STATUS_ID = os.STATUS_ID WHERE ol.ORDER_ID = ?";

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, orderId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int statusId = rs.getInt("STATUS_ID");
                String orderStatus = rs.getString("STATUS_NAME");
                Timestamp startTimeStamp = rs.getTimestamp("START_TIMESTAMP");
                Timestamp endTimeStamp = rs.getTimestamp("END_TIMESTAMP");
                orderStatusLogs.add(new OrderStatusLog(new OrderStatus(statusId, orderStatus), startTimeStamp, endTimeStamp));

            }

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return orderStatusLogs.toArray(new OrderStatusLog[orderStatusLogs.size()]);

    }
    
    private java.sql.Timestamp getCurrentTimeStamp() {

        java.util.Date today = new java.util.Date();
        return new java.sql.Timestamp(today.getTime());

    }
    
    public String updateOrderStatusLogByOrderId(int orderId, int previousStatusId, int newStatusId) throws SQLException {

        Timestamp curr_ts = getCurrentTimeStamp();
        
        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt1 = null;
        ResultSet rs = null;

        String sql = "UPDATE ORDER_STATUS_LOG SET END_TIMESTAMP = ?, DURATION_HOURS = ? WHERE ORDER_ID = ? AND STATUS_ID = ? AND DURATION_HOURS = 0";
        String sql1 = "INSERT ORDER_STATUS_LOG (ORDER_ID, STATUS_ID, START_TIMESTAMP, END_TIMESTAMP, DURATION_HOURS) VALUES (?,?,?,?,?)";
        
        try {

            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setTimestamp(1, curr_ts);
            stmt.setDouble(2, 10);
            stmt.setInt(3, orderId);
            stmt.setInt(4, previousStatusId);
            stmt.executeUpdate();
            
            stmt1 = conn.prepareStatement(sql1);
            stmt1.setInt(1, orderId);
            stmt1.setInt(2, newStatusId);
            stmt1.setTimestamp(3, curr_ts);
            stmt1.setTimestamp(4, curr_ts);
            stmt1.setDouble(5, 0);
            stmt1.executeUpdate();

        } finally {
            ConnectionManager.close(conn, stmt, rs);
            ConnectionManager.close(conn, stmt1, rs);
        }

        return "Success";

    }

}
