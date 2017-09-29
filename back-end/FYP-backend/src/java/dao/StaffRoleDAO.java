///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
package dao;
//
import database.ConnectionManager;
import entity.StaffRole;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
//
///**
// *
// * @author Ong Yi Xuan
// */
public class StaffRoleDAO {

    public String addStaffRole(StaffRole sr) throws SQLException{
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
       
        String sql = "INSERT INTO STAFF_ROLE VALUES (?,?)";
        if(getStaffRoleById(sr.getRoleId())!=null){
            
            try {

                conn = ConnectionManager.getConnection();
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, sr.getRoleId());
                stmt.setString(2, sr.getRoleName());
                
                rs = stmt.executeQuery();
                
            } finally {
                ConnectionManager.close(conn, stmt, rs);
            }
        }else{
            return "Staff role already exist";
        }
        
        return "Success";
    }
    
    public StaffRole getStaffRoleById(int roleId) throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StaffRole result = null;
        
        String sql = "SELECT * FROM STAFF_ROLE WHERE ROLE_ID = ?";
        
        try {
            
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, roleId);
            rs = stmt.executeQuery();
            
            while(rs.next()){
                String roleName = rs.getString("ROLE_NAME");
                
                result = new StaffRole(roleId, roleName);
            }

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        
        return result;
        
    }
    
    public ArrayList<StaffRole> getAllStaffRole() throws SQLException{
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<StaffRole> roleList = new ArrayList<StaffRole>();
        
        String sql = "SELECT * FROM STAFF_ROLE";
        try {
            
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while(rs.next()){
                int roleId = rs.getInt("ROLE_ID");
                String roleName = rs.getString("ROLE_NAME");
                
                StaffRole sr = new StaffRole(roleId, roleName);
                roleList.add(sr);
            }

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        
        return roleList;
        
    }
    
    public String deleteStaffRoleById(String id) throws SQLException{
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
       
       String sql = "DELETE FROM STAFF_ROLE WHERE ROLE_ID = ?";
        
        try {

            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);

            rs = stmt.executeQuery();

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return "Success";
    }
    public String updateStaffRole(StaffRole sr)throws SQLException{
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
       
        String sql = "UPDATE STAFF_ROLE SET ROLE_NAME = ? WHERE ROLE_ID = ?";
        
        if(getStaffRoleById(sr.getRoleId())!=null){
            
            try {

                conn = ConnectionManager.getConnection();
                stmt = conn.prepareStatement(sql);
                
                stmt.setString(1, sr.getRoleName());
                stmt.setInt(2, sr.getRoleId());
                
                rs = stmt.executeQuery();
                
            } finally {
                ConnectionManager.close(conn, stmt, rs);
            }
        }else{
            return "Staff role does not exist";
        }
        
        return "Success";
    }
    
    private static void handleSQLException(SQLException ex, String sql, String... parameters) {
        String msg = "Unable to access data; SQL=" + sql + "\n";
        for (String parameter : parameters) {
            msg += "," + parameter;
        }
        Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, msg, ex);
    }
    
}
