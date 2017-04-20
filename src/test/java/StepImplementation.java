import com.cloudogu.ces.verification.Verifier;
import com.thoughtworks.gauge.Gauge;
import com.thoughtworks.gauge.Step;
import driver.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.hamcrest.CoreMatchers.containsString;

public class StepImplementation {

    @Step("Navigate to <url>")
    public void navigateTo(String url) {
        Driver.webDriver.get(url);
        Verifier.verifyTitle(Driver.webDriver, containsString("Gauge"));
    }

    @Step("Go to Gauge Get Started Page")
    public void gotoGetStartedPage() throws InterruptedException {
        WebElement getStartedButton = Driver.webDriver.findElement(By.xpath("//*[@id=\"content\"]/section[1]/section/div[2]/a[1]"));
        getStartedButton.click();
        Gauge.writeMessage("Page title is ", Driver.webDriver.getTitle());
    }
}
