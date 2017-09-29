/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.util.ArrayList;

/**
 *
 * @author Ong Yi Xuan
 */
public class Bedding extends Product{
    
    private BeddingSize size;

    public Bedding(BeddingSize size, int productId, String productType, Pattern design, Colour colour, Fabric fabric, Image[] images) {
        super(productId, productType, design, colour, fabric, images);
        this.size = size;
    }

    public BeddingSize getSize() {
        return size;
    }

    public void setSize(BeddingSize size) {
        this.size = size;
    }
    
}
