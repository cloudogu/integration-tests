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
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 *
 * @author malte
 */
public class RedmineSteps {
    
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
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
    }
    
    @Step("Access Redmine API via REST client for <user> with password <password>")
    public void createRESTClientForRedmineAPI(String user, String password){
        RedmineAPI api = new RedmineAPI(user,password);
        String xmlFile = api.getInformation();        
        Document doc = EcoSystem.buildXmlDocument(xmlFile);
        NodeList list = doc.getElementsByTagName("firstname");
        String userName = list.item(0).getTextContent();
        assertThat(userName, is(user));
        api.close();
    }
}
