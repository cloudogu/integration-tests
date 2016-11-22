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
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertFalse;

/**
 *
 * @author malte
 */
public class RedmineSteps {

    /*-----------------------------------
    Szenario 1 Authentication
    -----------------------------------*/
    @Step("Open Redmine")
    public void openRedmine(){
        Driver.webDriver.get(EcoSystem.getUrl("/redmine"));
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
    }

    @Step("Redmine-Login <user> with password <pwd>")
    public void loginToCasRedmine(String user, String pwd){
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));

        CasPage page = EcoSystem.getPage(CasPage.class);
        page.login(user,pwd);

        RedminePage redminePage = EcoSystem.getPage(RedminePage.class);

        assertThat(redminePage.getCurrentUsername(), is(user));
        assertThat(Driver.webDriver.getTitle(), containsString("Redmine"));
    }

    @Step("Logout of Redmine")
    public void logOutOfCas(){
        RedminePage page = EcoSystem.getPage(RedminePage.class);
        page.logout();
        openRedmineApp();
        Driver.webDriver.get(EcoSystem.getUrl("/cas/logout"));
    }

    /*-----------------------------------
    Szenario 2 REST API u+p
    -----------------------------------*/
    @Step("Create REST client to access Redmine API for <user> with password <password>")
    public void createRESTClientForRedmineAPI(String user, String password){
        RedmineAPI api = new RedmineAPI(user,password);

        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        scenarioStore.put("api", api);
        scenarioStore.put("user", user);
    }
    @Step("Obtain Redmine json file")
    public void compareJsonFile(){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        RedmineAPI api = (RedmineAPI) scenarioStore.get("api");
        String user = (String) scenarioStore.get("user");

        JsonNode jnode = api.getInformation();

        String userName = EcoSystem.readUserFromJson(jnode, "users", "login", user);

        assertThat(userName, is(user));
    }
    @Step("Close Redmine API REST client")
    public void closeRestClient(){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        RedmineAPI api = (RedmineAPI) scenarioStore.get("api");
        api.close();
    }
    /*-----------------------------------
    Szenario 3 REST token API key
    -----------------------------------*/
    @Step("Obtain Redmine key with <username> and <password>")
    public void obtainRedmineKey(String username, String password){
        openRedmineApp();

        loginToCasRedmineApp(username, password);


        RedminePage redminePage = EcoSystem.getPage(RedminePage.class);
        redminePage.goToMyAccountPage();

        assertThat(Driver.webDriver.getCurrentUrl(), is(EcoSystem.getUrl("/redmine/my/account")));
        String key = redminePage.getKey();

        assertFalse(key.equals(""));

        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        scenarioStore.put("redmine-user", username);
        scenarioStore.put("redmine-user-key", key);

        logOutViaCasLogoutApp();
    }

    @Step("Redmine-Login with key")
    public void loginWithRedmineKey(){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        String key = (String) scenarioStore.get("redmine-user-key");
        String user = (String) scenarioStore.get("redmine-user");

        RedmineAPI api = new RedmineAPI(key,"disabled");
        JsonNode jnode = api.getInformation();

        String userName = EcoSystem.readUserFromJson(jnode, "users", "login", user);

        assertThat(userName, is(user));

        api.close();
    }
    /*-----------------------------------
    Szenario 4 Single Sign Out
    -----------------------------------*/
    @Step("Redmine-Login <user> with password <password> for Single Sign out")
    public void loginToTestSingleSignOut(String user, String password){
        openRedmineApp();
        loginToCasRedmineApp(user, password);
    }
    @Step("Log out from Redmine via cas/logout")
    public void logOutViaCasLogout(){
        Driver.webDriver.get(EcoSystem.getUrl("/cas/logout"));
        // be sure we are redirected to cas
        openRedmineApp();
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
    }
    /*-----------------------------------
    Szenario 5 Groups
    -----------------------------------*/
    @Step("Redmine-Login <user> with password <password> with admin rights")
    public void loginToTestAdminRights(String user, String password){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        scenarioStore.put("user", user);
        scenarioStore.put("password", password);
        openRedmineApp();
        loginToCasRedmineApp(user, password);
    }
    @Step("Access Administration of Redmine page")
    public void accessAdministrationPage(){
        RedminePage page = EcoSystem.getPage(RedminePage.class);
        page.goToAdministrationPage();
        assertThat(Driver.webDriver.getTitle(),is("Administration - Redmine"));
    }
    @Step("Logout of Redmine as user with admin rights")
    public void logoutOfCasAsAdmin(){
        logOutViaCasLogoutApp();
    }
    @Step("Create <tmpuser> with password <tmppw> in Redmine")
    public void createNewUser(String tmpuser, String tmppw){
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
    @Step("Redmine-Login without admin rights")
    public void loginToTestNoAdminRights(){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        String user = (String) scenarioStore.get("tmpuser");
        String password = (String) scenarioStore.get("tmppw");
        openRedmineApp();
        loginToCasRedmineApp(user, password);
    }
    @Step("Try to access Administration of Redmine page")
    public void accessTryAdministrationPage(){
        RedminePage page = EcoSystem.getPage(RedminePage.class);
        page.goToAdministrationPage();
        Boolean accessDenied = page.AccessDenied();
        assertThat(accessDenied,is(true));
    }
    @Step("Logout of Redmine as user without admin rights")
    public void logoutOfCasNotAsAdmin(){
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
    @Step("Obtain user attributes of <user> with <password> from usermgt for Redmine")
    public void getUserData(String user, String password){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();

        Driver.webDriver.get(EcoSystem.getUrl("/usermgt"));
        assertThat(Driver.webDriver.getTitle(),containsString("CAS – Central Authentication Service"));
        CasPage casPage = EcoSystem.getPage(CasPage.class);
        casPage.login(user, password);
        UsermgtAPI api = new UsermgtAPI(user,password);

        String givenName = api.getGivenName();
        String surname = api.getSurname();
        String displayName = api.getDisplayName();
        String email = api.getEmail();

        api.close();

        scenarioStore.put("givenname",givenName);
        scenarioStore.put("displayName",displayName);
        scenarioStore.put("surname",surname);
        scenarioStore.put("mail",email);
    }
    @Step("Switch to Redmine user site")
    public void goToRedmineUserSite(){

        Driver.webDriver.get(EcoSystem.getUrl("/redmine/my/account"));

    }
    @Step("Compare user attributes with data of Redmine")
    public void compareWithRedmineData(){
        DataStore scenarioStore = DataStoreFactory.getScenarioDataStore();
        String firstName = (String) scenarioStore.get("givenname");
        String lastName = (String) scenarioStore.get("surname");
        String email = (String) scenarioStore.get("mail");

        RedminePage page = EcoSystem.getPage(RedminePage.class);
        assertThat(page.getFirstName(),is(firstName));
        assertThat(page.getLastName(),is(lastName));
        assertThat(page.getEmail(),is(email));
    }
    @Step("Log out of Redmine User Attributes")
    public void logOut(){
        logOutViaCasLogoutApp();
    }
    /*-----------------------------------
    Tear down after each scenario
    -----------------------------------*/
    @Step("Tear down logout for Redmine")
    public void tearDownLogout(){
        EcoSystem.tearDownLogout();
    }

    private void openRedmineApp() {
        EcoSystem.openApp("redmine");
        // be sure we are redirected to cas
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
    }

    private void loginToCasRedmineApp(String username, String password){
        EcoSystem.loginToCasApp(username, password);
        assertThat(Driver.webDriver.getTitle(), containsString("Redmine"));
    }

    private void logOutViaCasLogoutApp(){
        Driver.webDriver.get(EcoSystem.getUrl("/cas/logout"));
        // be sure we are redirected to cas
        openRedmineApp();
        assertThat(Driver.webDriver.getTitle(), startsWith("CAS"));
    }
}
