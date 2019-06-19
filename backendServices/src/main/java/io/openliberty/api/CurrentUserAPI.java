package io.openliberty.api;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.annotation.security.RolesAllowed;

import io.openliberty.UserDAO;
import io.openliberty.core.user.User;

@RequestScoped
@Path("user")
public class CurrentUserAPI {
	
    @Inject
    private UserDAO userDAO;
    
//    @Inject
//    private JsonWebToken jwtPrincipal;
//    
//    @GET
//    @RolesAllowed({ "admin", "user" })
//    @Path("/username")
//    public Response currentUser() {
//    	return Response.ok(this.jwtPrincipal.getName()).build();
//    }
    

    /**
     * This method updates a new user from the submitted data (email, username, password, bio and
     * image) by the user.
     */
    @PUT
    @RolesAllowed({ "admin", "user" })
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    public Response updateUser(@PathParam("userID") String userID, @FormParam("email") String email, @FormParam("username") String username,
            @FormParam("password") String password, @FormParam("bio") String bio, @FormParam("image") String image) {
        User prevUser = userDAO.readUser(userID);
        
        if(prevUser == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("User does not exist").build();
        }
        
        if(!userDAO.findUser(username).isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Username already exists").build();
        }
        
        if (!"".equals(email)) {
        	prevUser.setEmail(email);
        }

        if (!"".equals(username)) {
        	prevUser.setUsername(username);
        }

        if (!"".equals(password)) {
        	prevUser.setPassword(password);
        }

        if (!"".equals(bio)) {
        	prevUser.setBio(bio);
        }

        if (!"".equals(image)) {
        	prevUser.setImage(image);
        }

        userDAO.updateUser(prevUser);
        return Response.status(Response.Status.NO_CONTENT).build(); 
    }
}
