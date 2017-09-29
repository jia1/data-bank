/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author Huiyan
 */
public class Courier {

    private String courierName;
    private String trackingUrl;

   
    public Courier(String courierName, String trackingUrl) {
        this.courierName = courierName;
        this.trackingUrl = trackingUrl;

    }
     /**
     * @return the courierName
     */

    public String getCourierName() {
        return courierName;
    }

    /**
     * @param courierName the courierName to set
     */
    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

    /**
     * @return the trackingUrl
     */
    public String getTrackingUrl() {
        return trackingUrl;
    }

    /**
     * @param trackingUrl the trackingUrl to set
     */
    public void setTrackingUrl(String trackingUrl) {
        this.trackingUrl = trackingUrl;
    }

}
