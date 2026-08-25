package com.demo.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DemoWebsiteTest {

    private WebDriver driver;

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        // Run in headless mode so browser opens silently inside Jenkins server
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
    }

    // --- SAUCEDEMO TESTS ---

    @Test(priority = 1)
    public void test01_VerifySauceDemoTitle() {
        driver.get("https://www.saucedemo.com/");
        Assert.assertEquals(driver.getTitle(), "Swag Labs");
    }

    @Test(priority = 2)
    public void test02_VerifyLoginFieldsPresent() {
        driver.get("https://www.saucedemo.com/");
        Assert.assertTrue(driver.findElement(By.id("user-name")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.id("password")).isDisplayed());
    }

    @Test(priority = 3)
    public void test03_InvalidLoginErrorMessage() {
        driver.get("https://www.saucedemo.com/");
        driver.findElement(By.id("user-name")).sendKeys("invalid_user");
        driver.findElement(By.id("password")).sendKeys("wrong_pass");
        driver.findElement(By.id("login-button")).click();
        WebElement error = driver.findElement(By.xpath("//h3[@data-test='error']"));
        Assert.assertTrue(error.getText().contains("Username and password do not match"));
    }

    @Test(priority = 4)
    public void test04_ValidLoginSuccess() {
        driver.get("https://www.saucedemo.com/");
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        Assert.assertTrue(driver.getCurrentUrl().contains("inventory.html"));
    }

    @Test(priority = 5)
    public void test05_VerifyProductCount() {
        // Depends on logged-in state from Test 4
        int productCount = driver.findElements(By.className("inventory_item")).size();
        Assert.assertEquals(productCount, 6);
    }

    @Test(priority = 6)
    public void test06_AddToCart() {
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        WebElement badge = driver.findElement(By.className("shopping_cart_badge"));
        Assert.assertEquals(badge.getText(), "1");
    }

    // --- HEROKUAPP TESTS ---

    @Test(priority = 7)
    public void test07_CheckHerokuPageHeader() {
        driver.get("https://the-internet.herokuapp.com/");
        WebElement heading = driver.findElement(By.className("heading"));
        Assert.assertEquals(heading.getText(), "Welcome to the-internet");
    }

    @Test(priority = 8)
    public void test08_CheckAddRemoveElements() {
        driver.get("https://the-internet.herokuapp.com/add_remove_elements/");
        driver.findElement(By.xpath("//button[text()='Add Element']")).click();
        WebElement deleteBtn = driver.findElement(By.className("added-manually"));
        Assert.assertTrue(deleteBtn.isDisplayed());
    }

    @Test(priority = 9)
    public void test09_CheckCheckBoxes() {
        driver.get("https://the-internet.herokuapp.com/checkboxes");
        WebElement checkbox1 = driver.findElement(By.xpath("//form[@id='checkboxes']/input[1]"));
        Assert.assertFalse(checkbox1.isSelected());
    }

    @Test(priority = 10)
    public void test10_CheckDropdownDefaultValue() {
        driver.get("https://the-internet.herokuapp.com/dropdown");
        WebElement dropdown = driver.findElement(By.id("dropdown"));
        Assert.assertTrue(dropdown.isDisplayed());
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}