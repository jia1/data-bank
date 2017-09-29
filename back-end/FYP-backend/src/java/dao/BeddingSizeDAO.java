/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import database.ConnectionManager;
import entity.BeddingSize;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JeremyBachtiar
 */
public class BeddingSizeDAO {

    public String addBeddingSize(BeddingSize bs) throws SQLException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "INSERT INTO BEDDING_SIZE VALUES (?,?,?)";

        if (getBeddingSizeByName(bs.getSizeName()) != null) {

            try {

                conn = ConnectionManager.getConnection();
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, bs.getSizeName());
                stmt.setString(2, bs.getDimensions());
                stmt.setDouble(3, bs.getSizePrice());

                rs = stmt.executeQuery();

            } finally {
                ConnectionManager.close(conn, stmt, rs);
            }
        } else {
            return "Beddig size already exist";
        }

        return "Success";
    }

    public BeddingSize getBeddingSizeByName(String sizeName) throws SQLException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        BeddingSize result = null;

        String sql = "SELECT * FROM BEDDING_SIZE WHERE SIZE_NAME = ?";

        try {

            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, sizeName);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String dimensions = rs.getString("DIMENSIONS");
                double price = rs.getDouble("SIZE_PRICE");

                result = new BeddingSize(sizeName, dimensions, price);
            }

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return result;

    }

    public ArrayList<BeddingSize> getAllBeddingSizes() throws SQLException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<BeddingSize> beddingSizeList = new ArrayList<BeddingSize>();

        String sql = "SELECT * FROM BEDDING_SIZE";
        try {

            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String sizeName = rs.getString("SIZE_NAME");
                String dimensions = rs.getString("DIMENSIONS");
                double price = rs.getDouble("SIZE_PRICE");

                BeddingSize bs = new BeddingSize(sizeName, dimensions, price);
                beddingSizeList.add(bs);
            }

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return beddingSizeList;

    }

    public String deleteBeddingSizeByName(String sizeName) throws SQLException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "DELETE FROM BEDDING_SIZE WHERE SIZE_NAME = ?";

        try {

            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, sizeName);

            rs = stmt.executeQuery();

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return "Success";
    }

    public String updateTag(BeddingSize bs) throws SQLException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "UPDATE TAG SET DIMENSIONS = ?, SIZE_PRICE = ? WHERE SIZE_NAME = ?";

        if (getBeddingSizeByName(bs.getSizeName()) != null) {

            try {

                conn = ConnectionManager.getConnection();
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, bs.getDimensions());
                stmt.setDouble(2, bs.getSizePrice());
                stmt.setString(3, bs.getSizeName());

                rs = stmt.executeQuery();

            } finally {
                ConnectionManager.close(conn, stmt, rs);
            }
        } else {
            return "Bedding Size does not exist";
        }

        return "Success";
    }

    public double getLowestSizePrice() throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        double price = 0;

        String sql = "SELECT MIN(SIZE_PRICE) FROM BEDDING_SIZE";

        try {

            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                price = rs.getDouble(1);
            }
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return price;

    }

    private static void handleSQLException(SQLException ex, String sql, String... parameters) {
        String msg = "Unable to access data; SQL=" + sql + "\n";
        for (String parameter : parameters) {
            msg += "," + parameter;
        }
        Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, msg, ex);
    }
}
