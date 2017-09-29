/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import database.ConnectionManager;
import entity.Fabric;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author JeremyBachtiar
 */
public class FabricDAO {
    
    public String addFabric(Fabric fabric) throws SQLException{
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
       
        String sql = "INSERT INTO FABRIC (FABRIC_ID, FABRIC_NAME, FABRIC_DESC, FABRIC_PRICE, DELETED) VALUES (?,?,?,?,?)";
        
        if(getFabricById(fabric.getFabricId())!=null){
            
            try {

                conn = ConnectionManager.getConnection();
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, getNextFabricId());
                stmt.setString(2, fabric.getFabricName());
                stmt.setString(3, fabric.getFabricDesc());
                stmt.setDouble(4, fabric.getFabricPrice());
                stmt.setString(5, "N");
                
                stmt.executeUpdate();
                
            } finally {
                ConnectionManager.close(conn, stmt, rs);
            }
        }else{
            return "Fabric already exist";
        }
        
        return "Success";
    }
    
    public Fabric getFabricById(int fabricId) throws SQLException{
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Fabric result = null;
        
        String sql = "SELECT * FROM FABRIC WHERE FABRIC_ID = ?";
        
        try {
            
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, fabricId);
            rs = stmt.executeQuery();
            
            while(rs.next()){
                String fabricName = rs.getString("FABRIC_NAME");
                String fabricDesc = rs.getString("FABRIC_DESC");
                double fabricPrice = rs.getDouble("FABRIC_PRICE");
                String deleted = rs.getString("DELETED");
                
                result = new Fabric(fabricId, fabricName,fabricDesc,fabricPrice);
            }

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        
        return result;
        
    }
    
    public ArrayList<Fabric> getAllAvailableFabrics() throws SQLException{
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Fabric> fabricList = new ArrayList<Fabric>();
        
        String sql = "SELECT * FROM FABRIC WHERE DELETED = ? ";
        try {
            
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "N");
            rs = stmt.executeQuery();
            
            while(rs.next()){
                int fabricId = rs.getInt("FABRIC_ID");
                String fabricName = rs.getString("FABRIC_NAME");
                String fabricDesc = rs.getString("FABRIC_DESC");
                double fabricPrice = rs.getDouble("FABRIC_PRICE");
                String deleted = rs.getString("DELETED");
                
                Fabric fabric = new Fabric(fabricId, fabricName,fabricDesc,fabricPrice);
                fabricList.add(fabric);
            }

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        
        return fabricList;
        
    }
    
    public ArrayList<Fabric> getFabricsByPatternId(int patternId) throws SQLException{
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Fabric> fabricList = new ArrayList();
        
        String sql = "SELECT * FROM FABRIC F, PATTERN P, PRODUCT PRO WHERE P.PATTERN_ID=PRO.PATTERN_ID AND PRO.FABRIC_ID=F.FABRIC_ID AND P.PATTERN_ID=? AND F.DELETED=? AND PRO.DELETED=? AND P.DELETED=?";
        try {
            
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, patternId);
            stmt.setString(2, "N");
            stmt.setString(3, "N");
            stmt.setString(4, "N");
            rs = stmt.executeQuery();
            
            while(rs.next()){
                int fabricId = rs.getInt("FABRIC_ID");
                String fabricName = rs.getString("FABRIC_NAME");
                String fabricDesc = rs.getString("FABRIC_DESC");
                double fabricPrice = rs.getDouble("FABRIC_PRICE");
              
                
                Fabric fabric = new Fabric(fabricId, fabricName,fabricDesc,fabricPrice);
                if(!checkForDuplicatedFabric(fabricList, fabric)){
                    fabricList.add(fabric);
                }
            }

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        
        return fabricList;
        
    }
    
    public boolean checkForDuplicatedFabric(ArrayList<Fabric> fabrics, Fabric fabric){
        for(int i=0; i<fabrics.size(); i++){
            if(fabrics.get(i).getFabricId()==fabric.getFabricId()){
                return true;
            }
        }
        
        return false;
    }
    
    
    
    
    public String deleteFabricById(int id) throws SQLException{
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
       
        String sql = "UPDATE FABRIC SET DELETED = ? WHERE FABRIC_ID = ?";
        
        try {

            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "Y");
            stmt.setInt(2, id);

            rs = stmt.executeQuery();

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return "Success";
    }
    public String updateFabric(Fabric fabric)throws SQLException{
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
       
        String sql = "UPDATE FABRIC SET FABRIC_NAME = ?, FABRIC_DESC = ?, FABRIC_PRICE = ? WHERE FABRIC_ID = ?";
        
        if(getFabricById(fabric.getFabricId())!=null){
            
            try {

                conn = ConnectionManager.getConnection();
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, fabric.getFabricName());
                stmt.setString(2, fabric.getFabricDesc());
                stmt.setDouble(3, fabric.getFabricPrice());
                stmt.setInt(4, fabric.getFabricId());
                
                rs = stmt.executeQuery();
                
            } finally {
                ConnectionManager.close(conn, stmt, rs);
            }
        }else{
            return "Fabric does not exist";
        }
        
        return "Success";
    }
    
    
    public Fabric getFarbicByProductId(int productId) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Fabric fabric = null;

        String sql = "SELECT * FROM BEDDING WHERE PRODUCT_ID=?";
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, productId);
            rs = stmt.executeQuery();

            int fabricId = rs.getInt("FABRIC_ID");
            fabric =getFabricById(fabricId);

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return fabric;

    }
    
    public Fabric[] getCurrentFabrics() throws SQLException{
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Fabric> fabricList = new ArrayList<Fabric>();
        
        String sql = "SELECT * FROM FABRIC WHERE DELETED = 'N' AND FABRIC_ID IN (SELECT DISTINCT FABRIC_ID FROM PRODUCT WHERE DELETED = 'N')"; 
        
        try {
            
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                
              
                int fabricId = rs.getInt("FABRIC_ID");
                String fabricName = rs.getString("FABRIC_NAME");
                String fabricDesc = rs.getString("FABRIC_DESC");
                Double fabricPrice = rs.getDouble("FABRIC_PRICE");
                Fabric f = new Fabric(fabricId, fabricName, fabricDesc, fabricPrice);
                fabricList.add(f);
                
            }

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        
        return fabricList.toArray(new Fabric[fabricList.size()]);
   
    }
    
    public int getNextFabricId() throws SQLException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int nextFabricId = 0;

        String sql = "SELECT MAX(FABRIC_ID) AS MAX FROM FABRIC";

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {

                nextFabricId = rs.getInt("MAX") + 1;

            }

        } finally {

            ConnectionManager.close(conn, stmt, rs);

        }

        return nextFabricId;

    }
    
    
//    private static void handleSQLException(SQLException ex, String sql, String... parameters) {
//        String msg = "Unable to access data; SQL=" + sql + "\n";
//        for (String parameter : parameters) {
//            msg += "," + parameter;
//        }
//        Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, msg, ex);
//    }
}
