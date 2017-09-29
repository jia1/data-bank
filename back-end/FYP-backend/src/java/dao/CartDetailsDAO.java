///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package dao;
//
//import database.ConnectionManager;
//import entity.CartItem;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
///**
// *
// * @author JeremyBachtiar
// */
//public class CartDetailsDAO {
//    public static String addCartDetails(String cartId, String SKU, int qty){
//        
//        Connection conn = null;
//        PreparedStatement stmt = null;
//
//        ResultSet rs = null;
//
//        String sql = "INSERT INTO CART_DETAILS VALUES (?,?,?)";
//
//        try {
//            conn = ConnectionManager.getConnection();
//            stmt = conn.prepareStatement(sql);
//            stmt.setString(1, cartId);
//            stmt.setString(2, SKU);
//            stmt.setInt(3, qty);
//
//            stmt.executeUpdate();
//
//        } catch (SQLException ex) {
//            
//           handleSQLException(ex, sql);
//            
//        } finally {
//            ConnectionManager.close(conn, stmt, rs);
//        }
//        
//        
//        return "";
//    }
//    
//    public static void updateCartDetails(String cartId, String productId, int qty ){
//        try{
//            deleteCartItem(cartId, qty, productId);
//            addCartDetails(cartId, productId, qty);
//        
//        }catch (SQLException ex) {
//              
//        } 
//        
//        
//    }
//    
//    public static void deleteCartItem(String cartId, int qty, String productId) throws SQLException {
//
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//
//        String sql = "DELETE from cart_details where cart_id=? and product_sku=? and quantity=?";
//
//        try {
//            conn = ConnectionManager.getConnection();
//            stmt = conn.prepareStatement(sql);
//            stmt.setString(1, cartId);
//            stmt.setString(2, productId);
//            stmt.setInt(3, qty);
//
//            stmt.executeUpdate();
//        } finally {
//            ConnectionManager.close(conn, stmt, rs);
//        }
//    }
//    
//     public static ArrayList<CartItem> getCartDetailsByCartId(String cartId) throws SQLException {
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//        
//        ArrayList<CartItem> cartDetails = new ArrayList<CartItem>();  
//        
//        String sql = "Select * from cart_details where cart_id=?" ;
//        
//        try {
//            conn = ConnectionManager.getConnection();
//            stmt = conn.prepareStatement(sql);
//            stmt.setString(1, cartId);
//            
//            rs = stmt.executeQuery();
//            
//            while (rs.next()) {
//                
//                String productSKU = rs.getString("PRODUCT_SKU");
//                int quantity = rs.getInt("quantity");
//                
//                CartItem cartDetail = new CartItem(cartId, productSKU, quantity);
//                cartDetails.add(cartDetail);
//             
//                
//            }
//        } finally {
//            ConnectionManager.close(conn, stmt, rs);
//        }        
//        
//        return cartDetails;
//     }
//    
//    
//    private static void handleSQLException(SQLException ex, String sql, String... parameters) {
//        String msg = "Unable to access data; SQL=" + sql + "\n";
//        for (String parameter : parameters) {
//            msg += "," + parameter;
//        }
//        Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, msg, ex);
//    }
//}
