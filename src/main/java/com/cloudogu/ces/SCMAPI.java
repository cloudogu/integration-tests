/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudogu.ces;

import com.fasterxml.jackson.databind.JsonNode;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author malte
 */
public class SCMAPI {
    
    private final Client client;
    private String username;
    private String password;
    
    public SCMAPI(String username, String password){
        this.username = username;
        this.password = password;
        this.client = EcoSystem.createRestClient(username, password);
    }
    
    public String getInformation(){
        return client.target(EcoSystem.getUrl("/scm/api/rest/users.xml"))
                  .request(MediaType.APPLICATION_XML_TYPE)
                  .get(String.class);
    }
    
    public JsonNode getInformationJson(){
        return client.target(EcoSystem.getUrl("/scm/api/rest/users.json"))
                  .request(MediaType.APPLICATION_JSON_TYPE)
                  .get(JsonNode.class);
    }
    
    public String getFirstName(){
        JsonNode root = getInformationJson();        
        String firstName = "";
        for(int i=0; i<root.size();i++){
            JsonNode inner = root.get(i);
            if(inner.get("name").asText().equals(username)){
                firstName = inner.get("name").asText();
            }
        }
        return firstName;
    }
    
    public String getDisplayName(){
        JsonNode root = getInformationJson();        
        String displayName = "";
        for(int i=0; i<root.size();i++){
            JsonNode inner = root.get(i);
            if(inner.get("name").asText().equals(username)){
                displayName = inner.get("displayName").asText();
            }
        }
        return displayName;
    }
    
    public String getEmail(){
        JsonNode root = getInformationJson();        
        String mail = "";
        for(int i=0; i<root.size();i++){
            JsonNode inner = root.get(i);
            if(inner.get("name").asText().equals(username)){
                mail = inner.get("mail").asText();
            }
        }
        return mail;
    }
    
    public void close(){
        this.client.close();
    }
    
}
