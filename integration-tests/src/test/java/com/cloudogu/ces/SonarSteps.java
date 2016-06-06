/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudogu.ces;

import com.thoughtworks.gauge.Step;
import driver.Driver;
import driver.Pages;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import org.openqa.selenium.By;

/**
 *
 * @author malte
 */
public class SonarSteps {
    
    @Step("Open Sonar <url>")
    public void openSCM(String url){
        Driver.webDriver.get(url);
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
    }
    
    @Step("Sonar-Login <user> with password <pwd>")
    public void loginToCasSCM(String user, String pwd){
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
        CasPage page = Pages.get(CasPage.class);
        page.login(user,pwd);
        assertThat(Driver.webDriver.getTitle(), containsString("Sonar"));
    }
    
    @Step("Logout of Sonar")
    public void logOutOfCas(){
        Driver.webDriver.findElement(By.linkText("Log out")).click(); // LogOut Button        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(SonarSteps.class.getName()).log(Level.SEVERE, null, ex);
        }
                
    }
}
