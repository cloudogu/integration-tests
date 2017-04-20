package com.cloudogu.ces.verification;

import org.hamcrest.Matcher;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertThat;

/**
 * This class provides common verification.
 * Created by omilke on 20.04.2017.
 */
public class Verifier {


    public static void verifyTitle(WebDriver webDriver, Matcher<String> titleMatcher) {

        String errorMessage = String.format("Title did not match: was <%s> \r\n\t\t\tCurrent URL %s", webDriver.getTitle(), webDriver.getCurrentUrl());
        assertThat(errorMessage, webDriver.getTitle(), titleMatcher);
    }
}
