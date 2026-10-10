package com.demo.framework.driver;

import com.demo.framework.config.FrameworkConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public final class DriverFactory {
    private DriverFactory() {
    }

    public static WebDriver createDriver() {
        String browser = FrameworkConfig.get("browser").toLowerCase();
        if (!browser.equals("chrome") && !browser.equals("chromeheadless")) {
            throw new IllegalArgumentException("Unsupported browser '" + browser + "'. Configure browser=chrome.");
        }

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT);
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--window-size=1440,1000");
        if (FrameworkConfig.getBoolean("headless") || browser.equals("chromeheadless")) {
            options.addArguments("--headless=new");
        }
        return new ChromeDriver(options);
    }
}
