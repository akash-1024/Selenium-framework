package com.demo.tests;

import com.demo.framework.base.BaseTest;
import com.demo.framework.data.CsvDataProvider;
import com.demo.pages.actions.SauceDemoActions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SauceDemoTest extends BaseTest {
    private SauceDemoActions sauceDemo;

    @BeforeMethod(alwaysRun = true)
    public void initializeActions() {
        sauceDemo = new SauceDemoActions(driver, browserMethods);
    }

    @Test(priority = 1, enabled = true, description = "SauceDemo login page has the expected title")
    public void sauceDemoTitleIsCorrect() {
        sauceDemo.openLoginPage();
        Assert.assertEquals(sauceDemo.getPageTitle(), "Swag Labs");
    }

    @Test(priority = 2, enabled = true, description = "Both login fields are displayed")
    public void loginFieldsAreDisplayed() {
        sauceDemo.openLoginPage();
        Assert.assertTrue(sauceDemo.areLoginFieldsDisplayed());
    }

    @Test(priority = 3, enabled = true, description = "Password field masks entered characters")
    public void passwordFieldIsMasked() {
        sauceDemo.openLoginPage();
        Assert.assertEquals(sauceDemo.getPasswordInputType(), "password");
    }

    @Test(priority = 4, enabled = true, description = "Login button is enabled on the login page")
    public void loginButtonIsEnabled() {
        sauceDemo.openLoginPage();
        Assert.assertTrue(sauceDemo.isLoginButtonEnabled());
    }

    @Test(priority = 5, enabled = true, description = "Submitting empty login fields shows validation")
    public void emptyCredentialsShowValidationError() {
        sauceDemo.openLoginPage();
        sauceDemo.submitEmptyLogin();
        Assert.assertTrue(sauceDemo.getLoginError().contains("Username is required"));
    }

    @Test(priority = 6, enabled = true, dataProvider = "sauceCredentials", dataProviderClass = CsvDataProvider.class,
            description = "Invalid credentials show the expected error")
    public void invalidLoginShowsError(String username, String password, String expectedError) {
        sauceDemo.openLoginPage();
        sauceDemo.login(username, password);
        Assert.assertTrue(sauceDemo.getLoginError().contains(expectedError));
    }

    @Test(priority = 7, enabled = true, dataProvider = "sauceCredentials", dataProviderClass = CsvDataProvider.class,
            description = "Locked-out credentials are rejected")
    public void lockedOutUserCannotLogin(String username, String password, String expectedError) {
        sauceDemo.openLoginPage();
        sauceDemo.login(username, password);
        Assert.assertTrue(sauceDemo.getLoginError().contains(expectedError));
    }

    @Test(priority = 8, enabled = true, dataProvider = "sauceCredentials", dataProviderClass = CsvDataProvider.class,
            description = "Valid credentials open the inventory page")
    public void validUserCanLogin(String username, String password, String expectedError) {
        sauceDemo.openLoginPage();
        sauceDemo.login(username, password);
        sauceDemo.waitForInventory();
        Assert.assertTrue(driver.getCurrentUrl().contains("inventory.html"));
        Assert.assertTrue(expectedError.isEmpty());
    }

    @Test(priority = 9, enabled = true, description = "Inventory displays all six products")
    public void inventoryContainsSixProducts() {
        openInventoryAsStandardUser();
        Assert.assertEquals(sauceDemo.getInventoryItemCount(), 6);
    }

    @Test(priority = 10, enabled = true, description = "Inventory includes expected product names")
    public void inventoryContainsExpectedProducts() {
        openInventoryAsStandardUser();
        List<String> products = sauceDemo.getProductNames();
        Assert.assertTrue(products.contains("Sauce Labs Backpack"));
        Assert.assertTrue(products.contains("Sauce Labs Bike Light"));
    }

    @Test(priority = 11, enabled = true, description = "Inventory product descriptions and prices are populated")
    public void inventoryDetailsArePopulated() {
        openInventoryAsStandardUser();
        Assert.assertEquals(sauceDemo.getProductDescriptions().size(), 6);
        Assert.assertTrue(sauceDemo.getProductDescriptions().stream().allMatch(text -> !text.isBlank()));
        Assert.assertEquals(sauceDemo.getDisplayedPrices().size(), 6);
        Assert.assertTrue(sauceDemo.getDisplayedPrices().stream().allMatch(price -> price > 0));
    }

    @Test(priority = 12, enabled = true, description = "Products can be sorted by price ascending")
    public void inventoryCanBeSortedByPriceAscending() {
        openInventoryAsStandardUser();
        sauceDemo.sortPriceLowToHigh();
        List<Double> actual = sauceDemo.getDisplayedPrices();
        List<Double> expected = new ArrayList<>(actual);
        Collections.sort(expected);
        Assert.assertEquals(actual, expected);
    }

    @Test(priority = 13, enabled = true, description = "Products can be sorted by price descending")
    public void inventoryCanBeSortedByPriceDescending() {
        openInventoryAsStandardUser();
        sauceDemo.sortPriceHighToLow();
        List<Double> actual = sauceDemo.getDisplayedPrices();
        List<Double> expected = new ArrayList<>(actual);
        expected.sort(Collections.reverseOrder());
        Assert.assertEquals(actual, expected);
    }

    @Test(priority = 14, enabled = true, description = "Cart item can be added and removed")
    public void cartItemCanBeAddedAndRemoved() {
        openInventoryAsStandardUser();
        sauceDemo.addBackpackToCart();
        sauceDemo.waitForCartBadgeCount("1");
        sauceDemo.openCart();
        sauceDemo.waitForCartItemCount(1);
        Assert.assertEquals(sauceDemo.getCartItemName(), "Sauce Labs Backpack");
        sauceDemo.removeBackpackFromCart();
        sauceDemo.waitForCartItemCount(0);
        sauceDemo.waitForCartBadgeToDisappear();
        Assert.assertEquals(sauceDemo.getCartItemCount(), 0);
    }

    @Test(priority = 15, enabled = true, dependsOnMethods = "cartItemCanBeAddedAndRemoved",
            description = "Checkout can be completed successfully")
    public void checkoutCanBeCompleted() {
        openInventoryAsStandardUser();
        sauceDemo.addBackpackToCart();
        sauceDemo.waitForCartBadgeCount("1");
        sauceDemo.openCart();
        sauceDemo.waitForCartItemCount(1);
        Assert.assertEquals(sauceDemo.getCartItemCount(), 1);
        sauceDemo.completeCheckout("Taylor", "Tester", "10001");
        Assert.assertEquals(sauceDemo.getCheckoutCompleteMessage(), "Thank you for your order!");
    }

    private void openInventoryAsStandardUser() {
        sauceDemo.openLoginPage();
        sauceDemo.login("standard_user", "secret_sauce");
        sauceDemo.waitForInventory();
    }
}
