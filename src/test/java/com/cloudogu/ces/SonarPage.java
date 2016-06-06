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
public class SonarPage {
    
    // Dropdown-Toogle Link must be open before 'Log out' can be clicked
    @FindBy(xpath = "/html/body/div/nav/div/ul[@class='nav navbar-nav navbar-right']/li/a")
    private WebElement toogle;
    
    public void logout(){
        toogle.click();
        Driver.webDriver.findElement(By.linkText("Log out")).click();
    }
    
    
}
