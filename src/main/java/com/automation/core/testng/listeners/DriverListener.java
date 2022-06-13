package com.automation.core.testng.listeners;

import static com.automation.core.utils.GenericUtils.getTimeStamp;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.IRetryAnalyzer;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.xml.XmlSuite;

import com.automation.core.config.ConfigProperties;
/*import com.maveric.core.cucumber.CucumberListener;
import com.maveric.core.cucumber.reporter.CucumberReporter;
import com.maveric.core.cucumber.reporter.ExtentBDDReportGenerator;*/
//import com.maveric.core.driver.BrowserStackClient;
import com.automation.core.driver.Driver;
import com.automation.core.driver.DriverFactory;
//import com.maveric.core.driver.SauceClient;
import com.automation.core.testng.reporter.CustomReporter;
import com.automation.core.utils.reporter.Report;
import com.automation.core.utils.ssh.BatchUtils;
import com.automation.core.utils.web.WebActions;

//import io.restassured.RestAssured;

public class DriverListener implements ISuiteListener, ITestListener, IInvokedMethodListener {
    private static final Logger logger = LogManager.getLogger();
	public static String reportFolder;
	public static String screenshotFolder;
	public String includedGroups;
	public String excludedGroups;
	public static boolean isCucumber = false;
    private DriverFactory driverFactory;
    public static ThreadLocal<String> tests = new ThreadLocal<>();

    @Override
    public void onStart(ISuite suite) {
    	reportFolder = "./reports/test-output-" + getTimeStamp();
		File reportsFolder = new File(reportFolder);
		File screenShotsFolder = new File(reportsFolder + "/screenshots");
		reportsFolder.mkdirs();

		logger.info("reports folder created successfully :" + reportFolder);
		screenShotsFolder.mkdirs();
		screenshotFolder = reportsFolder + "/screenshots";
		CustomReporter.createCustomReport(reportFolder + "/CustomReport.html");
		/*try {
			FileUtils.copyFile(new File("./lib/MavericLogo.png"), new File(screenshotFolder + "/MavericLogo.png"));
			FileUtils.copyFile(new File("./lib/Background.png"), new File(screenshotFolder + "/Background.png"));

		} catch (IOException e) {
			e.printStackTrace();
		}*/
		 setThreadCount(suite);
	     //RestAssured.filters(new RestLogFilter());

		includedGroups = StringUtils.join(suite.getXmlSuite().getIncludedGroups(), ",");
		excludedGroups = StringUtils.join(suite.getXmlSuite().getExcludedGroups(), ",");
		
    	
    	//DriverFactory.downloadDriver();
        //driverFactory = new DriverFactory();    	
    	
        /*BrowserStackClient.startBrowserStackLocal();
        BrowserStackClient.uploadFile();
        SauceClient.uploadFile();*/
    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
    	if (method.isTestMethod() && ConfigProperties.BROWSER_INITIATE.getBoolean()) {
            tests.set(method.getTestMethod().getMethodName());
            
            System.out.println("$$$$ This is the test method name : " + method.getTestMethod().getMethodName());
           
      /*      driverFactory.mobdriverSetup();
            driverFactory.createRemoteDriverUpdated();
            driverFactory.driverSetup();*/
            logger.info("Driver Setup Completed");
        }
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult result) {
       if (method.isTestMethod() && ConfigProperties.BROWSER_INITIATE.getBoolean()) {
            /*if ((!result.isSuccess()) && CucumberListener.getScenario() == null) {
                takeFailureScreenshot();
            }*/
            Driver.quitDriver();
            logger.info("Driver Quit Completed");
        }
       

		if (method.isTestMethod())
			if (!isCucumber) {
				String testExecTime = convert(result.getEndMillis() - result.getStartMillis());
				if (!result.isSuccess()) {
					StringWriter sw = new StringWriter();
					result.getThrowable().printStackTrace(new PrintWriter(sw));
					String exceptionAsString = sw.toString();
					Report.log(exceptionAsString);
				}
				String logs = String.join("<br>", Reporter.getOutput(result));
				String testName = result.getMethod().getMethodName();
				if (result.isSuccess()) {
					CustomReporter.appendPass(logs, testName, testExecTime);
				} else {
					/*int currentReteryCount = RetryAnalyzer.getCount();
					if (currentReteryCount > 0) {
						testName = testName + "_Retry_" + currentReteryCount;
					}*/
					CustomReporter.appendFail(logs, testName, testExecTime);
				}

			}
       
    }

