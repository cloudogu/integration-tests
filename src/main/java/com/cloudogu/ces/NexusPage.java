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
 * @author malte
 */
public class NexusPage {

    public WebElement getToogle() {
        String xpath = "//body/div/div/div/div/div[2]/div/span[3]";
        WebElement toogle = EcoSystem.findElementByClickable(
                By.xpath(xpath));
        return toogle;
    }

    public WebElement getLogout() {
        String xpath = "//body/div[6]/ul/li[2]/a";
        WebElement logout = EcoSystem.findElementByClickable(
                By.xpath(xpath));
        return logout;
    }

    public void logout() {
        getToogle().click();
        getLogout().click();
    }

    public String getCurrentUsername() {
        WebElement currentUser = null;

        if (EcoSystem.attributeContainsBy(By.id("head-link-r"), "class", "head-link head-link-logged-in")) {
            currentUser = Driver.webDriver.findElement(By.id("head-link-r"));
        }
        return currentUser.getText();
    }
}
