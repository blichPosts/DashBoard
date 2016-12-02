package testSuiteUsage;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.UsageHelper;
import usage.TotalUsage;


public class TotalUsageByCountry extends BaseClass {

	
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
	}
	
	
	@Test
	public static void TotalUsageByCountryTest() throws Exception
	{
		
		CommonTestStepActions.GoToUsagePageDetailedWait();
		
			
		// #1 Select Vendor View 
		UsageHelper.selectCountryView();
		
		
		// #2 Verify Month and Year displayed on Vendor View
		TotalUsage.verifyMonthYearOnUsageView();
		
		
		// #3 Verify that title "Total Usage by Country - ... " changes according to what Category was selected
		TotalUsage.verifyTotalUsageTitle(UsageHelper.totalUsageByCountry);
		
				
		// #4 Verify bar charts titles
		TotalUsage.verifyBarChartTitlesTotalUsage();
		
		
		// #5 Verify legends of charts 
		TotalUsage.verifyLegendsOfCharts();
		
		
		// #6 Verify that Vendors selected are listed in charts 
		TotalUsage.verifyCountriesInUsageByCountryCharts();
		
		
		
	}
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "Total Usage by Vendor test finished. Select OK to close browser.");
		driver.close();
		driver.quit();
	}
	
	
}
