package io.openliberty.DAO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.List;
import io.openliberty.core.comment.Comment;

public class CommentDAO {

	@PersistenceContext(name = "jpa-unit")
    private EntityManager em;
	
	
	public void save(Comment comment) {
		em.persist(comment);
	}

	
	public Comment findByID(String articleID) {
		return em.createNamedQuery("Comments.findByID", Comment.class)
				 .setParameter("articleID", articleID)
				 .getSingleResult();
	}
	
//	public List<Comment> findByArticleID(String articleID){
//		List <Comment> comments = new ArrayList<Comment>();
//		
//	}
	
	public void remove(Comment comment) {
		em.remove(comment);
	}
	
}
