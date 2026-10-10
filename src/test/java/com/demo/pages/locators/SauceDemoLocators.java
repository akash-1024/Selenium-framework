package com.demo.pages.locators;

import org.openqa.selenium.By;

public final class SauceDemoLocators {
    private SauceDemoLocators() {
    }

    public static final By USERNAME = By.id("user-name");
    public static final By PASSWORD = By.id("password");
    public static final By LOGIN_BUTTON = By.id("login-button");
    public static final By LOGIN_ERROR = By.cssSelector("[data-test='error']");
    public static final By INVENTORY_ITEMS = By.className("inventory_item");
    public static final By INVENTORY_NAMES = By.className("inventory_item_name");
    public static final By INVENTORY_DESCRIPTIONS = By.className("inventory_item_desc");
    public static final By INVENTORY_PRICES = By.className("inventory_item_price");
    public static final By SORT_DROPDOWN = By.className("product_sort_container");
    public static final By CART_BADGE = By.className("shopping_cart_badge");
    public static final By CART_LINK = By.className("shopping_cart_link");
    public static final By CART_ITEMS = By.className("cart_item");
    public static final By CART_ITEM_NAMES = By.cssSelector(".cart_item .inventory_item_name");
    public static final By REMOVE_BACKPACK = By.id("remove-sauce-labs-backpack");
    public static final By ADD_BACKPACK = By.id("add-to-cart-sauce-labs-backpack");
    public static final By CHECKOUT_BUTTON = By.id("checkout");
    public static final By FIRST_NAME = By.id("first-name");
    public static final By LAST_NAME = By.id("last-name");
    public static final By POSTAL_CODE = By.id("postal-code");
    public static final By CONTINUE_BUTTON = By.id("continue");
    public static final By CHECKOUT_SUMMARY = By.className("summary_info");
    public static final By FINISH_BUTTON = By.id("finish");
    public static final By COMPLETE_HEADER = By.className("complete-header");
}
