package io.openliberty.core.comment;

import java.time.LocalTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "Comments")
@NamedQuery(name = "Comments.findByID", query = "SELECT c FROM Comment c WHERE c.commentID = :commentID")
public class Comment {
	
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "commentID")
    private String commentID;
    
    @Column(name = "body")
    private String body;
    
    @Column(name = "userID")
    private String userID;
    
    @Column(name = "articleID")
    private String articleID;
    
    @Column(name = "createdAt")
    private LocalTime createdAt;

    public Comment(String body, String userId, String articleId) {
        this.commentID = UUID.randomUUID().toString();
        this.setBody(body);
        this.setUserID(userId);
        this.setArticleID(articleId);
        this.setCreatedAt(LocalTime.now());
    }
    
    public Comment() {
    	
    }

	public String getCommentID() {
		return commentID;
	}

	public void setCommentID(String commentID) {
		this.commentID = commentID;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getArticleID() {
		return articleID;
	}

	public void setArticleID(String articleID) {
		this.articleID = articleID;
	}

	public LocalTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalTime createdAt) {
		this.createdAt = createdAt;
	}
}
