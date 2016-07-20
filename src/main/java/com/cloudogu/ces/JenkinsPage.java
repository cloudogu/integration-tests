/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudogu.ces;

import driver.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 *
 * @author malte
 */
public class JenkinsPage {
    
    
    public void logout(){
        getLogout().click();
    }
    
    public WebElement getLogout(){
        String xpath = "//body/div[2]/div[1]/div[2]/span/a[2]";
        WebElement logOut = EcoSystem.findElementByClickable((By.xpath(xpath)));
        return logOut;
    }
    
    public void goToManageJenkinsPage(){
        Driver.webDriver.get(EcoSystem.getUrl("/jenkins/manage"));
    }
    
    public void goToConfigurationPage(String user){
        String url = EcoSystem.getUrl("/jenkins/user/"+user+"/configure");
        Driver.webDriver.get(url);
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
        String xpath = "//body/div[@id='page-body']/div[@id='main-panel']/p";
        String accessDenied = Driver.webDriver.findElement(By.xpath(xpath)).getAttribute("class");
        if(accessDenied.equals("error")){
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
