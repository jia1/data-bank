/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import database.ConnectionManager;
import entity.Beneficiary;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class BeneficiaryDAO {
     public static Beneficiary retrieveBeneficiaryById(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Beneficiary b = null;
        
        String sql = "SELECT * FROM Beneficiary WHERE id = ? "; 
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                
                String name = rs.getString(2);
                String location = rs.getString(3);
				String targetppl = rs.getString(4);
				String contactName = rs.getString(5);
				String email = rs.getString(6);
				int contactNum = rs.getString(7);
                
                b= new Beneficiary(id, name, location, targetppl, contactName, email, contactNum);
            
              
                
             
            }

        } catch (SQLException ex) {
            handleSQLException(ex, sql);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return b;
    }
     
        private static void handleSQLException(SQLException ex, String sql, String... parameters) {
        String msg = "Unable to access data; SQL=" + sql + "\n";
        for (String parameter : parameters) {
            msg += "," + parameter;
        }
        Logger.getLogger(DonorDAO.class.getName()).log(Level.SEVERE, msg, ex);
    }
    
}
