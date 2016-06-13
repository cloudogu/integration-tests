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
public class UsermgtAPI {
    
    private final Client client;
    
    public UsermgtAPI(String username, String password){
        this.client = EcoSystem.createRestClient(username, password);
    }
    
    public JsonNode getInformation(){
        return client.target(EcoSystem.getUrl("/usermgt/api/users"))
                  .request(MediaType.APPLICATION_JSON_TYPE)
                  .get(JsonNode.class);
    }
    
    public void close(){
        this.client.close();
    }
    
}
