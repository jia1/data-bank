/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webServices;

//import javax.ws.rs.GET;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dao.CartDAO;
import dao.CustomerAddressDAO;
import dao.CustomerDAO;
import dao.ProductDAO;
import entity.Address;
import entity.Cart;
import entity.CartItem;
import entity.Customer;
import entity.Order;
import entity.Product;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
//import javax.ws.rs.MatrixParam;
import javax.ws.rs.POST;
//import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
//import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import tokenManagement.tokenManagement;
import utility.SendEmail;

/**
 *
 * @author Huiyan
 */
@Path("/registration")
public class Registration {

    @Context
    private HttpServletResponse response;

    @POST
    @Path("/insert")
    @Produces(MediaType.APPLICATION_JSON)
    public String register(@FormParam("email") String email, @FormParam("firstName") String firstName, @FormParam("lastName") String lastName, @FormParam("phoneNumber") String phoneNumber, @FormParam("country") String country, @FormParam("city") String city, @FormParam("address") String address, @FormParam("postalCode") String postalCode, @FormParam("password") String password) {

        response.setHeader("Access-Control-Allow-Origin", "*");
        //String password = CustomerDAO.retrievePasswordByEmail(email);
        HashMap<String, String> responseMap = new HashMap();
        Gson gson = new GsonBuilder().create();

        try {
            CustomerAddressDAO customerAddressDao = new CustomerAddressDAO();
            CartDAO cartDao = new CartDAO();
            CustomerDAO customerDao = new CustomerDAO();

            Address a = new Address(email, firstName, phoneNumber, 1, address, city, country, postalCode, "Y");
            ArrayList<Address> addArrayList = new ArrayList<>();
            addArrayList.add(a);
            Address[] addArray = addArrayList.toArray(new Address[addArrayList.size()]);
            CartItem[] cartItem = new CartItem[0];
            Order[] order = new Order[0];
            Cart c = new Cart(cartDao.getNextCartId(), 0, cartItem);

            //generate hash string for email verification link
            StringBuffer hexString = new StringBuffer();
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = new byte[32];
            new Random().nextBytes(hash);

            for (int i = 0; i < hash.length; i++) {
                if ((0xff & hash[i]) < 0x10) {
                    hexString.append("0"
                            + Integer.toHexString((0xFF & hash[i])));
                } else {
                    hexString.append(Integer.toHexString(0xFF & hash[i]));
                }
            }

            System.out.println("HASH: " + hexString);

            Customer cus = new Customer(email, firstName, lastName, phoneNumber, password, hexString.toString(), c, addArray, order);
            String addCustomerResult = customerDao.addCustomer(cus);
            if (addCustomerResult.equals("Success")) {
                customerAddressDao.addAddressToCustomer(a);
                cartDao.addCart(c, email);
                
                //send verification link
                SendEmail.sendVerificationEmail(email, hexString.toString());

                responseMap.put("status", "200");
            } else {
                responseMap.put("status", "500");
                responseMap.put("description", addCustomerResult);

            }

        } catch (SQLException e) {
            responseMap.put("status", "500");
            responseMap.put("description", e.toString());

        } catch (NoSuchAlgorithmException ex) {
//            Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, ex);
            responseMap.put("status", "500");
            responseMap.put("description", ex.toString());
        }

        //responseMap.put("status", STATUS_ERROR_NULL_PASSWORD);
        return gson.toJson(responseMap);
    }

    @GET
    @Path("/verifyCode")
    @Produces(MediaType.APPLICATION_JSON)
    public String verifyCode(@QueryParam("email") String email, @QueryParam("code") String code) {

        response.setHeader("Access-Control-Allow-Origin", "*");
        Gson gson = new GsonBuilder().create();

        HashMap<String, String> responseMap = new HashMap();
        Customer customer = null;
        CustomerDAO customerDAO = new CustomerDAO();
        String description = "";
        if (email != null && !email.equals("")) {
            //String twoDigitsPostalCode = postalCode.substring(0,2);
            try {
                customer = customerDAO.retrieveCustomerByEmail(email);
            } catch (SQLException e) {
                description = "SQL Exception";
                responseMap.put("status", "500");
                responseMap.put("description", description);
            }

            if (customer == null) {
                description = "User not found";
                responseMap.put("status", "500");
                responseMap.put("description", description);
            } else {
                System.out.println("CODE: " + code);
                String custHash = customer.getVerified();
                System.out.println("HASH: " + custHash);

                if (custHash.equals(code)) {
                    responseMap.put("status", "200");
                } else {
                    description = "Incorrect verification code";
                    responseMap.put("status", "500");
                    responseMap.put("description", description);
                }
            }
        }
        String finalJsonOutput = gson.toJson(responseMap);
        return finalJsonOutput;
    }

    @OPTIONS
    @PermitAll
    @Path("/verifyAcc")
    public void optionsRetrieve() {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        response.setHeader("Access-Control-Allow-Headers", "auhtorization");

    }

    @POST
    @Path("/verifyAcc")
    @Produces(MediaType.APPLICATION_JSON)
    public String verifyAccount(@FormParam("token") String token, @FormParam("code") String code) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        JsonParser parser = new JsonParser();
        HashMap<String, String> responseMap = new HashMap();

        JsonObject jsonOutput = new JsonObject();
        Gson gson = new GsonBuilder().create();
        String description;
        CustomerDAO customerDAO = new CustomerDAO();
        String email = tokenManagement.parseJWT(token);
        try {
            Customer customer = customerDAO.retrieveCustomerByEmail(email);

            if (customer == null) {
                description = "User not found";
                jsonOutput.addProperty("status", "500");
                jsonOutput.addProperty("description", description);
            } else {

                System.out.println("CODE: " + code);
                String custHash = customer.getVerified();
                System.out.println("HASH: " + custHash);

                if (custHash.equals(code)) {
                    customerDAO.updateCustomerVerification(email, "Y");

                    responseMap.put("status", "200");

                } else {
                    description = "Invalid verification link";
                    responseMap.put("status", "500");
                    responseMap.put("description", description);
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, ex);
            responseMap.put("status", "500");
            responseMap.put("description", ex.toString());
        }

        String finalJsonOutput = gson.toJson(responseMap);
        return finalJsonOutput;

    }
}
