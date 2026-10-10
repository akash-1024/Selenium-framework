package com.demo.pages.actions;

import com.demo.framework.config.FrameworkConfig;
import com.demo.framework.reporting.ExtentTestListener;
import com.demo.framework.reusablemethods.BrowserMethods;
import com.demo.pages.locators.SauceDemoLocators;
import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SauceDemoActions {
    private final WebDriver driver;
    private final BrowserMethods browser;

    public SauceDemoActions(WebDriver driver, BrowserMethods browser) {
        this.driver = driver;
        this.browser = browser;
    }

    public void openLoginPage() {
        String loginUrl = FrameworkConfig.get("saucedemo.base.url");
        ExtentTestListener.logStep("Open the SauceDemo sign-in page");
        driver.get(loginUrl);
        browser.waitForPageReady();
        browser.waitForTitle("Swag Labs");
        browser.visible(SauceDemoLocators.USERNAME);
        ExtentTestListener.logStep("The sign-in page is ready");
    }

    public void login(String username, String password) {
        ExtentTestListener.logStep("Enter the selected test account and sign in");
        browser.type(SauceDemoLocators.USERNAME, username);
        browser.type(SauceDemoLocators.PASSWORD, password);
        browser.click(SauceDemoLocators.LOGIN_BUTTON);
    }

    public String getPageTitle() {
        ExtentTestListener.logStep("Check the sign-in page title");
        browser.waitForTitle("Swag Labs");
        return driver.getTitle();
    }

    public String getPasswordInputType() {
        ExtentTestListener.logStep("Check that the password is protected while typing");
        return browser.visible(SauceDemoLocators.PASSWORD).getAttribute("type");
    }

    public boolean isLoginButtonEnabled() {
        ExtentTestListener.logStep("Check that sign-in is available");
        return browser.visible(SauceDemoLocators.LOGIN_BUTTON).isEnabled();
    }

    public void submitEmptyLogin() {
        ExtentTestListener.logStep("Submit the sign-in form without account details");
        browser.click(SauceDemoLocators.LOGIN_BUTTON);
    }

    public boolean areLoginFieldsDisplayed() {
        ExtentTestListener.logStep("Check that both account fields are displayed");
        return browser.visible(SauceDemoLocators.USERNAME).isDisplayed()
                && browser.visible(SauceDemoLocators.PASSWORD).isDisplayed();
    }

    public String getLoginError() {
        ExtentTestListener.logStep("Check the sign-in validation message");
        return browser.text(SauceDemoLocators.LOGIN_ERROR);
    }

    public void waitForInventory() {
        ExtentTestListener.logStep("Wait for the product list to load");
        browser.waitUntilUrlContains("inventory.html");
        browser.waitForPageReady();
        browser.all(SauceDemoLocators.INVENTORY_ITEMS, 6);
        ExtentTestListener.logStep("The product list is ready");
    }

    public int getInventoryItemCount() {
        ExtentTestListener.logStep("Check the number of products displayed");
        return browser.all(SauceDemoLocators.INVENTORY_ITEMS, 6).size();
    }

    public List<String> getProductNames() {
        ExtentTestListener.logStep("Check the products available in the store");
        return browser.all(SauceDemoLocators.INVENTORY_NAMES, 6).stream()
                .map(WebElement::getText).map(String::trim).toList();
    }

    public List<String> getProductDescriptions() {
        ExtentTestListener.logStep("Check that product descriptions are shown");
        return browser.all(SauceDemoLocators.INVENTORY_DESCRIPTIONS, 6).stream()
                .map(WebElement::getText).map(String::trim).toList();
    }

    public void sortPriceLowToHigh() {
        ExtentTestListener.logStep("Sort products from lowest to highest price");
        browser.selectByVisibleText(SauceDemoLocators.SORT_DROPDOWN, "Price (low to high)");
        browser.waitForCount(SauceDemoLocators.INVENTORY_PRICES, 6);
    }

    public void sortPriceHighToLow() {
        ExtentTestListener.logStep("Sort products from highest to lowest price");
        browser.selectByVisibleText(SauceDemoLocators.SORT_DROPDOWN, "Price (high to low)");
        browser.waitForCount(SauceDemoLocators.INVENTORY_PRICES, 6);
    }

    public List<Double> getDisplayedPrices() {
        ExtentTestListener.logStep("Check the displayed product prices");
        return browser.all(SauceDemoLocators.INVENTORY_PRICES, 6).stream()
                .map(WebElement::getText)
                .map(price -> Double.parseDouble(price.replace("$", "").trim()))
                .toList();
    }

    public void addBackpackToCart() {
        ExtentTestListener.logStep("Add the backpack to the cart");
        browser.click(SauceDemoLocators.ADD_BACKPACK);
    }

    public void removeBackpackFromCart() {
        ExtentTestListener.logStep("Remove the backpack from the cart");
        browser.click(SauceDemoLocators.REMOVE_BACKPACK);
    }

    public void waitForCartBadgeCount(String count) {
        ExtentTestListener.logStep("Confirm the cart contains " + count + " item(s)");
        browser.waitForText(SauceDemoLocators.CART_BADGE, count);
    }

    public void openCart() {
        ExtentTestListener.logStep("Open the shopping cart");
        browser.click(SauceDemoLocators.CART_LINK);
    }

    public int getCartItemCount() {
        ExtentTestListener.logStep("Check the number of items in the cart");
        return driver.findElements(SauceDemoLocators.CART_ITEMS).size();
    }

    public String getCartItemName() {
        ExtentTestListener.logStep("Check the product in the cart");
        return browser.text(SauceDemoLocators.CART_ITEM_NAMES);
    }

    public void waitForCartItemCount(int count) {
        ExtentTestListener.logStep("Wait for " + count + " cart item(s)");
        browser.waitForCount(SauceDemoLocators.CART_ITEMS, count);
    }

    public void waitForCartBadgeToDisappear() {
        ExtentTestListener.logStep("Confirm the cart is empty");
        browser.waitForAbsent(SauceDemoLocators.CART_BADGE);
    }

    public void completeCheckout(String firstName, String lastName, String postalCode) {
        ExtentTestListener.logStep("Start checkout");
        logAlertAccepted(browser.clickAndAcceptAlert(SauceDemoLocators.CHECKOUT_BUTTON));
        ExtentTestListener.logStep("Enter delivery details");
        logAlertAccepted(browser.typeAndAcceptAlert(SauceDemoLocators.FIRST_NAME, firstName));
        logAlertAccepted(browser.typeAndAcceptAlert(SauceDemoLocators.LAST_NAME, lastName));
        logAlertAccepted(browser.typeAndAcceptAlert(SauceDemoLocators.POSTAL_CODE, postalCode));
        ExtentTestListener.logStep("Continue to the order summary");
        logAlertAccepted(browser.clickAndAcceptAlert(SauceDemoLocators.CONTINUE_BUTTON));
        browser.waitUntilUrlContains("checkout-step-two.html");
        browser.visible(SauceDemoLocators.CHECKOUT_SUMMARY);
        ExtentTestListener.logStep("Review the order and place it");
        logAlertAccepted(browser.clickAndAcceptAlert(SauceDemoLocators.FINISH_BUTTON));
    }

    private void logAlertAccepted(boolean alertAccepted) {
        if (alertAccepted) {
            ExtentTestListener.logStep("A browser message appeared and was accepted");
        }
    }

    public String getCheckoutCompleteMessage() {
        ExtentTestListener.logStep("Check the order confirmation");
        return browser.text(SauceDemoLocators.COMPLETE_HEADER);
    }
}
