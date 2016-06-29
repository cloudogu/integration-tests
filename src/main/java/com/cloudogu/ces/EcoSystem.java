/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudogu.ces;

import com.fasterxml.jackson.databind.JsonNode;
import driver.Driver;
import java.io.IOException;
import java.io.StringReader;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 
 * @author malte
 */
public final class EcoSystem {

    private static final String BASE_URL = System.getProperty("eco.system");
    
    private EcoSystem() {
    }
    
    public static String getUrl(String suffix){
        return BASE_URL.concat(suffix);
    }
    
    public static <T> T getPage(Class<T> pageClass){
        return PageFactory.initElements(Driver.webDriver, pageClass);
    }
    /**
     * Creates WebDriverWait object that waits up to 5 seconds to find an
     * element until it is clickable and returns it.
     * 
     * @param by Mechanism to locate element
     * @return found element
     */
    public static WebElement findElementByClickable(By by){        
        WebDriverWait wait = new WebDriverWait(Driver.webDriver,5);
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(by));
        return element;
    }
    /**
     * Creates WebDriverWait object that waits up to 5 seconds to find an 
     * element until it is clickable and returns it.
     * 
     * @param by Mechanism to locate element
     * @return found element
     */
    public static WebElement findElementByLocated(By by){        
        WebDriverWait wait = new WebDriverWait(Driver.webDriver,5);
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(by));
        return element;
    }
    /**
     * Creates WebDriverWait object that waits up to 5 seconds to find an
     * element depending on attributeContains method. If the element is found a
     * true will be returned.
     * 
     * @param by Mechanism to locate element
     * @param attribute attribute of element to search for
     * @param value value of the attribute
     * @return true if element found
     */
    public static boolean attributeContainsBy(By by, String attribute, String value){        
        WebDriverWait wait = new WebDriverWait(Driver.webDriver,5);
        boolean contains = wait.until(ExpectedConditions.attributeContains(by, attribute, value));
        return contains;
    }
    public static boolean textPresentInElementBy(WebElement currentUser, String username){        
        WebDriverWait wait = new WebDriverWait(Driver.webDriver,5);
        boolean contains = wait.until(ExpectedConditions.textToBePresentInElement(currentUser, username));
        return contains;
    }
    /**
     * Reads the child node of root by a certain name and then tries to find it’s
     * child node. Then it compares its value with the user String. If its true
     * the user String will be returned.
     * @param root JsonNode root
     * @param firstChild String name of first child node
     * @param secondChild String name of child node's child
     * @param user String to compare child node's child's value with
     * @return String if value equals String user
     */
    public static String readUserFromJson(JsonNode root, String firstChild,
            String secondChild, String user){
        JsonNode firstNode = root.get(firstChild);
        
        String userName = null;
        for(int i=0; i<firstNode.size();i++){
            JsonNode secondNode = firstNode.get(i);
            if(secondNode.get(secondChild).asText().equals(user)){
                userName = secondNode.get(secondChild).asText();
            }
        }
        return userName;
    }
    /**
     * Creates a new user on the usermgt page with name tmpuser and password
     * tmppw.
     * @param tmpuser Name of the new user
     * @param tmppw Password of the new user
     */
    public static void createNewUser(String tmpuser, String tmppw){
        
        UsermgtPage usermgtPage = EcoSystem.getPage(UsermgtPage.class);
        Driver.webDriver.get(EcoSystem.getUrl("/#/users"));
        
        if(!usermgtPage.userExists(tmpuser)){
            usermgtPage.clickCreateUserButton();
            usermgtPage.createNewUser(tmpuser,tmppw);
        }
    }
    /**
     * Delete an existing user on the usermgt page with name user.
     * @param user 
     */
    public static void deleteUser(String user){
        Driver.webDriver.get(EcoSystem.getUrl("/usermgt/#/users"));
        UsermgtPage usermgtPage = EcoSystem.getPage(UsermgtPage.class);

        if(usermgtPage.userExists(user)){
            usermgtPage.deleteUser(user);
        }
        Driver.webDriver.get(EcoSystem.getUrl("/cas/logout"));
    }
    
    public static Document buildXmlDocument(String xml){
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document doc = null;
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xml));
            doc = db.parse(is);

        } catch (SAXException | IOException | ParserConfigurationException ex) {
            Logger.getLogger(EcoSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return doc;
    }
    
    private static SSLContext createUnsecureSSLContext() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, new TrustManager[]{new AcceptAllX509TrustManager()}, new java.security.SecureRandom());
        return context;
    }
    
    public static Client createRestClient(String username, String password){
        SSLContext context;
        
        try {
            context = createUnsecureSSLContext();
        } catch (NoSuchAlgorithmException | KeyManagementException ex){
            throw new RuntimeException("could not create unsecure ssl context", ex);
        }
        

        HttpAuthenticationFeature basicAuth = HttpAuthenticationFeature.basicBuilder()
            .credentials(username, password)    
            .build();
        
        return ClientBuilder.newBuilder()
                .sslContext(context)
                .register(basicAuth)
                .register(new JacksonFeature())
                .build();
        
    }
    
    private static class AcceptAllX509TrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] xcs, String string) throws java.security.cert.CertificateException {
            
        }

        @Override
        public void checkServerTrusted(X509Certificate[] xcs, String string) throws java.security.cert.CertificateException {
            
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
        
    }
}
