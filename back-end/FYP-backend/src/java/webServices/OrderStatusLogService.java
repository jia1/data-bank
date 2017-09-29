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
import dao.OrderDAO;
import dao.OrderStatusLogDAO;
import entity.Order;
import java.sql.SQLException;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Ong Yi Xuan
 */
@Path("/OrderStatusLogService")
public class OrderStatusLogService {
    
    @Context
    private HttpServletResponse response;
    
    
    @GET
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateOrderStatus(@QueryParam("orderId") int orderId, @QueryParam("previousStatusId") int previousStatusId, @QueryParam("newStatusId") int newStatusId) {


        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");

        Gson gson = new GsonBuilder().create();
        JsonObject jsonOutput = new JsonObject();
        OrderStatusLogDAO oslDao = new OrderStatusLogDAO();

        try {

            String msg = oslDao.updateOrderStatusLogByOrderId(orderId, previousStatusId, newStatusId);
            
            if (msg.equals("Success")) {
                
                jsonOutput.addProperty("status", "200");
                

            } else {
                
                jsonOutput.addProperty("status", "500");

            }

        } catch (SQLException e) {

            jsonOutput.addProperty("status", "500");
            jsonOutput.addProperty("msg", "OrderStatusLogService: SQL Exception" +e.getMessage());

        }

        String finalJsonOutput = gson.toJson(jsonOutput);
        return finalJsonOutput;
    }


    @GET
    @Path("/getAllOrders")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllOrders() {

        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");

        Gson gson = new GsonBuilder().create();
        JsonObject jsonOutput = new JsonObject();
        
        JsonArray orderArray = new JsonArray();
        OrderDAO orderDao = new OrderDAO();

        try {

            Order[] oArr = orderDao.getAllOrders();
            if (oArr == null) {
                
                jsonOutput.addProperty("status", "500");
                jsonOutput.addProperty("msg", "No Orders Available");
                

            } else {
                
                jsonOutput.addProperty("status", "200");
                JsonArray orders = gson.toJsonTree(oArr).getAsJsonArray(); // convert arraylist to jsonArray
                jsonOutput.add("orders", orders);

            }

        } catch (SQLException e) {

            jsonOutput.addProperty("status", "500");
            jsonOutput.addProperty("msg", "OrderService: SQL Exception" +e.getMessage());

        }

        String finalJsonOutput = gson.toJson(jsonOutput);
        return finalJsonOutput;
    }
    
}
