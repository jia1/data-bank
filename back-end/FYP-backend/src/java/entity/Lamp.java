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
public class Lamp extends Product{
    
    private String lampType;
    private Double lampPrice;

    public Lamp(String lampType, Double lampPrice, int productId, String productType, Pattern design, Colour colour, Fabric fabric, Image[] images) {
        super(productId, productType, design, colour, fabric, images);
        this.lampType = lampType;
        this.lampPrice = lampPrice;
    }

    public String getLampType() {
        return lampType;
    }

    public void setLampType(String lampType) {
        this.lampType = lampType;
    }

    public Double getLampPrice() {
        return lampPrice;
    }

    public void setLampPrice(Double lampPrice) {
        this.lampPrice = lampPrice;
    }
    
}
