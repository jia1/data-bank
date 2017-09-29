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
import entity.Order;
import entity.OrderItem;
import entity.OrderStatusLog;
import entity.PromoCode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrderDAO {
//    
//    public Order[] retrieveOrdersByEmail(String email) throws SQLException {
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//        Order o = null;
//        ArrayList<Order> orders = new ArrayList<>();
//        
//        String sql = "SELECT * FROM order WHERE email= ?";
//        try {
//            conn = ConnectionManager.getConnection();
//            stmt = conn.prepareStatement(sql);
//            stmt.setString(1, email);
//            rs = stmt.executeQuery();
//            
//            while (rs.next()) {
//                
//                int orderId = rs.getInt(1);
//                Timestamp orderDate = rs.getTimestamp(2);
//                double netAmt = rs.getDouble(3);
//                double promoDiscAmt = rs.getDouble(4);
//                String addressLine = rs.getString(5);
//                String city = rs.getString(6);
//                String country = rs.getString(7);
//                String postalCode = rs.getString(8);
//                String paymentRefNo = rs.getString(9);
//                int promoCode = rs.getInt(10);
//                Address a = new Address(0, addressLine, city, country, postalCode, "N");
//                address.add(a);
//                
//            }
//        } finally {
//            ConnectionManager.close(conn, stmt, rs);
//        }
//        
//        return address.toArray(new Address[address.size()]);
//    }

    public String addOrder(Order o) throws SQLException {
        System.out.println("IM IN ADDORDER");
        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt1 = null;
        ResultSet rs = null;
        int orderId = getNextOrderId();
        OrderItem[] orderItems = o.getOrderItems();
        String email = o.getAddress().getEmail();
        Timestamp curr_ts = getCurrentTimeStamp();

        String sql = "INSERT INTO CUSTOMER_ORDER (ORDER_ID, ORDER_DATE, NET_AMT, PROMO_DISC_AMT, RECIPIENT_NAME, PHONE_NO, ADDRESS_LINE, CITY, COUNTRY, POSTAL_CODE, STRIPE_CHARGE_ID, EMAIL, PROMO_CODE_ID, COURIER_NAME, ORDER_TRACKING_NO) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sql1 = "INSERT INTO ORDER_STATUS_LOG (ORDER_ID, STATUS_ID, START_TIMESTAMP, END_TIMESTAMP, DURATION_HOURS) VALUES (?, ?, ?, ?, ?)";
        
        try {

            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, orderId);
            stmt.setTimestamp(2, curr_ts);
            stmt.setDouble(3, o.getNetAmt());
            stmt.setDouble(4, o.getPromoDiscAmt());
            stmt.setString(5, o.getAddress().getRecipientName());
            stmt.setString(6, o.getAddress().getPhoneNo());
            stmt.setString(7, o.getAddress().getAddressLine());
            stmt.setString(8, o.getAddress().getCity());
            stmt.setString(9, o.getAddress().getCountry());
            stmt.setString(10, o.getAddress().getPostalCode());
            stmt.setString(11, o.getPaymentRefNo());
            stmt.setString(12, email);
            PromoCode pc = o.getPromoCode();
            int promoCodeId = 0;
            if(pc!=null){
                promoCodeId = pc.getPromoCodeId();
            }   
            stmt.setInt(13, promoCodeId);
            stmt.setString(14, null);
            stmt.setString(15, null);

            stmt.executeUpdate();

            stmt1 = conn.prepareStatement(sql1);
            stmt1.setInt(1, orderId);
            stmt1.setInt(2, 1);
            stmt1.setTimestamp(3, curr_ts);
            stmt1.setTimestamp(4, null);
            stmt1.setDouble(5, 0);

            stmt1.executeUpdate();

            for (OrderItem oI : orderItems) {
                OrderItemDAO orderItemDAO = new OrderItemDAO();
                String result = orderItemDAO.addOrderItems(orderId, oI);
            }
        } finally {
            ConnectionManager.close(conn);
        }

        return "Success";

    }

    public Order[] getOrderById(int orderId) throws SQLException {

        PromoCodeDAO pcDao = new PromoCodeDAO();
        OrderItemDAO orderItemDao = new OrderItemDAO();
        ArrayList<Order> orderList = new ArrayList<Order>();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Order order = null;

        String sql = "SELECT * FROM CUSTOMER_ORDER WHERE ORDER_ID = ?";
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, orderId);
            rs = stmt.executeQuery();

            while (rs.next()) {

                Timestamp orderDate = rs.getTimestamp("ORDER_DATE");
                double netAmt = rs.getDouble("NET_AMT");
                double promoDiscAmt = rs.getDouble("PROMO_DISC_AMT");

                //create address object: address from order table does not have an address id and a default parameter
                String email = rs.getString("EMAIL");
                String recipientName = rs.getString("RECIPIENT_NAME");
                String phoneNo = rs.getString("PHONE_NO");
                String addressLine = rs.getString("ADDRESS_LINE");
                String city = rs.getString("CITY");
                String country = rs.getString("COUNTRY");
                String postalCode = rs.getString("POSTAL_CODE");
              
                Address address = new Address(email, recipientName, phoneNo, 0, addressLine, city, country, postalCode, "N");

                String paymentRefNo = rs.getString("STRIPE_CHARGE_ID");

                //create a promo code object
                int promoCode = rs.getInt("PROMO_CODE_ID");
                PromoCode pc = pcDao.getPromoCodeById(promoCode);

                //get all the order items under this order
                OrderItem[] orderItems = orderItemDao.getOrderItemsByOrderId(orderId);

                //get latest order status
                OrderStatusLogDAO orderStatusLogDAO = new OrderStatusLogDAO();
                OrderStatusLog[] osl = orderStatusLogDAO.getOrderStatusByOrderId(orderId);
                String courierName= rs.getString("COURIER_NAME");
                String oderTrackingNo= rs.getString("ORDER_TRACKING_NO");

                order = new Order(orderId, orderDate, netAmt, promoDiscAmt, address, paymentRefNo, pc, orderItems, osl, courierName, oderTrackingNo);
                orderList.add(order);

            }
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        Order[] orderArr = orderList.toArray(new Order[orderList.size()]);
        return orderArr;
    }

    public Order[] getAllOrders() throws SQLException {

        PromoCodeDAO pcDao = new PromoCodeDAO();
        OrderItemDAO orderItemDao = new OrderItemDAO();
      

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Order> orderList = new ArrayList<Order>();
        Order order = null;

        String sql = "SELECT * FROM CUSTOMER_ORDER";

        try {

            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {

                int orderId = rs.getInt("ORDER_ID");
                Timestamp orderDate = rs.getTimestamp("ORDER_DATE");
                double netAmt = rs.getDouble("NET_AMT");
                double promoDiscAmt = rs.getDouble("PROMO_DISC_AMT");

                //create address object: address from order table does not have an address id and a default parameter
                String email = rs.getString("EMAIL");
                String recipientName = rs.getString("RECIPIENT_NAME");
                String phoneNo = rs.getString("PHONE_NO");
                String addressLine = rs.getString("ADDRESS_LINE");
                String city = rs.getString("CITY");
                String country = rs.getString("COUNTRY");
                String postalCode = rs.getString("POSTAL_CODE");
                Address address = new Address(email, recipientName, phoneNo, 0, addressLine, city, country, postalCode, "N");

                String paymentRefNo = rs.getString("STRIPE_CHARGE_ID");

                //create a promo code object
                int promoCode = rs.getInt("PROMO_CODE_ID");
                PromoCode pc = pcDao.getPromoCodeById(promoCode);
                
                String courierName= rs.getString("COURIER_NAME");
                String oderTrackingNo= rs.getString("ORDER_TRACKING_NO");

                //get all the order items under this order
                OrderItem[] orderItems = orderItemDao.getOrderItemsByOrderId(orderId);

                //get latest order status
                OrderStatusLogDAO orderStatusLogDAO = new OrderStatusLogDAO();
                OrderStatusLog[] osl = orderStatusLogDAO.getOrderStatusByOrderId(orderId);

                order = new Order(orderId, orderDate, netAmt, promoDiscAmt, address, paymentRefNo, pc, orderItems, osl, courierName, oderTrackingNo);
                orderList.add(order);
            }
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        Order[] orderArr = orderList.toArray(new Order[orderList.size()]);
        return orderArr;
    }

    public Order[] getOrderByEmail(String email) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        ArrayList<Order> orderList = new ArrayList<>();
        Order order = null;

        String sql = "SELECT * FROM customer_order WHERE EMAIL = ?";

        System.out.println("GEt order by email");
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int orderId = rs.getInt(1);
                Timestamp orderDate = rs.getTimestamp("ORDER_DATE");
                double netAmt = rs.getDouble("NET_AMT");
                double promoDiscAmt = rs.getDouble("PROMO_DISC_AMT");
                String recipientName = rs.getString("RECIPIENT_NAME");
                String phoneNo = rs.getString("PHONE_NO");
                String addressLine = rs.getString("ADDRESS_LINE");
                String city = rs.getString("CITY");
                String country = rs.getString("COUNTRY");
                String postalCode = rs.getString("POSTAL_CODE");
                String paymentRefNo = rs.getString("STRIPE_CHARGE_ID");
                int promoCode = rs.getInt("PROMO_CODE_ID");
                OrderStatusLogDAO orderLog = new OrderStatusLogDAO();
                PromoCodeDAO pcDao = new PromoCodeDAO();
                Address a = new Address(email, recipientName, phoneNo, 0, addressLine, city, country, postalCode, "N");
                OrderItemDAO orderItemDao = new OrderItemDAO();
                order = new Order(orderId, orderDate, netAmt, promoDiscAmt, a, paymentRefNo, pcDao.getPromoCodeById(promoCode), orderItemDao.getOrderItemsByOrderId(orderId), orderLog.getOrderStatusByOrderId(orderId), null, null);
                orderList.add(order);
            }
        } catch (SQLException e) {
            System.out.println("FROM ORDER DAO " + e);
        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return orderList.toArray(new Order[orderList.size()]);
    }

    public Order[] getPastOrdersByEmail(String email) throws SQLException {
        Order[] allOrders = getOrderByEmail(email);
        ArrayList<Order> pastOrders = new ArrayList<>();
        for (Order o : allOrders) {
            OrderStatusLog theMostRecentStatusLog = o.getStatusLogs()[0];
            Timestamp theMostRecentStartTime = theMostRecentStatusLog.getStartTimeStamp();
            OrderStatusLog[] statusLogs = o.getStatusLogs();
            for (OrderStatusLog statusLog : statusLogs) {
                if (statusLog.getStartTimeStamp().after(theMostRecentStartTime)) {
                    theMostRecentStartTime = statusLog.getStartTimeStamp();
                    theMostRecentStatusLog = statusLog;
                }
            }

            if (theMostRecentStatusLog.getOrderStatus().getStatusName().equals("Completed")) {
                pastOrders.add(o);
            }
        }

        return pastOrders.toArray(new Order[pastOrders.size()]);

    }

    public Order[] getCurrentOrdersByEmail(String email) throws SQLException {
        Order[] allOrders = getOrderByEmail(email);
        ArrayList<Order> orderArrayList = new ArrayList<>(Arrays.asList(allOrders));   
        Order[] pastOrders = getPastOrdersByEmail(email);
        for (int i=0; i<orderArrayList.size(); i++) {
            for (Order pastOrder: pastOrders){
                if (orderArrayList.get(i).getOrderId()==pastOrder.getOrderId()){
                    orderArrayList.remove(i);
                    i--;
                }
            }
            
            
        }
        
        return orderArrayList.toArray(new Order[orderArrayList.size()]);

      
        
    }

    public int getNextOrderId() throws SQLException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int nextOrderId = 0;

        String sql = "SELECT MAX(ORDER_ID) AS MAX FROM CUSTOMER_ORDER";

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {

                nextOrderId = rs.getInt("MAX") + 1;

            }

        } finally {

            ConnectionManager.close(conn, stmt, rs);

        }

        return nextOrderId;

    }

    private java.sql.Timestamp getCurrentTimeStamp() {

        java.util.Date today = new java.util.Date();
        return new java.sql.Timestamp(today.getTime());

    }

//    private static void handleSQLException(SQLException ex, String sql, String... parameters) {
//        String msg = "Unable to access data; SQL=" + sql + "\n";
//        for (String parameter : parameters) {
//            msg += "," + parameter;
//
//        }
//        Logger.getLogger(CustomerDAO.class
//                .getName()).log(Level.SEVERE, msg, ex);
//    }
}
