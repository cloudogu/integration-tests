/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudogu.ces;

import com.fasterxml.jackson.databind.JsonNode;
import com.thoughtworks.gauge.Step;
import driver.Driver;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author malte
 */
public class JenkinsSteps {
    
    @Step("Open Jenkins")
    public void openJenkins() {
        Driver.webDriver.get(EcoSystem.getUrl("/jenkins"));
        // be sure we are redirected to cas
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
    }
    
    @Step("Jenkins-Login <username> with password <password>")
    public void loginToCasJenkins(String username, String password){
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
        CasPage casPage = EcoSystem.getPage(CasPage.class);
        casPage.login(username, password);
        JenkinsPage jenkinsPage = EcoSystem.getPage(JenkinsPage.class);
        assertThat(jenkinsPage.getCurrentUsername(), is(username));
        assertThat(Driver.webDriver.getTitle(), containsString("Jenkins"));
    }
    
    @Step("Logout of Jenkins")
    public void logOutOfCas(){
        JenkinsPage page = EcoSystem.getPage(JenkinsPage.class);
        page.logout();
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
    }
    
    //@Step("Access Jenkins API via REST client for <user> with password <password>")
    public void createRESTClientForJenkinsAPI(String user, String password){
        JenkinsAPI api = new JenkinsAPI(user,password);
        JsonNode jnode = api.getInformation();
        String url = jnode.get("primaryView").get("url").asText();
        assertThat(url, is(EcoSystem.getUrl("/jenkins/")));
    }
}
