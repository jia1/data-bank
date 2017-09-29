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
public class BeddingSize {
    
    private String sizeName;
    private String dimensions;
    private double sizePrice;

    public BeddingSize(String sizeName, String dimensions, double sizePrice) {
        this.sizeName = sizeName;
        this.dimensions = dimensions;
        this.sizePrice = sizePrice;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public double getSizePrice() {
        return sizePrice;
    }

    public void setSizePrice(double sizePrice) {
        this.sizePrice = sizePrice;
    }
    
}
