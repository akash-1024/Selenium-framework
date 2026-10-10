package com.demo.framework.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ExtentReportManager {
    private static ExtentReports extentReports;

    private ExtentReportManager() {
    }

    public static synchronized ExtentReports getInstance() {
        if (extentReports == null) {
            try {
                Path reportPath = Path.of("reports", "extent-report.html");
                Files.createDirectories(reportPath.getParent());
                ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath.toString());
                sparkReporter.config().setDocumentTitle("Selenium Test Results");
                sparkReporter.config().setReportName("Demo Web Automation Suite");
                extentReports = new ExtentReports();
                extentReports.attachReporter(sparkReporter);
                extentReports.setSystemInfo("Framework", "Selenium + TestNG");
                extentReports.setSystemInfo("Java", System.getProperty("java.version"));
                extentReports.setSystemInfo("OS", System.getProperty("os.name"));
            } catch (Exception exception) {
                throw new IllegalStateException("Could not initialize ExtentReports", exception);
            }
        }
        return extentReports;
    }

    public static synchronized void flush() {
        if (extentReports != null) {
            extentReports.flush();
        }
    }
}
