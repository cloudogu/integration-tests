/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudogu.ces;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author malte
 */
public class RedmineAPI {
    
    private final Client client;
    
    public RedmineAPI(String username, String password){
        this.client = EcoSystem.createRestClient(username, password);
    }
    
    public String getInformation(){
        return client.target(EcoSystem.getUrl("/redmine/users.xml"))
                  .request(MediaType.APPLICATION_XML_TYPE)
                  .get(String.class);
    }
    
    public void close(){
        this.client.close();
    }
    
}
