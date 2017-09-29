/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webServices;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dao.PromoCodeDAO;
import dao.StaffDAO;
import entity.PromoCode;
import entity.Staff;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import tokenManagement.tokenManagement;

/**
 *
 * @author Jeremy Bachtiar
 */
@Path("/promoCode")
public class PromoCodeService {

    @Context
    private HttpServletResponse response;

    @POST
    @Path("/retrieveAll")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllPromo(@FormParam("token") String token) {
        response.setHeader("Access-Control-Allow-Origin", "*");

        JsonObject jsonOutput = new JsonObject();
        Gson gson = new GsonBuilder().create();
        JsonParser parser = new JsonParser();
        PromoCode promoCode = null;
        Staff staff = null;
        String status = "";

        StaffDAO staffDao = new StaffDAO();
        PromoCodeDAO promoDao = new PromoCodeDAO();
        String email = tokenManagement.parseJWT(token);

        try {
            staff = staffDao.getStaffByEmail(email);
        } catch (SQLException e) {
            //HANDLE SQL ERROR
        }

        if (staff != null) {
            int staffRole = staff.getRoleId();
            if (staffRole == 1) {
                try {
                    JsonArray promos = new JsonArray();
                    PromoCode[] promoList = promoDao.getAllPromoCodes();

                    for (PromoCode p : promoList) {

                        String promoString = gson.toJson(p);
                        JsonElement promoElement = parser.parse(promoString);
                        promos.add(promoElement);
                    }
                    jsonOutput.addProperty("status", "200");
                    jsonOutput.add("promos", promos);

                } catch (SQLException e) {
                    //HANDLE SQL EXCEPTION
                }

            } else {
                String description = "Not Authorized";
                jsonOutput.addProperty("status", "500");
                jsonOutput.addProperty("description", description);
            }
        } else {
            String description = "Not Authenticated";
            jsonOutput.addProperty("status", "500");
            jsonOutput.addProperty("description", description);
        }

        return gson.toJson(jsonOutput);
    }
    
    @OPTIONS
    @PermitAll
    @Path("/delete")
    public void optionsDeletePromo() {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        response.setHeader("Access-Control-Allow-Headers", "auhtorization");

    }

    @POST
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public String deletePromo(@FormParam("token") String token, @FormParam("promoId") String promoIdDelete) {
        response.setHeader("Access-Control-Allow-Origin", "*");

        JsonObject jsonOutput = new JsonObject();
        Gson gson = new GsonBuilder().create();
        JsonParser parser = new JsonParser();

        PromoCodeDAO promoCodeDAO = new PromoCodeDAO();
        Staff staff = null;
        String status = "";
        String email = tokenManagement.parseJWT(token);
        if (email != null) {
            try {
                String result = promoCodeDAO.deletePromoCodeById(Integer.parseInt(promoIdDelete));
                if (result.equals("Success")) {

                    jsonOutput.addProperty("status", "200");
                    jsonOutput.addProperty("description", "Deleted Successfully");

                } else {

                    jsonOutput.addProperty("status", "500");
                    jsonOutput.addProperty("description", "Failed to Delete");

                }
            } catch (SQLException e) {

                jsonOutput.addProperty("status", "500");
                jsonOutput.addProperty("description", "SQL Exception");

            }
        } else {
            jsonOutput.addProperty("status", "500");
            jsonOutput.addProperty("description", "Not Authorised");

        }

        return gson.toJson(jsonOutput);
    }
}
