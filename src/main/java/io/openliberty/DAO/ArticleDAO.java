package io.openliberty.DAO;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.RequestScoped;
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
    
    public Article findByID(String articleID) {
    	return em.createNamedQuery("Articles.findByID", Article.class)
   			 .setParameter("id", articleID)
   			 .getSingleResult();
    }
    
    public Article findBySlug(String slug) {
    	return em.createNamedQuery("Articles.findBySlug", Article.class)
    			 .setParameter("slug", slug)
    			 .getSingleResult();
    }
////    
//    public List<Article> findRecentArticles(String tag, String author, String favoritedBy,) {
//    	
//    }
    
    public List<Article> findUserFeed(User user) {
    	return em.createNamedQuery("Articles.findUserFeed", Article.class)
    			 .setParameter("userID", user.getID()).getResultList();
    }
    
    public void remove(Article article) {
    	em.remove(article);
    }

}
