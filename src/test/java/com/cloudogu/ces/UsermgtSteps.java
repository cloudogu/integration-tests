/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudogu.ces;

import com.fasterxml.jackson.databind.JsonNode;
import com.thoughtworks.gauge.Step;
import driver.Driver;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

/**
 *
 * @author malte
 */
public class UsermgtSteps {
    
    @Step("Open Usermgt")
    public void openUsermgt(){
        Driver.webDriver.get(EcoSystem.getUrl("/usermgt"));
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
    }
    
    @Step("Usermgt-Login <user> with password <pwd>")
    public void loginToCasUsermgt(String user, String pwd){
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
        CasPage page = EcoSystem.getPage(CasPage.class);
        page.login(user,pwd);
        UsermgtPage usermgtPage = EcoSystem.getPage(UsermgtPage.class);
        assertThat(usermgtPage.getCurrentUsername(), is(user.toUpperCase()));
        assertThat(Driver.webDriver.getTitle(), containsString("User Management"));
    }
    
    @Step("Logout of Usermgt")
    public void logOutOfCas(){   
        UsermgtPage page = EcoSystem.getPage(UsermgtPage.class);
        page.logout();
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
    }
    @Step("Access Usermgt API via REST client for <user> with password <password>")
    public void createRESTClientForSonarAPI(String user, String password){
        UsermgtAPI api = new UsermgtAPI(user,password);
        JsonNode jnode = api.getInformation();        
        JsonNode root = jnode.get("entries");
        String userName = "";
        for(int i=0; i<root.size();i++){
            JsonNode inner = root.get(i);
            if(inner.get("displayName").asText().equals(user)){
                userName = inner.get("displayName").asText();
            }
        }
        assertThat(userName, is(user));
        api.close();
    }               
}
