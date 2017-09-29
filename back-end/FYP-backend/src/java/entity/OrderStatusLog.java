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
public class OrderStatusLog {
    
    private OrderStatus orderStatus;
    private Timestamp startTimeStamp;
    private Timestamp endTimeStamp;

    public OrderStatusLog(OrderStatus orderStatus, Timestamp startTimeStamp, Timestamp endTimeStamp) {
        this.orderStatus = orderStatus;
        this.startTimeStamp = startTimeStamp;
        this.endTimeStamp = endTimeStamp;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Timestamp getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setStartTimeStamp(Timestamp startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public Timestamp getEndTimeStamp() {
        return endTimeStamp;
    }

    public void setEndTimeStamp(Timestamp endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
    }
    
}
