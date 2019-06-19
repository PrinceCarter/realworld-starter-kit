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
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
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
import javax.ws.rs.core.SecurityContext;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

import com.ibm.websphere.security.jwt.*;

import io.openliberty.UserDAO;
import io.openliberty.core.user.User;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

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
   	private JsonWebToken jwtToken;
    
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
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    public Response createNewUser(@FormParam("email") String email, @FormParam("username") String username,
        @FormParam("password") String password, @FormParam("bio") String bio, @FormParam("image") String image) {
    	
        User newUser = new User(email, username, password, bio, image);
        
        if(!userDAO.findUser(username).isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Username already exists").build();
        }
        
        userDAO.createUser(newUser);
        return Response.status(Response.Status.NO_CONTENT).build(); 
    }
    

    
    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    public Response loginUser(@FormParam("email") String email, @FormParam("password") String password) {    
    	User user = userDAO.findByEmail(email);
    	if (user != null && password.equals(user.getPassword())) { 		
    		JsonObjectBuilder builder = Json.createObjectBuilder();
    		builder.add("email", email);
    		builder.add("password", password);
    		try {
        		JwtBuilder jwtBuilder = JwtBuilder.create();
        		customEntry = "www.bs.com";
        		jwtBuilder.subject(email);
        		JwtToken goToken = jwtBuilder.buildJwt();
        		String jwtTokenString = goToken.compact();
        		JwtConsumer jwtConsumer = new JwtConsumer();	
        		System.out.println("This is the JWT: " + jwtTokenString);
        		System.out.println("This is the data contained in the JWT: " + goToken.getClaims().getAllClaims());
    		} catch(Exception e) {
    			System.out.println("Something went wrong! " + e);
    		}	
    		return Response.status(Response.Status.ACCEPTED)
    				.entity("Successfully Logged In!").build();
    	} else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid Email or password").build();
    	}
    	
    }
    

//    jwtBuilder.subject("tom@op.com").claim(Claims.AUDIENCE, "https://acme.com/rs").claim("iss","https://sso.com/ibm/op" ).claim("scope", "impersonator monitor").claim("uid", "hasys123haksiqws");
//    JwtToken goToken = jwtBuilder.buildJwt();

//    /**
//     * This method deletes a specific existing/stored user
//     */
//    @DELETE
//    @Path("{userID}")
//    @Transactional
//    public Response deleteUser(@PathParam("userID") String userID) {
//        User user = userDAO.readUser(userID);
//        if(user == null) {
//            return Response.status(Response.Status.NOT_FOUND)
//                           .entity("User does not exist").build();
//        }
//        userDAO.deleteUser(user);
//        return Response.status(Response.Status.NO_CONTENT).build();
//    }

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
    
//    @GET
//    @Path("permit-all")
//    @PermitAll 
//    @Produces(MediaType.TEXT_PLAIN)
//    public String hello(@Context SecurityContext ctx) { // 
//
//        Principal caller =  ctx.getUserPrincipal(); 
//
//        String name = caller == null ? "anonymous" : caller.getName();
//        boolean hasJWT = callerPrincipal != null;
//        String helloReply = String.format("hello + %s, isSecure: %s, authScheme: %s, hasJWT: %s", name, ctx.isSecure(), ctx.getAuthenticationScheme(), hasJWT);
//        return helloReply; // 
//
//    }
//    
//
//    @GET()
//    @Path("roles-allowed")
//    @RolesAllowed({"Echoer", "Subscriber"}) 
//    @Produces(MediaType.TEXT_PLAIN)
//    public String helloRolesAllowed(@Context SecurityContext ctx) {
//        Principal caller =  ctx.getUserPrincipal();
//        String name = caller == null ? "anonymous" : caller.getName();
//        boolean hasJWT = callerPrincipal != null;
//        String helloReply = String.format("hello + %s, isSecure: %s, authScheme: %s, hasJWT: %s", name, ctx.isSecure(), ctx.getAuthenticationScheme(), hasJWT);
//        return helloReply;
//    }
//    
//    

}
