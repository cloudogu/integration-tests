/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudogu.ces;

import driver.Driver;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
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
        String xpath = "//body/div[@id='wrapper']/div[@id='wrapper2']"
                + "/div[@id='wrapper3']/div[@id='top-menu']"
                + "/div[@id='account']/ul/li[2]/a";
        WebElement logout = Driver.webDriver.findElement(By.xpath(xpath));
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

    void goToAdministrationPage() {
        Driver.webDriver.get(EcoSystem.getUrl("/redmine/admin"));
    }
    
    public Boolean AccessDenied(){
        Boolean denied = false;
        String xpath = "//body/div[@id='wrapper']/div[@id='wrapper2']"
                + "/div[@id='wrapper3']/div[@id='main']/div[@id='content']/h2";
        String accessDenied = Driver.webDriver.findElement(By.xpath(xpath)).getText();
        if(accessDenied.equals("403")){
            denied = true;
        }
        return denied;
    }
    
    public String getFirstName(){
        assertThat(Driver.webDriver.getCurrentUrl(),is(EcoSystem.getUrl("/redmine/my/account")));
        String firstName = "";
        String xpath = "//body/div/div/div/div[@id='main']/div[2]/form/div/fieldset/p/input";
        firstName = Driver.webDriver.findElement(By.xpath(xpath)).getAttribute("value");
        return firstName;
    }
    public String getLastName(){
        assertThat(Driver.webDriver.getCurrentUrl(),is(EcoSystem.getUrl("/redmine/my/account")));
        String firstName = "";
        String xpath = "//body/div/div/div/div[@id='main']/div[2]/form/div/fieldset/p[2]/input";
        firstName = Driver.webDriver.findElement(By.xpath(xpath)).getAttribute("value");
        return firstName;
    }
    public String getEmail(){
        assertThat(Driver.webDriver.getCurrentUrl(),is(EcoSystem.getUrl("/redmine/my/account")));
        String firstName = "";
        String xpath = "//body/div/div/div/div[@id='main']/div[2]/form/div/fieldset/p[3]/input";
        firstName = Driver.webDriver.findElement(By.xpath(xpath)).getAttribute("value");
        return firstName;
    }
}
