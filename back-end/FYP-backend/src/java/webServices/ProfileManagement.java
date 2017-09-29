/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webServices;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dao.CustomerDAO;
import entity.Address;
import entity.Cart;
import entity.Customer;
import entity.Order;
import java.sql.SQLException;
import java.util.HashMap;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import tokenManagement.tokenManagement;

/**
 *
 * @author Huiyan
 */
@Path("/profile")
public class ProfileManagement {

    @Context
    private HttpServletResponse response;

    @OPTIONS
    @PermitAll
    @Path("/retrieve")
    public void optionsRetrieve() {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        response.setHeader("Access-Control-Allow-Headers", "auhtorization");

    }

    @POST
    @Path("/retrieve")
    @Produces(MediaType.APPLICATION_JSON)
    public String retrieve(@FormParam("token") String token) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        JsonParser parser = new JsonParser();

        JsonObject jsonOutput = new JsonObject();
        Gson gson = new GsonBuilder().create();
        String description;
        CustomerDAO customerDAO = new CustomerDAO();

        String email = tokenManagement.parseJWT(token);
        try {
            Customer customer = customerDAO.retrieveCustomerByEmail(email);

            if (customer == null) {
                description = "User not found";
                //responseMap.put("status", STATUS_NOT_FOUND);
                jsonOutput.addProperty("status", "500");
                jsonOutput.addProperty("description", description);
            } else {

                String firstName = customer.getFirstName();
                String lastName = customer.getLastName();
                String phoneNo = customer.getPhoneNo();
                Address[] address = customer.getAddress();
                String password = customer.getPassword();
                String verified = customer.getVerified();
                Cart cart = customer.getCart();
                Order[] orders = customer.getOrders();
                Customer newCust = new Customer(email, firstName, lastName, phoneNo, password, verified, cart, address, orders);
                String custString = gson.toJson(newCust);
                JsonObject custObject = (JsonObject) parser.parse(custString);

                jsonOutput.addProperty("status", "200");
                jsonOutput.add("user", custObject);
            }
        } catch (SQLException e) {

        }
        //responseMap.put("status", STATUS_ERROR_NULL_PASSWORD);
        return gson.toJson(jsonOutput);
    }

    @OPTIONS
    @PermitAll
    @Path("/update")
    public void options() {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        response.setHeader("Access-Control-Allow-Headers", "auhtorization");

    }

    @PUT
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateCustomer(@FormParam("token") String token, @FormParam("firstName") String firstName, @FormParam("lastName") String lastName, @FormParam("phoneNo") String phoneNo, @FormParam("password") String password) {
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        //String password = CustomerDAO.retrievePasswordByEmail(email);
        JsonObject jsonOutput = new JsonObject();
        Gson gson = new GsonBuilder().create();
        String description;
        //String token = httpHeaders.getRequestHeader("Authorization").get(0);
        String email = tokenManagement.parseJWT(token);
        //Customer customer = gson.fromJson(userJson, Customer.class);
        CustomerDAO customerDAO = new CustomerDAO();

        if (email == null) {
            description = "User not found";
            //responseMap.put("status", STATUS_NOT_FOUND);
            jsonOutput.addProperty("status", "500");
            jsonOutput.addProperty("description", description);
        } else {

            try {

                customerDAO.updateCustomerByEmail(email, firstName, lastName, phoneNo, password);
                description = "200";
                jsonOutput.addProperty("status", description);
            } catch (SQLException e) {
                System.out.println(e);
                description = "SQL Exception";
                //responseMap.put("status", STATUS_NOT_FOUND);
                jsonOutput.addProperty("status", "500");
                jsonOutput.addProperty("description", description);
            }

        }

        //responseMap.put("status", STATUS_ERROR_NULL_PASSWORD);
        return gson.toJson(jsonOutput);
    }
}
