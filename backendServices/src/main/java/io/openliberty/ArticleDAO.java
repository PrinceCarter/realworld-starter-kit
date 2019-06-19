package io.openliberty;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import io.openliberty.core.article.Article;
import io.openliberty.core.user.User;

@RequestScoped
public class ArticleDAO {
	
    @PersistenceContext(name = "jpa-unit")
    private EntityManager em;
    
    public void createArticle(Article article) {
        em.persist(article);
    }
    
//    public Article findByID(String articleID) {
//    	
//    }
//    
    public Article findBySlug(String slug) {
    	return em.createNamedQuery("Articles.findBySlug", Article.class)
    			 .setParameter("slug", slug)
    			 .getSingleResult();
    }
////    
//    public List<Article> findRecentArticles(String tag, String author, String favoritedBy,) {
//    	
//    }
    
    public List<Article> findUserFeed(String userID) {
    	return em.createNamedQuery("Articles.findUserFeed", Article.class)
          .setParameter("userID", userID).getResultList();
    }

}
