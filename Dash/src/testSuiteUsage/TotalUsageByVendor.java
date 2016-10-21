package testSuiteUsage;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.UsageHelper;



public class TotalUsageByVendor extends BaseClass {
		
		@BeforeClass
		public static void setUp() throws Exception
		{
			setUpDriver();
		}
		
		
		@Test
		public static void KpiTilesVisualUsageTest() throws Exception
		{
			
			CommonTestStepActions.GoToUsagePageDetailedWait();
			
				
			// #1 Select Vendor View 
			UsageHelper.selectVendorView();
			
			
			// #2 Verify Month and Year displayed on Vendor View
			UsageHelper.verifyMonthYearOnVendorView();
			
			
			// #3 Verify that title "Total Usage by Vendor - ... " changes according to what Category was selected
			UsageHelper.verifyTotalUsageTitle();
			
			
			// #4 Verify bar charts titles
			UsageHelper.verifyBarChartTitlesUsageByVendor();
			
			
			// #5 Verify legends of charts 
			UsageHelper.verifyLegendsOfCharts();
			
			
			// #6 Verify that Vendors selected are listed in charts 
			UsageHelper.verifyVendorsInUsageByVendorCharts();
			
			
			
			
			
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

	

