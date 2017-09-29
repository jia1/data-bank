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
import entity.Address;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerAddressDAO {

    public Address[] getAddressesByEmail(String email) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Address a = null;
        ArrayList<Address> address = new ArrayList<>();

        String sql = "SELECT * FROM customer_address WHERE email= ? ";
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            rs = stmt.executeQuery();

            while (rs.next()) {

                String recipientName = rs.getString("RECIPIENT_NAME");
                String phoneNo = rs.getString("PHONE_NO");
                int addressId = rs.getInt("ADDRESS_ID");
                String addressLine = rs.getString("ADDRESS_LINE");
                String city = rs.getString("CITY");
                String country = rs.getString("COUNTRY");
                String postalCode = rs.getString("POSTAL_CODE");
                String isDefault = rs.getString("ISDEFAULT");

                a = new Address(email, recipientName, phoneNo, addressId, addressLine, city, country, postalCode, isDefault);
                address.add(a);

            }
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return address.toArray(new Address[address.size()]);
    }

    public Address getAddressById(int addressId) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Address a = null;

        String sql = "SELECT * FROM customer_address WHERE address_id= ? ";
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, addressId);
            rs = stmt.executeQuery();

            while (rs.next()) {

                String email = rs.getString("EMAIL");
                String recipientName = rs.getString("RECIPIENT_NAME");
                String phoneNo = rs.getString("PHONE_NO");
                String addressLine = rs.getString("ADDRESS_LINE");
                String city = rs.getString("CITY");
                String country = rs.getString("COUNTRY");
                String postalCode = rs.getString("POSTAL_CODE");
                String isDefault = rs.getString("ISDEFAULT");

                a = new Address(email, recipientName, phoneNo, addressId, addressLine, city, country, postalCode, isDefault);

            }
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return a;
    }

    public int getTheNumOfAddressesByEmail(String email) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int num = 0;

        String sql = "SELECT COUNT(*) FROM CUSTOMER_ADDRESS WHERE EMAIL=?";
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            while (rs.next()) {
                num = rs.getInt(1);
            }

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return num;

    }

    public String addAddressToCustomer(Address address) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Address a = null;

        String sql = "INSERT INTO CUSTOMER_ADDRESS (ADDRESS_ID, EMAIL, RECIPIENT_NAME, PHONE_NO, ADDRESS_LINE, CITY, COUNTRY, POSTAL_CODE, ISDEFAULT) VALUES (?,?,?,?,?,?,?,?,?)";
        String email = address.getEmail();

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            String addressLine = address.getAddressLine();
            String city = address.getCity();
            String country = address.getCountry();
            String postalCode = address.getPostalCode();
            String isDefault = address.getIsDefault();
            String recipientName = address.getRecipientName();
            String phoneNo = address.getPhoneNo();

            stmt.setInt(1, getTheNumOfAddressesByEmail(email) + 1);
            stmt.setString(2, email);
            stmt.setString(3, recipientName);
            stmt.setString(4, phoneNo);
            stmt.setString(5, addressLine);
            stmt.setString(6, city);
            stmt.setString(7, country);
            stmt.setString(8, postalCode);
            stmt.setString(9, isDefault);

            stmt.executeUpdate();
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return "Success";

    }

    public String deleteAddressByCustomerEmail(String email, String addressId) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "DELETE FROM CUSTOMER_ADDRESS WHERE EMAIL = ? AND ADDRESS_ID=? ";
        try {

            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, addressId);
            rs = stmt.executeQuery();

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return "Success";

    }

//    public String updateAddressByCustomerEmail(String email, String addressId, String addressLine, String city, String country, String postalCode, String isDefault) throws SQLException {
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//        String sql = "UPDATE CUSTOMER_ADDRESS SET ADDRESS_LINE = ?, CITY = ?, COUNTRY = ?, POSTAL_CODE = ?, ISDEFAULT = ? WHERE EMAIL=? AND ADDDRESS_ID=?";
//        try {
//
//            conn = ConnectionManager.getConnection();
//            stmt = conn.prepareStatement(sql);
//            stmt.setString(1, addressLine);
//            stmt.setString(2, city);
//            stmt.setString(3, country);
//            stmt.setString(4, postalCode);
//            stmt.setString(5, isDefault);
//            stmt.setString(6, email);
//            stmt.setString(7, addressId);
//
//        } finally {
//            ConnectionManager.close(conn, stmt, rs);
//        }
//
//        return "Success";
//
//    }
}
