/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudogu.ces;

import com.thoughtworks.gauge.Step;
import driver.Driver;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import org.openqa.selenium.By;

/**
 *
 * @author malte
 */
public class SCMSteps {
    
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
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
    }
}
