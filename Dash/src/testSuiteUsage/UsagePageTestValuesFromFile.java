package testSuiteUsage;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.ReadFilesHelper;
import helperObjects.UsageHelper;
import usage.UsageKPITilesActions;


public class UsagePageTestValuesFromFile extends BaseClass{

	
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		login();
		CommonTestStepActions.switchToContentFrame();
		// Initialization of month selector - we may want to call this method from somewhere else, or just when the month selector is needed
		// I've put it here to make sure that it gets initialized and that will not error 
		//CommonTestStepActions.initializeMonthSelector();
	}
	
	
	@Test
	public static void UsagePageTestValuesFromFileTest() throws Exception
	{
		
		String path = "D:/Documents/CMD Dashboard/CreateFilesProgram/";
		String file = "AT&T Mobility.txt";
		String completePath = path + file;
		
		
		CommonTestStepActions.GoToUsagePageDetailedWait();
			
		// #1 Select Vendor View 
		UsageHelper.selectVendorView();
		
		
		// #2 Read data from file
		String[][] valuesfromFile = ReadFilesHelper.getDataFromSpreadsheet(completePath);
		
		
		// #3 Select only one vendor
		CommonTestStepActions.UnSelectAllVendors();
		CommonTestStepActions.selectOneVendor("AT&T Mobility");
		
		String[] oneMonthData = valuesfromFile[0]; 
		String year =  oneMonthData[0];
		String month = Integer.toString((Integer.parseInt(oneMonthData[1]) - 1));
		
		String monthYear = CommonTestStepActions.convertMonthNumberToName(month, year);
		System.out.println("monthYear: " + monthYear);
		
		// #4 Select month on month/year selector
		CommonTestStepActions.selectMonthYearPulldown(monthYear);
		
		Thread.sleep(2000);
		
		String domesticVoiceUsage = oneMonthData[2];
		String domesticVoiceOverageUsage = oneMonthData[3];
		String domesticMessagesUsage = oneMonthData[4];
		String domesticDataUsage = oneMonthData[5];
		String roamingDataUsage = oneMonthData[7];
						
		// #5 Compare the values displayed on the KPIs to the values from spreadsheet
		UsageKPITilesActions.verifyKPItileValues(domesticVoiceUsage, domesticVoiceOverageUsage, domesticMessagesUsage, domesticDataUsage, roamingDataUsage);
		
	}
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "'Usage Trending - Usage by Vendor' test finished. Select OK to close browser.");
		driver.close();
		driver.quit();
	}
	
}
