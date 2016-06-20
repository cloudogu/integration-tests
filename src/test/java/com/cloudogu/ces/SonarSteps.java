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
public class SonarSteps {
    
    @Step("Open Sonar")
    public void openSonar(){
        Driver.webDriver.get(EcoSystem.getUrl("/sonar"));
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
    }
    
    @Step("Sonar-Login <user> with password <pwd>")
    public void loginToCasSonar(String user, String pwd){
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
        CasPage page = EcoSystem.getPage(CasPage.class);
        page.login(user,pwd);
        SonarPage sonarPage = EcoSystem.getPage(SonarPage.class);
        assertThat(sonarPage.getCurrentUsername(user), is(user));
        assertThat(Driver.webDriver.getTitle(), containsString("Sonar"));
    }
    
    @Step("Logout of Sonar")
    public void logOutOfCas(){   
        SonarPage page = EcoSystem.getPage(SonarPage.class);
        page.logout();
        openSonar();
    }
    
    @Step("Access Sonar API via REST client for <user> with password <password>")
    public void createRESTClientForSonarAPI(String user, String password){
        SonarAPI api = new SonarAPI(user,password);
        JsonNode jnode = api.getInformation();        
        JsonNode root = jnode.get("users");
        String userName = "";
        for(int i=0; i<root.size();i++){
            JsonNode inner = root.get(i);
            if(inner.get("login").asText().equals(user)){
                userName = inner.get("login").asText();
            }
        }
        assertThat(userName, is(user));
        api.close();
    }    
}
