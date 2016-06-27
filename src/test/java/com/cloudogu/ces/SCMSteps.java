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
import javax.ws.rs.ForbiddenException;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

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
        openSCM();
    }
    /*-----------------------------------
    Szenario 2 REST API u+p
    -----------------------------------*/
    @Step("Access SCM API via REST client for <user> with password <password>")
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
        String xmlFile = api.getInformation();        
        Document doc = EcoSystem.buildXmlDocument(xmlFile);
        NodeList list = doc.getElementsByTagName("displayName");
        String userName = "";
        for(int i = 0; i<list.getLength();i++){
            if(list.item(i).getTextContent().equals(user)){
                userName = list.item(i).getTextContent();
            }            
        }
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
        openSCM();
        loginToCasSCM(user, password);                
    }
    @Step("Log out from SCM via cas/logout")
    public void logOutViaCasLogout(){
        Driver.webDriver.get(EcoSystem.getUrl("/cas/logout"));
        // be sure we are redirected to cas
        openSCM();
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
    }
    /*-----------------------------------
    Szenario 4 Groups
    -----------------------------------*/
    @Step("Access SCM API as <user> with password <password> with admin rights")
    public void accessRestApiAsAdmin(String user, String password){
        SCMAPI api = new SCMAPI(user,password);
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
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
    @Step("Access SCM API as <user> with password <password> without admin rights")
    public void accessRestApiNotAsAdmin(String user, String password){
        SCMAPI api = new SCMAPI(user,password);
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
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
    }
    /*-----------------------------------
    Szenario 5 User Attributes
    -----------------------------------*/
    @Step("Obtain user attributes of <user> with <password> from usermgt for SCM")
    public void getUserData(String user, String password){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        
        UsermgtAPI api = new UsermgtAPI(user,password);
        SCMAPI scmapi = new SCMAPI(user,password);
        
        String givenName = api.getGivenName(); 
        String surname = api.getSurname();
        String displayName = api.getDisplayName(); 
        String email = api.getEmail();
        
        api.close();
        
        scenarioStore.put("api", scmapi);
        
        scenarioStore.put("givenname",givenName);
        scenarioStore.put("displayName",displayName);
        scenarioStore.put("surname",surname);
        scenarioStore.put("mail",email);
    }
    @Step("Compare user attributes with data of SCM")
    public void compareWithSCMData(){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        String firstName = (String) scenarioStore.get("givenname");
        String displayName = (String) scenarioStore.get("displayName");
        String email = (String) scenarioStore.get("mail");
        
        SCMAPI api = (SCMAPI) scenarioStore.get("api");
        assertThat(api.getFirstName(),is(firstName));
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
}
