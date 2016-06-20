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
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 *
 * @author malte
 */
public class RedmineSteps {
    
    /*-----------------------------------
    Szenario 1
    -----------------------------------*/
    @Step("Open Redmine")
    public void openRedmine(){
        Driver.webDriver.get(EcoSystem.getUrl("/redmine"));
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
    }
    
    @Step("Redmine-Login <user> with password <pwd>")
    public void loginToCasRedmine(String user, String pwd){
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
        CasPage page = EcoSystem.getPage(CasPage.class);
        page.login(user,pwd);
        RedminePage redminePage = EcoSystem.getPage(RedminePage.class);
        assertThat(redminePage.getCurrentUsername(), is(user));
        assertThat(Driver.webDriver.getTitle(), containsString("Redmine"));
    }
    
    @Step("Logout of Redmine")
    public void logOutOfCas(){
        RedminePage page = EcoSystem.getPage(RedminePage.class);
        page.logout();
        openRedmine();
    }
    
    /*-----------------------------------
    Szenario 2
    -----------------------------------*/
    @Step("Access Redmine API via REST client for <user> with password <password>")
    public void createRESTClientForRedmineAPI(String user, String password){
        RedmineAPI api = new RedmineAPI(user,password);
        String xmlFile = api.getInformation();
        Document doc = EcoSystem.buildXmlDocument(xmlFile);
        NodeList list = doc.getElementsByTagName("firstname");
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
    @Step("Obtain Redmine key with <username> and <password>")
    public void obtainRedmineKey(String username, String password){       
        openRedmine();
        loginToCasRedmine(username, password);        
        WebDriverWait wait = new WebDriverWait(Driver.webDriver,10);
        RedminePage redminePage = EcoSystem.getPage(RedminePage.class);
        redminePage.goToMyAccountPage(wait);
        assertThat(Driver.webDriver.getTitle(), containsString("My account"));
        String key = redminePage.getKey(wait);
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        scenarioStore.put("redmine-user", username);
        scenarioStore.put("redmine-user-key", key);
        logOutOfCas();
    }
    
    @Step("Redmine-Login with key")
    public void loginWithRedmineKey(){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        String key = (String) scenarioStore.get("redmine-user-key");
        String user = (String) scenarioStore.get("redmine-user");
        RedmineAPI api = new RedmineAPI(key,"disabled");
        String xmlFile = api.getInformation();
        Document doc = EcoSystem.buildXmlDocument(xmlFile);
        NodeList list = doc.getElementsByTagName("firstname");
        String userName = "";
        for(int i = 0; i<list.getLength();i++){
            if(list.item(i).getTextContent().equals(user)){
                userName = list.item(i).getTextContent();
            }            
        }
        assertThat(userName, is(user));
        api.close();
    }
}
