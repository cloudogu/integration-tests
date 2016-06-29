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
        String mail = getItemFromJson("name", "name");     
        return mail;
    }
    
    public String getDisplayName(){
        String mail = getItemFromJson("name", "displayName");     
        return mail;
    }
    
    public String getEmail(){     
        String mail = getItemFromJson("name", "mail");     
        return mail;
    }
    
    private String getItemFromJson(String user, String item){
        JsonNode root = getInformationJson();        
        String itemToReturn = null;
        for(int i=0; i<root.size();i++){
            JsonNode inner = root.get(i);
            if(inner.get(user).asText().equals(username)){
                itemToReturn = inner.get(item).asText();
            }
        }
        return itemToReturn;
    } 
    
    public void close(){
        this.client.close();
    }
    
}
