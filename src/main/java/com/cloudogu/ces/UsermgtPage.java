/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudogu.ces;

import driver.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author malte
 */
public class UsermgtPage {
    
    public WebElement getLogout(){
        WebDriver driver = Driver.webDriver;
        WebDriverWait wait = new WebDriverWait(driver,10);
        WebElement logout = wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText("LOGOUT")));
        return logout;
    }
    
    public void logout(){
        getLogout().click();
    }
    public String getCurrentUsername(){
        WebDriver driver = Driver.webDriver;
        WebDriverWait wait = new WebDriverWait(driver,10);
        WebElement currentUser = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("html/body/div[@class='container ng-scope']/h1")));
        return currentUser.getText();
    }
    
}
