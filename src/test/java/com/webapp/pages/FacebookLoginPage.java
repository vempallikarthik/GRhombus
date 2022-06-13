package com.webapp.pages;

import org.openqa.selenium.By;

import com.webapp.commonDef.CommonDef;

import io.appium.java_client.MobileBy;

public class FacebookLoginPage {
	
	static By emailBY =    By.xpath("//input[@id='email' ]");
	static By passwordBy =  By.xpath("//input[@id='pass' ]");
	static By logInBtnBy =  By.xpath("//input[@value='Log In']");
	static By cancelBtnBy =  By.xpath("//span[text()='Cancel']");
	
	
	public static boolean loginFB(String email,String pass)
	{
		try {
			CommonDef.sendKeys(emailBY, email);
			CommonDef.sendKeys(passwordBy, pass);
			CommonDef.click(logInBtnBy);
			CommonDef.getCurrentDriver().findElement(cancelBtnBy).click();
			
			return true;
		} catch (Exception e) {
			CommonDef.logs(e.getMessage());
			return false;
		}
		
	}

}
