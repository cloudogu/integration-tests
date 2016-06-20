/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudogu.ces;

import com.fasterxml.jackson.databind.JsonNode;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import driver.Driver;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author malte
 */
public class JenkinsSteps {
    
    /*-----------------------------------
    Szenario 1
    -----------------------------------*/
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
        openJenkins();
    }
    
    /*-----------------------------------
    Szenario 2
    -----------------------------------*/
    @Step("Access Jenkins API via REST client for <user> with password <password>")
    public void createRESTClientForJenkinsAPI(String user, String password){
        JenkinsAPI api = new JenkinsAPI(user,password);
        JsonNode jnode = api.getInformation();
        String url = jnode.get("primaryView").get("url").asText();
        assertThat(url, is(EcoSystem.getUrl("/jenkins/")));
        api.close();
    }
    
    /*-----------------------------------
    Szenario 3
    -----------------------------------*/
    @Step("Obtain Jenkins token with <username> and <password>")
    public void obtainJenkinsToken(String username, String password){       
        openJenkins();
        loginToCasJenkins(username, password);        
        WebDriverWait wait = new WebDriverWait(Driver.webDriver,10);
        JenkinsPage jenkinsPage = EcoSystem.getPage(JenkinsPage.class);
        jenkinsPage.goToConfigurationPage(username,wait);        
        assertThat(Driver.webDriver.getTitle(), containsString("Configuration"));
        String token = jenkinsPage.getToken(wait);
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        scenarioStore.put("username",username);
        scenarioStore.put("jenkins-user-token", token);
        logOutOfCas();
    }
    
    @Step("Jenkins-Login with token")
    public void loginWithJenkinsToken(){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        String user = (String) scenarioStore.get("username");
        String token = (String) scenarioStore.get("jenkins-user-token");
        
        createRESTClientForJenkinsAPI(user,token);
    }
}
