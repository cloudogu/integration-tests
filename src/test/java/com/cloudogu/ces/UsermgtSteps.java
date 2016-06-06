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
public class UsermgtSteps {
    
    @Step("Open Usermgt <url>")
    public void openUsermgt(String url){
        Driver.webDriver.get(url);
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
    }
    
    @Step("Usermgt-Login <user> with password <pwd>")
    public void loginToCasUsermgt(String user, String pwd){
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
        CasPage page = Pages.get(CasPage.class);
        page.login(user,pwd);
        assertThat(Driver.webDriver.getTitle(), containsString("User Management"));
    }
    
    @Step("Logout of Usermgt")
    public void logOutOfCas(){   
        UsermgtPage page = Pages.get(UsermgtPage.class);
        page.logout();
    }
                   
}
