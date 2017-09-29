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
import dao.CustomerDAO;
import dao.StaffDAO;
import dao.StaffRoleDAO;
import entity.Customer;
import entity.Staff;
import entity.StaffRole;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
//import javax.ws.rs.MatrixParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
//import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
//import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import tokenManagement.tokenManagement;

/**
 *
 * @author JeremyBachtiar
 */
@Path("/staff")
public class Admin {

    @Context
    private HttpServletResponse response;

    @OPTIONS
    @PermitAll
    @Path("/addStaff")
    public void optionsNewStaff() {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        response.setHeader("Access-Control-Allow-Headers", "auhtorization");

    }

    @POST
    @Path("/addStaff")
    @Produces(MediaType.APPLICATION_JSON)
    public String addNewStaff(@FormParam("token") String token, @FormParam("staff") String staffJson) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String email = tokenManagement.parseJWT(token);

        JsonObject jsonOutput = new JsonObject();
        Gson gson = new GsonBuilder().create();
        StaffDAO staffDao = new StaffDAO();

        String status = "";

        Staff staff = gson.fromJson(staffJson, Staff.class);

        try {
            String result = staffDao.addStaff(staff);
            if (result.equals("Success")) {
                jsonOutput.addProperty("status", "200");
            } else {
                jsonOutput.addProperty("status", "500");
            }
        } catch (SQLException e) {
            jsonOutput.addProperty("status", "SQL Exception");

        }

