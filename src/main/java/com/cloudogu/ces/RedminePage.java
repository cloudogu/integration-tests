/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudogu.ces;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author malte
 */
public class RedminePage {

    public void logout() {
        getLogout().click();
    }
    
    public WebElement getLogout(){
        WebElement logout = EcoSystem.searchElementByTagAndAttribute("a","class","logout");
        return logout;
    }

    public String getCurrentUsername() {
        WebElement currentUsername = EcoSystem.searchElementByTagAndAttribute("a","class","user active");
        return currentUsername.getText();
    }
    
    public void goToMyAccountPage(WebDriverWait wait){        
        WebElement myAccount = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("My account")));
        myAccount.click();       
    }
    
    public String getKey(WebDriverWait wait){
        String key = "";
        WebElement showKey = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Show")));
        showKey.click();
        key = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("api-access-key"))).getText();
        return key;
    }
}
