package com.automation.core.testng;

import org.testng.annotations.Listeners;

import com.automation.core.testng.listeners.ExtentReportListener;

@Listeners(ExtentReportListener.class)
public class TestNGBaseTest{

}