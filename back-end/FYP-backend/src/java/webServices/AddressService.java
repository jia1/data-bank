/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webServices;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dao.CustomerAddressDAO;
import entity.Address;
import java.sql.SQLException;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Ong Yi Xuan
 */
@Path("/AddressService")
public class AddressService {
    
    @Context
    private HttpServletResponse response;


    @OPTIONS
    @PermitAll
    @Path("/save")
    public void optionsSave() {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        response.setHeader("Access-Control-Allow-Headers", "authorization");

    }
    
    @POST
    @Path("/save")
    @Produces(MediaType.APPLICATION_JSON)
    public String saveAddress(@FormParam("address") String json) {
        
        CustomerAddressDAO cDAO = new CustomerAddressDAO();
//        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");

        Gson gs = new Gson();
        Address addressToSave = gs.fromJson(json, Address.class);
        
        Gson gson = new GsonBuilder().create();
        JsonObject jsonOutput = new JsonObject();

        try {
                System.out.println("JSON: " + json);
                jsonOutput.addProperty("status", "200");
                cDAO.addAddressToCustomer(addressToSave);

        } catch (SQLException e) {
            System.out.println(e);
            jsonOutput.addProperty("status", "500");

        }

        String finalJsonOutput = gson.toJson(jsonOutput);
        return finalJsonOutput;
    }

    
}
