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
    Szenario 1
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
    Szenario 2
    -----------------------------------*/
    @Step("Access SCM API via REST client for <user> with password <password>")
    public void createRESTClientForSCMAPI(String user, String password){
        SCMAPI api = new SCMAPI(user,password);
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
        api.close();
    }
    /*-----------------------------------
    Szenario 3
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
    Szenario 4
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
}
