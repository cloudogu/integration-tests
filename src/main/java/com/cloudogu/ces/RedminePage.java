/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudogu.ces;

import org.openqa.selenium.WebElement;

/**
 *
 * @author malte
 */
public class RedminePage {

    public void logout() {
        getLogout().click();
    }
    
    public WebElement getLogout(){
        WebElement logout = EcoSystem.searchElementByTagAndAttribute("a","class","logout");
        return logout;
    }

    public String getCurrentUsername() {
        WebElement currentUsername = EcoSystem.searchElementByTagAndAttribute("a","class","user active");
        return currentUsername.getText();
    }
}
