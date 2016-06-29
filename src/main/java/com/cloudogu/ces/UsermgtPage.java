/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudogu.ces;

import driver.Driver;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 *
 * @author malte
 */
public class UsermgtPage {

    public WebElement getLogout() {
        WebElement logout = EcoSystem.findElementByLocated(
                By.linkText("LOGOUT"));
        return logout;
    }

    public WebElement getSaveUserBtn() {
        String xpath = "//body/div[2]/div[2]/div/div/form/button";
        return Driver.webDriver.findElement(By.xpath(xpath));
    }

    public void logout() {
        getLogout().click();
    }

    public String getCurrentUsername() {
        WebElement currentUser = EcoSystem.findElementByLocated(
                By.xpath("html/body/div[@class='container ng-scope']/h1"));
        return currentUser.getText();
    }

    public void clickCreateUserButton() {
        assertThat(Driver.webDriver.getCurrentUrl(), is(EcoSystem.getUrl("/usermgt/#/users")));
        String xpathToCreateBtn = "//body/div[2]/div/div[2]/a";
        WebElement createBtn = Driver.webDriver.findElement(By.xpath(xpathToCreateBtn));
        createBtn.click();
    }

    public void createNewUserExt(String username, String givenname, String surname, String displayName, String mail,
            String password) {

        WebElement input_username = Driver.webDriver.findElement(By.id("username"));
        input_username.sendKeys(username);

        WebElement input_givenname = Driver.webDriver.findElement(By.id("givenname"));
        input_givenname.sendKeys(givenname);

        WebElement input_surname = Driver.webDriver.findElement(By.id("surname"));
        input_surname.sendKeys(surname);

        WebElement input_displayName = Driver.webDriver.findElement(By.id("displayName"));
        input_displayName.sendKeys(displayName);

        WebElement input_mail = Driver.webDriver.findElement(By.id("email"));
        input_mail.sendKeys(mail);

        WebElement input_password = Driver.webDriver.findElement(By.id("password"));
        input_password.sendKeys(password);

        WebElement input_passwordConfirm = Driver.webDriver.findElement(By.id("confirmPassword"));
        input_passwordConfirm.sendKeys(password);

        getSaveUserBtn().click();
    }

    public void createNewUser(String username, String password) {
        createNewUserExt(username, username, username, username, username + "@" + username + ".de", password);
    }

    public boolean userExists(String username) {
        assertThat(Driver.webDriver.getCurrentUrl(),is(EcoSystem.getUrl("/usermgt/#/users")));
        
        Boolean userExists = false;
        String xpath = "//body/div[2]/table/tbody";
        WebElement tableroot = Driver.webDriver.findElement(By.xpath(xpath));
        List<WebElement> tablerow = tableroot.findElements(By.xpath(".//tr"));

        for (int i = 0; i < tablerow.size(); i++) {
            List<WebElement> tabledata = tablerow.get(i).findElements(By.xpath(".//td"));
            if (tabledata.get(0).findElement(By.xpath(".//b")).getText().equals(username)
                    && tabledata.get(1).getText().equals(username)) {
                userExists = true;
            }
        }
        return userExists;
    }

    public void deleteUser(String username) {

        Driver.webDriver.get(EcoSystem.getUrl("/#/user/" + username));
        
        String xpath = "//body/div[2]/div[2]/div/div/form/button[2]";
        WebElement remove = EcoSystem.findElementByClickable(By.xpath(xpath));
        remove.click();

        String xpathRm = "//body/div[5]/div/div/div[3]/button";
        WebElement removeOk = EcoSystem.findElementByClickable(By.xpath(xpathRm));
        removeOk.click();

    }
}
