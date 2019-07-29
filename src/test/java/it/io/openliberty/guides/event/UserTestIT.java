// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2018 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
// end::copyright[]
package it.io.openliberty.guides.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import org.junit.Test;

import io.openliberty.core.user.User;

public class UserTestIT {

    private WebTarget webTarget;

    protected Form form;
    protected Client client;
    protected Response response;
    protected HashMap<String, String> eventForm;

    protected static String baseUrl;
    protected static String port;
    protected static final String USERS = "users";

    /**
     *  Makes a POST request to the /events endpoint
     */
    protected int postRequest(HashMap<String, String> formDataMap) {
        formDataMap.forEach((formField, data) -> {
            form.param(formField, data);
        });
        webTarget = client.target(baseUrl + USERS);
        response = webTarget.request().post(Entity.form(form));
        form = new Form();
        return response.getStatus();
    }
    
    public void test() {
    	assertTrue(true);
    }

    /**
     *  Makes a PUT request to the /events/{eventId} endpoint
     */
    protected int updateRequest(HashMap<String, String> formDataMap, int eventId) {
        formDataMap.forEach((formField, data) -> {
            form.param(formField, data);
        });
        webTarget = client.target(baseUrl + USERS + "/" + eventId);
        response = webTarget.request().put(Entity.form(form));
        form = new Form();
        return response.getStatus();
    }
    
    /**
     *  Makes a DELETE request to /events/{eventId} endpoint and return the response 
     *  code 
     */
    protected int deleteRequest(int eventId) {
        webTarget = client.target(baseUrl + USERS + "/" + eventId);
        response = webTarget.request().delete();
        return response.getStatus();
    }
    
    /**
     *  Makes a GET request to the /events endpoint and returns result in a JsonArray
     */
    protected JsonArray getRequest() {
        webTarget = client.target(baseUrl + USERS);
        response = webTarget.request().get();
        return response.readEntity(JsonArray.class);
    }

    /**
     *  Makes a GET request to the /events/{eventId} endpoint and returns a JsonObject
     */ 
    protected JsonObject getIndividualEvent(int eventId) {
        webTarget = client.target(baseUrl + USERS + "/" + eventId);
        response = webTarget.request().get();
        return response.readEntity(JsonObject.class);
    }
    
    /**
     *  Makes a GET request to the /events endpoint and returns the event provided
     *  if it exists. 
     */
    protected JsonObject findEvent(User u) {
        JsonArray users = getRequest();
        for (int i = 0; i < users.size(); i++) {
            JsonObject testUser = users.getJsonObject(i);
            User test = new User(testUser.getString("email"),
            		testUser.getString("username"), testUser.getString("password"), testUser.getString("bio"), testUser.getString("image"));
            if (test.equals(u)) {
                return testUser;
            }
        }
        return null;
    }

    /**
     *  Asserts event fields (name, location, time) equal the provided name, location
     *  and date
     */
//    @Test
//    protected void assertData(JsonObject user, String email, String username, String password, String bio, String image) {
//        assertEquals(user.getString("email"), email);
//        assertEquals(user.getString("username"), username);
//        assertEquals(user.getString("password"), password);
//        assertEquals(user.getString("bio"), bio);
//        assertEquals(user.getString("image"), image);
//
//    }

}