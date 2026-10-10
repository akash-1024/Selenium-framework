package com.demo.framework.base;

import com.demo.framework.config.FrameworkConfig;
import com.demo.framework.driver.DriverFactory;
import com.demo.framework.driver.DriverManager;
import com.demo.framework.reusablemethods.BrowserMethods;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseTest {
    protected WebDriver driver;
    protected BrowserMethods browserMethods;

    @BeforeMethod(alwaysRun = true)
    public void setUpBrowser() {
        WebDriver createdDriver = DriverFactory.createDriver();
        DriverManager.setDriver(createdDriver);
        driver = createdDriver;
        driver.manage().timeouts().implicitlyWait(java.time.Duration.ZERO);
        driver.manage().timeouts().pageLoadTimeout(java.time.Duration.ofSeconds(30));
        driver.manage().timeouts().scriptTimeout(java.time.Duration.ofSeconds(20));
        browserMethods = new BrowserMethods(driver,
                java.time.Duration.ofSeconds(FrameworkConfig.getInt("explicit.wait.seconds")));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownBrowser() {
        DriverManager.quitDriver();
        driver = null;
    }
}
