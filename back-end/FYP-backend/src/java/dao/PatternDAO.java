/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import database.ConnectionManager;
import entity.Collection;
import entity.Pattern;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ong Yi Xuan
 */
public class PatternDAO {
    
    public String addPattern(Pattern pattern) throws SQLException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "INSERT INTO PATTERN (PATTERN_ID, PATTERN_NAME, PATTERN_DESC, PATTERN_PRICE, DELETED, COLLECTION_ID) VALUES (?,?,?,?,?,?)";

            try {

                conn = ConnectionManager.getConnection();
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, getNextPatternId());
                stmt.setString(2, pattern.getPatternName());
                stmt.setString(3, pattern.getPatternDesc());
                stmt.setDouble(4, pattern.getPatternPrice());
                stmt.setString(5, "N");
                stmt.setInt(6, pattern.getCollection().getCollectionId());

                stmt.executeUpdate();

            } finally {
                ConnectionManager.close(conn, stmt, rs);
            }
        
        return "Success";
    }
    
   /* 
    public void updatePatternToDB(Pattern p) throws SQLException{
        
        //get the pattern id
        int patternID = p.getPatternId();
        System.out.println("Pattern ID: " +patternID);
        if(patternID == 0){
        System.out.println("Add Pattern");    
            //add new pattern to DB
            patternID = getNextPatternId();
            System.out.println(patternID);
            System.out.println(p.getCollection().getCollectionId());
            //addPattern(patternID, p.getPatternName(), p.getPatternDesc(), p.getPatternPrice() , p.getCollection().getCollectionId());
            System.out.println("Success");
            //add to pattern_fabric table
            Fabric[] fabricArr = p.getPatternDesc();
            
            for(CustomFabric cf : fabricArr){
                
                addPatternFabric(patternID, cf.getFabric_id());
                CustomColour[] fabricColourArr = cf.getColours();
                
                for(CustomColour cc : fabricColourArr){
                    
                    //add to product table (colour ..)
                    addProduct(patternID, cf.getFabric_id(), cc.getColour_id(), cc.getColour_price(), cc.getImage_url());
                }     
              
            }             
            
        }else{
            
            //pattern exists in database
            updatePattern(patternID, cp.getPattern_name(), cp.getPattern_description(), cp.getPattern_price() , cp.getCollection_id());
            
            //delete from product table in db
            deleteProductByPatternID(patternID);
            
            //delete from pattern fabric table in db
            deletePatternFabricByPatternID(patternID);
            
            //add to pattern_fabric table
            CustomFabric[] fabricArr = cp.getFabrics();
            
            for(CustomFabric cf : fabricArr){
                
                addPatternFabric(patternID, cf.getFabric_id());
                
                CustomColour[] fabricColourArr = cf.getColours();
                
                for(CustomColour cc : fabricColourArr){
                    
                    //add to product table (colour ..)
                    addProduct(patternID, cf.getFabric_id(), cc.getColour_id(), cc.getColour_price(), cc.getImage_url());
                }
                
            }
            
            
        }
        
        
    }*/
