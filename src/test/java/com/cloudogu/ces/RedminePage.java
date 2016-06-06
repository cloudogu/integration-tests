/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudogu.ces;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 *
 * @author malte
 */
public class RedminePage {
    
    @FindBy(linkText = "Sign out")
    private WebElement logOut;
    
    public void logout(){
        logOut.click();
    }
    
    
}
