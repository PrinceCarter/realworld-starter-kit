package io.openliberty.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.json.JSONObject;

import io.openliberty.DAO.ArticleDAO;
import io.openliberty.DAO.UserDAO;
import io.openliberty.core.article.Article;
import io.openliberty.core.user.User;

@RequestScoped
@Path("/articles")
public class ArticlesAPI {

	@Inject
	private UserDAO userDAO;

	@Inject
	private ArticleDAO articleDAO;

	@Inject
	private JsonWebToken jwtToken;
	
	@OPTIONS
    @Produces(MediaType.TEXT_PLAIN)
    public Response getSimple() {
		return Response.ok()
			      .header("Access-Control-Allow-Origin", "*")
			      .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
			      .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization").build();
    }

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
		System.out.println("ID: " + newArticle.getID());
		return Response.status(Response.Status.OK)
					   .header("Access-Control-Allow-Origin", "*")
					   .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
					   .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization")
					   .entity((new HashMap<String, Object>() {
							private static final long serialVersionUID = 1L; {
								put("article", articleDAO.findByID(newArticle.getID()));
							}
						}))
					   .build();
	}

	@GET
	@Path("feed")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response getFeed(String requestBody) {
		User user = userDAO.findByUsername(jwtToken.getName());
		System.out.println(user);
//		return Response.ok().build();
		return Response.ok(articleResponse(articleDAO.findUserFeed(user)))
				       .header("Access-Control-Allow-Origin", "*")
				       .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
				       .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization")
					   .build();
	}

	// Set to QueryParams
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response getArticles(String requestBody) {
		
		JSONObject obj = new JSONObject(requestBody);
		JSONObject user = obj.getJSONObject("user");
		
		System.out.println(obj);

		return Response.status(Response.Status.OK).entity(requestBody).build();
	}
	
	private Map<String, Object> articleResponse(List <Article> articles) {
		return new HashMap<String, Object>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L; {
				put("articles", articles);
			}
		};
	}
}
