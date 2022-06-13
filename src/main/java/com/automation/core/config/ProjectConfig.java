package com.automation.core.config;

import java.io.FileInputStream;
import java.util.Properties;

public class ProjectConfig {
	public static Properties prop = new Properties();
	public static String filepath;
	static {
		try {
			if (filepath == null) {
				filepath = System.getProperty("user.dir")+"//ProjectConfig.properties";							
				System.out.println("FILE PATH 2"  + filepath);
				try{
					Thread.currentThread().getContextClassLoader().getResourceAsStream(filepath);
					prop.load(new FileInputStream(filepath));
					if(!(System.getProperty("runMode")==null))
						setPropertyValue("RUN_MODE",System.getProperty("runMode"));
				} catch (Exception E) {
					System.out.println("Error occured "+E);
				}
			} else {
				System.out.println(filepath);
				filepath = filepath + "/" + "projectconfig.properties";
				prop.load(new FileInputStream(filepath));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getPropertyValue(String key) {
		return prop.getProperty(key);
	}
	
	public static void setPropertyValue(String key,String val) {
		 prop.setProperty(key,val);
	}
}
