/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudogu.ces;

import com.cloudogu.ces.verification.Verifier;
import com.fasterxml.jackson.databind.JsonNode;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import driver.Driver;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

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
        Verifier.verifyTitle(Driver.webDriver, startsWith("CAS"));
    }
    
    @Step("Jenkins-Login <username> with password <password>")
    public void loginToCasJenkins(String username, String password){
        Verifier.verifyTitle(Driver.webDriver, startsWith("CAS"));
        
        CasPage casPage = EcoSystem.getPage(CasPage.class);
        casPage.login(username, password);
        
        JenkinsPage jenkinsPage = EcoSystem.getPage(JenkinsPage.class);
        UsermgtAPI api = new UsermgtAPI(username, password);
        String user = api.getDisplayName();

        assertThat(jenkinsPage.getCurrentUsername(), is(equalToIgnoringCase(user)));
        Verifier.verifyTitle(Driver.webDriver, containsString("Jenkins"));
    }
    
    @Step("Logout of Jenkins")
    public void logOutOfCas(){
        JenkinsPage page = EcoSystem.getPage(JenkinsPage.class);
        page.logout();
        openJenkinsApp();
        Driver.webDriver.get(EcoSystem.getUrl("/cas/logout"));
    }
    
    /*-----------------------------------
    Szenario 2 REST API u+p
    -----------------------------------*/
    @Step("Create REST client to access Jenkins API for <user> with password <password>")
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
        openJenkinsApp();
        loginToCasJenkinsApp(username, password);        

        JenkinsPage jenkinsPage = EcoSystem.getPage(JenkinsPage.class);
        jenkinsPage.goToConfigurationPage(username);
        
        assertThat(Driver.webDriver.getCurrentUrl(), is(EcoSystem.getUrl("/jenkins/user/admin/configure")));
        
        String token = jenkinsPage.getToken();
        
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        scenarioStore.put("username",username);
        scenarioStore.put("jenkins-user-token", token);
        
        logOutViaCasLogoutApp();
    }
    
    @Step("Jenkins-Login with token")
    public void loginWithJenkinsToken(){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        String user = (String) scenarioStore.get("username");
        String token = (String) scenarioStore.get("jenkins-user-token");
        
        JenkinsAPI api = new JenkinsAPI(user,token);
        api.close();
    }
    /*-----------------------------------
    Szenario 4 Single Sign Out
    -----------------------------------*/
    @Step("Jenkins-Login <user> with password <password> for Single Sign out")
    public void loginToTestSingleSignOut(String user, String password){
        openJenkinsApp();
        loginToCasJenkinsApp(user, password);                
    }
    @Step("Log out from Jenkins via cas/logout")
    public void logOutViaCasLogout(){
        Driver.webDriver.get(EcoSystem.getUrl("/cas/logout"));
        // be sure we are redirected to cas
        openJenkinsApp();
        Verifier.verifyTitle(Driver.webDriver, startsWith("CAS"));
    }
    /*-----------------------------------
    Szenario 5 Groups
    -----------------------------------*/
    @Step("Jenkins-Login <user> with password <password> with admin rights")
    public void loginToTestAdminRights(String user, String password){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        scenarioStore.put("user", user);
        scenarioStore.put("password", password);
        
        openJenkinsApp();
        loginToCasJenkinsApp(user, password);
    }
    @Step("Access Manage Jenkins")
    public void accessManageJenkinsPage(){
        JenkinsPage page = EcoSystem.getPage(JenkinsPage.class);
        page.goToManageJenkinsPage();
        assertThat(Driver.webDriver.getCurrentUrl(),is(EcoSystem.getUrl("/jenkins/manage")));
    }
    @Step("Logout of Jenkins as user with admin rights")
    public void logoutOfCasAsAdmin(){
        logOutViaCasLogoutApp();
    }
    @Step("Create <tmpuser> with password <tmppw> in Jenkins")
    public void createNewUser(String tmpuser, String tmppw){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        String user = (String) scenarioStore.get("user");
        String password = (String) scenarioStore.get("password");
        
        Driver.webDriver.get(EcoSystem.getUrl("/usermgt"));
        CasPage casPage = EcoSystem.getPage(CasPage.class);
        casPage.login(user, password);
        
        EcoSystem.createNewUser(tmpuser, tmppw);
        
        Driver.webDriver.get(EcoSystem.getUrl("/cas/logout"));
        scenarioStore.put("tmpuser", tmpuser);
        scenarioStore.put("tmppw", tmppw);
    }
    @Step("Jenkins-Login without admin rights")
    public void loginToTestNoAdminRights(){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        String user = (String) scenarioStore.get("tmpuser");
        String password = (String) scenarioStore.get("tmppw");
        
        openJenkinsApp();

        Verifier.verifyTitle(Driver.webDriver, startsWith("CAS"));
        
        CasPage casPage = EcoSystem.getPage(CasPage.class);
        casPage.login(user, password);

        Verifier.verifyTitle(Driver.webDriver, containsString("Jenkins"));
    }
    @Step("Try to access Manage Jenkins")
    public void accessTryManageJenkinsPage(){
        JenkinsPage page = EcoSystem.getPage(JenkinsPage.class);
        page.goToManageJenkinsPage();
        
        boolean accessDenied = page.AccessDenied();
        assertThat(accessDenied,is(true));
    }
    @Step("Logout of Jenkins as user without admin rights")
    public void logoutOfCasNotAsAdmin(){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        String user = (String) scenarioStore.get("user");
        String password = (String) scenarioStore.get("password");
        
        logOutViaCasLogoutApp();
        
        Driver.webDriver.get(EcoSystem.getUrl("/usermgt"));
        CasPage casPage = EcoSystem.getPage(CasPage.class);
        casPage.login(user, password);
        EcoSystem.deleteUser((String) scenarioStore.get("tmpuser"));
    }
    /*-----------------------------------
    Szenario 6 User Attributes
    -----------------------------------*/
    @Step("Obtain user attributes of <user> with <password> from usermgt for Jenkins")
    public void getUserData(String user, String password){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        
        Driver.webDriver.get(EcoSystem.getUrl("/usermgt"));
        Verifier.verifyTitle(Driver.webDriver, containsString("CAS – Central Authentication Service"));
        CasPage casPage = EcoSystem.getPage(CasPage.class);
        casPage.login(user, password);
        UsermgtAPI api = new UsermgtAPI(user,password);
        
        String username = api.getUsername(); 
        String surname = api.getSurname();
        String displayName = api.getDisplayName(); 
        String email = api.getEmail();

        scenarioStore.put("username",username);
        scenarioStore.put("displayName",displayName);
        scenarioStore.put("surname",surname);
        scenarioStore.put("mail",email);
    }
    @Step("Switch to Jenkins user site")
    public void goToJenkinsUserSite(){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        String username = (String) scenarioStore.get("username");
        
        Driver.webDriver.get(EcoSystem.getUrl("/jenkins/user/"+username+"/configure"));
        
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
        logOutViaCasLogoutApp();
    }
    /*-----------------------------------
    Tear down after each scenario
    -----------------------------------*/
    @Step("Tear down logout for Jenkins")
    public void tearDownLogout(){
        EcoSystem.tearDownLogout();
    }
    
    private void openJenkinsApp() {
        EcoSystem.openApp("jenkins");
        // be sure we are redirected to cas
        Verifier.verifyTitle(Driver.webDriver, startsWith("CAS"));
    }
    
    private void loginToCasJenkinsApp(String username, String password){
        EcoSystem.loginToCasApp(username, password);
        Verifier.verifyTitle(Driver.webDriver, containsString("Jenkins"));
    }

    private void logOutViaCasLogoutApp(){
        Driver.webDriver.get(EcoSystem.getUrl("/cas/logout"));
        // be sure we are redirected to cas
        openJenkinsApp();
        Verifier.verifyTitle(Driver.webDriver, startsWith("CAS"));
    }
}
