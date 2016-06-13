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
public class SCMAPI {
    
    private final Client client;
    
    public SCMAPI(String username, String password){
        this.client = EcoSystem.createRestClient(username, password);
    }
    
    public String getInformation(){
        return client.target(EcoSystem.getUrl("/scm/api/rest/users.xml"))
                  .request(MediaType.APPLICATION_XML_TYPE)
                  .get(String.class);
    }
    
    public void close(){
        this.client.close();
    }
    
}
