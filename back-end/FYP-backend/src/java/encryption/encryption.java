/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption;
import java.math.*;
import java.security.*;

/**
 *
 * @author Sheryl
 */

 //sha1 encryption
public class encryption {
    public static String SHA1(String input){
        try{
            MessageDigest mDigest = MessageDigest.getInstance("SHA1");
            byte[] result = mDigest.digest(input.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < result.length; i++){
                sb.append(Integer.toString((result[i] & 0xff) + 0x100,16 ).substring(1));
            }
            return sb.toString();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    
}