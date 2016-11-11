package testSuiteUsage;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
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
		
		String vendor = "";
		//vendor = "AT&T Mobility";
		//vendor = "Etisalat";
		//vendor = "O2 UK";
		vendor = "Rogers";

		
		String path = "D:/Documents/CMD Dashboard/CreateFilesProgram/ ";
		String file = vendor + ".txt";
		String completePath = path + file;
		
		System.out.println("Path: " + completePath);
		
		CommonTestStepActions.GoToUsagePageDetailedWait();
			
		// #1 Select Vendor View 
		UsageHelper.selectVendorView();
		
		
		// #2 Read data from file
		String[][] valuesfromFile = ReadFilesHelper.getDataFromSpreadsheet(completePath);
		
		
		// #3 Select only one vendor
		CommonTestStepActions.UnSelectAllVendors();
		CommonTestStepActions.selectOneVendor(vendor);
		
		
		String lastMonthListedMonthSelector = driver.findElement(By.cssSelector(".tdb-pov__monthPicker>div>select>option:last-of-type")).getText();
		String monthYear = "";
		
		int i = 0;
		
		do {
		
			String[] oneMonthData = valuesfromFile[i];   
			String year =  "";
			String month = "";
			
			if(oneMonthData[1].equals("1")){
				month = "12";
				year = Integer.toString(Integer.parseInt(oneMonthData[0]) - 1);
			}else{
				month = Integer.toString((Integer.parseInt(oneMonthData[1]) - 1));
				year =  oneMonthData[0];
			}
			
			
			monthYear = CommonTestStepActions.convertMonthNumberToName(month, year);
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
			
			i++;
			
		} while (!monthYear.equals(lastMonthListedMonthSelector));
		
		
		
	}
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "Test finished. Select OK to close browser.");
		driver.close();
		driver.quit();
	}
	
}
