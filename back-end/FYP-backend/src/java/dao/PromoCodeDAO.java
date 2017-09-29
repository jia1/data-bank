/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import database.ConnectionManager;
import entity.PromoCode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Huiyan
 */
public class PromoCodeDAO {

    //Create 1 PromoCode
    public void addPromoCode(PromoCode pc) throws SQLException {
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "INSERT INTO PROMO_CODE (PROMO_CODE_ID, PROMO_CODE, PROMO_NAME, PROMO_TYPE, PROMO_VALUE, MIN_PURCHASE, MAX_DISCOUNT, QUOTA, COUNTER, START_DATE, END_DATE, DELETED) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, pc.getPromoCodeId());
            stmt.setString(2, pc.getPromoCode());
            stmt.setString(3, pc.getPromoName());
            stmt.setString(4, pc.getPromoType());
            stmt.setDouble(5, pc.getPromoValue());
            stmt.setDouble(6, pc.getMinPurchase());
            stmt.setDouble(7, pc.getMaxDiscount());
            stmt.setInt(8, pc.getQuota());
            stmt.setInt(9, pc.getCounter());
            stmt.setDate(10, pc.getStartDate());
            stmt.setDate(11, pc.getEndDate());
            stmt.setDouble(12, 'N');
            stmt.executeUpdate();

            
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        
    }

    //Retrieve next promo ID
    public int getNextPromoCodeId() throws SQLException {
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        int nextPromoCodeId = 0;

        String sql = "SELECT MAX(PROMO_CODE_ID) AS MAX FROM PROMO_CODE";

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {

                nextPromoCodeId = rs.getInt("MAX") +1;
                
            }

        } finally {
            
            ConnectionManager.close(conn, stmt, rs);
            
        }

        return nextPromoCodeId;
        
    }

    //Retrieve 1 PromoCode by ID
    public PromoCode getPromoCodeById(int id) throws SQLException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        PromoCode p = null;

        String sql = "SELECT * FROM PROMO_CODE WHERE PROMO_CODE_ID = ?";

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            while (rs.next()) {

                int promoId = rs.getInt("PROMO_CODE_ID");
                String promoCode = rs.getString("PROMO_CODE");
                String promoName = rs.getString("PROMO_NAME");
                String promoType = rs.getString("PROMO_TYPE");
                double promoValue = rs.getDouble("PROMO_VALUE");
                double minPurchase = rs.getDouble("MIN_PURCHASE");
                double discount = rs.getDouble("MAX_DISCOUNT");
                int quota = rs.getInt("QUOTA");
                int counter = rs.getInt("COUNTER");
                Date start_date = rs.getDate("START_DATE");
                Date end_date = rs.getDate("END_DATE");
                p = new PromoCode(promoId, promoCode, promoName, promoType, promoValue, minPurchase, discount, quota, counter, start_date, end_date);

            }

        } finally {
            
            ConnectionManager.close(conn, stmt, rs);
            
        }

        return p;

    }

    //Retrieve 1 PromoCode by ID
    public PromoCode[] getAllPromoCodes() throws SQLException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        PromoCode p = null;
        
        ArrayList<PromoCode> promoList = new ArrayList<>();

        String sql = "SELECT * FROM PROMO_CODE WHERE DELETED = 'N'";

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {

                int promoId = rs.getInt("PROMO_CODE_ID");
                String promoCode = rs.getString("PROMO_CODE");
                String promoName = rs.getString("PROMO_NAME");
                String promoType = rs.getString("PROMO_TYPE");
                double promoValue = rs.getDouble("PROMO_VALUE");
                double minPurchase = rs.getDouble("MIN_PURCHASE");
                double discount = rs.getDouble("MAX_DISCOUNT");
                int quota = rs.getInt("QUOTA");
                int counter = rs.getInt("COUNTER");
                Date start_date = rs.getDate("START_DATE");
                Date end_date = rs.getDate("END_DATE");
                p = new PromoCode(promoId, promoCode, promoName, promoType, promoValue, minPurchase, discount, quota, counter, start_date, end_date);
                promoList.add(p);
                
            }

        } finally {
            
            ConnectionManager.close(conn, stmt, rs);
            
        }

        PromoCode[] promoArr = promoList.toArray(new PromoCode[promoList.size()]);
        return promoArr;

    }
    
    //Update
    public void updatePromoCode(PromoCode pc) throws SQLException{
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "UPDATE PROMO_CODE SET PROMO_CODE = ?, PROMO_NAME = ?, PROMO_TYPE = ?, PROMO_VALUE = ?, MIN_PURCHASE = ?, MAX_DISCOUNT = ?, QUOTA = ?, COUNTER = ?, START_DATE = ?, END_DATE = ? WHERE PROMO_CODE_ID = ?";

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, pc.getPromoCode());
            stmt.setString(2,pc.getPromoName());
            stmt.setString(3, pc.getPromoType());
            stmt.setDouble(4, pc.getPromoValue());
            stmt.setDouble(5, pc.getMinPurchase());
            stmt.setDouble(6, pc.getMaxDiscount());
            stmt.setInt(7, pc.getQuota());
            stmt.setInt(8, pc.getCounter());
            stmt.setDate(9, pc.getStartDate());
            stmt.setDate(10, pc.getEndDate());
            stmt.setInt(11, pc.getPromoCodeId());
            stmt.executeUpdate();

        } finally {
            
            ConnectionManager.close(conn, stmt, rs);
            
        }
        
    }
    
    //Soft Delete
    public String deletePromoCodeById(int promoId) throws SQLException {
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "UPDATE PROMO_CODE SET DELETED = 'Y' WHERE PROMO_CODE_ID = ?";

        try {
            
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, promoId);
            stmt.executeUpdate();

        } finally {
            
            ConnectionManager.close(conn, stmt, rs);
            
        }
        return "Success";
        
    }
 
    
}
