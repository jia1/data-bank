/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StripeManagement;
import java.util.HashMap;
import java.util.Map;
 
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
import com.stripe.net.RequestOptions.RequestOptionsBuilder;


public class Stripe {

    public static void main(String[] args) {
        RequestOptions requestOptions = (new RequestOptionsBuilder()).setApiKey("sk_test_U8hw5pb6Rs9LTaa67hANRXFW").build();
        Map<String, Object> chargeMap = new HashMap<String, Object>();
        chargeMap.put("amount", 100);
        chargeMap.put("currency", "sgd");
        chargeMap.put("source", "tok_1234"); // obtained via Stripe.js
        try {
            Charge charge = Charge.create(chargeMap, requestOptions);
            System.out.println(charge);
        } catch (StripeException e) {
            e.printStackTrace();
        }
    }
}


