/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudogu.ces;

import driver.Driver;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author malte
 */
public class SonarPage {

    static final String XPATH_NAVBAR_MAIN = "//body/div/nav/div/ul[2]/li";
    static final String XPATH_NAVBAR_MAIN_ADMINISTRATION = "//body/div/nav/div/ul/li[7]";
    static final String XPATH_NAVBAR_SECURITY = "//body/div/nav[2]/div/ul[2]/li[2]";
    static final String XPATH_ADMINPAGE_USER = "//body/div[@class='modal in']/div[2]/table/tbody";
    static final String XPATH_LINKTABLE = "//body/div/div/div/div/div[3]/div/table/tbody";
    static final String XPATH_TOKENFRAME_BODY = "//body/div[@class='modal in']/div[@class='modal-body']";

    public WebElement getToogle() {
        WebElement toogle = Driver.webDriver.findElement(By.xpath(XPATH_NAVBAR_MAIN + "/a"));
        return toogle;
    }

    public WebElement getLogout() {
        WebElement logout = Driver.webDriver.findElement(By.xpath(XPATH_NAVBAR_MAIN + "/ul/li[2]/a"));
        return logout;
    }

    public void logout() {
        getToogle().click();
        getLogout().click();
    }

    public String getCurrentUsername(String username) {
        WebElement currentUser = null;
        String user = "";
        WebDriverWait wait = new WebDriverWait(Driver.webDriver, 10);
        currentUser = Driver.webDriver.findElement(By.xpath(XPATH_NAVBAR_MAIN + "/a/span[2]"));
        if (wait.until(ExpectedConditions.textToBePresentInElement(currentUser, username))) {
            user = currentUser.getText();
        }
        return user;
    }

    public String obtainToken(String username) {
        String token = "";
        WebElement linkToAdministration = EcoSystem.findElementByClickable(
                By.xpath(XPATH_NAVBAR_MAIN_ADMINISTRATION + "/a"));
        linkToAdministration.click();
        
        WebElement securityDropdownButton = EcoSystem.findElementByClickable(
                By.xpath(XPATH_NAVBAR_SECURITY + "/a"));
        securityDropdownButton.click();
        
        WebElement usersMenuItem = EcoSystem.findElementByClickable(
                By.xpath(XPATH_NAVBAR_SECURITY + "/ul/li/a"));
        usersMenuItem.click();
        
        WebElement updateTokens = EcoSystem.findElementByClickable(
                By.xpath(XPATH_LINKTABLE + "/tr/td[5]/a"));
        updateTokens.click();
        
        deleteExistingToken("tmptoken");
        
        // without thread.sleep inputfield will not be found after token deletion
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(SonarPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        WebElement tokenNameInputField = EcoSystem.findElementByClickable(
                By.xpath(XPATH_TOKENFRAME_BODY + "/form/input"));
        tokenNameInputField.sendKeys("tmptoken");
        tokenNameInputField.submit();

        WebElement generatedToken = EcoSystem.findElementByLocated(
                By.xpath(XPATH_TOKENFRAME_BODY + "/div[2]/table/tbody/tr/td[2]/code"));
        token = generatedToken.getText();
        
        WebElement doneButton = EcoSystem.findElementByClickable(
                By.xpath("//body/div[@class='modal in']/div[3]/a"));
        doneButton.click();

        return token;
    }

    private void deleteExistingToken(String token) {
        List<WebElement> allTokens = Driver.webDriver.findElements(
                By.xpath(XPATH_ADMINPAGE_USER + "/*/td[1]"));
        
        for (int i = 0; i < allTokens.size(); i++) {
            if (allTokens.get(i).getText().equals(token)) {
                String append = "/tr[" + (i + 1) + "]/td[3]/div/form";
                WebElement button = EcoSystem.findElementByClickable(By.xpath(XPATH_ADMINPAGE_USER + append));
                button.click();
                WebElement sureButton = EcoSystem.findElementByClickable(By.xpath(XPATH_ADMINPAGE_USER + append));
                sureButton.click();
                break;
            }
        }
    }
    
    void goToAdministrationPage() {
        Driver.webDriver.get(EcoSystem.getUrl("/sonar/settings"));
    }
    
    public Boolean AccessDenied(){
        Boolean denied = false;
        String xpath = "//body/div/div/div/form/div";
        String accessDenied = Driver.webDriver.findElement(By.xpath(xpath)).getText();
        if(accessDenied.equals("You are not authorized to access this page. "
                + "Please log in with more privileges and try again.")){
            denied = true;
        }
        return denied;
    }
}
