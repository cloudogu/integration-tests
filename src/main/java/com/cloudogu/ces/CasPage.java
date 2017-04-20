/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudogu.ces;

import com.thoughtworks.gauge.Gauge;
import driver.Driver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author malte
 */
public class CasPage {

    @FindBy(tagName = "html")
    private WebElement htmlElement;
    
    @FindBy(id = "username")
    private WebElement usernameInput;
    
    @FindBy(id = "password")
    private WebElement passwordInput;
    
    @FindBy(name = "submit")
    private WebElement submitButton;
    
    public void login(String username, String password){
        usernameInput.sendKeys(username);
        passwordInput.sendKeys(password);

        submitButton.click();

        long startingTime = System.currentTimeMillis();
        new WebDriverWait(Driver.webDriver, 10).until(ExpectedConditions.stalenessOf(htmlElement));

        Gauge.writeMessage(String.format("waited %,dms for login", System.currentTimeMillis() - startingTime) );
    }

    
}
