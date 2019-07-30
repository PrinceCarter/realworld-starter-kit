package io.openliberty.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.openliberty.DAO.ArticleDAO;
import io.openliberty.DAO.CommentDAO;
import io.openliberty.DAO.UserDAO;
import io.openliberty.core.article.Article;
import io.openliberty.core.comment.Comment;

@RequestScoped
@Path("/articles/{slug}/comments")
public class ArticleCommentsAPI {
	
	@Inject
	ArticleDAO articleDAO = new ArticleDAO();
	
	@Inject
	UserDAO userDAO = new UserDAO();
	
	@Inject
	CommentDAO commentDAO = new CommentDAO();
	
	@OPTIONS
    @Produces(MediaType.TEXT_PLAIN)
    public Response getSimple() {
		return Response.ok()
			      .header("Access-Control-Allow-Origin", "*")
			      .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
			      .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization").build();
    }
	
	public ArticleCommentsAPI() {
		
	}
	
	@POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
	public Response createComment(@PathParam("slug") String slug, @FormParam("userID") String userID, @FormParam("body") String body) {
		
		Article article = articleDAO.findBySlug(slug);
		
		if (article != null) {
			Comment comment = new Comment(body, userID, article.getID());
			commentDAO.save(comment);
			return Response.status(Response.Status.ACCEPTED)
						   .entity("Successfully Posted Comment!").build();
		}
		
		return Response.status(Response.Status.NOT_FOUND)
				   .entity("Article Not Found!").build();

	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getComments(@PathParam("slug") String slug) {
		Article article = articleDAO.findBySlug(slug);
		if (article != null) {
			List<Comment> comments = commentDAO.findByArticleID(article.getID());
			return Response.ok(new HashMap<String, Object>() {{
	            				put("comments", comments);
	        			}})
					   .header("Access-Control-Allow-Origin", "*")
					   .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
					   .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization")
					   .build();
			
		}
		
		return Response.ok()
					   .header("Access-Control-Allow-Origin", "*")
					   .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
					   .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization")
					   .build();
	}
	
//	@DELETE
//	public Response deleteComment() {
//		
//	}
//	
//	private Article findArticle(String slug) {
//		return articleDAO.find
//	}
//	
//	private Map<String, Object> commentResponse(Comment comment){
//		
//	}
//	
//	
}
