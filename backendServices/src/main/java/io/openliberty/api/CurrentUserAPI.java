package io.openliberty.api;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.Map;

import javax.annotation.security.RolesAllowed;

import io.openliberty.UserDAO;
import io.openliberty.core.user.AuthUser;
import io.openliberty.core.user.User;

@RequestScoped
@Path("user")
public class CurrentUserAPI {

	@Inject
	private UserDAO userDAO;

	@Inject
	private JsonWebToken jwtToken;

	/**
	 * This method returns a specific existing/stored user in Json format
	 */
	@GET
	@RolesAllowed({ "admin", "user" })
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response currentUser(@Context HttpServletRequest httpRequest) {
		User user = userDAO.findByUsername(jwtToken.getName());
		return Response.status(Response.Status.CREATED)
				.entity(userResponse(new AuthUser(user, httpRequest.getHeader("Authorization")))).build();
	}

	/**
	 * This method updates a new user from the submitted data (email, username,
	 * password, bio and image) by the user.
	 */
	@PUT
	@RolesAllowed({ "admin", "user" })
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response updateUser(String requestBody, @Context HttpServletRequest httpRequest) {

		JSONObject body = new JSONObject(requestBody);
		JSONObject userObject = body.getJSONObject("user");
		User updateUser = userDAO.findByUsername(jwtToken.getName());

		if (updateUser == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("User does not exist").build();
		}

		if (!userDAO.findUser(userObject.get("username").toString()).isEmpty()) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Username already exists").build();
		}

		if (!"".equals(userObject.get("email").toString())) {
			updateUser.setEmail(userObject.get("email").toString());
		}

		if (!"".equals(userObject.get("username").toString())) {
			updateUser.setUsername(userObject.get("username").toString());
		}

		if (!"".equals(userObject.get("password").toString())) {
			updateUser.setPassword(userObject.get("password").toString());
		}

		if (!"".equals(userObject.get("bio").toString())) {
			updateUser.setBio(userObject.get("bio").toString());
		}

		if (!"".equals(userObject.get("image").toString())) {
			updateUser.setImage(userObject.get("image").toString());
		}
		
		userDAO.updateUser(updateUser);
		
		return Response.status(Response.Status.CREATED)
				.entity(userResponse(new AuthUser(updateUser, httpRequest.getHeader("Authorization")))).build();
	}

	private Map<String, Object> userResponse(AuthUser userWithToken) {
		return new HashMap<String, Object>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				put("user", userWithToken);
			}
		};
	}

}
