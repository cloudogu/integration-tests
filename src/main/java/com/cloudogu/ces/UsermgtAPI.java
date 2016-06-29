/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudogu.ces;

import com.fasterxml.jackson.databind.JsonNode;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author malte
 */
public class UsermgtAPI {
    
    private final Client client;

    private String username;
    private String password;
    
    public UsermgtAPI(String username, String password){
        this.username = username;
        this.password = password;
        this.client = EcoSystem.createRestClient(username, password);
    }
    
    public JsonNode getInformation(){
        return client.target(EcoSystem.getUrl("/usermgt/api/users"))
                  .request(MediaType.APPLICATION_JSON_TYPE)
                  .get(JsonNode.class);
    }
    
    public String getUsername(){
        JsonNode jnode = getInformation();
        JsonNode root = jnode.get("entries");
        
        String givenName = ""; 

        
        for(int i=0; i<root.size();i++){
            JsonNode inner = root.get(i);
            if(inner.get("username").asText().equals(username)){
                givenName = inner.get("username").asText();
            }
        }
        return givenName;
    }
    
    public String getGivenName(){
        JsonNode jnode = getInformation();
        JsonNode root = jnode.get("entries");
        
        String givenName = ""; 

        
        for(int i=0; i<root.size();i++){
            JsonNode inner = root.get(i);
            if(inner.get("username").asText().equals(username)){
                givenName = inner.get("givenname").asText();
            }
        }
        return givenName;
    }
    public String getSurname(){
        JsonNode jnode = getInformation();
        JsonNode root = jnode.get("entries");
        
        String surname = ""; 

        
        for(int i=0; i<root.size();i++){
            JsonNode inner = root.get(i);
            if(inner.get("username").asText().equals(username)){
                surname = inner.get("surname").asText();
            }
        }
        return surname;
    }
    public String getEmail(){
        JsonNode jnode = getInformation();
        JsonNode root = jnode.get("entries");
        
        String mail = ""; 

        
        for(int i=0; i<root.size();i++){
            JsonNode inner = root.get(i);
            if(inner.get("username").asText().equals(username)){
                mail = inner.get("mail").asText();
            }
        }
        return mail;
    }
    public String getDisplayName(){
        JsonNode jnode = getInformation();
        JsonNode root = jnode.get("entries");
        
        String displayName = ""; 

        
        for(int i=0; i<root.size();i++){
            JsonNode inner = root.get(i);
            if(inner.get("username").asText().equals(username)){
                displayName = inner.get("displayName").asText();
            }
        }
        return displayName;
    }
    
    public void close(){
        this.client.close();
    }
    
}
