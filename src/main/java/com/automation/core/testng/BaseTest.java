package com.automation.core.testng;


import com.automation.core.config.ProjectConfig;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.testng.listener.ExtentITestListenerClassAdapter;
import com.automation.core.testng.listeners.DriverListener;
import com.automation.core.utils.reporter.Report;

import javax.management.Descriptor;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Listeners({DriverListener.class})
public class BaseTest {
	protected static WebDriver driver = null;
	
	@BeforeSuite(alwaysRun = true)
	private void beforeSuite() {

	}

	@BeforeClass(alwaysRun = true)
	private void beforeClass() {
	}

	@BeforeMethod(alwaysRun = true)
	private void beforeMethod() {

	}
	
	@BeforeTest(alwaysRun = true)
	@Parameters({ "testCaseID", "dataSheet" })
	public void launchBrowser(String testCaseID, String dataSheet) throws Exception {
		driverManager();
		driver.navigate().to(ProjectConfig.getPropertyValue("Bursa_Marketplace_URL"));
	
		Report.log("~~~~~~~ Execution Started ~~~~~~~");
	}
	
	@AfterTest(alwaysRun = true)
	public void closeBrowser() {
		Report.log("~~~~~~~ Execution Completed ~~~~~~~");
		driver.quit();
	}

	public ChromeOptions chromeOptions()
	{
		Map prefs = new HashMap();
		prefs.put("profile.default_content_settings.cookies", 2);
		ChromeOptions options = new ChromeOptions();
		return options.setExperimentalOption("prefs", prefs);
	}
	public void driverManager()
	{
		// WebDriverManager.chromeDriver().setUp(); // Can be used to manage browser and Driver versions
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"/src/test/resources/Drivers/chromedriver.exe");
		driver = new ChromeDriver(chromeOptions());
		driver.manage().window().maximize();
	}


	public DesiredCapabilities initiateAndroid(){
		DesiredCapabilities dc = new DesiredCapabilities();
		dc.setCapability(MobileCapabilityType.AUTOMATION_NAME, ProjectConfig.getPropertyValue("Android_Real_AUTOMATION_NAME"));
		dc.setCapability(MobileCapabilityType.PLATFORM_NAME, ProjectConfig.getPropertyValue("Android_Real_PLATFORM_NAME"));
		dc.setCapability(MobileCapabilityType.PLATFORM_VERSION, ProjectConfig.getPropertyValue("Android_Real_PLATFORM_VERSION"));
		dc.setCapability(MobileCapabilityType.DEVICE_NAME, ProjectConfig.getPropertyValue("Android_Real_DEVICE_NAME"));
		dc.setCapability(MobileCapabilityType.UDID, ProjectConfig.getPropertyValue("Android_Real_UDID"));
		dc.setCapability(MobileCapabilityType.APP,System.getProperty("user.dir")+ProjectConfig.getPropertyValue("Android_Real_APP"));
		dc.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, ProjectConfig.getPropertyValue("APP_PACKAGE"));
		dc.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ProjectConfig.getPropertyValue("APP_ACTIVITY"));
		return dc;
	}

	public DesiredCapabilities initiateIOS(){
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(IOSMobileCapabilityType.AUTO_ACCEPT_ALERTS, Boolean.TRUE);
		capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, ProjectConfig.getPropertyValue("IOS_Real_AUTOMATION_NAME"));
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, ProjectConfig.getPropertyValue("IOS_Real_PLATFORM_NAME"));
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, ProjectConfig.getPropertyValue("IOS_Real_PLATFORM_VERSION"));
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, ProjectConfig.getPropertyValue("IOS_Real_DEVICE_NAME"));
		capabilities.setCapability(MobileCapabilityType.UDID, ProjectConfig.getPropertyValue("IOS_Real_UDID"));
		capabilities.setCapability(MobileCapabilityType.APP,
				System.getProperty("user.dir") + "/" + ProjectConfig.getPropertyValue("IOS_Real_APP"));
		capabilities.setCapability(IOSMobileCapabilityType.BUNDLE_ID, ProjectConfig.getPropertyValue("BUNDLE_ID"));
		return capabilities;
	}
}