    @Override
    public void onFinish(ISuite suite) {
    	if(ConfigProperties.BATCH_TRIGGER.getBoolean()) {
    		try {
				BatchUtils.triggerBatchforQTP();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	/*if(isCucumber) {
    		ExtentBDDReportGenerator.generateCucumberReport();
    	}*/
        /*BrowserStackClient.stopBrowserStackLocal();
        BrowserStackClient.deleteFiles();*/
    }

    private void takeFailureScreenshot() {
        if (!Driver.isWebDriverEmpty() || !Driver.isAppiumDriverEmpty()) {
            WebActions actions = new WebActions();
            actions.logScreenshot("Failure Screenshot");
        }
    }

	@Override
	public void onTestStart(ITestResult result) {
		
		   IRetryAnalyzer curRetryAnalyzer = getRetryAnalyzer(result);
	        if (curRetryAnalyzer == null) {
	            result.getMethod().setRetryAnalyzer(curRetryAnalyzer);
	        }
		
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTestFailure(ITestResult result) {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart(ITestContext context) {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFinish(ITestContext ctx) {
		
		long start = ctx.getStartDate().getTime();
		long end = ctx.getEndDate().getTime();
		String executionTime = convert(end - start);
		String endDate = ctx.getEndDate().toString();
		if (!isCucumber) {
			int passedCount = ctx.getPassedTests().size();
			int failedCount = ctx.getFailedTests().size();
			CustomReporter.updateValues(passedCount, failedCount, endDate, executionTime,
					includedGroups + "\n" + excludedGroups);
		} else {

			/*CucumberReporter.generateCucumberReport();
			CustomReporter.updateValues(CucumberReporter.passedFeatures,
					CucumberReporter.totalFeatures - CucumberReporter.passedFeatures, endDate, executionTime,
					CucumberReporter.featureTags);*/

		}
		
	}
	public String convert(long miliSeconds) {
		int hrs = (int) TimeUnit.MILLISECONDS.toHours(miliSeconds) % 24;
		int min = (int) TimeUnit.MILLISECONDS.toMinutes(miliSeconds) % 60;
		int sec = (int) TimeUnit.MILLISECONDS.toSeconds(miliSeconds) % 60;
		return String.format("%02d:%02d:%02d", hrs, min, sec);
	}
    public void setThreadCount(ISuite suite) {
        if (ConfigProperties.THREAD_COUNT.getInt() >= 1) {
            int count = ConfigProperties.THREAD_COUNT.getInt();
            suite.getXmlSuite().setThreadCount(count);
            logger.info("Thread Count : " + count);
        }

        if (ConfigProperties.DATAPROVIDER_COUNT.getInt() >= 1) {
            int count = ConfigProperties.DATAPROVIDER_COUNT.getInt();
            suite.getXmlSuite().setThreadCount(ConfigProperties.DATAPROVIDER_COUNT.getInt());
            logger.info("Data Provider Count : " + count);
        }

        if (suite.getXmlSuite().getParallel() == XmlSuite.ParallelMode.NONE) {
            suite.getXmlSuite().setParallel(XmlSuite.ParallelMode.METHODS);
            logger.info("Parallel Type : Methods");
        }

    }


    private RetryAnalyzer getRetryAnalyzer(ITestResult result) {
        RetryAnalyzer retryAnalyzer = null;
        IRetryAnalyzer iRetry = result.getMethod().getRetryAnalyzer();
        if (iRetry instanceof RetryAnalyzer) {
            retryAnalyzer = (RetryAnalyzer) iRetry;
        }
        return retryAnalyzer;
    }
}
