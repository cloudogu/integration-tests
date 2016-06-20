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

    public WebElement getToogle() {
        WebElement toogle = Driver.webDriver.findElement(By.xpath("//body/div/nav/div/ul[2]/li/a"));
        return toogle;
    }

    public WebElement getLogout() {
        WebElement logout = Driver.webDriver.findElement(By.xpath("//body/div/nav/div/ul[2]/li/ul/li[2]/a"));
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
        currentUser = Driver.webDriver.findElement(By.xpath("//body/div/nav/div/ul[2]/li/a/span[2]"));
        if (wait.until(ExpectedConditions.textToBePresentInElement(currentUser, username))) {
            user = currentUser.getText();
        }
        return user;
    }

    public String obtainToken(String username, WebDriverWait wait) {
        String token = "";
        WebElement linkToAdministration = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//body/div/nav/div/ul/li[7]/a")));
        linkToAdministration.click();
        WebElement securityDropdownButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//body/div/nav[2]/div/ul[2]/li[2]/a")));
        securityDropdownButton.click();
        WebElement usersMenuItem = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//body/div/nav[2]/div/ul[2]/li[2]/ul/li/a")));
        usersMenuItem.click();
        WebElement updateTokens = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//body/div/div/div/div/div[3]/div/table/tbody/tr/td[5]/a")));
        updateTokens.click();
        List<WebElement> allTokens = Driver.webDriver.findElements(
                By.xpath("//body/div[@class='modal in']/div[2]/table/tbody/*/td[1]"));

        int row = 1;
        int data = 1;
        for (int i = 0; i < allTokens.size(); i++) {
            if (allTokens.get(i).getText().equals("tmptoken")) {
                allTokens.get(i + 2).click();
                WebElement sureButton = wait.until(
                        ExpectedConditions.elementToBeClickable(
                                By.xpath("//body/div[@class='modal in']/div[2]/table/tbody/tr[" + row + "]/td[3]/div/form")));
                sureButton.click();
                break;
            }
            data += 1;
            if (data == 3) {
                data = 1;
                row += 1;
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(SonarPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        WebElement tokenNameInputField = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//body/div[@class='modal in']/div[@class='modal-body']/form/input")));
        tokenNameInputField.sendKeys("tmptoken");     
        
        WebElement generateTokenButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//body/div[@class='modal in']/div[2]/form/button")));
        generateTokenButton.click();
        WebElement generatedToken = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//body/div[@class='modal in']/div[2]/div[2]/table/tbody/tr/td[2]/code")));
        token = generatedToken.getText();
        WebElement doneButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//body/div[@class='modal in']/div[3]/a")));
        doneButton.click();

        return token;
    }

}
