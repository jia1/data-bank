/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webServices;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dao.ColourDAO;
import entity.Colour;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
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

@Path("/ColourService")
public class ColourService {
    
    @Context
    private HttpServletResponse response;
    
    @GET
    @Path("/GetColours")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllColours() {
        
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");

        Gson gson = new GsonBuilder().create();
        JsonObject jsonOutput = new JsonObject();
        
        JsonArray colourArray = new JsonArray();
        ColourDAO colourDao = new ColourDAO();

        try {

            ArrayList<Colour> cList = colourDao.getAllAvailableColours();
            if (cList == null) {
                
                jsonOutput.addProperty("status", "500");
                jsonOutput.addProperty("msg", "No Colours Available");
                

            } else {
                
                jsonOutput.addProperty("status", "200");
                JsonArray colours = gson.toJsonTree(cList).getAsJsonArray(); // convert arraylist to jsonArray
                jsonOutput.add("colours", colours);

            }

        } catch (SQLException e) {

            jsonOutput.addProperty("status", "500");
            jsonOutput.addProperty("msg", "ColourService: SQL Exception");

        }

        String finalJsonOutput = gson.toJson(jsonOutput);
        return finalJsonOutput;
        
    }
    
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
    public String saveColour(@FormParam("colour") String json) {
        
        ColourDAO cDAO = new ColourDAO();
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");

        Gson gs = new Gson();
        Colour colourToSave = gs.fromJson(json, Colour.class);
        
        Gson gson = new GsonBuilder().create();
        JsonObject jsonOutput = new JsonObject();

        try {
                System.out.println("JSON: " + json);
                jsonOutput.addProperty("status", "200");
                cDAO.addColour(colourToSave);

        } catch (SQLException e) {
            System.out.println(e);
            jsonOutput.addProperty("status", "500");

        }

        String finalJsonOutput = gson.toJson(jsonOutput);
        return finalJsonOutput;
    }

    
    
}
