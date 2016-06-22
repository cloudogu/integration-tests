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
import javax.ws.rs.ForbiddenException;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

/**
 *
 * @author malte
 */
public class NexusSteps {
    /*-----------------------------------
    Szenario 1
    -----------------------------------*/
    @Step("Open Nexus")
    public void openNexus(){
        Driver.webDriver.get(EcoSystem.getUrl("/nexus"));
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
    }
    
    @Step("Nexus-Login <user> with password <pwd>")
    public void loginToCasNexus(String user, String pwd){
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
        CasPage page = EcoSystem.getPage(CasPage.class);
        page.login(user,pwd);
        NexusPage nexusPage = EcoSystem.getPage(NexusPage.class);
        assertThat(nexusPage.getCurrentUsername(), is(user));
        assertThat(Driver.webDriver.getTitle(), containsString("Nexus"));
    }
    
    @Step("Logout of Nexus")
    public void logOutOfCas(){   
        NexusPage page = EcoSystem.getPage(NexusPage.class);
        page.logout();
        openNexus();
    }
    /*-----------------------------------
    Szenario 2
    -----------------------------------*/
    @Step("Access Nexus API via REST client for <user> with password <password>")
    public void createRESTClientForNexusAPI(String user, String password){
        NexusAPI api = new NexusAPI(user,password);
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        scenarioStore.put("api", api);
        scenarioStore.put("user", user);
    }
    @Step("Obtain Nexus json file")
    public void compareJsonFile(){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        NexusAPI api = (NexusAPI) scenarioStore.get("api");
        String user = (String) scenarioStore.get("user");
        JsonNode jnode = api.getInformation(); 
        Iterator<JsonNode> elements = jnode.elements();       
        String url = elements.next().get(0).get("resourceURI").asText();
        assertThat(url, is(EcoSystem.getUrl("/nexus/service/local/users/"+user)));
    }
    @Step("Close Nexus API REST client")
    public void closeRestClient(){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        NexusAPI api = (NexusAPI) scenarioStore.get("api");
        api.close();
    }
    /*-----------------------------------
    Szenario 3
    -----------------------------------*/
    @Step("Obtain Nexus key with <user> and <password>")
    public void obtainNexusKey(){
        //stub: todo
    }
    @Step("Nexus-Login with key")
    public void loginWithNexusKey(){
        //stub: todo
    }   
    /*-----------------------------------
    Szenario 4
    -----------------------------------*/
    @Step("Nexus-Login <user> with password <password> for Single Sign out")
    public void loginToTestSingleSignOut(String user, String password){
        openNexus();
        loginToCasNexus(user, password);                
    }
    @Step("Log out from Nexus via cas/logout")
    public void logOutViaCasLogout(){
        Driver.webDriver.get(EcoSystem.getUrl("/cas/logout"));
        // be sure we are redirected to cas
        openNexus();
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
    }
    /*-----------------------------------
    Szenario 5
    -----------------------------------*/
    @Step("Access Nexus API as <user> with password <password> with admin rights")
    public void accessRestApiAsAdmin(String user, String password){
        NexusAPI api = new NexusAPI(user,password);
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        scenarioStore.put("user", user);
        scenarioStore.put("nexus_api_admin",api);
    }
    @Step("Check if access to Nexus file accepted")
    public void acceptAccessAsAdmin(){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        String user = (String) scenarioStore.get("user");
        NexusAPI api = (NexusAPI) scenarioStore.get("nexus_api_admin");
        JsonNode jnode = api.getInformation();
        Iterator<JsonNode> elements = jnode.elements();       
        String url = elements.next().get(0).get("resourceURI").asText();
        assertThat(url, is(EcoSystem.getUrl("/nexus/service/local/users/"+user)));
    }
    @Step("Quit Nexus client with admin rights")
    public void quitAdminClient(){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        NexusAPI api = (NexusAPI) scenarioStore.get("nexus_api_admin");
        api.close();
    }
    @Step("Access Nexus API as <user> with password <password> without admin rights")
    public void accessRestApiNotAsAdmin(String user, String password){
        NexusAPI api = new NexusAPI(user,password);
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        scenarioStore.put("nexus_api_noadmin",api);
    }
    @Step("Check if access to Nexus file not accepted")
    public void acceptAccessNotAsAdmin(){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        NexusAPI api = (NexusAPI) scenarioStore.get("nexus_api_noadmin");
        try{
            JsonNode n = api.getInformation();
        }catch(ForbiddenException e){
            //System.out.println(e);
        }
    }
    @Step("Quit Nexus client without admin rights")
    public void quitNonAdminClient(){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        NexusAPI api = (NexusAPI) scenarioStore.get("nexus_api_noadmin");
        api.close();
    }
}
