package com.automation.core.utils.reporter;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

import com.automation.core.testng.listeners.DriverListener;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;

//import com.maveric.core.cucumber.CucumberListener;


public class Report {

    private final static AtomicInteger counter = new AtomicInteger();
    private final static Logger logger = LogManager.getLogger();
    private static final DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");

    public static void log(String message) {
        Reporter.log(getTimestamp() + " : " + message);
        /*if (CucumberListener.getScenario() != null) {
            CucumberListener.getScenario().log(getTimestamp() + " : " + message);
        } else {
            Reporter.log(getTimestamp() + " : " + message);
        }*/
    }

    public static String getTimestamp() {
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    public static void logScreenshot(WebDriver mdriver, String name) {
        String path = captureScreenshot(mdriver);
        System.out.println("path"+path);
        String html = "<a target=_blank href=" + "screenshots" + path.replaceAll(" ", "%20") + ">" + name + "  </a>";
        Report.log(html);
    }
    private static String captureScreenshot(WebDriver mdriver) {
        String screenshotName = null;
        try {
            TakesScreenshot ts = (TakesScreenshot) mdriver;
            File file = ts.getScreenshotAs(OutputType.FILE);
            screenshotName = "/screenshot" + "_" + getId() + ".png";
            String screenshotPath = DriverListener.screenshotFolder + screenshotName;
            System.out.println(screenshotPath);
            FileUtils.copyFile(file, new File(screenshotPath));
        } catch (Exception e) {
            logger.error("unable to capture screenshot" + e);
        }
        return screenshotName;
    }
    private static int getId() {
        return counter.incrementAndGet();
    }
}
