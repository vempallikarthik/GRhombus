package com.webapp.commonDef;

import com.automation.core.config.ConfigProperties;
import com.automation.core.utils.reporter.Report;
import com.webapp.tests.AllTests;

import de.retest.web.selenium.UnbreakableDriver;
import freemarker.core.ReturnInstruction.Return;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.formula.functions.T;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CommonDef extends AllTests{


	private final static Logger logger = LogManager.getLogger();
	public static WebDriver getCurrentDriver()
	{
		return driver;
	}

	public static void implicitWait()
	{
		driver.manage().timeouts().implicitlyWait(ConfigProperties.WAIT_TIMEOUT.getInt(), TimeUnit.SECONDS);
	}

	public static void navigateBack()
	{
		driver.navigate().back();
	}
	// Method to check element displayed or not returns boolean
	public static boolean isElementDisplay(By by)
	{
		try {
			implicitWait();
			Assert.assertTrue(driver.findElement(by).isDisplayed());
			return true;
		} catch (Exception e) {

			Assert.fail("Element not present"+by);
			Report.logScreenshot(driver,"~~~~~~Exception occurred~~~~~~");
			return false;
		}

	}

	public static void click(By by) throws IOException {
		try {
			implicitWait();
			WebElement element = getCurrentDriver().findElement(by);
			new WebDriverWait(driver, 50).until(ExpectedConditions.elementToBeClickable(element));
			element.click();
			Report.logScreenshot(driver,"Element clicked Successfully -"+by);
		} catch (Exception e) {
			Assert.fail("Element not Clicked"+ by);
			Report.logScreenshot(driver,"~~~~~~Exception occurred~~~~~~");
		}

	}
	public static void clear(By by) throws IOException {
		try {
			implicitWait();
			WebElement element = getCurrentDriver().findElement(by);
			new WebDriverWait(driver, 50).until(ExpectedConditions.elementToBeClickable(element));
			element.clear();
			Report.logScreenshot(driver,"Element cleared Successfully -"+by);
		} catch (Exception e) {
			Assert.fail("Element not Cleared"+ by);
			Report.logScreenshot(driver,"~~~~~~Exception occurred~~~~~~");
		}

	}


	public static String getText(By by) throws IOException {
		String text = null;
		try {
			text = getCurrentDriver().findElement(by).getText();
		} catch (Exception e) {
			Assert.fail("LocatorNotFound "+by);
			Report.logScreenshot(driver,"~~~~~~Exception occurred~~~~~~");
		}

		return text;
	}
	public static void sleep(long time) throws IOException {

		try {
			Thread.sleep(time);
		} catch (Exception e) {
			Report.logScreenshot(driver,"~~~~~~Exception occurred~~~~~~");
		}

	}


	public static void sendKeys(By by, String keysToSend) throws IOException {
		try {
			implicitWait();
			click(by);
			clear(by);
			getCurrentDriver().findElement(by).sendKeys(keysToSend);
			Report.logScreenshot(driver,"Value "+keysToSend+" Successfully on -"+by);

		} catch (Exception E) {
			Assert.fail("Value : "+keysToSend+" - Not send into "+by);

		}
	}

	public static void sendKeyswithEnter(By by, String keysToSend) throws IOException {
		try {

			Actions action = new Actions(getCurrentDriver());
			getCurrentDriver().findElement(by).sendKeys(keysToSend);
			action.sendKeys(Keys.ENTER).build().perform();
			Report.logScreenshot(driver,"Value "+keysToSend+" Successfully on -"+by);
		} catch (Exception E) {
			Assert.fail("Value : "+keysToSend+" - Not send into "+by);
			Report.logScreenshot(driver,"~~~~~~Exception occurred~~~~~~");
		}
	}

	public static void waitForPageLoad(By elementToBeFound) {


		try {
			WebDriverWait wait = new WebDriverWait(driver, 50);
			wait.until(ExpectedConditions.elementToBeClickable((elementToBeFound)));
		} catch (Exception E) {
			Assert.fail( "Page Load Condition failed. Continuing with test");
			Report.logScreenshot(driver,"~~~~~~Exception occurred~~~~~~");
		}
	}

	public static void stringCompare(By by,String text)  throws IOException {
		try {
			Assert.assertEquals(getText(by),text);
			Report.logScreenshot(driver,"WebElementFound" +  by);
		} catch (Exception e) {
			Assert.fail("Assert not equal "+ by);
			Report.logScreenshot(driver,"TextMissMatch-- Expected :"+text + "Actual :" +getText(by)  +  by);
		}
	}

	public static WebElement findElement(By by)  throws IOException {
		try {

			Report.logScreenshot(driver,"WebElementFound" +  by);
			return getCurrentDriver().findElement(by);

		} catch (Exception e) {
			Assert.fail("WebElementNotFound "+ by);
			return null;
		}
	}
	
	public static List<WebElement> findElements(By by)  throws IOException {
		try {

			//Report.logScreenshot(driver,"WebElementFound" +  by);
			return getCurrentDriver().findElements(by);

		} catch (Exception e) {
			Assert.fail("WebElementNotFound "+ by);
			return null;
		}
	}

	public static void clickXpath(String xpath) throws IOException {
		try {
			implicitWait();
			WebElement element = getCurrentDriver().findElement(MobileBy.xpath(xpath));
			new WebDriverWait(driver, 50).until(ExpectedConditions.elementToBeClickable(element));
			element.click();
			Report.logScreenshot(driver,"Element clicked Successfully -"+xpath);
		} catch (Exception e) {
			Assert.fail("Element not Clicked"+ xpath);
			Report.logScreenshot(driver,"~~~~~~Exception occurred~~~~~~");
		}

	}


	public static void logScreenShot(String message){
		try {
			Report.logScreenshot(driver,message);
		} catch (Exception E) {
			Report.log("Failed to take screenshot");
		}
	}
	public static void logs(String message){
		try {
			Report.log(message);
		} catch (Exception E) {
			Report.log("Failed to write logs");
		}
	}
	
	
	public static By replaceXXXXFromString(By by,String replaceWith) {

		String s= by.toString().substring(by.toString().lastIndexOf(": ") + 2, by.toString().length());
		return By.xpath(s.replace("XXXX", replaceWith));

	}

	public  static boolean moveToElement(By element) {
		try {
        WebElement webElement =getCurrentDriver().findElement(element);
        Assert.assertNotNull(webElement);
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) getCurrentDriver();
        javascriptExecutor.executeScript("arguments[0].scrollIntoView();", webElement);
        return true;
		}catch (Exception E) {
			Report.log("Failed to move to elament"+element);
			return false;
		}

    }
	 public static String rightPadding(String input)
	    {
	  
	        String result= String.format("%" + (-10) + "s", input).replaceAll("0.000", "  -  ");
	  
	        // Return the resultant string
	        return result;
	    }


}
