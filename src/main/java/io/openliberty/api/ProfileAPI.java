package io.openliberty.api;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.transaction.Transactional;
import javax.ws.rs.PathParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.openliberty.DAO.UserDAO;
import io.openliberty.core.user.User;

@RequestScoped
@Path("/profiles")
public class ProfileAPI {
	
	@Inject
    private UserDAO userDAO;
	
	@GET
	@Path("{username}")
	@Produces(MediaType.APPLICATION_JSON)
    @Transactional
	public JsonObject getProfile(@PathParam("username") String un) {	
		User user = userDAO.findByUsername(un);
		if (user != null) {
    		JsonObjectBuilder builder = Json.createObjectBuilder();
    		builder.add("username", user.getUsername())
    			   .add("bio", user.getBio())
    			   .add("image", user.getImage());
    		return builder.build();
		}
		return null;
	}
	
// TODO:
//	@POST
//	public Response followUser() {
//		
//	}

//  TODO:
//	@DELETE
//	public Response unfollowUser() {
//		
//	}
}
