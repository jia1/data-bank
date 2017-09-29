/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author Ong Yi Xuan
 */
public class Fabric {
    
    private int fabricId;
    private String fabricName;
    private String fabricDesc;
    private double fabricPrice;

    public Fabric(int fabricId, String fabricName, String fabricDesc, double fabricPrice) {
        this.fabricId = fabricId;
        this.fabricName = fabricName;
        this.fabricDesc = fabricDesc;
        this.fabricPrice = fabricPrice;
    }

    public int getFabricId() {
        return fabricId;
    }

    public void setFabricId(int fabricId) {
        this.fabricId = fabricId;
    }

    public String getFabricName() {
        return fabricName;
    }

    public void setFabricName(String fabricName) {
        this.fabricName = fabricName;
    }

    public String getFabricDesc() {
        return fabricDesc;
    }

    public void setFabricDesc(String fabricDesc) {
        this.fabricDesc = fabricDesc;
    }

    public double getFabricPrice() {
        return fabricPrice;
    }

    public void setFabricPrice(double fabricPrice) {
        this.fabricPrice = fabricPrice;
    }
    
}
