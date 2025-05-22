import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;

public class AndroidTest {

    AppiumDriver driver;

    @BeforeTest
    public void setUp() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("appium:platformName", "Android");
        caps.setCapability("appium:automationName", "UiAutomator2");
        caps.setCapability("appium:platformVersion", "16");
        caps.setCapability("appium:deviceName", "Android Emulador");
        caps.setCapability("appium:appPackage", "com.swaglabsmobileapp");
        caps.setCapability("appium:appActivity", "com.swaglabsmobileapp.SplashActivity");
        caps.setCapability("appium:appWaitActivity", "*");
        caps.setCapability("appium:app", Paths.get(System.getProperty("user.dir"), "apps", "TestApp.apk").toString());

        driver = new AndroidDriver(new URL("http://localhost:4725"), caps);
    }

    @Test
    public void testApp() throws InterruptedException {
        Thread.sleep(1000);

        driver.findElement(new AppiumBy.ByAccessibilityId("test-Username")).sendKeys("standard_user");
        driver.findElement(new AppiumBy.ByAccessibilityId("test-Password")).sendKeys("secret_sauce");
        driver.findElement(new AppiumBy.ByAccessibilityId("test-LOGIN")).click();

        Thread.sleep(1000);

        driver.findElement(new AppiumBy.ByAndroidUIAutomator("new UiSelector().text(\"ADD TO CART\").instance(0)"))
                .click();

        String validarProduto = "Sauce Labs Backpack";
        driver.findElement(new AppiumBy.ByAndroidUIAutomator("new UiSelector().text(\"Sauce Labs Backpack\")"))
                .getText();
        System.out.println("Produto encontrado: " + validarProduto);

        driver.findElement(new AppiumBy.ByAndroidUIAutomator(
                "new UiSelector().className(\"android.widget.ImageView\").instance(3)")).click();

        Thread.sleep(1000);

        driver.findElement(new AppiumBy.ByAccessibilityId("test-CHECKOUT")).click();

        Thread.sleep(1000);

        driver.findElement(new AppiumBy.ByAccessibilityId("test-First Name")).sendKeys("Ricardo");
        driver.findElement(new AppiumBy.ByAccessibilityId("test-Last Name")).sendKeys("Costa");
        driver.findElement(new AppiumBy.ByAccessibilityId("test-Zip/Postal Code")).sendKeys("91110000");
        driver.findElement(new AppiumBy.ByAndroidUIAutomator("new UiSelector().text(\"CONTINUE\")")).click();

        Thread.sleep(1000);

        String produtoNoSite = driver
                .findElement(new AppiumBy.ByAndroidUIAutomator("new UiSelector().text(\"Sauce Labs Backpack\")"))
                .getText();
        Assert.assertEquals(validarProduto, produtoNoSite);

        String valorDoProduto = driver
                .findElement(new AppiumBy.ByAndroidUIAutomator("new UiSelector().text(\"$29.99\")")).getText();
        String validarValorDoProduto = "$29.99";
        Assert.assertEquals(valorDoProduto, validarValorDoProduto);

        String valorDoTaxa = driver
                .findElement(new AppiumBy.ByAndroidUIAutomator("new UiSelector().text(\"Tax: $2.40\")")).getText();
        String validarDoTaxa = "Tax: $2.40";
        Assert.assertEquals(valorDoTaxa, validarDoTaxa);

        String total = driver.findElement(new AppiumBy.ByAndroidUIAutomator("new UiSelector().text(\"Total: $32.39\")"))
                .getText();
        String validarValorDoTotal = "Total: $32.39";
        Assert.assertEquals(total, validarValorDoTotal);

        Thread.sleep(1000);

        driver.findElement(new AppiumBy.ByAndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true))" +
                ".scrollIntoView(new UiSelector().text(\"test-FINISH\"))")).click();

    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}