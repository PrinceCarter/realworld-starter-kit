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

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.jwt.Claims;

import com.ibm.websphere.security.jwt.*;

import io.openliberty.DAO.UserDAO;
import io.openliberty.core.user.AuthUser;
import io.openliberty.core.user.User;

import java.util.HashMap;
import java.util.Map;


import org.json.JSONObject;
import javax.enterprise.context.RequestScoped;

@RequestScoped
@Path("users")
public class UsersAPI {

	@Inject
	private UserDAO userDAO;

	/**
	 * This method creates a new user from the submitted data (email, username,
	 * password, bio and image) by the user.
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
		return Response.status(Response.Status.CREATED).entity(userResponse(new AuthUser(newUser, getToken(newUser))))
				.build();

	}

	@POST
	@Path("login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response loginUser(@Context HttpServletRequest request, @Context HttpServletResponse response,
			String requestBody) throws Exception {
		JSONObject obj = new JSONObject(requestBody);
		JSONObject user = obj.getJSONObject("user");
		User loginUser = userDAO.findByEmail(user.getString("email"));
		if (loginUser != null && user.getString("password").equals(loginUser.getPassword())) {

			try {
				request.logout();
				request.login(loginUser.getUsername(), loginUser.getPassword());
			} catch (ServletException e) {
				System.out.println("Login failed.");
				e.printStackTrace();
			}

			// to get remote user using getRemoteUser()
			String remoteUser = request.getRemoteUser();

			System.out.println(remoteUser);
			//Set<String> roles = getRoles(request);

			// update session
			if (remoteUser != null && remoteUser.equals(loginUser.getUsername())) {

				String jwt = getToken(loginUser);
				// get the current session
				HttpSession ses = request.getSession();
				if (ses == null) {
					System.out.println("Session is timeout.");
				} else {
					ses.setAttribute("jwt", jwt);
				}
			} else {
				System.out.println("Update Sessional JWT Failed.");
			}

			return Response.status(Response.Status.CREATED)
					.entity(userResponse(new AuthUser(loginUser, getToken(loginUser)))).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).entity("User does not exist!").build();
		}
	}

	private String getToken(User loginUser) {
		try {
			JwtBuilder jwtBuilder = JwtBuilder.create();
			jwtBuilder.subject(loginUser.getEmail()).claim(Claims.sub.toString(), loginUser.getUsername())
					.claim("upn", loginUser.getUsername()) // MP-JWT defined subject claim
					.claim("customClaim", "customValue");
			JwtToken goToken = jwtBuilder.buildJwt();
			String jwtTokenString = goToken.compact();
			return jwtTokenString;
		} catch (Exception e) {
			System.out.println("Something went wrong! " + e);
			return null;
		}

	}

//	private Set<String> getRoles(HttpServletRequest request) {
//		Set<String> roles = new HashSet<String>();
//		boolean isAdmin = request.isUserInRole("admin");
//		boolean isUser = request.isUserInRole("user");
//		if (isAdmin) {
//			roles.add("admin");
//		}
//		if (isUser) {
//			roles.add("user");
//		}
//		return roles;
//	}

	private Map<String, Object> userResponse(AuthUser authUser) {
		return new HashMap<String, Object>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				put("user", authUser);
			}
		};
	}

}
