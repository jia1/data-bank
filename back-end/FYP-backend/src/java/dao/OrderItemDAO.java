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
import entity.OrderItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderItemDAO {

    static void addOrderItem() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public OrderItem[] getOrderItemsByOrderId(int orderId) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<OrderItem> orderItems = new ArrayList<>();

        String sql = "SELECT * FROM order_item WHERE order_id= ?";
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, orderId);
            rs = stmt.executeQuery();

            while (rs.next()) {

                int productId = rs.getInt("PRODUCT_ID");
                int quantity = rs.getInt("QUANTITY");
                double unitPrice = rs.getDouble("UNIT_PRICE");
                String itemStatus = rs.getString("ITEM_STATUS");
                ProductDAO pd = new ProductDAO();
                orderItems.add(new OrderItem(pd.getProductById(productId), quantity, unitPrice, itemStatus));

            }
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return orderItems.toArray(new OrderItem[orderItems.size()]);
    }

    public String addOrderItems(int orderId, OrderItem oI) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "INSERT INTO ORDER_ITEM (ORDER_ID, PRODUCT_ID, QUANTITY, UNIT_PRICE, ITEM_STATUS) VALUES (?, ?, ?, ?, ?)";

        try {

            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, orderId);
            stmt.setInt(2, oI.getProduct().getProductId());
            stmt.setInt(3, oI.getQuantity());
            stmt.setDouble(4, oI.getUnitPrice());
            stmt.setString(5, "INCOMPLETE");

            stmt.executeUpdate();   

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }


        return "Success";

    }
    
    public String updateOrderItemStatus(int orderId, int productId, String newStatus) throws SQLException {
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "UPDATE ORDER_ITEM SET ITEM_STATUS = ? WHERE ORDER_ID = ? AND PRODUCT_ID = ?";

        try {

            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, newStatus);
            stmt.setInt(2, orderId);
            stmt.setInt(3, productId);
            stmt.executeUpdate();

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }


        return "Success";

    }

}
