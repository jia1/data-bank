
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dao.CartDAO;
import dao.CartItemDAO;
import dao.StaffDAO;
import entity.Cart;
import entity.Staff;
import java.sql.SQLException;
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

@Path("/Cart")
public class CartItems {

    @Context
    private HttpServletResponse response;

    @OPTIONS
    @PermitAll
    @Path("/updateCart")
    public void optionsUpdate() {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        response.setHeader("Access-Control-Allow-Headers", "auhtorization");

    }

    @POST
    @Path("/updateCart")
    @Produces(MediaType.APPLICATION_JSON)
    public String update(@FormParam("token") String token, @FormParam("cart") String cartJson) {
        response.setHeader("Access-Control-Allow-Origin", "*");

        JsonObject jsonOutput = new JsonObject();
        Gson gson = new GsonBuilder().create();
        JsonParser parser = new JsonParser();

        CartDAO cartDao = new CartDAO();
        Cart cart = gson.fromJson(cartJson, Cart.class);
        System.out.println(cart.getPrice());
        String email = tokenManagement.parseJWT(token);
        System.out.println(email);

        try {
            Cart custCart = cartDao.getCartByEmail(email);

            System.out.println(cart.getCartId());
            cartDao.addCart(cart, email);
            return "cart ADDED";

        } catch (SQLException e) {
            return "SQL";
        }

       
    }

    @OPTIONS
    @PermitAll
    @Path("/retrieveCart")
    public void optionsRetrieve() {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        response.setHeader("Access-Control-Allow-Headers", "auhtorization");

    }

    @POST
    @Path("/retrieveCart")
    @Produces(MediaType.APPLICATION_JSON)
    public String retrieve(@FormParam("token") String token) {

        response.setHeader("Access-Control-Allow-Origin", "*");

        JsonObject jsonOutput = new JsonObject();
        Gson gson = new GsonBuilder().create();
        JsonParser parser = new JsonParser();

        CartDAO cartDao = new CartDAO();
        String email = tokenManagement.parseJWT(token);
        System.out.println(email);

        try {
            Cart custCart = cartDao.getCartByEmail(email);
            if (custCart != null) {
                String cartString = gson.toJson(custCart);
                JsonElement cartElement = parser.parse(cartString);
                jsonOutput.addProperty("status", "200");
                jsonOutput.add("cart", cartElement);

                return gson.toJson(jsonOutput);
            } else {

            }

        } catch (SQLException e) {
            return "SQL";
        }

        return "HEY";
    }
    
    @OPTIONS
    @PermitAll
    @Path("/deleteItem")
    public void optionsDelete() {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        response.setHeader("Access-Control-Allow-Headers", "auhtorization");

    }

    @POST
    @Path("/deleteItem")
    @Produces(MediaType.APPLICATION_JSON)
    public String delete(@FormParam("token") String token, @FormParam("productId") String productId) {

        response.setHeader("Access-Control-Allow-Origin", "*");

        JsonObject jsonOutput = new JsonObject();
        Gson gson = new GsonBuilder().create();
        JsonParser parser = new JsonParser();

        CartDAO cartDao = new CartDAO();
        CartItemDAO cartItemDao = new CartItemDAO();
        String email = tokenManagement.parseJWT(token);
        System.out.println(email);
        
        try {
            Cart cart = cartDao.getCartByEmail(email);
            int cartId = cart.getCartId();
            
            String result = cartItemDao.deleteCartItem(cartId, Integer.parseInt(productId));
         
            cartDao.updateCartPrice(email);
            
            return "Success";
        } catch (SQLException e) {
            return "SQL";
        }

        
    }
}
