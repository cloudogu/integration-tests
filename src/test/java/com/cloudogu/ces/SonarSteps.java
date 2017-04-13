/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudogu.ces;

import com.fasterxml.jackson.databind.JsonNode;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import driver.Driver;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;

import org.openqa.selenium.By;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author malte
 */
public class SonarSteps {
    /*-----------------------------------
    Szenario 1 Authentication
    -----------------------------------*/
    @Step("Open Sonar")
    public void openSonar() {
        Driver.webDriver.get(EcoSystem.getUrl("/sonar"));

        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
    }

    @Step("Sonar-Login <user> with password <pwd>")
    public void loginToCasSonar(String user, String pwd) {
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));

        CasPage page = EcoSystem.getPage(CasPage.class);
        page.login(user, pwd);

        SonarPage sonarPage = EcoSystem.getPage(SonarPage.class);

        assertThat(sonarPage.getCurrentUsername(user), is(user));
        assertThat(Driver.webDriver.getTitle(), containsString("Sonar"));
    }

    @Step("Logout of Sonar")
    public void logOutOfCas() {
        SonarPage page = EcoSystem.getPage(SonarPage.class);
        page.logout();
        openSonarApp();
        Driver.webDriver.get(EcoSystem.getUrl("/cas/logout"));
    }

    /*-----------------------------------
    Szenario 2 REST API u+p
    -----------------------------------*/
    @Step("Create REST client to access Sonar API for <user> with password <password>")
    public void createRESTClientForSonarAPI(String user, String password) {
        SonarAPI api = new SonarAPI(user, password);

        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        scenarioStore.put("api", api);
        scenarioStore.put("user", user);
    }

    @Step("Obtain Sonar json file")
    public void compareJsonFile() {
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        SonarAPI api = (SonarAPI) scenarioStore.get("api");
        String user = (String) scenarioStore.get("user");

        JsonNode jnode = api.getInformation();

        String userName = EcoSystem.readUserFromJson(jnode, "users", "login", user);

        assertThat(userName, is(user));
    }

    @Step("Close Sonar API REST client")
    public void closeRestClient() {
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        SonarAPI api = (SonarAPI) scenarioStore.get("api");
        api.close();
    }

    /*-----------------------------------
    Szenario 3 REST token API key
    -----------------------------------*/
    @Step("Obtain Sonar token with <username> and <password>")
    public void obtainSonarToken(String username, String password) {
        openSonarApp();
        loginToCasSonarApp(username, password);

        SonarPage sonarPage = EcoSystem.getPage(SonarPage.class);
        String token = sonarPage.obtainToken(username);

        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        scenarioStore.put("username", username);
        scenarioStore.put("sonar-user-token", token);

        logOutViaCasLogoutApp();
    }

    @Step("Sonar-Login with token")
    public void loginWithSonarToken() {
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        String user = (String) scenarioStore.get("username");
        String token = (String) scenarioStore.get("sonar-user-token");

        SonarAPI api = new SonarAPI(token);
        JsonNode jnode = api.getInformation();

        String userName = EcoSystem.readUserFromJson(jnode, "users", "login", user);

        assertThat(userName, is(user));
        api.close();
    }

    /*-----------------------------------
    Szenario 4 Single Sign Out
    -----------------------------------*/
    @Step("Sonar-Login <user> with password <password> for Single Sign out")
    public void loginToTestSingleSignOut(String user, String password) {
        openSonarApp();
        loginToCasSonarApp(user, password);
    }

    @Step("Log out from Sonar via cas/logout")
    public void logOutViaCasLogout() {
        Driver.webDriver.get(EcoSystem.getUrl("/cas/logout"));
        // be sure we are redirected to cas
        openSonarApp();
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
    }

    /*-----------------------------------
    Szenario 5 Groups
    -----------------------------------*/
    @Step("Sonar-Login <user> with password <password> with admin rights")
    public void loginToTestAdminRights(String user, String password) {
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        scenarioStore.put("user", user);
        scenarioStore.put("password", password);

        openSonarApp();
        loginToCasSonarApp(user, password);
    }

    @Step("Access Administration of Sonar page")
    public void accessAdministrationPage() {
        SonarPage page = EcoSystem.getPage(SonarPage.class);
        page.goToAdministrationPage();

        String xpathNavHeadline = Driver.webDriver.findElement(By.xpath(
                "//body/div/nav[2]/div/ul/li/a")).getText();
        assertThat(xpathNavHeadline, is("Administration"));
    }

    @Step("Logout of Sonar as user with admin rights")
    public void logoutOfCasAsAdmin() {
        logOutViaCasLogoutApp();
    }

    @Step("Create <tmpuser> with password <tmppw> in Sonar")
    public void createNewUser(String tmpuser, String tmppw) {
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        String user = (String) scenarioStore.get("user");
        String password = (String) scenarioStore.get("password");

        Driver.webDriver.get(EcoSystem.getUrl("/usermgt"));
        CasPage casPage = EcoSystem.getPage(CasPage.class);
        casPage.login(user, password);

        EcoSystem.createNewUser(tmpuser, tmppw);

        Driver.webDriver.get(EcoSystem.getUrl("/cas/logout"));
        scenarioStore.put("tmpuser", tmpuser);
        scenarioStore.put("tmppw", tmppw);
    }

    @Step("Sonar-Login without admin rights")
    public void loginToTestNoAdminRights() {
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        String user = (String) scenarioStore.get("tmpuser");
        String password = (String) scenarioStore.get("tmppw");
        openSonarApp();
        loginToCasSonarApp(user, password);
    }

    @Step("Try to access Administration of Sonar page")
    public void accessTryAdministrationPage() {
        SonarPage page = EcoSystem.getPage(SonarPage.class);
        page.goToAdministrationPage();

        Boolean accessPermitted = page.isAccessPermitted();
        assertThat("Expected not be allowed to access the Administration Page, but was allowed.", accessPermitted, is(false));
    }

    @Step("Logout of Sonar as user without admin rights")
    public void logoutOfCasNotAsAdmin() {
        Driver.webDriver.get(EcoSystem.getUrl("/sonar"));
        logOutViaCasLogoutApp();

        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        String user = (String) scenarioStore.get("user");
        String password = (String) scenarioStore.get("password");

        Driver.webDriver.get(EcoSystem.getUrl("/usermgt"));
        CasPage casPage = EcoSystem.getPage(CasPage.class);
        casPage.login(user, password);
        EcoSystem.deleteUser((String) scenarioStore.get("tmpuser"));
    }

    /*-----------------------------------
    Szenario 6 User Attributes
    -----------------------------------*/
    @Step("Obtain user attributes of <user> with <password> from usermgt for Sonar")
    public void getUserData(String user, String password) {
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();

        UsermgtAPI api = new UsermgtAPI(user, password);
        SonarAPI sonarapi = new SonarAPI(user, password);

        String username = api.getUsername();
        String surname = api.getSurname();
        String displayName = api.getDisplayName();
        String email = api.getEmail();

        api.close();

        scenarioStore.put("api", sonarapi);

        scenarioStore.put("username", username);
        scenarioStore.put("displayName", displayName);
        scenarioStore.put("surname", surname);
        scenarioStore.put("mail", email);
    }

    @Step("Compare user attributes with data of Sonar")
    public void compareWithSonarData() {
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        String username = (String) scenarioStore.get("username");
        String email = (String) scenarioStore.get("mail");

        SonarAPI api = (SonarAPI) scenarioStore.get("api");
        assertThat(api.getFirstName(), is(username));
        assertThat(api.getEmail(), is(email));
    }

    @Step("Log out of Sonar User Attributes")
    public void logOut() {
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        SonarAPI api = (SonarAPI) scenarioStore.get("api");
        api.close();
    }

    /*-----------------------------------
    Tear down after each scenario
    -----------------------------------*/
    @Step("Tear down logout for Sonar")
    public void tearDownLogout() {
        EcoSystem.tearDownLogout();
    }

    private void openSonarApp() {
        EcoSystem.openApp("sonar");
        // be sure we are redirected to cas
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
    }

    private void loginToCasSonarApp(String username, String password) {
        EcoSystem.loginToCasApp(username, password);
        assertThat(Driver.webDriver.getTitle(), containsString("Sonar"));
    }

    private void logOutViaCasLogoutApp() {
        Driver.webDriver.get(EcoSystem.getUrl("/cas/logout"));
        // be sure we are redirected to cas
        openSonarApp();
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
    }
}
