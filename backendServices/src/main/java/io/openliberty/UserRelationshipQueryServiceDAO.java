package io.openliberty;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class UserRelationshipQueryServiceDAO {

	@PersistenceContext(name = "jpa-unit")
    private EntityManager em;
	
//	public boolean isUserFollowing() {
//		
//	}
//	
//	public Set<String> followingAuthors(String userID, List<String> ids){
//		
//	}
//	
//	public List<String> followedUsers(String userID){
//		
//	}
}
