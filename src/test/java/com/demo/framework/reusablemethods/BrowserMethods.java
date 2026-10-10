package com.demo.framework.reusablemethods;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.Alert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/** Shared explicit-wait operations used by page action classes. */
public class BrowserMethods {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public BrowserMethods(WebDriver driver, Duration timeout) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, timeout);
    }

    public WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void waitForPageReady() {
        wait.until(driver -> "complete".equals(
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("return document.readyState")));
    }

    public void waitForTitle(String expectedTitle) {
        wait.until(ExpectedConditions.titleIs(expectedTitle));
    }

    public void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    public boolean clickAndAcceptAlert(By locator) {
        boolean alertAccepted = acceptAlertIfPresent();
        try {
            click(locator);
        } catch (UnhandledAlertException exception) {
            alertAccepted = true;
            acceptAlertIfPresent();
        }
        return acceptAlertIfPresent() || alertAccepted;
    }

    public boolean acceptAlertIfPresent() {
        try {
            Alert alert = driver.switchTo().alert();
            alert.accept();
            return true;
        } catch (NoAlertPresentException exception) {
            return false;
        }
    }

    public void type(By locator, String value) {
        WebElement element = visible(locator);
        element.clear();
        element.sendKeys(value);
    }

    public String text(By locator) {
        return visible(locator).getText().trim();
    }

    public List<WebElement> all(By locator, int expectedCount) {
        return wait.until(driver -> {
            List<WebElement> foundElements = driver.findElements(locator);
            return foundElements.size() == expectedCount ? foundElements : null;
        });
    }

    public void waitForCount(By locator, int expectedCount) {
        wait.until(driver -> driver.findElements(locator).size() == expectedCount);
    }

    public void waitForAbsent(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public void waitForText(By locator, String expectedText) {
        wait.until(ExpectedConditions.textToBe(locator, expectedText));
    }

    public void selectByVisibleText(By locator, String option) {
        new Select(visible(locator)).selectByVisibleText(option);
    }

    public void waitUntilUrlContains(String fragment) {
        wait.until(ExpectedConditions.urlContains(fragment));
    }

}
