/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudogu.ces;

import com.thoughtworks.gauge.Step;
import driver.Driver;
import driver.Pages;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

/**
 *
 * @author malte
 */
public class NexusSteps {
    
    @Step("Open Nexus <url>")
    public void openNexus(String url){
        Driver.webDriver.get(url);
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
    }
    
    @Step("Nexus-Login <user> with password <pwd>")
    public void loginToCasNexus(String user, String pwd){
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
        CasPage page = Pages.get(CasPage.class);
        page.login(user,pwd);
        assertThat(Driver.webDriver.getTitle(), containsString("Nexus"));
    }
    
    @Step("Logout of Nexus")
    public void logOutOfCas(){   
        NexusPage page = Pages.get(NexusPage.class);
        page.logout();
    }
                   
}