        return gson.toJson(jsonOutput);
    }

    @POST
    @Path("/getAllStaff")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllStaff(@FormParam("token") String token) {
        response.setHeader("Access-Control-Allow-Origin", "*");

        JsonObject jsonOutput = new JsonObject();
        Gson gson = new GsonBuilder().create();
        JsonParser parser = new JsonParser();

        StaffDAO staffDao = new StaffDAO();
        Staff staff = null;
        String status = "";
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
                    JsonArray staffs = new JsonArray();
                    ArrayList<Staff> staffList = staffDao.getAllStaff();

                    for (Staff s : staffList) {
                        String staffString = gson.toJson(s);
                        JsonElement staffElement = parser.parse(staffString);
                        staffs.add(staffElement);
                    }
                    jsonOutput.addProperty("status", "200");
                    jsonOutput.add("staffs", staffs);

                } catch (SQLException e) {
                    //HANDLE SQL EXCEPTION
                }

            } else {
                status = "Not Authorized";
            }
        } else {
            status = "Not Authenticated";
        }

        return gson.toJson(jsonOutput);
    }

    @OPTIONS
    @PermitAll
    @Path("/retrieve")
    public void optionsGetProfile() {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        response.setHeader("Access-Control-Allow-Headers", "auhtorization");

    }

    @POST
    @Path("/retrieve")
    @Produces(MediaType.APPLICATION_JSON)
    public String getProfile(@FormParam("token") String token) {
        response.setHeader("Access-Control-Allow-Origin", "*");

        JsonObject jsonOutput = new JsonObject();
        Gson gson = new GsonBuilder().create();
        JsonParser parser = new JsonParser();

        StaffDAO staffDao = new StaffDAO();
        Staff staff = null;
        String status = "";
        String email = tokenManagement.parseJWT(token);

        try {
            staff = staffDao.getStaffByEmail(email);
            if (staff != null) {

                String staffString = gson.toJson(staff);
                JsonObject staffObject = (JsonObject) parser.parse(staffString);

                jsonOutput.addProperty("status", "200");
                jsonOutput.add("staff", staffObject);

            } else {
                status = "Not Authenticated";
            }
        } catch (SQLException e) {
            //HANDLE SQL ERROR
        }

        return gson.toJson(jsonOutput);
    }

    @OPTIONS
    @PermitAll
    @Path("/delete")
    public void optionsDeleteStaff() {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        response.setHeader("Access-Control-Allow-Headers", "auhtorization");

    }

    @POST
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteStaff(@FormParam("token") String token, @FormParam("email") String emailDelete) {
        response.setHeader("Access-Control-Allow-Origin", "*");

        JsonObject jsonOutput = new JsonObject();
        Gson gson = new GsonBuilder().create();
        JsonParser parser = new JsonParser();

        StaffDAO staffDao = new StaffDAO();
        Staff staff = null;
        String status = "";
        String email = tokenManagement.parseJWT(token);
        if (email != null) {
            try {
                String result = staffDao.deleteStaffByEmail(emailDelete);
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
    @OPTIONS
    @PermitAll
    @Path("/edit")
    public void optionsEditStaff() {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        response.setHeader("Access-Control-Allow-Headers", "auhtorization");

    }

    @POST
    @Path("/edit")
    @Produces(MediaType.APPLICATION_JSON)
    public String editStaff(@FormParam("token") String token, @FormParam("staff") String staffJson) {
        response.setHeader("Access-Control-Allow-Origin", "*");

        JsonObject jsonOutput = new JsonObject();
        Gson gson = new GsonBuilder().create();
        JsonParser parser = new JsonParser();
        
        StaffDAO staffDao = new StaffDAO();
        Staff staff = gson.fromJson(staffJson, Staff.class);
        
        String status = "";
        String email = tokenManagement.parseJWT(token);
        if (email != null) {
            try {
                String result = staffDao.updateStaff(staff);
                if (result.equals("Success")) {

                    jsonOutput.addProperty("status", "200");
                    jsonOutput.addProperty("description", "Updated Successfully");

                } else {

                    jsonOutput.addProperty("status", "500");
                    jsonOutput.addProperty("description", "Failed to update");

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

    @GET
    @Path("/getRoles")
    @Produces(MediaType.APPLICATION_JSON)
    public String getRoles() {
        response.setHeader("Access-Control-Allow-Origin", "*");

        JsonObject jsonOutput = new JsonObject();
        Gson gson = new GsonBuilder().create();
        JsonParser parser = new JsonParser();

        StaffRoleDAO staffRoleDao = new StaffRoleDAO();
        ArrayList<StaffRole> staffRoles = null;
        String status = "";
        //String email = tokenManagement.parseJWT(token);

        try {
            staffRoles = staffRoleDao.getAllStaffRole();
            JsonArray staffRolesJson = new JsonArray();
            for (StaffRole sR : staffRoles) {
                String staffRoleString = gson.toJson(sR);
                JsonElement staffRoleElement = parser.parse(staffRoleString);
                staffRolesJson.add(staffRoleElement);
            }

            jsonOutput.addProperty("status", "200");
            jsonOutput.add("staffRoles", staffRolesJson);

        } catch (SQLException e) {
            //HANDLE SQL ERROR
        }

        return gson.toJson(jsonOutput);
    }

    @OPTIONS
    @PermitAll
    @Path("/update")
    public void optionsUpdateProfile() {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        response.setHeader("Access-Control-Allow-Headers", "auhtorization");

    }

    @POST
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateProfile(@FormParam("token") String token, @FormParam("staff") String staffJson) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String email = tokenManagement.parseJWT(token);

        JsonObject jsonOutput = new JsonObject();
        Gson gson = new GsonBuilder().create();
        StaffDAO staffDao = new StaffDAO();

        String status = "";

        Staff staff = gson.fromJson(staffJson, Staff.class);

        try {
            Staff staffCheck = staffDao.getStaffByEmail(email);

            if (staffCheck.getEmail().equals(staff.getEmail())) {
                String result = staffDao.updateStaff(staff);
                if (result.equals("Success")) {
                    jsonOutput.addProperty("status", "200");
                } else {
                    jsonOutput.addProperty("status", "500");
                    jsonOutput.addProperty("status", "Failed to add");
                }
            } else {
                jsonOutput.addProperty("status", "500");
                jsonOutput.addProperty("description", "Not Authorised");
            }

        } catch (SQLException e) {
            jsonOutput.addProperty("status", "SQL Exception");

        }

        return gson.toJson(jsonOutput);
    }

//    @OPTIONS
//    @PermitAll
//    @Path("/update")
//    public void optionsUpdate() {
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
//        response.setHeader("Access-Control-Allow-Headers", "auhtorization");
//        
//    }
//      @PUT
//      @Path("/update")
//      @Produces(MediaType.APPLICATION_JSON)
//      public String updateStaff (@FormParam("token") String token, @FormParam("firstName") String firstName, @FormParam("lastName") String lastName, @FormParam("phoneNumber") String phoneNumber, @FormParam("password") String password, @FormParam("roleCode") String roleCode){
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        //String password = CustomerDAO.retrievePasswordByEmail(email);
//        HashMap<String, String> responseMap = new HashMap<>();
//        Gson gson = new GsonBuilder().create();
//        String status;
//        //String token = httpHeaders.getRequestHeader("Authorization").get(0);
//        String email=tokenManagement.parseJWT(token);
//        Staff staff = StaffDAO.retrieveStaffByEmail(email);
//                
//        if (staff == null) {
//            status = "User not found";
//            //responseMap.put("status", STATUS_NOT_FOUND);
//            responseMap.put("status", status);
//        }else{
//            Staff updateStaff = new Staff(email, firstName, lastName, phoneNumber, password, roleCode);
//            StaffDAO.updateStaff(updateStaff);
//            status = "200";
//            responseMap.put("status", status);
//        }
//      
//        //responseMap.put("status", STATUS_ERROR_NULL_PASSWORD);
//        return gson.toJson(responseMap);
//    }
//    
//   
//    @POST
//    @Path("/retrieve")
//    @Produces(MediaType.APPLICATION_JSON)
//    public String retrieve (@FormParam("token") String token){
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        //String password = CustomerDAO.retrievePasswordByEmail(email);
//        HashMap<String, String> responseMap = new HashMap<>();
//        Gson gson = new GsonBuilder().create();
//        String status;
//        //String token = httpHeaders.getRequestHeader("Authorization").get(0);
//        String email=tokenManagement.parseJWT(token);
//        Staff staff = StaffDAO.retrieveStaffByEmail(email);
//        
//        if (staff == null) {
//            status = "User not found";
//            //responseMap.put("status", STATUS_NOT_FOUND);
//            responseMap.put("status", status);
//        }else{
//            
//            String firstName= staff.getFirstName();
//            String lastName =staff.getLastName();
//            String phoneNumber=staff.getPhoneNumber();
//            String password=staff.getPassword();
//            String roleCode=staff.getRoleCode();
//
//            
//            //get role name from the role code and send over role name to the front end
//            StaffRole role = StaffRoleDAO.retrieveRoleByCode(roleCode);
//            String roleName = role.getRoleName();
//
//            responseMap.put("email", email);
//            responseMap.put("firstName", firstName);
//            responseMap.put("lastName", lastName );
//            responseMap.put("phoneNumber", phoneNumber);
//            responseMap.put("password", password);
//            responseMap.put("roleCode", roleName);
//            status="200";
//            responseMap.put("status", status);
//        }
//       
//         //responseMap.put("status", STATUS_ERROR_NULL_PASSWORD);
//        return gson.toJson(responseMap);
//    }
}
