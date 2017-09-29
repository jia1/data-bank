/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.DonorDAO;
import entity.Donor;
import java.util.HashMap;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
//import javax.ws.rs.MatrixParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
//import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
//import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import tokenManagement.tokenManagement;

/**
 *
 * @author User
 */
@Path("/donor")
public class DonorWebservice {
    @Context private HttpServletResponse response;
    
    @POST
    @Path("/retrieve")
    @Produces(MediaType.APPLICATION_JSON)
    public String retrieve (@FormParam("id") int id){
        response.setHeader("Access-Control-Allow-Origin", "*");
        //String password = CustomerDAO.retrievePasswordByEmail(email);
        HashMap<String, String> responseMap = new HashMap<>();
        Gson gson = new GsonBuilder().create();
        String status;
        //String token = httpHeaders.getRequestHeader("Authorization").get(0);

        Donor d= DonorDAO.retrieveDonorById(id);
        
        if (d == null) {
            status = "User not found";
            //responseMap.put("status", STATUS_NOT_FOUND);
            responseMap.put("status", status);
        }else{
            
            String donorName= d.getDonorName();
            String donorLocation= d.getDonorLocation();
          

       
            responseMap.put("donorName", donorName);
            responseMap.put("donorLocation",donorLocation);
     
            status="200";
            responseMap.put("status", status);
        }
       
         //responseMap.put("status", STATUS_ERROR_NULL_PASSWORD);
        return gson.toJson(responseMap);
    }
    
    

    
}
