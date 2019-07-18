package io.openliberty.DAO;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import io.openliberty.core.user.User;

import javax.enterprise.context.RequestScoped;


@RequestScoped
public class UserDAO {

    @PersistenceContext(name = "jpa-unit")
    private EntityManager em;
    

    public void createUser(User user) {
        em.persist(user);
    }
    
    public void createRelationship(FollowRelationship followRelationship) {
    	em.persist(followRelationship);
    }
    
//    public List<FollowRelationship> findRelationship(String userID, String otherID){
//    	return em.createNamedQuery("Users.findRelationship", User.class)
//    			 .setParameter("userID", userID)
//    			 .getResultList();
//    }
    
    public User readUser(String userID) {
        return em.find(User.class, userID);
    }

    public void updateUser(User user) {
        em.merge(user);
    }

    public void deleteUser(User user) {
        em.remove(user);
    }
    
    public User findByUsername(String username) {
    	return em.createNamedQuery("Users.findUser", User.class)
                 .setParameter("username", username).getSingleResult();
    }
    
    public User findByEmail(String email) {
    	return em.createNamedQuery("Users.findByEmail", User.class)
                 .setParameter("email", email).getSingleResult();
    }

    public List<User> findAllUsers() {
        return em.createNamedQuery("Users.findAllUsers", User.class).getResultList();
    }

    public List<User> findUser(String username) {
        return em.createNamedQuery("Users.findUser", User.class)
                 .setParameter("username", username).getResultList();
    }
    

    
}
