package io.openliberty.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonRootName;

import io.openliberty.ArticleDAO;
import io.openliberty.core.article.Article;

@RequestScoped
@Path("articles/{slug}")
public class ArticleAPI {

	@Inject
	private ArticleDAO articleDAO;

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response article(@PathParam("slug") String slug) {
		Article article = articleDAO.findBySlug(slug);

		return Response.ok(articleResponse(article)).build();

	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response updateArticle(@PathParam("slug") String slug, String requestBody) {

		Article article = articleDAO.findBySlug(slug);
		JSONObject obj = new JSONObject(requestBody);
		JSONObject newArticle = obj.getJSONObject("article");

		String title = "";
		String body = "";
		String description = "";

		try {
			String newTitle = newArticle.getString("title");
			Optional<String> optTitle = Optional.of(newTitle);
			title = optTitle.get();
		} catch (Exception e) {
			title = article.getTitle();
		}

		try {
			String newDescription = newArticle.getString("description");
			Optional<String> optDescription = Optional.of(newDescription);
			description = optDescription.get();
		} catch (Exception e) {
			description = article.getDescription();
		}

		try {
			String newBody = newArticle.getString("body");
			Optional<String> optBody = Optional.of(newBody);
			body = optBody.get();
		} catch (Exception e) {
			body = article.getBody();
		}

		article.update(title, description, body);

		return Response.ok(articleResponse(article)).build();

	}
	
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response deleteArticle(@PathParam("slug") String slug) {
		Article article = articleDAO.findBySlug(slug);
		articleDAO.remove(article);
		return Response.noContent().build();
	}

	private HashMap<String, Object> articleResponse(Article articleData) {
		return new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;
			{
				put("article", articleData);
			}
		};
	}

}

@JsonRootName("article")
class UpdateArticleParam {
	private String title = "";
	private String body = "";
	private String description = "";
}
