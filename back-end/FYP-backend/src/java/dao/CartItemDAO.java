/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import database.ConnectionManager;
import entity.CartItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author JeremyBachtiar
 */
public class CartItemDAO {
    
    public CartItem[] getCartItemsByCartId(int cartId) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<CartItem> cartItems = new ArrayList<CartItem>();

        String sql = "SELECT * FROM CART_ITEM WHERE CART_ID= ?";
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, cartId);
            rs = stmt.executeQuery();

            while (rs.next()) {

                int productId = rs.getInt(2);
                int quantity = rs.getInt(3);
                double unitPrice = rs.getDouble(4);
                ProductDAO pd = new ProductDAO();
                cartItems.add(new CartItem(pd.getProductById(productId), quantity, unitPrice));

            }
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return cartItems.toArray(new CartItem[cartItems.size()]);
    }
    
    public CartItem getCartItemByCartProductId(int cartId, int productId) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        CartItem cartItem = null;

        String sql = "SELECT * FROM CART_ITEM WHERE CART_ID= ? AND PRODUCT_ID= ?";
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, cartId);
            stmt.setInt(2, productId);
            rs = stmt.executeQuery();

            while (rs.next()) {

                int quantity = rs.getInt(3);
                double unitPrice = rs.getDouble(4);
                ProductDAO pd = new ProductDAO();
                cartItem = new CartItem(pd.getProductById(productId), quantity, unitPrice);

            }
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return cartItem;
    }

    public String addCartItem(int cartId, CartItem cI) throws SQLException {
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        System.out.println("cart ID : " + cartId);
        CartItem cartItem = getCartItemByCartProductId(cartId, cI.getProduct().getProductId());
        
        if(cartItem == null){
            System.out.println("HEY IM HEREEEE");
        
            String sql = "INSERT INTO CART_ITEM VALUES (?,?,?,?)";

            try {

                conn = ConnectionManager.getConnection();
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, cartId);
                stmt.setInt(2, cI.getProduct().getProductId());
                stmt.setInt(3, cI.getQuantity());
                stmt.setDouble(4, cI.getUnitPrice());

                stmt.executeUpdate();

            } finally {
                ConnectionManager.close(conn, stmt, rs);
            }

        }else{
            System.out.println("product ID : " + cI.getProduct().getProductId());
            System.out.println("quantity : " + cI.getQuantity());
            String sql = "UPDATE CART_ITEM SET QUANTITY = ? WHERE CART_ID = ? AND PRODUCT_ID = ?";

            try {

                conn = ConnectionManager.getConnection();
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, cI.getQuantity());
                stmt.setInt(2, cartId);
                stmt.setInt(3, cI.getProduct().getProductId());

                stmt.executeUpdate();

            } finally {
                ConnectionManager.close(conn, stmt, rs);
            }
        }
        

        return "Success";

    }
    
    public String deleteCartItem(int cartId, int productId) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        CartItem cartItem = null;

        String sql = "DELETE FROM CART_ITEM WHERE CART_ID= ? AND PRODUCT_ID= ?";
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, cartId);
            stmt.setInt(2, productId);
            
            stmt.executeUpdate();

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return "Success";
    }
    
    public String deleteCartItemByCartId(int cartId) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        CartItem cartItem = null;

        String sql = "DELETE FROM CART_ITEM WHERE CART_ID= ?";
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, cartId);
            System.out.println("SQL" + cartId);
            stmt.executeUpdate();

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return "Success";
    }

}
