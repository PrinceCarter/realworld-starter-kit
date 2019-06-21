package io.openliberty.api;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RequestBody {
    @XmlElement public String email;
    @XmlElement public String password;
    @XmlElement public String username;
    
    public RequestBody(String email, String password, String username) {
    	this.email = email;
    	this.password = password;
    	this.username = username;
    }
    
    public RequestBody() {
    	
    }
}
