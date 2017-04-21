/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudogu.ces;

import com.cloudogu.ces.verification.Verifier;
import com.fasterxml.jackson.databind.JsonNode;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import driver.Driver;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author malte
 */
public class UsermgtSteps {
    /*-----------------------------------
    Szenario 1 Authentication
    -----------------------------------*/
    @Step("Open Usermgt")
    public void openUsermgt() {
        Driver.webDriver.get(EcoSystem.getUrl("/usermgt"));
        Verifier.verifyTitle(Driver.webDriver, startsWith("CAS"));
    }

    @Step("Usermgt-Login <user> with password <pwd>")
    public void loginToCasUsermgt(String user, String pwd) {
        Verifier.verifyTitle(Driver.webDriver, startsWith("CAS"));

        CasPage page = EcoSystem.getPage(CasPage.class);
        page.login(user, pwd);

        UsermgtPage usermgtPage = EcoSystem.getPage(UsermgtPage.class);

        assertThat(usermgtPage.getCurrentUsername(), is(user.toUpperCase()));
        Verifier.verifyTitle(Driver.webDriver, containsString("User Management"));
    }

    @Step("Logout of Usermgt")
    public void logOutOfCas() {
        UsermgtPage page = EcoSystem.getPage(UsermgtPage.class);
        page.logout();
        openUsermgtApp();
        Driver.webDriver.get(EcoSystem.getUrl("/cas/logout"));
    }

    /*-----------------------------------
    Szenario 2 REST API u+p
    -----------------------------------*/
    @Step("Create REST client to access Usermgt API for <user> with password <password>")
    public void createRESTClientForSonarAPI(String user, String password) {
        UsermgtAPI api = new UsermgtAPI(user, password);

        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        scenarioStore.put("api", api);
        scenarioStore.put("user", user);
    }

    @Step("Obtain Usermgt json file")
    public void compareJsonFile() {
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        UsermgtAPI api = (UsermgtAPI) scenarioStore.get("api");
        String user = (String) scenarioStore.get("user");

        JsonNode jnode = api.getInformation();

        String userName = EcoSystem.readUserFromJson(jnode, "entries", "username", user);

        assertThat(userName, is(user));
    }

    @Step("Close Usermgt API REST client")
    public void closeRestClient() {
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        UsermgtAPI api = (UsermgtAPI) scenarioStore.get("api");

        api.close();
    }

    /*-----------------------------------
    Szenario 3 Single Sign Out
    -----------------------------------*/
    @Step("Usermgt-Login <user> with password <password> for Single Sign out")
    public void loginToTestSingleSignOut(String user, String password) {
        openUsermgtApp();
        loginToCasUsermgtApp(user, password);
    }

    @Step("Log out from Usermgt via cas/logout")
    public void logOutViaCasLogout() {
        Driver.webDriver.get(EcoSystem.getUrl("/cas/logout"));
        // be sure we are redirected to cas
        openUsermgtApp();

        Verifier.verifyTitle(Driver.webDriver, startsWith("CAS"));
    }

    /*-----------------------------------
    Tear down after each scenario
    -----------------------------------*/
    @Step("Tear down logout for Usermgt")
    public void tearDownLogout() {
        EcoSystem.tearDownLogout();
    }

    private void openUsermgtApp() {
        EcoSystem.openApp("usermgt");
        // be sure we are redirected to cas
        Verifier.verifyTitle(Driver.webDriver, startsWith("CAS"));
    }

    private void loginToCasUsermgtApp(String username, String password) {
        EcoSystem.loginToCasApp(username, password);
        Verifier.verifyTitle(Driver.webDriver, containsString("User Management"));
    }
}
