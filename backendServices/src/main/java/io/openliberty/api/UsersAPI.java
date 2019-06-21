// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2018 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
// end::copyright[]
package io.openliberty.api;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.SecurityContext;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

import com.ibm.websphere.security.jwt.*;

import io.openliberty.UserDAO;
import io.openliberty.core.user.AuthUser;
import io.openliberty.core.user.User;

import java.io.BufferedReader;
import java.security.Principal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.json.JSONObject;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
@Path("users")
public class UsersAPI {

    @Inject
    private UserDAO userDAO;

    @Inject
    @Claim(standard = Claims.iss)
    private String issuer;

    @Inject
    @Claim("custom_entry")
    private String customEntry;

    /**
     * This method creates a new user from the submitted data (email, username, password, bio and
     * image) by the user.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createNewUser(String requestBody) {

    	JSONObject obj = new JSONObject(requestBody);
    	JSONObject user = obj.getJSONObject("user");
        User newUser = new User(user.getString("email"), user.getString("username"), user.getString("password"), "", "");   
        userDAO.createUser(newUser);
        return Response.status(Response.Status.CREATED).entity(userResponse(new AuthUser(newUser, getToken(newUser)))).build();
        
    }
    
    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response loginUser(String requestBody) {  
    	JSONObject obj = new JSONObject(requestBody);
    	JSONObject user = obj.getJSONObject("user");
    	User loginUser = userDAO.findByEmail(user.getString("email"));
    	if (loginUser != null && user.getString("password").equals(loginUser.getPassword())) {
    		return Response.status(Response.Status.CREATED).entity(userResponse(new AuthUser(loginUser, getToken(loginUser)))).build();
    	} else {
    		return Response.status(Response.Status.NOT_FOUND).entity("User does not exist!").build();
    	}
    	
    } 
    
    private String getToken(User loginUser) {
		try {
    		JwtBuilder jwtBuilder = JwtBuilder.create();
    		customEntry = "www.bs.com";
    		jwtBuilder.subject(loginUser.getEmail());
    		JwtToken goToken = jwtBuilder.buildJwt();
    		System.out.println("AUTHHHH: " + goToken.getHeader("Authorization"));
    		String jwtTokenString = goToken.compact();
    		return jwtTokenString;
		} catch(Exception e) {
			System.out.println("Something went wrong! " + e);
			return null;
		}
		
    }
    
    private Map<String, Object> userResponse(AuthUser authUser) {
        return new HashMap<String, Object>() {{
            put("user", authUser);
        }};
    }
    
    /**
     * This method returns a specific existing/stored user in Json format
     */
    @GET
    @Path("{userID}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public JsonObject getUser(@PathParam("userID") String userID) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        User user = userDAO.readUser(userID);
        if(user != null) {
            builder.add("ID", user.getID())
            	   .add("Username", user.getUsername())
            	   .add("Email", user.getEmail())
            	   .add("Bio", user.getBio())
            	   .add("Image", user.getImage());
        }
        return builder.build();
    }

    /**
     * This method returns the existing/stored events in Json format
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public JsonArray getUsers() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        JsonArrayBuilder finalArray = Json.createArrayBuilder();
        for (User user : userDAO.findAllUsers()) {
        	builder.add("ID", user.getID())
        		   .add("Username", user.getUsername())
        		   .add("Email", user.getEmail())
            	   .add("Bio", user.getBio());
//            	   .add("Image", user.getImage());
            finalArray.add(builder.build());
        }
        return finalArray.build();
    }

}
