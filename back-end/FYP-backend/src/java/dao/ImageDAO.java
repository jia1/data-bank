/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import database.ConnectionManager;
import entity.Colour;
import entity.Image;
import entity.OrderStatus;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Huiyan
 */
public class ImageDAO {
    
        public void addImage(int productId, Image i) throws SQLException {
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "INSERT INTO PRODUCT_IMAGE (PRODUCT_ID, IMAGE_ID, IMAGE_URL) VALUES (?,?,?)";

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, productId);
            stmt.setInt(2, getNextProductImageId(productId));
            stmt.setString(3, i.getImageUrl());
            stmt.executeUpdate();

            
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        
    }

    public int getNextProductImageId(int productId) throws SQLException {
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        int nextImageId = 0;

        String sql = "SELECT MAX(IMAGE_ID) AS MAX FROM PRODUCT_IMAGE WHERE PRODUCT_ID = ?";

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, productId);
            rs = stmt.executeQuery();

            while (rs.next()) {

                nextImageId = rs.getInt("MAX") +1;
                
            }

        } finally {
            
            ConnectionManager.close(conn, stmt, rs);
            
        }

        return nextImageId;
        
    }
    
    public Image getImageById(int imageId) throws SQLException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Image i = null;

        String sql = "SELECT * FROM PRODUCT_IMAGE WHERE IMAGE_ID = ?";

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, imageId);
            rs = stmt.executeQuery();

            while (rs.next()) {

                String imageUrl = rs.getString("IMAGE_URL");
                i = new Image(imageId, imageUrl);
                
            }

        } finally {
            
            ConnectionManager.close(conn, stmt, rs);
            
        }

        return i;

    }

    public Image[] getAllImagesByProductId(int productId) throws SQLException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Image i = null;
        
        ArrayList<Image> imageList = new ArrayList<>();

        String sql = "SELECT * FROM PRODUCT_IMAGE WHERE PRODUCT_ID = ?";

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, productId);
            rs = stmt.executeQuery();

            while (rs.next()) {

                int imageId = rs.getInt("IMAGE_ID");
                String imageUrl = rs.getString("IMAGE_URL");
                i = new Image(imageId, imageUrl);
                imageList.add(i);
                
            }

        } finally {
            
            ConnectionManager.close(conn, stmt, rs);
            
        }

        Image[] imageArr = imageList.toArray(new Image[imageList.size()]);
        return imageArr;

    }
    
    //Update
    public void updateImage(Image i) throws SQLException{
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "UPDATE PRODUCT_IMAGE SET IMAGE_URL = ? WHERE IMAGE_ID = ?";

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, i.getImageUrl());
            stmt.setInt(2, i.getImageId());
            stmt.executeUpdate();

        } finally {
            
            ConnectionManager.close(conn, stmt, rs);
            
        }
        
    }
    
    //Hard Delete
    public void deleteImage(int imageId) throws SQLException {
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "DELETE FROM PRODUCT_IMAGE WHERE IMAGE_ID = ?";

        try {
            
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, imageId);
            stmt.executeUpdate();

        } finally {
            
            ConnectionManager.close(conn, stmt, rs);
            
        }
        
    }

//    public Image[] getImagesById(int productId) throws SQLException {
//
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//        ArrayList<Image> images = new ArrayList<Image>();
//
//        String sql = "SELECT * FROM product_image where product_id=?";
//
//        try {
//            conn = ConnectionManager.getConnection();
//            stmt = conn.prepareStatement(sql);
//            stmt.setInt(1, productId);
//            rs = stmt.executeQuery();
//
//            while (rs.next()) {
//
//                int imageId = rs.getInt("image_id");
//                String imageUrl = rs.getString("image_url");
//                images.add(new Image(imageId, imageUrl));
//
//            }
//
//        } finally {
//            ConnectionManager.close(conn, stmt, rs);
//        }
//
//        return images.toArray(new Image[images.size()]);
//
//    }

}
