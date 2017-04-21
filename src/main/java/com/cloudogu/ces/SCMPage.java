/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudogu.ces;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author malte
 */
public class SCMPage {

    public WebElement getLogout() {
        String xpath = "//body/div[2]/div[2]/div/div[6]/div[2]/div/div/div/div/ul/li/a";
        WebElement logout = EcoSystem.findElementByClickable(
                By.xpath(xpath));
        return logout;
    }

    public void logout() {
        getLogout().click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(SCMPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getCurrentUsername() {
        WebElement currentUser = EcoSystem.findElementByClickable(By.xpath("//*[@id=\"scm-userinfo-tip\"]"));
        return currentUser.getText();
    }

}
