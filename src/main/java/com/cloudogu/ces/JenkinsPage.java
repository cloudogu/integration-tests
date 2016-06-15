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
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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
    
    public void goToConfigurationPage(String user, WebDriverWait wait){        
        WebElement menuSelector = wait.until(ExpectedConditions.elementToBeClickable(By.linkText(user)));
        menuSelector.click();
        WebElement menuItemConfigure = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Configure")));
        menuItemConfigure.click();
    }
    
    public String getToken(WebDriverWait wait){
        String token = "";
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.id("yui-gen1")));
        button.click();
        token = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("apiToken"))).getAttribute("value");
        return token;
    }
    
    public String getCurrentUsername(){
        WebElement currentUser = Driver.webDriver.findElement(By.tagName("b"));
        return currentUser.getText();
    }
}
