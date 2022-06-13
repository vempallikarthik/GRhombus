package com.automation.core.frameworkExecution;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Recordset;
import com.automation.core.utils.data.ExcelDataReader;

public class TestSuiteCreator {

    public static void main(String[] args) throws FilloException {
        System.out.println("----- Test Suite Creation started -----");
        Recordset result = null;
        String filePath = System.getProperty("user.dir") + "/Execution/RunManager.xls";

        String queryDataSheet = ("select * from MasterTestExecutor where Run='YES'");
        System.out.println(queryDataSheet);
        result = ExcelDataReader.getDataRecordSet(filePath, queryDataSheet);
        while (result.next()) {
            String sheetName = result.getField("ApplicationName");
            System.setProperty("dataSheet",result.getField("DataSheet"));
            String query = "select * from "+sheetName+" where Run='YES'";
            Recordset rs= ExcelDataReader.getDataRecordSet(filePath, query);
            ExecutionHelper.createSuites(rs,sheetName);
        }
        System.out.println("----- Test Suite Creation Completed -----");
    }
}
