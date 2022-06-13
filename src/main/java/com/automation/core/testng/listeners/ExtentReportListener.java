package com.automation.core.testng.listeners;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.xml.XmlSuite;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.service.ExtentService;
import com.aventstack.extentreports.testng.listener.ExtentIReporterSuiteClassListenerAdapter;
import com.aventstack.extentreports.testng.listener.commons.ExtentTestCommons;

public class ExtentReportListener extends ExtentIReporterSuiteClassListenerAdapter{

    private static final Calendar CALENDAR = Calendar.getInstance();

    private static Map<String, ExtentTest> classTestMap = new HashMap<>();

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        ExtentService.getInstance().setReportUsesManualConfiguration(true);
        ExtentService.getInstance().setAnalysisStrategy(AnalysisStrategy.SUITE);

        for (ISuite suite : suites) {
            Map<String, ISuiteResult> result = suite.getResults();

            for (ISuiteResult r : result.values()) {
                ITestContext context = r.getTestContext();

                ExtentTest suiteTest = ExtentService.getInstance().createTest(r.getTestContext().getSuite().getName());
                buildTestNodes(suiteTest, context.getFailedTests(), Status.FAIL);
                buildTestNodes(suiteTest, context.getSkippedTests(), Status.SKIP);
                buildTestNodes(suiteTest, context.getPassedTests(), Status.PASS);
            }
        }

        for (String s : Reporter.getOutput()) {
            ExtentService.getInstance().setTestRunnerOutput(s);
        }

        ExtentService.getInstance().flush();
    }

    private void buildTestNodes(ExtentTest suiteTest, IResultMap tests, Status status) {
        ExtentTest testNode;
        ExtentTest classNode;

        if (tests.size() > 0) {
            for (ITestResult result : tests.getAllResults()) {
                String className = result.getInstance().getClass().getSimpleName();

                if (classTestMap.containsKey(className)) {
                    classNode = classTestMap.get(className);
                } else {
                    classNode = suiteTest.createNode(className);
                    classTestMap.put(className, classNode);
                }

                testNode = classNode.createNode(result.getMethod().getMethodName()+Arrays.toString(result.getParameters()),
                        result.getMethod().getDescription());
                

                String[] groups = result.getMethod().getGroups();
                ExtentTestCommons.assignGroups(testNode, groups);
                
                
                if (result.getThrowable() != null) {
                    testNode.log(status, result.getThrowable());
                    
                } else {
                    testNode.log(status, "Test " + status.toString().toLowerCase() + "ed");
                    testNode.log(status, String.join("", Reporter.getOutput(result)));
                    System.out.println(">>>"+Reporter.getOutput(result));
                }

                testNode.getModel().getLogContext().getAll().forEach(x -> x.setTimestamp(getTime(result.getEndMillis())));
                testNode.getModel().setStartTime(getTime(result.getStartMillis()));
                testNode.getModel().setEndTime(getTime(result.getEndMillis()));
            }
        }
    }

    private Date getTime(long millis) {
        CALENDAR.setTimeInMillis(millis);
        return CALENDAR.getTime();
    }

}