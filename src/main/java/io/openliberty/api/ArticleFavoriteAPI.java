package io.openliberty.api;

import java.util.HashMap;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.jwt.JsonWebToken;

import io.openliberty.DAO.ArticleDAO;
import io.openliberty.DAO.ArticleFavoriteDAO;
import io.openliberty.DAO.UserDAO;
import io.openliberty.core.article.Article;
import io.openliberty.core.article.ArticleFavorite;
import io.openliberty.core.user.User;

@Path("/articles/{slug}/favorite")
public class ArticleFavoriteAPI {
	
	@Inject
	private UserDAO userDAO;
	
	@Inject
	private JsonWebToken jwtToken;
	
	@Inject
	private ArticleDAO articleDAO;
	
	@Inject
	private ArticleFavoriteDAO articleFavoriteDAO;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response favoriteArticle(@PathParam("slug") String slug) {
		Article article = articleDAO.findBySlug(slug);
		User user = userDAO.findByUsername(jwtToken.getName());
		ArticleFavorite articleFavorite = new ArticleFavorite(article.getID(), user.getID());
		articleFavoriteDAO.save(articleFavorite);
		return Response.ok(responseArticleData(article)).build();
	}
	
	private HashMap<String, Object> responseArticleData(final Article articleData) {
		return new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L; {
				put("article", articleData);
			}
		};
	}
}
