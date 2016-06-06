/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudogu.ces;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 *
 * @author malte
 */
public class SCMPage {
    
    @FindBy(id = "ext-gen51")
    private WebElement logOut;
    
    public void logout(){
        logOut.click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(SCMPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
