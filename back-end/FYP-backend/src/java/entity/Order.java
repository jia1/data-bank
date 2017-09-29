/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.sql.Timestamp;

/**
 *
 * @author Ong Yi Xuan
 */
public class Order {
    
    private int orderId;
    private Timestamp order_TimeStamp;
    private double netAmt;
    private double promoDiscAmt;
    private Address address;
    private String paymentRefNo;
    private PromoCode promoCode;
    private OrderItem[] orderItems;
    private OrderStatusLog[] statusLogs;
    private String courierName;
    private String trackingNo;

    public Order(int orderId, Timestamp order_TimeStamp, double netAmt, double promoDiscAmt, Address address, String paymentRefNo, PromoCode promoCode, OrderItem[] orderItems, OrderStatusLog[] statusLogs, String courierName, String trackingNo) {
        this.orderId = orderId;
        this.order_TimeStamp = order_TimeStamp;
        this.netAmt = netAmt;
        this.promoDiscAmt = promoDiscAmt;
        this.address = address;
        this.paymentRefNo = paymentRefNo;
        this.promoCode = promoCode;
        this.orderItems = orderItems;
        this.statusLogs = statusLogs;
        this.courierName = courierName;
        this.trackingNo = trackingNo;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Timestamp getOrder_TimeStamp() {
        return order_TimeStamp;
    }

    public void setOrder_TimeStamp(Timestamp order_TimeStamp) {
        this.order_TimeStamp = order_TimeStamp;
    }

    public double getNetAmt() {
        return netAmt;
    }

    public void setNetAmt(double netAmt) {
        this.netAmt = netAmt;
    }

    public double getPromoDiscAmt() {
        return promoDiscAmt;
    }

    public void setPromoDiscAmt(double promoDiscAmt) {
        this.promoDiscAmt = promoDiscAmt;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPaymentRefNo() {
        return paymentRefNo;
    }

    public void setPaymentRefNo(String paymentRefNo) {
        this.paymentRefNo = paymentRefNo;
    }

    public PromoCode getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(PromoCode promoCode) {
        this.promoCode = promoCode;
    }

    public OrderItem[] getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(OrderItem[] orderItems) {
        this.orderItems = orderItems;
    }

    public OrderStatusLog[] getStatusLogs() {
        return statusLogs;
    }

    public void setStatusLogs(OrderStatusLog[] statusLogs) {
        this.statusLogs = statusLogs;
    }

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

    public String getTrackingNo() {
        return trackingNo;
    }

    public void setTrackingNo(String trackingNo) {
        this.trackingNo = trackingNo;
    }
    
}
