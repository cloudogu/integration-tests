/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudogu.ces;

import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import driver.Driver;
import javax.ws.rs.ForbiddenException;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 *
 * @author malte
 */
public class SCMSteps {
    /*-----------------------------------
    Szenario 1 Authentication
    -----------------------------------*/
    @Step("Open SCM")
    public void openSCM(){
        Driver.webDriver.get(EcoSystem.getUrl("/scm"));
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
    }
    
    @Step("SCM-Login <user> with password <pwd>")
    public void loginToCasSCM(String user, String pwd){
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
        
        CasPage page = EcoSystem.getPage(CasPage.class);
        page.login(user,pwd);
        
        SCMPage scmPage = EcoSystem.getPage(SCMPage.class);
        
        assertThat(scmPage.getCurrentUsername(), is(user));
        assertThat(Driver.webDriver.getTitle(), containsString("SCM Manager"));
    }
    
    @Step("Logout of SCM")
    public void logOutOfCas(){
        SCMPage page = EcoSystem.getPage(SCMPage.class);
        page.logout();
        openSCMApp();
        Driver.webDriver.get(EcoSystem.getUrl("/cas/logout"));
    }
    /*-----------------------------------
    Szenario 2 REST API u+p
    -----------------------------------*/
    @Step("Create REST client to access SCM API for <user> with password <password>")
    public void createRESTClientForSCMAPI(String user, String password){
        SCMAPI api = new SCMAPI(user,password);
        
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        scenarioStore.put("api", api);
        scenarioStore.put("user", user);
    }
    @Step("Obtain SCM json file")
    public void compareJsonFile(){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        SCMAPI api = (SCMAPI) scenarioStore.get("api");
        String user = (String) scenarioStore.get("user");
        
        String userName = api.getFirstName();
        
        assertThat(userName, is(user));
    }
    @Step("Close SCM API REST client")
    public void closeRestClient(){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        
        SCMAPI api = (SCMAPI) scenarioStore.get("api");
        api.close();
    }
    /*-----------------------------------
    Szenario 3 Single Sign Out
    -----------------------------------*/
    @Step("SCM-Login <user> with password <password> for Single Sign out")
    public void loginToTestSingleSignOut(String user, String password){
        openSCMApp();
        loginToCasSCMApp(user, password);                
    }
    @Step("Log out from SCM via cas/logout")
    public void logOutViaCasLogout(){
        Driver.webDriver.get(EcoSystem.getUrl("/cas/logout"));
        // be sure we are redirected to cas
        openSCMApp();
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
    }
    /*-----------------------------------
    Szenario 4 Groups
    -----------------------------------*/
    @Step("Access SCM API as <user> with password <password> with admin rights")
    public void accessRestApiAsAdmin(String user, String password){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        scenarioStore.put("user", user);
        scenarioStore.put("password", password);
        
        SCMAPI api = new SCMAPI(user,password);       
        scenarioStore.put("scm_api_admin",api);
    }
    @Step("Check if access accepted")
    public void acceptAccessAsAdmin(){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        SCMAPI api = (SCMAPI) scenarioStore.get("scm_api_admin");
        
        assertThat(api.getInformation(),startsWith("<?xml"));
    }
    @Step("Quit client with admin rights")
    public void quitAdminClient(){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        SCMAPI api = (SCMAPI) scenarioStore.get("scm_api_admin");
        
        api.close();
    }
    @Step("Create <tmpuser> with password <tmppw> in SCM")
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
    @Step("Access SCM API without admin rights")
    public void accessRestApiNotAsAdmin(){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        String user = (String) scenarioStore.get("tmpuser");
        String password = (String) scenarioStore.get("tmppw"); 
        
        SCMAPI api = new SCMAPI(user,password);
        scenarioStore.put("scm_api_noadmin",api);
    }
    @Step("Check if access not accepted")
    public void acceptAccessNotAsAdmin(){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        SCMAPI api = (SCMAPI) scenarioStore.get("scm_api_noadmin");
        
        try{
            String s = api.getInformation();
        }catch(ForbiddenException e){
            //System.out.println(e);
        }
    }
    @Step("Quit client without admin rights")
    public void quitNonAdminClient(){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        SCMAPI api = (SCMAPI) scenarioStore.get("scm_api_noadmin");
        api.close();
        
        String user = (String) scenarioStore.get("user");
        String password = (String) scenarioStore.get("password");
        
        Driver.webDriver.get(EcoSystem.getUrl("/usermgt"));
        CasPage casPage = EcoSystem.getPage(CasPage.class);
        casPage.login(user, password);
        EcoSystem.deleteUser((String) scenarioStore.get("tmpuser"));
    }
    /*-----------------------------------
    Szenario 5 User Attributes
    -----------------------------------*/
    @Step("Obtain user attributes of <user> with <password> from usermgt for SCM")
    public void getUserData(String user, String password){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        
        UsermgtAPI api = new UsermgtAPI(user,password);
        SCMAPI scmapi = new SCMAPI(user,password);
        
        String username = api.getUsername(); 
        String surname = api.getSurname();
        String displayName = api.getDisplayName(); 
        String email = api.getEmail();
        
        api.close();
        
        scenarioStore.put("api", scmapi);
        
        scenarioStore.put("username",username);
        scenarioStore.put("displayName",displayName);
        scenarioStore.put("surname",surname);
        scenarioStore.put("mail",email);
    }
    @Step("Compare user attributes with data of SCM")
    public void compareWithSCMData(){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        String username = (String) scenarioStore.get("username");
        String displayName = (String) scenarioStore.get("displayName");
        String email = (String) scenarioStore.get("mail");
        
        SCMAPI api = (SCMAPI) scenarioStore.get("api");
        assertThat(api.getFirstName(),is(username));
        assertThat(api.getDisplayName(),is(displayName));
        assertThat(api.getEmail(),is(email));
        api.close();
    }
    @Step("Log out of SCM User Attributes")
    public void logOut(){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        SCMAPI api = (SCMAPI) scenarioStore.get("api");
        
        api.close();
    }
    /*-----------------------------------
    Tear down after each scenario
    -----------------------------------*/
    @Step("Tear down logout for SCM")
    public void tearDownLogout(){
        EcoSystem.tearDownLogout();
    }
    
    private void openSCMApp() {
        EcoSystem.openApp("scm");
        // be sure we are redirected to cas
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
    }
    
    private void loginToCasSCMApp(String username, String password){
        EcoSystem.loginToCasApp(username, password);
        assertThat(Driver.webDriver.getTitle(), containsString("SCM Manager"));
    }
}
