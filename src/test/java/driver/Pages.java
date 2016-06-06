/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package driver;

import org.openqa.selenium.support.PageFactory;

/**
 *
 * @author malte
 */
public final class Pages {

    private Pages() {
    }
    
    public static <T> T get(Class<T> pageClass){
        return PageFactory.initElements(Driver.webDriver, pageClass);
    }
    
}
