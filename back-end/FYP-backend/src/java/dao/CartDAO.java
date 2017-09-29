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
import entity.Cart;
import entity.CartItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class CartDAO {

    public String updateCartPrice(String email) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        CartItemDAO cartItemDao = new CartItemDAO();
        Cart c = getCartByEmail(email);
        CartItem[] cartItems = cartItemDao.getCartItemsByCartId(c.getCartId());
        double totalPrice = 0;

        for (CartItem cI : cartItems) {
            totalPrice += (cI.getUnitPrice() * cI.getQuantity());
        }

        String sql = "UPDATE CART SET CART_PRICE = ? WHERE CART_ID = ?";

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, totalPrice);
            stmt.setInt(2, c.getCartId());

            stmt.executeUpdate();
        } finally {
            ConnectionManager.close(conn);
        }

        return "Success";
    }

    public String addCart(Cart c, String email) throws SQLException {
        System.out.println("ADD CART DAO");
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        CartItemDAO cartItemDAO = new CartItemDAO();
        Cart cart = getCartByEmail(email);

        if (cart == null) {
            CartItem[] cartItems = c.getCartItems();

            int cartId = getNextCartId();
            String sql = "INSERT INTO CART VALUES (?,?,?)";

            try {
                conn = ConnectionManager.getConnection();
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, cartId);
                stmt.setDouble(2, c.getPrice());
                stmt.setString(3, email);
                System.out.println(email);
                System.out.println(cartId);
                System.out.println(c.getPrice());

                stmt.executeUpdate();
                System.out.println("Cart added");
                System.out.println("cart size : " + c.getCartItems().length);
                for (CartItem cI : cartItems) {
                    String result = cartItemDAO.addCartItem(cartId, cI);
                }
            } finally {
                ConnectionManager.close(conn);
            }

        } else {

            CartItem[] cartItems = c.getCartItems();
            System.out.println("Cart Items No : " + cartItems.length);
            System.out.println("Cart Price : " + c.getPrice());
            System.out.println("Cart Id : " + c.getCartId());
            int cartId = cart.getCartId();

            if (cartItems.length != 0) {
                for (CartItem cI : cartItems) {

                    String result = cartItemDAO.addCartItem(cartId, cI);
                }
            } else {
                System.out.println("CARTID : " + c.getCartId());
                String result = cartItemDAO.deleteCartItemByCartId(cartId);
                System.out.println("Result" + result);
            }

            String sql = "UPDATE CART SET CART_PRICE = ? WHERE CART_ID = ?";

            try {
                conn = ConnectionManager.getConnection();
                stmt = conn.prepareStatement(sql);
                stmt.setDouble(1, c.getPrice());
                stmt.setInt(2, cartId);

                stmt.executeUpdate();
            } finally {
                ConnectionManager.close(conn);
            }

        }
        
        return "Success";

    }

    public Cart getCartByEmail(String email) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Cart cart = null;

        String sql = "SELECT * FROM CART WHERE EMAIL = ?";
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int cartId = rs.getInt(1);
                double cartPrice = rs.getDouble(2);
                CartItemDAO cartItemDao = new CartItemDAO();
                CartItem[] cartItems = cartItemDao.getCartItemsByCartId(cartId);
                cart = new Cart(cartId, cartPrice, cartItems);
            }
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return cart;
    }

    public int getNextCartId() throws SQLException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int nextCartId = 0;

        String sql = "SELECT MAX(CART_ID) AS MAX FROM CART";

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {

                nextCartId = rs.getInt("MAX") + 1;

            }

        } finally {

            ConnectionManager.close(conn, stmt, rs);

        }

        return nextCartId;

    }

    private java.sql.Timestamp getCurrentTimeStamp() {

        java.util.Date today = new java.util.Date();
        return new java.sql.Timestamp(today.getTime());

    }

    public String deleteCart(Cart cart, String email) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        CartItem cartItem = null;

        CartItemDAO cartItemDao = new CartItemDAO();
        CartItem[] cartItems = cart.getCartItems();
        for (CartItem cI : cartItems) {
            cartItemDao.deleteCartItem(cart.getCartId(), cI.getProduct().getProductId());
        }

        String sql = "DELETE FROM CART_ITEM WHERE CART_ID= ? AND EMAIL= ?";
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, cart.getCartId());
            stmt.setString(2, email);
            rs = stmt.executeQuery();

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return "Success";
    }

}
