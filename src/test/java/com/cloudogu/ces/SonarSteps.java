/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudogu.ces;

import com.thoughtworks.gauge.Step;
import driver.Driver;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

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
        assertThat(sonarPage.getCurrentUsername(), is(user));
        assertThat(Driver.webDriver.getTitle(), containsString("Sonar"));
    }
    
    @Step("Logout of Sonar")
    public void logOutOfCas(){   
        SonarPage page = EcoSystem.getPage(SonarPage.class);
        page.logout();
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
    }
                   
}
