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
    
    public void goToManageJenkinsPage(){
        Driver.webDriver.get(EcoSystem.getUrl("/jenkins/manage"));
    }
    
    public void goToConfigurationPage(String user){
        String xpathToMenuSelector = "//body/div[2]/div/div[2]/span/a";
        WebElement menuSelector = EcoSystem.findElementByClickable(
                By.xpath(xpathToMenuSelector));
        menuSelector.click();
        
        WebElement menuItemConfigure = EcoSystem.findElementByClickable(
                By.linkText("Configure"));
        menuItemConfigure.click();
    }
    
    public String getToken(){
        String token = "";
        WebElement button = EcoSystem.findElementByClickable(By.id("yui-gen1"));
        button.click();
        
        token = EcoSystem.findElementByLocated(By.id("apiToken"))
                .getAttribute("value");
        return token;
    }
    
    public String getCurrentUsername(){
        String xpath = "//body/div[2]/div/div[2]/span/a/b";
        WebElement currentUser = Driver.webDriver.findElement(By.xpath(xpath));
        return currentUser.getText();
    }
    
    public boolean AccessDenied(){
        Boolean denied = false;
        String xpath = "//body/div[@id='page-body']/div[@id='main-panel']/h1";
        String accessDenied = Driver.webDriver.findElement(By.xpath(xpath)).getText();
        if(accessDenied.equals("Access Denied")){
            denied = true;
        }
        return denied;
    }
    
    public String getUserName(){
        String userName = "";
        String xpath = "//body/div[5]/div[2]/form/table/tbody/tr/td[3]/input";
        userName = Driver.webDriver.findElement(By.xpath(xpath)).getAttribute("value");
        return userName;
    }
    
    public String getEmail(){
        String email = "";
        String xpath = "//body/div[5]/div[2]/form/table/tbody/tr[17]/td[3]/input";
        email = Driver.webDriver.findElement(By.xpath(xpath)).getAttribute("value");
        return email;
    }
}
