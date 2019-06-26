package io.openliberty.api;

import java.util.HashMap;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.openliberty.TagDAO;

@RequestScoped
@Path("tags")
public class TagsAPI {
	
	@Inject
	private TagDAO tagDAO;
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response getTags() {
		return Response.ok(new HashMap<String, Object>(){/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		{
			put("tags", tagDAO.allTags());
		}}).build();
	}
}
