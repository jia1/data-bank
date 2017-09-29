/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import database.ConnectionManager;
import entity.Collection;
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
public class CollectionDAO {
    
    public String addCollection(Collection collection) throws SQLException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "INSERT INTO COLLECTION (COLLECTION_ID, COLLECTION_NAME) VALUES (?,?)";

            try {

                conn = ConnectionManager.getConnection();
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, getNextCollectionId());
                stmt.setString(2, collection.getCollectionName());

                stmt.executeUpdate();

            } finally {
                ConnectionManager.close(conn, stmt, rs);
            }

        return "Success";
    }
    
    public int getNextCollectionId() throws SQLException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int nextCollectionId = 0;

        String sql = "SELECT MAX(COLLECTION_ID) AS MAX FROM COLLECTION";

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {

                nextCollectionId = rs.getInt("MAX") + 1;

            }

        } finally {

            ConnectionManager.close(conn, stmt, rs);

        }

        return nextCollectionId;

    }
    
    public String addTag(Collection c) throws SQLException{
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
       
        String sql = "INSERT INTO TAG VALUES (?,?)";
        if(getCollectionById(c.getCollectionId())!=null){
            
            try {

                conn = ConnectionManager.getConnection();
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, c.getCollectionId());
                stmt.setString(2, c.getCollectionName());
                
                rs = stmt.executeQuery();
                
            } finally {
                ConnectionManager.close(conn, stmt, rs);
            }
        }else{
            return "Collection already exist";
        }
        
        return "Success";
    }
    
    public Collection getCollectionById(int collectionId) throws SQLException{
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection c=null;
  
        
        String sql = "SELECT * FROM collection where collection_id=?"; 
        
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, collectionId);
            rs = stmt.executeQuery();
        
            
            while (rs.next()) {
                
              
                String collectionName = rs.getString("collection_name");
                c= new Collection (collectionId, collectionName);
                
            }

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        
        return c;
   
    }
    
      public Collection getCollectionByPatternId(int patternId) throws SQLException{
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Collection c=null;
  
        
        String sql = "SELECT * FROM pattern where pattern_id=?"; 
        
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, patternId);
            rs = stmt.executeQuery();
        
            
            while (rs.next()) {
                
              
                int collectionId = rs.getInt("collection_id");
                c=getCollectionById(collectionId);
                
            }

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        
        return c;
   
    }
      
    public Collection[] getCurrentCollections() throws SQLException{
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Collection> collectionList = new ArrayList<Collection>();
        
        String sql = "SELECT * FROM COLLECTION WHERE COLLECTION_ID IN (SELECT DISTINCT COLLECTION_ID FROM PATTERN WHERE PATTERN_ID IN (SELECT DISTINCT PATTERN_ID FROM PRODUCT WHERE DELETED = 'N'))"; 
        
        try {
            
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                
              
                int collectionId = rs.getInt("COLLECTION_ID");
                String collectionName = rs.getString("COLLECTION_NAME");
                Collection c = new Collection(collectionId, collectionName);
                collectionList.add(c);
                
            }

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        
        return collectionList.toArray(new Collection[collectionList.size()]);
   
    }
}
