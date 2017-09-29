/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webServices;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 *
 * @author JeremyBachtiar
 */
public class OptionsResource {
    
    private HttpServletResponse httpResponse;
    
    @OPTIONS
    @PermitAll
    public void options() {
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
    }

// Match sub-resources
    @OPTIONS
    @Path("{path:.*}")
    @PermitAll
    public void optionsAll(@PathParam("path") String path) {
         httpResponse.setHeader("Access-Control-Allow-Origin", "*");
    }

    
}
