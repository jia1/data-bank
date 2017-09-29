///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
package dao;
//
import database.ConnectionManager;
import entity.Staff;
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
public class StaffDAO {
//    
//    public static String addStaff(Staff s){
//        
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//       
//        String sql = "INSERT INTO STAFF VALUES (?,?,?,?,?,?)";
//        
//        try {
//            
//            conn = ConnectionManager.getConnection();
//            stmt = conn.prepareStatement(sql);
//            stmt.setString(1, s.getEmail());
//            stmt.setString(2, s.getFirstName());
//            stmt.setString(3, s.getLastName());
//            stmt.setString(4, s.getPhoneNumber());
//            stmt.setString(5, s.getPassword());
//            stmt.setString(6, s.getRoleCode());
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
//        return "Success";
//        
//    }
//    
//    public static String updateStaff(Staff s){
//        
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//       
//        String sql = "UPDATE Staff SET first_name = ?, last_name = ?, phone_num = ?, password = ?, role_code = ? WHERE email =?";
//        
//        try {
//            
//            conn = ConnectionManager.getConnection();
//            stmt = conn.prepareStatement(sql);
//            stmt.setString(6, s.getEmail());
//            stmt.setString(1, s.getFirstName());
//            stmt.setString(2, s.getLastName());
//            stmt.setString(3, s.getPhoneNumber());
//            stmt.setString(4, s.getPassword());
//            stmt.setString(5, s.getRoleCode());
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
//        return "Success";
//        
//    }
//    
//    public static Staff retrieveStaffByEmail(String email) {
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//        Staff staff = null;
//        
//        String sql = "SELECT * FROM staff WHERE email = ? "; 
//        try {
//            conn = ConnectionManager.getConnection();
//            stmt = conn.prepareStatement(sql);
//            stmt.setString(1, email);
//            rs = stmt.executeQuery();
//            
//            while (rs.next()) {
//                
//                String firstName = rs.getString(2);
//                String lastName = rs.getString(3);
//                String phoneNumber = rs.getString(4);
//                String password = rs.getString(5);
//                String roleCode = rs.getString(6);
//               
//                staff = new Staff(email,firstName,lastName, phoneNumber, password, roleCode);
//             
//            }
//
//        } catch (SQLException ex) {
//            handleSQLException(ex, sql);
//        } finally {
//            ConnectionManager.close(conn, stmt, rs);
//        }
//        return staff;
//    }
//  
 
    
    
    public Staff getStaffByEmail(String email) throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Staff result = null;
        
        String sql = "SELECT * FROM STAFF WHERE EMAIL = ?";
        try {
            
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            while(rs.next()){
                String firstName = rs.getString("FIRST_NAME");
                String lastName = rs.getString("LAST_NAME");
                String phoneNumber = rs.getString("PHONE_NO");
                String password = rs.getString("PASSWORD");
                int roleId = rs.getInt("ROLE_ID");
                
                result = new Staff(email, firstName, lastName, phoneNumber, password, roleId);
            }

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        
        return result;
        
    }
    
    public ArrayList<Staff> getAllStaff() throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Staff> staffList = new ArrayList<Staff>();
        
        String sql = "SELECT * FROM STAFF";
        try {
            
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while(rs.next()){
                String email = rs.getString("EMAIL");
                String firstName = rs.getString("FIRST_NAME");
                String lastName = rs.getString("LAST_NAME");
                String phoneNumber = rs.getString("PHONE_NO");
                String password = rs.getString("PASSWORD");
                int roleId = rs.getInt("ROLE_ID");
                
                Staff s = new Staff(email, firstName, lastName, phoneNumber, password, roleId);
                staffList.add(s);
            }

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        
        return staffList;
        
    }
    
    public String addStaff(Staff s) throws SQLException{
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        if(getStaffByEmail(s.getEmail())==null){
            
            try {
                
                String sql = "INSERT INTO STAFF VALUES (?,?,?,?,?,?)";
                conn = ConnectionManager.getConnection();
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, s.getEmail());
                stmt.setString(2, s.getFirstName());
                stmt.setString(3, s.getLastName());
                stmt.setString(4, s.getPhoneNo());
                stmt.setString(5, s.getPassword());
                stmt.setInt(6, s.getRoleId());
                System.out.println(s.getFirstName());
                System.out.println(s.getLastName());
                System.out.println(s.getEmail());
                System.out.println(s.getPhoneNo());
                System.out.println(s.getPassword());
                System.out.println(s.getRoleId());

                stmt.executeUpdate();
                
                
            }
            finally {
                ConnectionManager.close(conn, stmt, rs);
            }
        }else{
            return "Staff already exist";
        }
        
        return "Success";
    }
    
    public String deleteStaffByEmail(String email) throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
    
        String sql = "DELETE FROM STAFF WHERE EMAIL = ?";
        
        try {

            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);

            stmt.executeUpdate();

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return "Success";
    }
    public String updateStaff(Staff s) throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
       
        String sql = "UPDATE STAFF SET FIRST_NAME = ?, LAST_NAME = ?, PHONE_NO = ?, PASSWORD = ?, ROLE_ID = ? WHERE EMAIL = ?";
        if(getStaffByEmail(s.getEmail())!=null){
            
            try {

                conn = ConnectionManager.getConnection();
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, s.getFirstName());
                stmt.setString(2, s.getLastName());
                stmt.setString(3, s.getPhoneNo());
                stmt.setString(4, s.getPassword());
                stmt.setInt(5, s.getRoleId());
                stmt.setString(6, s.getEmail());
                
                stmt.executeUpdate();
                
            } finally {
                ConnectionManager.close(conn, stmt, rs);
            }
        }else{
            return "Staff does not exist";
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
