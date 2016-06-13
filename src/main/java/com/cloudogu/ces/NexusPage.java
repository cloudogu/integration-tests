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
public class NexusPage {
    
    public WebElement getToogle(){
        WebElement toogle = EcoSystem.searchElementByTagAndAttribute("span","id","head-link-r");
        return toogle;
    }
    public WebElement getLogout(){
        WebElement logout = EcoSystem.searchElementByTagAndAttribute("a","id","ext-comp-1004");
        return logout;
    }
    
    public void logout(){
            getToogle().click();
            getLogout().click();
    }
    
    public String getCurrentUsername(){
        WebElement currentUser = EcoSystem.searchElementByTagAndAttribute("span","id","head-link-r");
        return currentUser.getText();
    }
    
    
}
