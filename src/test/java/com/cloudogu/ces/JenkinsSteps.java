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
    Szenario 1 Authentication
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
    Szenario 2 REST API u+p
    -----------------------------------*/
    @Step("Access Jenkins API via REST client for <user> with password <password>")
    public void createRESTClientForJenkinsAPI(String user, String password){
        JenkinsAPI api = new JenkinsAPI(user,password);
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        scenarioStore.put("api", api);      
    }
    @Step("Obtain Jenkins json file")
    public void compareJsonFile(){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        JenkinsAPI api = (JenkinsAPI) scenarioStore.get("api");
        JsonNode jnode = api.getInformation();
        String url = jnode.get("primaryView").get("url").asText();
        assertThat(url, is(EcoSystem.getUrl("/jenkins/")));
    }
    @Step("Close Jenkins API REST client")
    public void closeRestClient(){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        JenkinsAPI api = (JenkinsAPI) scenarioStore.get("api");
        api.close();
    }
    
    /*-----------------------------------
    Szenario 3 REST token API key
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
    /*-----------------------------------
    Szenario 4 Single Sign Out
    -----------------------------------*/
    @Step("Jenkins-Login <user> with password <password> for Single Sign out")
    public void loginToTestSingleSignOut(String user, String password){
        openJenkins();
        loginToCasJenkins(user, password);                
    }
    @Step("Log out from Jenkins via cas/logout")
    public void logOutViaCasLogout(){
        Driver.webDriver.get(EcoSystem.getUrl("/cas/logout"));
        // be sure we are redirected to cas
        openJenkins();
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
    }
    /*-----------------------------------
    Szenario 5 Groups
    -----------------------------------*/
    @Step("Jenkins-Login <user> with password <password> with admin rights")
    public void loginToTestAdminRights(String user, String password){
        openJenkins();
        loginToCasJenkins(user, password);
    }
    @Step("Access Manage Jenkins")
    public void accessManageJenkinsPage(){
        JenkinsPage page = EcoSystem.getPage(JenkinsPage.class);
        page.goToManageJenkinsPage();
        assertThat(Driver.webDriver.getTitle(),is("Manage Jenkins [Jenkins]"));
    }
    @Step("Logout of Jenkins as user with admin rights")
    public void logoutOfCasAsAdmin(){
        logOutOfCas();
    }
    @Step("Jenkins-Login <user> with password <password> without admin rights")
    public void loginToTestNoAdminRights(String user, String password){
        openJenkins();
        loginToCasJenkins(user, password);
    }
    @Step("Try to access Manage Jenkins")
    public void accessTryManageJenkinsPage(){
        JenkinsPage page = EcoSystem.getPage(JenkinsPage.class);
        page.goToManageJenkinsPage();
        Boolean accessDenied = page.AccessDenied();
        assertThat(accessDenied,is(true));
    }
    @Step("Logout of Jenkins as user without admin rights")
    public void logoutOfCasNotAsAdmin(){
        logOutOfCas();
    }
    /*-----------------------------------
    Szenario 6 User Attributes
    -----------------------------------*/
    @Step("Obtain user attributes of <user> with <password> from usermgt for Jenkins")
    public void getUserData(String user, String password){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        
        Driver.webDriver.get(EcoSystem.getUrl("/usermgt"));
        assertThat(Driver.webDriver.getTitle(),containsString("CAS – Central Authentication Service"));
        CasPage casPage = EcoSystem.getPage(CasPage.class);
        casPage.login(user, password);
        UsermgtAPI api = new UsermgtAPI(user,password);
        
        String givenName = api.getGivenName(); 
        String surname = api.getSurname();
        String displayName = api.getDisplayName(); 
        String email = api.getEmail();

        scenarioStore.put("givenname",givenName);
        scenarioStore.put("displayName",displayName);
        scenarioStore.put("surname",surname);
        scenarioStore.put("mail",email);
    }
    @Step("Switch to Jenkins user site")
    public void goToJenkinsUserSite(){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        String givenName = (String) scenarioStore.get("givenname");
        
        Driver.webDriver.get(EcoSystem.getUrl("/jenkins/user/"+givenName+"/configure"));
        
    }
    @Step("Compare user attributes with data of Jenkins")
    public void compareWithJenkinsData(){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        String displayName = (String) scenarioStore.get("displayName");
        String email = (String) scenarioStore.get("mail");
        
        JenkinsPage page = EcoSystem.getPage(JenkinsPage.class);
        assertThat(page.getUserName(),is(displayName));
        assertThat(page.getEmail(),is(email));
    }
    @Step("Log out of Jenkins User Attributes")
    public void logOut(){
        logOutOfCas();
    }
}
