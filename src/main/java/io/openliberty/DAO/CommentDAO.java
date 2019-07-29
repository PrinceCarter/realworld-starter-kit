package io.openliberty.DAO;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.List;
import io.openliberty.core.comment.Comment;

@RequestScoped
public class CommentDAO {

	@PersistenceContext(name = "jpa-unit")
    private EntityManager em;
	
	
	public void save(Comment comment) {
		em.persist(comment);
	}

	
	public Comment findByID(String commentID) {
		return em.createNamedQuery("Comments.findByID", Comment.class)
				 .setParameter("commentID", commentID)
				 .getSingleResult();
	}
	
	public List<Comment> findByArticleID(String articleID){
		return em.createNamedQuery("Comments.findByArticleID", Comment.class)
			     .setParameter("articleID", articleID)
			     .getResultList();
	}
	
	public void remove(Comment comment) {
		em.remove(comment);
	}
	
}
