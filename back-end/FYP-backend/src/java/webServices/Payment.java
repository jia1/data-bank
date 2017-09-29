/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webServices;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import static com.stripe.net.APIResource.request;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Clarissa Poedjiono
 */
@Path("/Payment")
public class Payment {

    @Context
    private HttpServletResponse response;

    @POST
    @Path("/chargeStripe")
    @Produces(MediaType.APPLICATION_JSON)
    public String chargeStripe(@FormParam("stripeToken") String token, @FormParam("amount") String amount) {
        System.out.print("TOKENNNNN: " + token);
        response.setHeader("Access-Control-Allow-Origin", "*");
        HashMap<String, String> responseMap = new HashMap();
        Gson gson = new GsonBuilder().create();
        String status = "";
        // Set your secret key: remember to change this to your live secret key in production
        // See your keys here: https://dashboard.stripe.com/account/apikeys
        Stripe.apiKey = "sk_test_U8hw5pb6Rs9LTaa67hANRXFW";

        // Token is created using Stripe.js or Checkout!
        // Get the payment token ID submitted by the form:
        //String token = request.getParameter("stripeToken");
        // Charge the user's card:
        int amountInt = Integer.parseInt(amount);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("amount", amountInt);
        params.put("currency", "sgd");
        params.put("description", "Example charge");
        params.put("source", token);
        try {
            Charge charge = Charge.create(params);
            System.out.println("CHARGE ID: " + charge.getId());
            status = "200";
            responseMap.put("status", status);
            responseMap.put("token", token);
            responseMap.put("paymentRefNo", charge.getId());

        } catch (StripeException e) {
            status = ""+e.getStatusCode();
            String error = e.toString();
            responseMap.put("status", status);
            responseMap.put("error", error);
            responseMap.put("token", token);

        }
        return gson.toJson(responseMap);
    }
}
