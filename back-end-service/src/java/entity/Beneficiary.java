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
public class Beneficiary {
    private int id;
    private String beneficiaryName;
    private String beneficiaryLocation;
	private String targetPpl;
	private String contactName
	private String email;
	private int contactNumber;
    
	public Beneficiary(int id, String beneficiaryName, String beneficiaryLocation, String targetPpl, String contactName, String email, int contactNumber){
		this.id = id;
		this.beneficiaryName = beneficiaryName;
		this.beneficiaryLocation = beneficiaryLocation;
		this.targetPpl = targetPpl;
		this.contactName = contactName;
		this.email = email;
		this.contactNumber = contactNumber;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public int getId(){
		return id;
	}
	
	public void setBeneficiaryName(String beneficiaryName){
		this.beneficiaryName = beneficiaryName;
	}
	
	public String getBeneficiaryName(){
		return beneficiaryName;
	}
    
	public void setBeneficiaryLocation(String beneficiaryLocation){
		this.beneficiaryLocation = beneficiaryLocation;
	}
	
	public String getBeneficiaryLocation(){
		return beneficiaryLocation;
	}
	public void setTargetPpl(String targetPpl){
		this.targetPpl = targetPpl;
	}
	
	public String getTargetPpl(){
		return targetPpl;
	}
	public void setContactName(String contactName){
		this.contactName = contactName;
	}
	
	public String getContactName(){
		return contactName;
	}
	
	public void setEmail(String email){
		this.email = email;
	}
	
	public String getEmail(){
		return email;
	}
	
	public void setContactNumber(int contactNumber){
		this.contactNumber = contactNumber;
	}
	
	public int getContactNumber(){
		return contactNumber;
	}
	
}
