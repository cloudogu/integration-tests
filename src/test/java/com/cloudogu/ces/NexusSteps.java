/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudogu.ces;

import com.fasterxml.jackson.databind.JsonNode;
import com.thoughtworks.gauge.Step;
import driver.Driver;
import java.util.Iterator;
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
        JsonNode jnode = api.getInformation(); 
        Iterator<JsonNode> elements = jnode.elements();       
        String url = elements.next().get(0).get("resourceURI").asText();
        assertThat(url, is(EcoSystem.getUrl("/nexus/service/local/users/"+user)));
        api.close();
    }
    /*-----------------------------------
    Szenario 3
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
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
    }
}
