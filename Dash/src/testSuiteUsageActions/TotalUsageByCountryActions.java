package testSuiteUsageActions;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.UsageHelper;
import usage.TotalUsageActions;

public class TotalUsageByCountryActions extends BaseClass {
	
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
	}
	
	
	@Test
	public static void TotalUsageByCountryActionsTest() throws Exception
	{
		
		CommonTestStepActions.GoToUsagePageDetailedWait();
		
			
		// #1 Select Vendor View 
		UsageHelper.selectCountryView();
		
		
		// #2 Select vendors from PoV - one by one - and verify that the corresponding countries are added to graphs (tooltip) and legend,
		// # for Voice, Data and Messages
		
		// Set month selector value
		CommonTestStepActions.selectMonthYearPulldown("April 2017");
		Thread.sleep(2000);
		
		// #3 Verify the text shown on the tooltip when hovering the mouse over the bar charts.
		// * Total Usage Domestic chart
		TotalUsageActions.verifyTotalUsageChartTooltip(UsageHelper.totalUsageDomesticChart, UsageHelper.categoryVoice);
		Thread.sleep(2000);
		
		// * Total Usage Roaming chart
		TotalUsageActions.verifyTotalUsageChartTooltip(UsageHelper.totalUsageRoamingChart, UsageHelper.categoryVoice);
		Thread.sleep(2000);
		
		UsageHelper.selectCategory(UsageHelper.totalUsageSection, UsageHelper.categoryData);
		
		// * Total Usage Domestic chart
		TotalUsageActions.verifyTotalUsageChartTooltip(UsageHelper.totalUsageDomesticChart, UsageHelper.categoryData);
		Thread.sleep(2000);
		
		// * Total Usage Roaming chart
		TotalUsageActions.verifyTotalUsageChartTooltip(UsageHelper.totalUsageRoamingChart, UsageHelper.categoryData);
		Thread.sleep(2000);
		
		UsageHelper.selectCategory(UsageHelper.totalUsageSection, UsageHelper.categoryMessages);
		
		// * Total Usage Domestic chart
		TotalUsageActions.verifyTotalUsageChartTooltip(UsageHelper.totalUsageDomesticChart, UsageHelper.categoryMessages);
		Thread.sleep(2000);
		
		// * Total Usage Roaming chart
		TotalUsageActions.verifyTotalUsageChartTooltip(UsageHelper.totalUsageRoamingChart, UsageHelper.categoryMessages);
		Thread.sleep(2000);		
		
		
		// #4 Verify that countries selected are added to the Total Usage by Country chart - "Domestic Usage"
		
//		// Unselect all vendors
//		CommonTestStepActions.UnSelectAllVendors();
//		
//		// Voice
//		TotalUsageActions.countriesAddedToCharts(UsageHelper.totalUsageDomesticChart, UsageHelper.categoryVoice);
//		
//		// Unselect all vendors
//		CommonTestStepActions.UnSelectAllVendors();
//				
//		// Data
//		TotalUsageActions.countriesAddedToCharts(UsageHelper.totalUsageDomesticChart, UsageHelper.categoryData);
//		
//		// Unselect all vendors
//		CommonTestStepActions.UnSelectAllVendors();
//				
//		// Messages
//		TotalUsageActions.countriesAddedToCharts(UsageHelper.totalUsageDomesticChart, UsageHelper.categoryMessages);
//			
//		
//		// #5 Verify that countries selected are added to the Total Usage by Country chart - "Roaming Usage"
//		
//		// Unselect all vendors
//		CommonTestStepActions.UnSelectAllVendors();
//		
//		// Voice
//		TotalUsageActions.countriesAddedToCharts(UsageHelper.totalUsageRoamingChart, UsageHelper.categoryVoice);
//		
//		// Unselect all vendors
//		CommonTestStepActions.UnSelectAllVendors();
//				
//		// Data
//		TotalUsageActions.countriesAddedToCharts(UsageHelper.totalUsageRoamingChart, UsageHelper.categoryData);
//		
//		// Unselect all vendors
//		CommonTestStepActions.UnSelectAllVendors();
//				
//		// Messages
//		TotalUsageActions.countriesAddedToCharts(UsageHelper.totalUsageRoamingChart, UsageHelper.categoryMessages);
			
		 
		
	}
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "Total Usage by Country Actions test finished. Select OK to close browser.");
		driver.close();
		driver.quit();
	}

}
