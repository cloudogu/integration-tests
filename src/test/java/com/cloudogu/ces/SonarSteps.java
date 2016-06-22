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
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 *
 * @author malte
 */
public class SonarSteps {
    /*-----------------------------------
    Szenario 1
    -----------------------------------*/
    @Step("Open Sonar")
    public void openSonar(){
        Driver.webDriver.get(EcoSystem.getUrl("/sonar"));
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
    }
    
    @Step("Sonar-Login <user> with password <pwd>")
    public void loginToCasSonar(String user, String pwd){
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
        CasPage page = EcoSystem.getPage(CasPage.class);
        page.login(user,pwd);
        SonarPage sonarPage = EcoSystem.getPage(SonarPage.class);
        assertThat(sonarPage.getCurrentUsername(user), is(user));
        assertThat(Driver.webDriver.getTitle(), containsString("Sonar"));
    }
    
    @Step("Logout of Sonar")
    public void logOutOfCas(){   
        SonarPage page = EcoSystem.getPage(SonarPage.class);
        page.logout();
        openSonar();
    }
    /*-----------------------------------
    Szenario 2
    -----------------------------------*/
    @Step("Access Sonar API via REST client for <user> with password <password>")
    public void createRESTClientForSonarAPI(String user, String password){
        SonarAPI api = new SonarAPI(user,password);
        JsonNode jnode = api.getInformation();        
        JsonNode root = jnode.get("users");
        String userName = "";
        for(int i=0; i<root.size();i++){
            JsonNode inner = root.get(i);
            if(inner.get("login").asText().equals(user)){
                userName = inner.get("login").asText();
            }
        }
        assertThat(userName, is(user));
        api.close();
    }
    /*-----------------------------------
    Szenario 3
    -----------------------------------*/
    @Step("Obtain Sonar token with <username> and <password>")
    public void obtainSonarToken(String username, String password){       
        openSonar();
        loginToCasSonar(username, password);        
        WebDriverWait wait = new WebDriverWait(Driver.webDriver,10);
        SonarPage sonarPage = EcoSystem.getPage(SonarPage.class);
        String token = sonarPage.obtainToken(username,wait);        
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        scenarioStore.put("username",username);
        scenarioStore.put("sonar-user-token", token);
        logOutOfCas();
    }
    
    @Step("Sonar-Login with token")
    public void loginWithSonarToken(){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        String user = (String) scenarioStore.get("username");
        String token = (String) scenarioStore.get("sonar-user-token");
        
        SonarAPI api = new SonarAPI(token,"disabled");
        JsonNode jnode = api.getInformation();        
        JsonNode root = jnode.get("users");
        String userName = "";
        for(int i=0; i<root.size();i++){
            JsonNode inner = root.get(i);
            if(inner.get("login").asText().equals(user)){
                userName = inner.get("login").asText();
            }
        }
        assertThat(userName, is(user));
        api.close();
    }
    /*-----------------------------------
    Szenario 4
    -----------------------------------*/
    @Step("Sonar-Login <user> with password <password> for Single Sign out")
    public void loginToTestSingleSignOut(String user, String password){
        openSonar();
        loginToCasSonar(user, password);                
    }
    @Step("Log out from Sonar via cas/logout")
    public void logOutViaCasLogout(){
        Driver.webDriver.get(EcoSystem.getUrl("/cas/logout"));
        // be sure we are redirected to cas
        openSonar();
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
    }
}
