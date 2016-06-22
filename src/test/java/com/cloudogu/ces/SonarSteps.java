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
import java.util.Iterator;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.junit.Assert.assertThat;
import org.openqa.selenium.By;

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
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        scenarioStore.put("api", api);
        scenarioStore.put("user", user);
    }
    @Step("Obtain Sonar json file")
    public void compareJsonFile(){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        SonarAPI api = (SonarAPI) scenarioStore.get("api");
        String user = (String) scenarioStore.get("user");
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
    }
    @Step("Close Sonar API REST client")
    public void closeRestClient(){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        SonarAPI api = (SonarAPI) scenarioStore.get("api");
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
    /*-----------------------------------
    Szenario 5
    -----------------------------------*/
    @Step("Sonar-Login <user> with password <password> with admin rights")
    public void loginToTestAdminRights(String user, String password){
        openSonar();
        loginToCasSonar(user, password);
    }
    @Step("Access Administration of Sonar page")
    public void accessAdministrationPage(){
        SonarPage page = EcoSystem.getPage(SonarPage.class);
        page.goToAdministrationPage();
        String xpathNavHeadline = Driver.webDriver.findElement(By.xpath(
                "//body/div/nav[2]/div/ul/li/a")).getText();
        assertThat(xpathNavHeadline, is("Administration"));
    }
    @Step("Logout of Sonar as user with admin rights")
    public void logoutOfCasAsAdmin(){
        logOutOfCas();
    }
    @Step("Sonar-Login <user> with password <password> without admin rights")
    public void loginToTestNoAdminRights(String user, String password){
        openSonar();
        loginToCasSonar(user, password);
    }
    @Step("Try to access Administration of Sonar page")
    public void accessTryAdministrationPage(){
        SonarPage page = EcoSystem.getPage(SonarPage.class);
        page.goToAdministrationPage();
        Boolean accessDenied = page.AccessDenied();
        assertThat(accessDenied,is(true));
    }
    @Step("Logout of Sonar as user without admin rights")
    public void logoutOfCasNotAsAdmin(){
        Driver.webDriver.get(EcoSystem.getUrl("/sonar"));
        logOutOfCas();
    }
}
