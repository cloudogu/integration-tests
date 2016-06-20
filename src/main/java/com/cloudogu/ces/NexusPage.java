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
public class NexusPage {

    public WebElement getToogle() {
        WebElement toogle = EcoSystem.searchElementByTagAndAttribute("span", "id", "head-link-r");
        return toogle;
    }

    public WebElement getLogout() {
        WebElement logout = EcoSystem.searchElementByTagAndAttribute("a", "id", "ext-comp-1004");
        return logout;
    }

    public void logout() {
        getToogle().click();
        getLogout().click();
    }

    public String getCurrentUsername() {
        WebElement currentUser = null;
        WebDriver driver = Driver.webDriver;
        WebDriverWait wait = new WebDriverWait(driver, 10);
        if (wait.until(ExpectedConditions.attributeContains(By.id("head-link-r"), "class", "head-link head-link-logged-in"))) {
            currentUser = Driver.webDriver.findElement(By.id("head-link-r"));
        }
        return currentUser.getText();
    }

}
