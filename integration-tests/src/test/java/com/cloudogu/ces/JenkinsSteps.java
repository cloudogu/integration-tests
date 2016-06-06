/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudogu.ces;

import com.thoughtworks.gauge.Step;
import driver.Driver;
import driver.Pages;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.openqa.selenium.By;

/**
 *
 * @author malte
 */
public class JenkinsSteps {
    
    @Step("Open Jenkins <url>")
    public void openJenkins(String url) {
        Driver.webDriver.get(url);
        // be sure we are redirected to cas
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
    }
    
    @Step("Jenkins-Login <username> with password <password>")
    public void loginToCasJenkins(String username, String password){
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
        CasPage page = Pages.get(CasPage.class);
        page.login(username, password);
        assertThat(Driver.webDriver.getTitle(), containsString("Jenkins"));
    }
    
    @Step("Logout of Jenkins")
    public void logOutOfCas(){
        Driver.webDriver.findElement(By.linkText("log out")).click();
    }
    
}
