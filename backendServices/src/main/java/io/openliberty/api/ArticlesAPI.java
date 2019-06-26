package io.openliberty.api;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.json.JSONObject;

import io.openliberty.ArticleDAO;
import io.openliberty.UserDAO;
import io.openliberty.core.article.Article;
import io.openliberty.core.user.AuthUser;
import io.openliberty.core.user.User;

@RequestScoped
@Path("articles")
public class ArticlesAPI {

	@Inject
	private UserDAO userDAO;

	@Inject
	private ArticleDAO articleDAO;

	@Inject
	private JsonWebToken jwtToken;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response createArticles(String requestBody) {
		JSONObject obj = new JSONObject(requestBody);
		JSONObject article = obj.getJSONObject("article");
		User user = userDAO.findByUsername(jwtToken.getName());
		Article newArticle = new Article(article.getString("title").toString(),
										 article.getString("description").toString(), 
										 article.getString("body").toString(),
										 JSONObject.getNames("tagList"), 
										 user.getID().toString());
		articleDAO.createArticle(newArticle);
		return Response.status(Response.Status.OK).entity((new HashMap<String, Object>() {
			{
				put("article", articleDAO.findByID(newArticle.getID()));
			}
		})).build();
	}

	@GET
	@Path("feed")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response getFeed(String requestBody) {
		return Response.ok(articleDAO.findUserFeed(userDAO.findByUsername(jwtToken.getName()))).build();
	}

	// Set to QueryParams
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response getArticles(String requestBody) {
//		
//		JSONObject obj = new JSONObject(requestBody);
////		JSONObject user = obj.getJSONObject("user");
//		
//		System.out.println(obj);

		return Response.status(Response.Status.OK).entity(requestBody).build();
	}

}
