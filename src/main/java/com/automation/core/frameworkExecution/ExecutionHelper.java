package com.automation.core.frameworkExecution;

import com.codoid.products.fillo.Recordset;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ExecutionHelper {

	public static String createSuites(Recordset recordset, String sheetName) {
		XmlSuite suite = new XmlSuite();
		ArrayList<XmlTest> alXmlTest = new ArrayList<XmlTest>();
		XmlTest xmlTest = new XmlTest();
		try {
			System.out.println("----- *********** -----");
			while (recordset.next()) {
				String includedMethods = "";
				includedMethods = recordset.getField("TestMethod");
				String appName = recordset.getField("ApplicationName");
				String testClassName = recordset.getField("TestClassName");
				String TestCaseID = recordset.getField("TestCaseID");
				suite.setName(appName);
				suite.setVerbose(2);
			    System.out.println("Sheet Name ----> "+sheetName);
				System.out.println("TestClass Name : " + testClassName);
				System.out.println("Included Method : " + includedMethods);
				System.out.println("Testcase Number : " + TestCaseID);
				System.out.println("----- *********** -----");

				xmlTest = getTest(suite,
						testClassName,
						includedMethods,
						TestCaseID,sheetName);
				alXmlTest.add(xmlTest);
			}

			List<XmlTest> disntictList = alXmlTest.stream().distinct().collect(Collectors.toList());
		} catch (Exception e) {
			System.out.println("Exception Occurred while creating the xml file => "+e.getMessage());
		} finally {
			recordset.close();
		}
		return writeTestNGFile(suite);
	}

	public static XmlTest getTest(XmlSuite suite, String... strings) {
		List<String> listValue = Arrays.asList(strings);
		String testClassName = listValue.get(0);
		String includedMethods = listValue.get(1);
		String TestcaseID = listValue.get(2);
		String sheetName = listValue.get(3);
		/*Test tag*/
		XmlTest test1 = new XmlTest(suite);
		//------------Arun
		//Setting the xTestNG xml Test name and Parameters
		HashMap<String, String> hmParam = new HashMap<String, String>();
		hmParam.put("testCaseID",TestcaseID);
		hmParam.put("dataSheet",System.getProperty("dataSheet"));
		test1.setParameters(hmParam);
		//------------Arun
		/*Class tag*/
		List<XmlClass> lstClasses = new ArrayList<XmlClass>();
		XmlClass xmlClass = new XmlClass();
		if(sheetName.equals("WEB_APP"))
		{
			xmlClass.setName("com.webapp.tests." + testClassName);
		}
		else if(sheetName.equals("ANDROID_APP"))
		{
			//xmlClass.setName("com.webapp.tests." + testClassName);
		}else if(sheetName.equals("IOS_APP"))
		{
		//	xmlClass.setName("com.webapp.tests." + testClassName);
		}
		/*Exclude tag*/
		/*List<String> lstxmlExclude = new ArrayList<String>();
        for (String str : excludedMethods.split("!:!")) {
            lstxmlExclude.add(str);
        }
        xmlClass.setExcludedMethods(lstxmlExclude);*/
		/*Include tag*/
		List<XmlInclude> lstxmlInclude = new ArrayList<XmlInclude>();
		XmlInclude xmlInclude = null;
		for (String str : includedMethods.split("!:!")) {
			xmlInclude = new XmlInclude(str);
			lstxmlInclude.add(xmlInclude);
		}
		xmlClass.setIncludedMethods(lstxmlInclude);
		lstClasses.add(xmlClass);
		test1.setClassNames(lstClasses);
		return test1;
	}

	public static String writeTestNGFile(XmlSuite suite) {
		String xmlPath = System.getProperty("user.dir") + "/Execution/executionUpdate.xml";
		try {
			FileWriter writer = new FileWriter(new File(xmlPath));
			BufferedWriter bw = new BufferedWriter(writer);
			bw.write(suite.toXml());
			bw.close();
		} catch (IOException e) {
			System.out.println("Exception Occurred while creating the xml file => "+e.getMessage());
		}
		return xmlPath;
	}
}