//    
//    public static void deleteProductByPatternID(String patternID) throws SQLException{
//        
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//        
//        String sql = "DELETE FROM PRODUCT WHERE PATTERN_ID=?"; 
//        
//        try {
//            conn = ConnectionManager.getConnection();
//            stmt = conn.prepareStatement(sql);
//            stmt.setString(1,patternID);
//            stmt.executeUpdate();
//            
//
//        } finally {
//            
//            ConnectionManager.close(conn, stmt, rs);
//            
//        }
//        
//        
//    }
//    
//    public static void deletePatternFabricByPatternID(String patternID) throws SQLException{
//        
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//        
//        String sql = "DELETE FROM PATTERN_FABRIC WHERE PATTERN_ID=?"; 
//        
//        try {
//            conn = ConnectionManager.getConnection();
//            stmt = conn.prepareStatement(sql);
//            stmt.setString(1,patternID);
//            stmt.executeUpdate();
//            
//
//        } finally {
//            
//            ConnectionManager.close(conn, stmt, rs);
//            
//        }
//        
//        
//    }
//    
//    public static String getNextPatternID() throws SQLException{
//        
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//        String patternID = "";
//        int maxID = -1;
//        
//        String sql = "SELECT PATTERN_ID FROM PATTERN"; 
//        
//        try {
//            conn = ConnectionManager.getConnection();
//            stmt = conn.prepareStatement(sql);
//            rs = stmt.executeQuery();
//            
//            while (rs.next()) {
//                
//               patternID = rs.getString("PATTERN_ID");
//               int patternNumber = Integer.parseInt(patternID.substring(1));
//               
//               
//               if(patternNumber > maxID){
//                   
//                   maxID = patternNumber;
//                   
//               }
//               
//            }
//            
//        } finally {
//            
//            ConnectionManager.close(conn, stmt, rs);
//            
//        }
//        
//        maxID ++;
//        return "P" + maxID;
//        
//    }
//    
//    
//    public Pattern[] getAllPatterns() throws SQLException{
//        
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//        Pattern pattern = null;
//        ArrayList<Pattern> patternArrayList = new ArrayList<Pattern>();
//        
//        String sql = "SELECT p.*, c.COLLECTION_NAME FROM pattern p LEFT OUTER JOIN COLLECTION c ON p.COLLECTION_ID = c.COLLECTION_ID"; 
//        
//        try {
//            conn = ConnectionManager.getConnection();
//            stmt = conn.prepareStatement(sql);
//            rs = stmt.executeQuery();
//            
//            while (rs.next()) {
//                
//                String patternID = rs.getString("PATTERN_ID");
//                String patternName = rs.getString("PATTERN_NAME");
//                String patternDescription = rs.getString("PATTERN_DESCRIPTION");
//                Double patternPrice = rs.getDouble("PATTERN_PRICE");
//                String collectionID = rs.getString("COLLECTION_ID");
//                String collectionName = rs.getString("COLLECTION_NAME");
//                
//                pattern = new Pattern(patternID, patternName, patternDescription, patternPrice, new Collection(collectionID,collectionName));
//                patternArrayList.add(pattern);
//            }
//
//        } finally {
//            ConnectionManager.close(conn, stmt, rs);
//        }
//        return patternArrayList.toArray(new Pattern[patternArrayList.size()]);
//    }
//    

    public int getNextPatternId() throws SQLException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int nextPatternId = 0;

        String sql = "SELECT MAX(PATTERN_ID) AS MAX FROM PATTERN";

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {

                nextPatternId = rs.getInt("MAX") + 1;

            }

        } finally {

            ConnectionManager.close(conn, stmt, rs);

        }

        return nextPatternId;

    }
    
    public Pattern getPatternById(int patternId) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Pattern pattern = null;

        String sql = "SELECT d.*, c.COLLECTION_NAME FROM PATTERN d LEFT OUTER JOIN COLLECTION c ON d.COLLECTION_ID = c.COLLECTION_ID where d.pattern_id=? ";
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, patternId);
            rs = stmt.executeQuery();
            while (rs.next()) {

                String patternName = rs.getString("PATTERN_NAME");
                String patternDescription = rs.getString("PATTERN_DESC");
                Double patternPrice = rs.getDouble("PATTERN_PRICE");
                int collectionId = rs.getInt("COLLECTION_ID");
                String collectionName = rs.getString("COLLECTION_NAME");
                TagDAO td = new TagDAO();
                pattern = new Pattern(patternId, patternName, patternDescription, patternPrice, new Collection(collectionId, collectionName), td.getTagsByPatternId(patternId));
            }
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return pattern;
    }

    public Pattern getPatternByProductId(int productId) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Pattern pattern = null;

        String sql = "SELECT * FROM PRODUCT WHERE PRODUCT_ID=?";
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, productId);
            rs = stmt.executeQuery();

            int patternId = rs.getInt("PATTERN_ID");
            pattern = getPatternById(patternId);

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return pattern;
    }
