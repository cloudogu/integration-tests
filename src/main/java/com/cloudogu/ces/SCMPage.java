/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudogu.ces;

import driver.Driver;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author malte
 */
public class SCMPage {
    
    public WebElement getLogout(){
        WebDriver driver = Driver.webDriver;
        WebDriverWait wait = new WebDriverWait(driver,10);
        WebElement logout = wait.until(
                ExpectedConditions.elementToBeClickable(By.linkText("Log out")));
        return logout;
    }
    
    public void logout(){
        getLogout().click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(SCMPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getCurrentUsername(){
        WebDriver driver = Driver.webDriver;
        WebElement currentUser = EcoSystem.findElementByLocated(By.id("scm-userinfo-tip"));
        return currentUser.getText();
    }    
    
}
