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

public class Pattern {
    
    private int patternId;
    private String patternName;
    private String patternDesc;
    private double patternPrice;
    private Collection collection;
    private Tag[] tags;

    public Pattern(int patternId, String patternName, String patternDesc, double patternPrice, Collection collection, Tag[] tags) {
        this.patternId = patternId;
        this.patternName = patternName;
        this.patternDesc = patternDesc;
        this.patternPrice = patternPrice;
        this.collection = collection;
        this.tags = tags;
    }

    public int getPatternId() {
        return patternId;
    }

    public void setPatternId(int patternId) {
        this.patternId = patternId;
    }

    public String getPatternName() {
        return patternName;
    }

    public void setPatternName(String patternName) {
        this.patternName = patternName;
    }

    public String getPatternDesc() {
        return patternDesc;
    }

    public void setPatternDesc(String patternDesc) {
        this.patternDesc = patternDesc;
    }

    public double getPatternPrice() {
        return patternPrice;
    }

    public void setPatternPrice(double patternPrice) {
        this.patternPrice = patternPrice;
    }

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public Tag[] getTags() {
        return tags;
    }

    public void setTags(Tag[] tags) {
        this.tags = tags;
    }

}
