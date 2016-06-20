/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudogu.ces;

import driver.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author malte
 */
public class SonarPage {
    
    public WebElement getToogle(){
        WebElement toogle = EcoSystem.searchElementByTagAndAttribute("span","data-reactid",".0.2.0.0.1");
        return toogle;
    }
    public WebElement getLogout(){
        WebElement logout = EcoSystem.searchElementByTagAndAttribute("a","data-reactid",".0.2.0.1.1.0");
        return logout;
    }
    
    public void logout(){
        getToogle().click();
        getLogout().click();
    }
    
    public String getCurrentUsername(String username){
        WebElement currentUser= null;
        String user = "";
        WebDriverWait wait = new WebDriverWait(Driver.webDriver,10);
        //WebElement rounded = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("rounded")));
        currentUser = Driver.webDriver.findElement(By.xpath("//body/div/nav/div/ul[2]/li/a/span[2]"));
        if(wait.until(ExpectedConditions.textToBePresentInElement(currentUser, username))){
            user = currentUser.getText();
        }     
        return user;
    }    
}
