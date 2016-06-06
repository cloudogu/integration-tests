/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudogu.ces;

import driver.Driver;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 *
 * @author malte
 */
public class UsermgtPage {
    
    @FindBy(xpath="/html/body/div/div/div[@class='collapse navbar-collapse']/ul/li/a[@href='api/logout']")
    private WebElement logout;
    
    public void logout(){
        logout.click();
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
    }
    
    
}
