/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import database.ConnectionManager;
import entity.Address;
import entity.Customer;
import entity.Order;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author JeremyBachtiar
 */
public class CustomerDAO {
//    

    public Customer retrieveCustomerByEmail(String email) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Customer customer = null;

        String sql = "SELECT * FROM customer WHERE email = ? ";
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            while (rs.next()) {

                String firstName = rs.getString(2);
                String lastName = rs.getString(3);
                String phoneNumber = rs.getString(4);
                String password = rs.getString(5);
                String verified = rs.getString(6);
                CustomerAddressDAO customerAddressDao = new CustomerAddressDAO();
                CartDAO cartDAO = new CartDAO();
                OrderDAO orderDAO = new OrderDAO();
                
                //Error might be in these 2 DAOs
                Order[] orders = orderDAO.getOrderByEmail(email);
                
                Address[] address = customerAddressDao.getAddressesByEmail(email);
                               
                customer = new Customer(email, firstName, lastName, phoneNumber, password, verified, cartDAO.getCartByEmail(email), address, orders);
                
                if (customer != null) {
                    System.out.println("Im not Null");
                } else {
                    System.out.println("Im null");
                }
            }

        }
        catch(SQLException e){
            System.out.println("FROM CUST DAO" + e);
        }
        finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        System.out.println("HEY");
        return customer;
    }

    public ArrayList<Customer> retrieveAllCustomers() throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Customer> customers = new ArrayList<>();

        String sql = "SELECT * FROM customer";
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String email = rs.getString(1);
                String firstName = rs.getString(2);
                String lastName = rs.getString(3);
                String phoneNumber = rs.getString(4);
                String password = rs.getString(5);
                String verified = rs.getString(6);
                CustomerAddressDAO customerAddressDao = new CustomerAddressDAO();
                Address[] address = customerAddressDao.getAddressesByEmail(email);
                Order[] orders = new Order[0];
                customers.add(new Customer(email, firstName, lastName, phoneNumber, password, verified, null, address, orders));

            }

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return customers;
    }
    
    public String updateCustomerVerification(String email, String status) throws SQLException{
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        if (retrieveCustomerByEmail(email) == null) {
            return "Customer does not exist";
        } else {

            String sql = "UPDATE CUSTOMER SET verified = ? WHERE EMAIL=?";
            try {
                conn = ConnectionManager.getConnection();
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, status);
                stmt.setString(2, email);
                stmt.executeUpdate();

            } finally {
                ConnectionManager.close(conn, stmt, rs);
            }
        }
        return "Success";
    }

    public String updateCustomerByEmail(String email, String firstName, String lastName, String phoneNo, String password) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        if (retrieveCustomerByEmail(email) == null) {
            return "Customer does not exist";
        } else {

            String sql = "UPDATE CUSTOMER SET FIRST_NAME = ?, LAST_NAME = ?, PHONE_NO = ?, PASSWORD = ? WHERE EMAIL=?";
            try {
                conn = ConnectionManager.getConnection();
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, firstName);
                stmt.setString(2, lastName);
                stmt.setString(3, phoneNo);
                stmt.setString(4, password);
                stmt.setString(5, email);
                stmt.executeUpdate();

            } finally {
                ConnectionManager.close(conn, stmt, rs);
            }
        }
        return "Success";
    }

    public String addCustomer(Customer customer) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "INSERT INTO CUSTOMER VALUES (?,?,?,?,?,?)";
        if (retrieveCustomerByEmail(customer.getEmail()) == null) {

            try {

                conn = ConnectionManager.getConnection();
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, customer.getEmail());
                stmt.setString(2, customer.getFirstName());
                stmt.setString(3, customer.getLastName());
                stmt.setString(4, customer.getPhoneNo());
                stmt.setString(5, customer.getPassword());
                stmt.setString(6, customer.getVerified());

                stmt.executeUpdate();

            } finally {
                ConnectionManager.close(conn, stmt, rs);
            }
        } else {
            return "Customer already exist";
        }

        return "Success";

    }

    public String deleteCustomerByEmail(String email) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "DELETE FROM CUSTOMER WHERE EMAIL = ?";

        try {

            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);

            rs = stmt.executeQuery();

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return "Success";
    }

