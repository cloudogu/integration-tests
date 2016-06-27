/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudogu.ces;

import driver.Driver;
import java.io.IOException;
import java.io.StringReader;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;
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
    
    public static WebElement searchElementByTagAndAttribute(String tag, String attribute, String attributeName){
        List<WebElement> webElements = Driver.webDriver.findElements(By.tagName(tag));       
        WebElement webElementToReturn = null;  
        for(WebElement webElement : webElements){
            if(webElement.getAttribute(attribute).equals(attributeName)){
                webElementToReturn = webElement;
                return webElementToReturn;
            }
        }
        return webElementToReturn;
    }
    
    public static void createNewUser(String tmpuser, String tmppw){
        
        UsermgtPage usermgtPage = EcoSystem.getPage(UsermgtPage.class);
        Driver.webDriver.get(EcoSystem.getUrl("/#/users"));
        
        if(!usermgtPage.userExists(tmpuser)){
            usermgtPage.clickCreateUserButton();
            usermgtPage.createNewUser(tmpuser,tmppw);
        }
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
