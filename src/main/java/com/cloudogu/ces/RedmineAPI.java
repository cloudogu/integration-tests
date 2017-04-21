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
 * @author malte
 */
public class RedmineAPI {

    private final Client client;

    /**
     * Creates an API that is designed to work the Redmine API Token.
     *
     * @param token The Redmine Access Token that has been created upfront.
     */
    public RedmineAPI(String token) {
        this(token, "");
    }

    public RedmineAPI(String username, String password) {
        this.client = EcoSystem.createRestClient(username, password);
    }

    public String getInformationXML() {
        return client.target(EcoSystem.getUrl("/redmine/users.xml"))
                .request(MediaType.APPLICATION_XML_TYPE)
                .get(String.class);
    }

    public JsonNode getInformation() {
        return client.target(EcoSystem.getUrl("/redmine/users.json"))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(JsonNode.class);
    }

    public void close() {
        this.client.close();
    }

}
