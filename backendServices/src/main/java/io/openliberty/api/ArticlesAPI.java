//package io.openliberty.api;
//
//import javax.enterprise.context.RequestScoped;
//import javax.inject.Inject;
//import javax.json.Json;
//import javax.json.JsonArray;
//import javax.json.JsonArrayBuilder;
//import javax.json.JsonObject;
//import javax.json.JsonObjectBuilder;
//import javax.transaction.Transactional;
//import javax.ws.rs.Consumes;
//import javax.ws.rs.FormParam;
//import javax.ws.rs.GET;
//import javax.ws.rs.POST;
//import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//
//import io.openliberty.ArticleDAO;
//import io.openliberty.UserDAO;
//import io.openliberty.core.article.Article;
//import io.openliberty.core.user.User;
//
//
//@RequestScoped
//@Path("articles")
//public class ArticlesAPI {
//	
//	@Inject
//    private UserDAO userDAO;
//	
//	@Inject
//    private ArticleDAO articleDAO;
//	
//	@POST
//    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//    @Transactional
//	public Response createArticles(@FormParam("title") String title, @FormParam("description") String description, 
//									@FormParam("body") String body, @FormParam("userID") String userID) {
//		User user = userDAO.readUser(userID);
//		
//		Article article = new Article(title, description, body, user.getID());
//		
//		articleDAO.createArticle(article);
//		
//		return Response.accepted().build();
//	}
//	
//	@GET
//	@Path("feed")
//    @Produces(MediaType.APPLICATION_JSON)
//    @Transactional
//	public JsonArray getFeed(@FormParam("offset") int offset, @FormParam("limit") int limit, @FormParam("userID") String userID) {
//		JsonObjectBuilder builder = Json.createObjectBuilder();
//
//        JsonArrayBuilder finalArray = Json.createArrayBuilder();
//        for (Article article : articleDAO.findUserFeed(userID)) {
//        	builder.add("title", article.getTitle())
//        		   .add("description", article.getDescription())
//        		   .add("body", article.getBody())
//            	   .add("userID", article.getUserID());
//            finalArray.add(builder.build());
//        }
//        return finalArray.build();
//	}
//	
//	
//	// Set to QueryParams
//	@GET
//	public Response getArticles(@FormParam("offset") int offset, @FormParam("limit") int limit, 
//			@FormParam("offset") String tag, @FormParam("favorited") String favoritedBy, @FormParam("author") String author) {
//		
//		
//		
//		return Response.accepted().build();
//	}
//	
//}
