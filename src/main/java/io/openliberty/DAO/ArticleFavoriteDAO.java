package io.openliberty.DAO;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import io.openliberty.core.article.ArticleFavorite;

@RequestScoped
public class ArticleFavoriteDAO {
	@PersistenceContext(name = "jpa-unit")
    private EntityManager em;
	
    public void save(ArticleFavorite articleFavorite) {
        em.persist(articleFavorite);
    }
    
    public ArticleFavorite find(String articleID, String userID) {
    	return em.createNamedQuery("ArticleFavorite.find", ArticleFavorite.class)
    			 .setParameter("articleID", articleID)
    			 .getSingleResult();
    }
    
    public void remove(ArticleFavorite articleFavorite) {
    	em.remove(articleFavorite);
    }
}
