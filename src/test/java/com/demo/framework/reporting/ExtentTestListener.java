package com.demo.framework.reporting;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.demo.framework.driver.DriverManager;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Base64;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.IExecutionListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExtentTestListener implements ITestListener, IExecutionListener {
    private static final ThreadLocal<ExtentTest> CURRENT_TEST = new ThreadLocal<>();

    @Override
    public void onExecutionStart() {
        ExtentReportManager.getInstance();
    }

    @Override
    public void onExecutionFinish() {
        ExtentReportManager.flush();
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = ExtentReportManager.getInstance().createTest(
                result.getMethod().getDescription() == null
                        ? result.getMethod().getMethodName()
                        : result.getMethod().getDescription());
        test.assignCategory("Store shopping");
        CURRENT_TEST.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        currentTest().pass("Passed: the expected result was confirmed.");
        CURRENT_TEST.remove();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = currentTest();
        test.fail(describeFailure(result.getThrowable()));
        attachScreenshot(test, result);
        CURRENT_TEST.remove();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = currentTest();
        test.skip("Skipped: this test was not run, possibly because a prerequisite did not pass.");
        CURRENT_TEST.remove();
    }

    private static ExtentTest currentTest() {
        ExtentTest test = CURRENT_TEST.get();
        if (test == null) {
            throw new IllegalStateException("No Extent test is active for this test thread");
        }
        return test;
    }

    /** Adds a timestamped action entry to the currently running test in the HTML report. */
    public static void logStep(String message) {
        ExtentTest test = CURRENT_TEST.get();
        if (test != null) {
            test.info(message);
        }
    }

    private static String describeFailure(Throwable failure) {
        if (failure instanceof AssertionError) {
            return "Failed: the actual result did not match the expected result.";
        }
        if (failure instanceof WebDriverException) {
            return "Failed: the browser could not complete an expected action ("
                    + failure.getClass().getSimpleName() + ").";
        }
        return "Failed: an unexpected error occurred ("
                + (failure == null ? "unknown error" : failure.getClass().getSimpleName()) + ").";
    }

    private static void attachScreenshot(ExtentTest test, ITestResult result) {
        try {
            WebDriver driver = DriverManager.getDriver();
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Path screenshotDirectory = Path.of("reports", "screenshots");
            Files.createDirectories(screenshotDirectory);
            String testName = (result.getTestClass().getName() + "-" + result.getMethod().getMethodName())
                    .replaceAll("[^a-zA-Z0-9._-]", "_");
            Path screenshotPath = screenshotDirectory.resolve(testName + "-" + Instant.now().toEpochMilli() + ".png");
            Files.write(screenshotPath, screenshot);
            test.fail("Failure screenshot", MediaEntityBuilder.createScreenCaptureFromBase64String(
                    Base64.getEncoder().encodeToString(screenshot)).build());
        } catch (Exception screenshotException) {
            test.warning("A screenshot could not be captured for this failure.");
        }
    }
}
