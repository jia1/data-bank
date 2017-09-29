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
import dao.OrderItemDAO;
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

@Path("/OrderItemService")
public class OrderItemService {
    
    @Context
    private HttpServletResponse response;
    
    
    @GET
    @Path("/updateOrderItemStatus")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateOrderItemStatus(@QueryParam("orderId") int orderId, @QueryParam("productId") int productId, @QueryParam("newStatus") String newStatus) {
        
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        
        Gson gson = new GsonBuilder().create();
        JsonObject jsonOutput = new JsonObject();
        ;
        OrderItemDAO orderItemDao = new OrderItemDAO();

        try {

            String msg = orderItemDao.updateOrderItemStatus(orderId, productId, newStatus);
            if (msg.equals("Success")) {
                
                jsonOutput.addProperty("status", "200");
                jsonOutput.addProperty("msg", "Order Item status updated");
                

            } else {
                
                jsonOutput.addProperty("status", "500");

            }

        } catch (SQLException e) {

            jsonOutput.addProperty("status", "500");
            jsonOutput.addProperty("msg", "OrderItemService: SQL Exception" +e.getMessage());

        }

        String finalJsonOutput = gson.toJson(jsonOutput);
        return finalJsonOutput;       
        
    }
    
}