////    
////    public static String retrievePasswordByEmail(String email) {
////        Connection conn = null;
////        PreparedStatement stmt = null;
////        ResultSet rs = null;
////        String password = null;
////        
////        String sql = "SELECT password FROM customer WHERE email =  ? "; 
////        try {
////            conn = ConnectionManager.getConnection();
////            stmt = conn.prepareStatement(sql);
////            stmt.setString(1, email);
////            rs = stmt.executeQuery();
////            
////            while (rs.next()) {
////                password = rs.getString(1);
////             
////            }
////
////        } catch (SQLException ex) {
////            handleSQLException(ex, sql);
////        } finally {
////            ConnectionManager.close(conn, stmt, rs);
////        }
////        return password;
////    }
////    
////    
////    public static int retrieveNumOfCustomers() {
////        Connection conn = null;
////        PreparedStatement stmt = null;
////        ResultSet rs = null;
////        int numOfCustomer=0;
////        
////        String sql = "SELECT count(*) FROM customer"; 
////        try {
////            conn = ConnectionManager.getConnection();
////            stmt = conn.prepareStatement(sql);
////             rs = stmt.executeQuery();
////            
////            while (rs.next()) {
////                numOfCustomer = rs.getInt(1);
////             
////            }
////
////        } catch (SQLException ex) {
////            handleSQLException(ex, sql);
////        } finally {
////            ConnectionManager.close(conn, stmt, rs);
////        }
////        return numOfCustomer;
////    }
////        
////     public static void insertCustomer(String email, String firstName, String lastName, String phoneNumber, String address, String postalCode, String password ) {
////        Connection conn = null;
////        PreparedStatement stmt = null;
////        ResultSet rs = null;
////        String sql = "INSERT into customer values(?,?,?,?,?,?,?,?,?)";
//// 
////        try {
////            conn = ConnectionManager.getConnection();
////            stmt = conn.prepareStatement(sql);
////            stmt.setString(1, email);
////            stmt.setString(2,firstName);
////            stmt.setString(3, lastName);
////            stmt.setString(4, phoneNumber);
////            stmt.setString(5, address);
////            stmt.setString(6, "Singapore");
////            stmt.setString(7, postalCode);
////            stmt.setString(8, password);
////            stmt.setString(9, "N");
////           
////            stmt.executeUpdate();
////            
////        } catch (SQLException ex) {
////            handleSQLException(ex, sql);
////        } finally {
////            ConnectionManager.close(conn, stmt, rs);
////        }
////     }
////        
////        public static void updateCustomer(String email, String firstName, String lastName, String phoneNumber, String address, String postalCode, String password ) {
////            Connection conn = null;
////            PreparedStatement stmt = null;
////            ResultSet rs = null;
////            String sql = "update customer set first_name=?, last_name=?, phone_number=?, address=?, country=?, postal_code=?, password=? where email=?";
//// 
////            try {
////                conn = ConnectionManager.getConnection();
////                stmt = conn.prepareStatement(sql);
////              
////                stmt.setString(1,firstName);
////                stmt.setString(2, lastName);
////                stmt.setString(3, phoneNumber);
////                stmt.setString(4, address);
////                stmt.setString(5, "Singapore");
////                stmt.setString(6, postalCode);
////                stmt.setString(7, password);
////                stmt.setString(8, email);
////       
////
////                stmt.executeUpdate();
////
////            } catch (SQLException ex) {
////                handleSQLException(ex, sql);
////            } finally {
////                ConnectionManager.close(conn, stmt, rs);
////            }
////    
////    
////    }
////    
////    
////    
////    
////    private static void handleSQLException(SQLException ex, String sql, String... parameters) {
////        String msg = "Unable to access data; SQL=" + sql + "\n";
////        for (String parameter : parameters) {
////            msg += "," + parameter;
////        }
////        Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, msg, ex);
////    }
}
