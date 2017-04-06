package driver;

import com.google.common.base.Preconditions;
import java.net.MalformedURLException;
import java.net.URL;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DriverFactory {

    // Get a new WebDriver Instance.
    // There are various implementations for this depending on browser. The required browser can be set as an environment variable.
    // Refer http://getgauge.io/documentation/user/current/managing_environments/README.html
    public static WebDriver getDriver() {
        String browser = System.getenv("BROWSER");
        
        if (browser == null) {
            return new FirefoxDriver();
        }
        switch (browser)
        {
          case "REMOTE":
                return createRemoteWebDriver();
            case "IE":
                return new InternetExplorerDriver();
            case "CHROME":
                return new ChromeDriver();
            default:
                return new FirefoxDriver();
        }
    }
    
    private static RemoteWebDriver createRemoteWebDriver() {
      String seleniumUrl = System.getenv("SELENIUM_URL");
      Preconditions.checkNotNull(seleniumUrl, "SELENIUM_URL environment variable not found");
      try {
        return new RemoteWebDriver(new URL(seleniumUrl), DesiredCapabilities.chrome());
      } catch (MalformedURLException ex) {
        throw new IllegalStateException("found malformed selenium url, please check SELENIUM_URL environment variable", ex);
      }
    }
}
