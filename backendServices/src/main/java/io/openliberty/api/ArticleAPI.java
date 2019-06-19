package io.openliberty.api;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@RequestScoped
@Path("articles/{slug}")
public class ArticleAPI {
//	@GET
//	public Response article(@PathParam("slug") String slug){
//		
//	}
}
