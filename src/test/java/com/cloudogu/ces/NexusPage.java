/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudogu.ces;

import driver.Driver;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.By;

/**
 *
 * @author malte
 */
public class NexusPage {
    
    public void logout(){
        try {
            Thread.sleep(2000);
            Driver.webDriver.findElement(By.id("head-link-r")).click();
            Thread.sleep(2000);
            Driver.webDriver.findElement(By.id("ext-gen209")).click();
        } catch (InterruptedException ex) {
            Logger.getLogger(NexusPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
