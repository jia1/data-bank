/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import database.ConnectionManager;
import entity.OrderStatus;
import entity.PromoCode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Ong Yi Xuan
 */
public class OrderStatusDAO {
    

    public void addOrderStatus(OrderStatus os) throws SQLException {
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "INSERT INTO ORDER_STATUS (STATUS_ID, STATUS_NAME) VALUES (?,?)";

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, os.getStatusId());
            stmt.setString(2, os.getStatusName());
            stmt.executeUpdate();

            
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        
    }

    public int getNextOrderStatusId() throws SQLException {
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        int nextOrderStatusId = 0;

        String sql = "SELECT MAX(STATUS_ID) AS MAX FROM ORDER_STATUS";

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {

                nextOrderStatusId = rs.getInt("MAX") +1;
                
            }

        } finally {
            
            ConnectionManager.close(conn, stmt, rs);
            
        }

        return nextOrderStatusId;
        
    }

    public OrderStatus getOrderStatusById(int id) throws SQLException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        OrderStatus os = null;

        String sql = "SELECT * FROM ORDER_STATUS WHERE STATUS_ID = ?";

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            while (rs.next()) {

                int orderStatusId = rs.getInt("STATUS_ID");
                String orderStatusName = rs.getString("STATUS_NAME");
                os = new OrderStatus(orderStatusId, orderStatusName);

            }

        } finally {
            
            ConnectionManager.close(conn, stmt, rs);
            
        }

        return os;

    }

    public OrderStatus[] getAllOrderStatuses() throws SQLException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        OrderStatus os = null;
        
        ArrayList<OrderStatus> orderStatusList = new ArrayList<>();

        String sql = "SELECT * FROM ORDER_STATUS";

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {

                int orderStatusId = rs.getInt("STATUS_ID");
                String orderStatusName = rs.getString("STATUS_NAME");
                os = new OrderStatus(orderStatusId, orderStatusName);
                orderStatusList.add(os);
                
            }

        } finally {
            
            ConnectionManager.close(conn, stmt, rs);
            
        }

        OrderStatus[] orderStatusArr = orderStatusList.toArray(new OrderStatus[orderStatusList.size()]);
        return orderStatusArr;

    }
    
    //Update
    public void updateOrderStatus(OrderStatus os) throws SQLException{
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "UPDATE ORDER_STATUS SET STATUS_NAME = ? WHERE STATUS_ID = ?";

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, os.getStatusName());
            stmt.setInt(2, os.getStatusId());
            stmt.executeUpdate();

        } finally {
            
            ConnectionManager.close(conn, stmt, rs);
            
        }
        
    }
    
    //Hard Delete
    public void deleteOrderStatus(int orderStatusId) throws SQLException {
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "DELETE FROM ORDER_STATUS WHERE STATUS_ID = ?";

        try {
            
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, orderStatusId);
            stmt.executeUpdate();

        } finally {
            
            ConnectionManager.close(conn, stmt, rs);
            
        }
        
    }

    
}