//     
//       
//       
//    public static ArrayList<String> getTagsByPatternId(String patternId)  throws SQLException{
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//        ArrayList<String> tags = new ArrayList();
//        
//        String sql = "SELECT * FROM pattern p, tag t, pattern_tag pt where p.pattern_id=pt.pattern_id and pt.tag_id =t.tag_id and p.pattern_id=?"; 
//        try {
//            conn = ConnectionManager.getConnection();
//            stmt = conn.prepareStatement(sql);
//            stmt.setString(1, patternId);
//            rs = stmt.executeQuery();
//            
//            while (rs.next()) {
//                
//                tags.add(rs.getString("TAG_NAME"));
//           
//            }
//
//        } finally {
//            ConnectionManager.close(conn, stmt, rs);
//        }
//        return tags;
//       
//       
//       
//    
//    }
//
//    public static void insertPattern(String patternID, String patternName, String patternDescription, double patternPrice, String collectionID) {
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//        String sql = "INSERT into pattern values(?,?,?,?,?)";
//
//        try {
//            conn = ConnectionManager.getConnection();
//            stmt = conn.prepareStatement(sql);
//            stmt.setString(1, patternID);
//            stmt.setString(2, patternName);
//            stmt.setString(3, patternDescription);
//            stmt.setDouble(4, patternPrice);
//            stmt.setString(5, collectionID);
//           
//            stmt.executeUpdate();
//            
//        } catch (SQLException ex) {
//            handleSQLException(ex, sql);
//        } finally {
//            ConnectionManager.close(conn, stmt, rs);
//        }
//    }
//    
//    public static void updatePattern(String patternID, String patternName, String patternDescription, double patternPrice, String collectionID) {
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//        String sql = "update pattern set PATTERN_NAME=?, PATTERN_DESCRIPTION=?, PATTERN_PRICE=?, COLLECTION_ID=? where PATTERN_ID=?";
//
//        try {
//            conn = ConnectionManager.getConnection();
//            stmt = conn.prepareStatement(sql);
//            stmt.setString(1, patternName);
//            stmt.setString(2, patternDescription);
//            stmt.setDouble(3, patternPrice);
//            stmt.setString(4, collectionID);
//            stmt.setString(5, patternID);
//           
//            stmt.executeUpdate();
//            
//        } catch (SQLException ex) {
//            handleSQLException(ex, sql);
//        } finally {
//            ConnectionManager.close(conn, stmt, rs);
//        }
//    }
//    
//    public static void addPattern(String patternID, String patternName, String patternDescription, double patternPrice, String collectionID) throws SQLException{
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//        String sql = "INSERT INTO PATTERN VALUES (?,?,?,?,?);";
//
//        try {
//            conn = ConnectionManager.getConnection();
//            stmt = conn.prepareStatement(sql);
//            stmt.setString(1, patternID);
//            stmt.setString(2, patternName);
//            stmt.setString(3, patternDescription);
//            stmt.setDouble(4, patternPrice);
//            stmt.setString(5, collectionID);
//           
//            stmt.executeUpdate();
//            
//        } finally {
//            ConnectionManager.close(conn, stmt, rs);
//        }
//    }
//    
//    public static void addPatternFabric(String patternID, String fabricID) {
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//        String sql = "INSERT INTO PATTERN_FABRIC VALUES (?,?);";
//
//        try {
//            conn = ConnectionManager.getConnection();
//            stmt = conn.prepareStatement(sql);
//            stmt.setString(1, patternID);
//            stmt.setString(2, fabricID);
//
//           
//            stmt.executeUpdate();
//            
//        } catch (SQLException ex) {
//            handleSQLException(ex, sql);
//        } finally {
//            ConnectionManager.close(conn, stmt, rs);
//        }
//    }
//    
//    
//    public static void addProduct(String patternID, String fabricID, String colourID, Double colourPrice, String imageURL) throws SQLException {
//        
//        //generate next SKU
//        String sku = getNextSKU();
//        
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//        String sql = "INSERT INTO PRODUCT VALUES (?, ?, ?, ?, ?, ?)";
//
//        try {
//            conn = ConnectionManager.getConnection();
//            stmt = conn.prepareStatement(sql);
//            stmt.setString(1, sku);
//            stmt.setString(2, patternID);
//            stmt.setString(3, fabricID);
//            stmt.setString(4, colourID);
//            stmt.setDouble(5, colourPrice);
//            stmt.setString(6, imageURL);
//           
//            stmt.executeUpdate();
//            
//        } catch (SQLException ex) {
//            handleSQLException(ex, sql);
//        } finally {
//            ConnectionManager.close(conn, stmt, rs);
//        }
//    }
//    
//    
//    private static void handleSQLException(SQLException ex, String sql, String... parameters) {
//        String msg = "Unable to access data; SQL=" + sql + "\n";
//        for (String parameter : parameters) {
//            msg += "," + parameter;
//        }
//        Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, msg, ex);
//    }
//
}
