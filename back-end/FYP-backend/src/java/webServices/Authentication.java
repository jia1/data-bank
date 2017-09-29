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
import dao.CustomerDAO;
import dao.ProductDAO;
import dao.StaffDAO;
import entity.Customer;
import entity.Product;
import entity.Staff;
import java.sql.SQLException;
import java.util.HashMap;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
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

/**
 *
 * @author JeremyBachtiar
 */
@Path("/authentication")
public class Authentication {

    @Context
    private HttpServletResponse response;

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public String login(@FormParam("email") String email, @FormParam("password") String password) {

        response.setHeader("Access-Control-Allow-Origin", "*");

        //String password = CustomerDAO.retrievePasswordByEmail(email);
        HashMap<String, String> responseMap = new HashMap<String, String>();
        Gson gson = new GsonBuilder().create();

        String response = "";
        String status = "";
        String token = "";
        String description = "";
        Customer customer = null;
        CustomerDAO customerDAO = new CustomerDAO();

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
                //responseMap.put("status", STATUS_NOT_FOUND);
                responseMap.put("status", "500");
                responseMap.put("description", description);
            } else {
                if (password != null && !password.equals("")) {
                    if (password.equals(customer.getPassword())) {
                        //out.println("exist");
                        //String name = customer.getName();
                        status = "200";
                        token = tokenManagement.createJWT(email, "highlander", "login");
                        //String address = fireStation.getAddress();
                        //responseMap.put("Customer ", name);
                        //responseMap.put("address", address);
                        //responseMap.put("status", STATUS_OK);
                        responseMap.put("status", status);
                        responseMap.put("token", token);

                    } else {
                        description = "Invalid password";
                        responseMap.put("status", "200");
                        responseMap.put("description", description);
                        //responseMap.put("status", STATUS_INVALID_PASSWORD);
                    }
                } else {
                    status = "Invalid passsword";
                    responseMap.put("status", "200");
                    responseMap.put("description", description);
                    //responseMap.put("status", STATUS_ERROR_NULL_PASSWORD);
                }

            }

        } else {
            status = "User not found";
            responseMap.put("status", status);
            // country code is null
            //responseMap.put("status", STATUS_ERROR_NULL_EMAIL);
        }

        return gson.toJson(responseMap);
    }

    @POST
    @Path("/admin/login")
    @Produces(MediaType.APPLICATION_JSON)
    public String adminLogin(@FormParam("email") String email, @FormParam("password") String password) {
        response.setHeader("Access-Control-Allow-Origin", "*");

        HashMap<String, String> responseMap = new HashMap<String, String>();
        Gson gson = new GsonBuilder().create();
        Staff staff = null;
        String response = "";
        String status = "";
        String token = "";
        if (email != null && !email.equals("")) {
            //String twoDigitsPostalCode = postalCode.substring(0,2);
            StaffDAO staffDao = new StaffDAO();

            try {
                staff = staffDao.getStaffByEmail(email);

            } catch (SQLException e) {

            }

            if (staff == null) {
                status = "User not found";
                //responseMap.put("status", STATUS_NOT_FOUND);
                responseMap.put("status", status);
            } else {
                if (password != null && !password.equals("")) {
                    if (password.equals(staff.getPassword())) {
                        //out.println("exist");
                        //String name = customer.getName();
                        status = "200";
                        token = tokenManagement.createJWT(email, "highlander", "login");

                        responseMap.put("status", status);
                        responseMap.put("token", token);

                    } else {
                        status = "Invalid password";
                        responseMap.put("status", status);
                        //responseMap.put("status", STATUS_INVALID_PASSWORD);
                    }
                } else {
                    status = "Invalid passsword";
                    responseMap.put("status", status);
                    //responseMap.put("status", STATUS_ERROR_NULL_PASSWORD);
                }

            }

        } else {
            status = "User not found";
            responseMap.put("status", status);
            // country code is null
            //responseMap.put("status", STATUS_ERROR_NULL_EMAIL);
        }

        return gson.toJson(responseMap);
    }

    @GET
    @Path("/getProductId")
    @Produces(MediaType.APPLICATION_JSON)
    public String getProductById(@QueryParam("patternId") String patternId, @QueryParam("fabricId") String fabricId, @QueryParam("colourId") String colourId) {

        response.setHeader("Access-Control-Allow-Origin", "*");

        JsonObject jsonOutput = new JsonObject();
        Gson gson = new GsonBuilder().create();
        ProductDAO productDAO = new ProductDAO();
        JsonParser parser = new JsonParser();
        try {

            Product p = productDAO.getProductByPatternFabricColor(Integer.parseInt(patternId), Integer.parseInt(fabricId), Integer.parseInt(colourId));
            if (p == null) {
                jsonOutput.addProperty("status", "Product not found");

            } else {
                String productString = gson.toJson(p);
                JsonElement productElement = parser.parse(productString);

                jsonOutput.addProperty("status", "200");
                jsonOutput.add("product", productElement);

            }
        } catch (SQLException e) {

            jsonOutput.addProperty("status", "error");

        }

        String finalJsonOutput = gson.toJson(jsonOutput);
        return finalJsonOutput;
    }

}
