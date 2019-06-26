package io.openliberty;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import io.openliberty.core.article.Tag;

@RequestScoped
public class TagDAO {
	
    @PersistenceContext(name = "jpa-unit")
    private EntityManager em;
	
	public List<Tag> allTags() {
		return em.createNamedQuery("Tag.getAllTags", Tag.class).getResultList();
	}
}
