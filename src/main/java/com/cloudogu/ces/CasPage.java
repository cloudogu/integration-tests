/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudogu.ces;

import com.thoughtworks.gauge.Gauge;
import driver.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author malte
 */
public class CasPage {

    @FindBy(id = "username")
    private WebElement usernameInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(name = "submit")
    private WebElement submitButton;

    public void login(String username, String password) {
        usernameInput.sendKeys(username);
        passwordInput.sendKeys(password);

        /* remember current HTML element for check of staleness. When this element goes stale, the page will
         * have been reloaded.
         */
        WebElement htmlElement = Driver.webDriver.findElement(By.tagName("html"));

        submitButton.click();

        long startingTime = System.currentTimeMillis();
        new WebDriverWait(Driver.webDriver, 10)
                .ignoring(TimeoutException.class)
                .until(ExpectedConditions.stalenessOf(htmlElement));

        Gauge.writeMessage(String.format("waited %,dms for login", System.currentTimeMillis() - startingTime));
    }


}
