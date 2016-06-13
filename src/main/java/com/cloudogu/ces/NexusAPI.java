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
public class NexusAPI {
    
    private final Client client;
    
    public NexusAPI(String username, String password){
        this.client = EcoSystem.createRestClient(username, password);
    }
    
    public JsonNode getInformation(){
        return client.target("https://192.168.115.169/nexus/content/repositories/public/")
                  .request(MediaType.APPLICATION_XML_TYPE)
                  .get(JsonNode.class);
    }
    
}
