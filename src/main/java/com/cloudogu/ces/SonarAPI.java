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
public class SonarAPI {

    private final Client client;
    private String username;
    private String password;


    /**
     * Creates an API that is designed to work the Sonar API Token.
     *
     * @param token The Sonar Access Token that has been created upfront.
     */
    public SonarAPI(String token) {
        this(token, "");
    }

    public SonarAPI(String username, String password) {
        this.username = username;
        this.password = password;
        this.client = EcoSystem.createRestClient(username, password);
    }

    public JsonNode getInformation() {
        return client.target(EcoSystem.getUrl("/sonar/api/users/search"))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(JsonNode.class);
    }

    public String getFirstName() {
        String firstName = "";

        JsonNode jnode = getInformation();
        JsonNode root = jnode.get("users");
        for (int i = 0; i < root.size(); i++) {
            JsonNode inner = root.get(i);
            if (inner.get("login").asText().equals(username)) {
                firstName = inner.get("name").asText();
            }
        }
        return firstName;
    }

    public String getEmail() {
        String email = "";

        JsonNode jnode = getInformation();
        JsonNode root = jnode.get("users");
        for (int i = 0; i < root.size(); i++) {
            JsonNode inner = root.get(i);
            if (inner.get("login").asText().equals(username)) {
                email = inner.get("email").asText();
            }
        }
        return email;
    }

    public void close() {
        this.client.close();
    }

}
