/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author User
 */
public class Donor {
    private int id;
    private String donorName;
    private String donorLocation;
    
    public Donor(int id, String donorName, String donorLocation){
        this.id=id;
        this.donorName=donorName;
        this.donorLocation=donorLocation;
        
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the donorName
     */
    public String getDonorName() {
        return donorName;
    }

    /**
     * @param donorName the donorName to set
     */
    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }

    /**
     * @return the donorLocation
     */
    public String getDonorLocation() {
        return donorLocation;
    }

    /**
     * @param donorLocation the donorLocation to set
     */
    public void setDonorLocation(String donorLocation) {
        this.donorLocation = donorLocation;
    }
    
}
