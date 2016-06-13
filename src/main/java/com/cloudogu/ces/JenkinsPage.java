/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudogu.ces;

import driver.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 *
 * @author malte
 */
public class JenkinsPage {
    
    @FindBy(linkText = "log out")
    private WebElement logOut;
    
    public void logout(){
        logOut.click();
    }
    
    public String getCurrentUsername(){
        WebElement currentUser = Driver.webDriver.findElement(By.tagName("b"));
        return currentUser.getText();
    }
}
