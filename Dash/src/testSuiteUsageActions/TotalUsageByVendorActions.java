package testSuiteUsageActions;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.UsageHelper;
import usage.TotalUsageActions;


public class TotalUsageByVendorActions extends BaseClass {
	
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
	}
	
	
	@Test
	public static void TotalUsageByVendorActionsTest() throws Exception
	{
		
		CommonTestStepActions.GoToUsagePageDetailedWait();
		
			
		// #1 Select Vendor View 
		UsageHelper.selectVendorView();
		
		
		// #2 Select vendors from PoV - one by one - and verify that they are added to graphs (tooltip) and legend,
		// # for Voice, Data and Messages
		
		// Set month selector value
		CommonTestStepActions.selectMonthYearPulldown("April 2017");
		Thread.sleep(2000);
		
		// #3 Verify the text shown on the tooltip when hovering the mouse over the bar charts.
		
//		UsageHelper.selectCategory(UsageHelper.totalUsageSection, UsageHelper.categoryVoice);
//		
//		// * Total Usage Domestic chart
//		TotalUsageActions.verifyTotalUsageChartTooltip(UsageHelper.totalUsageDomesticChart, UsageHelper.categoryVoice);
//		Thread.sleep(2000);
//		
//		// * Total Usage Roaming chart
//		TotalUsageActions.verifyTotalUsageChartTooltip(UsageHelper.totalUsageRoamingChart, UsageHelper.categoryVoice);
//		Thread.sleep(2000);
//		
//		UsageHelper.selectCategory(UsageHelper.totalUsageSection, UsageHelper.categoryData);
//		
//		// * Total Usage Domestic chart
//		TotalUsageActions.verifyTotalUsageChartTooltip(UsageHelper.totalUsageDomesticChart, UsageHelper.categoryData);
//		Thread.sleep(2000);
//		
//		// * Total Usage Roaming chart
//		TotalUsageActions.verifyTotalUsageChartTooltip(UsageHelper.totalUsageRoamingChart, UsageHelper.categoryData);
//		Thread.sleep(2000);
//		
//		UsageHelper.selectCategory(UsageHelper.totalUsageSection, UsageHelper.categoryMessages);
//		
//		// * Total Usage Domestic chart
//		TotalUsageActions.verifyTotalUsageChartTooltip(UsageHelper.totalUsageDomesticChart, UsageHelper.categoryMessages);
//		Thread.sleep(2000);
//		
//		// * Total Usage Roaming chart
//		TotalUsageActions.verifyTotalUsageChartTooltip(UsageHelper.totalUsageRoamingChart, UsageHelper.categoryMessages);
//		Thread.sleep(2000);		
		
		
		// Select one category and verify that only one category is selected on selector  -- added June 13
		UsageHelper.selectCategoryNew(UsageHelper.totalUsageSection, UsageHelper.categoryVoice);
		TotalUsageActions.verifySelectedCategories(UsageHelper.totalUsageSection, UsageHelper.categoryVoice);
		
		UsageHelper.selectCategoryNew(UsageHelper.totalUsageSection, UsageHelper.categoryData);
		TotalUsageActions.verifySelectedCategories(UsageHelper.totalUsageSection, UsageHelper.categoryData);
		
		UsageHelper.selectCategoryNew(UsageHelper.totalUsageSection, UsageHelper.categoryMessages);
		TotalUsageActions.verifySelectedCategories(UsageHelper.totalUsageSection, UsageHelper.categoryMessages);
		
		
//		// #4 Verify that vendors selected are added to the Total Usage by Vendor chart - "Domestic Usage"
//		
//		// Unselect all vendors
//		CommonTestStepActions.UnSelectAllVendors();
//		
//		// Voice
//		TotalUsageActions.vendorsAddedToCharts(UsageHelper.totalUsageDomesticChart, UsageHelper.categoryVoice);
//		
//		// Unselect all vendors
//		CommonTestStepActions.UnSelectAllVendors();
//				
//		// Data
//		TotalUsageActions.vendorsAddedToCharts(UsageHelper.totalUsageDomesticChart, UsageHelper.categoryData);
//		
//		// Unselect all vendors
//		CommonTestStepActions.UnSelectAllVendors();
//				
//		// Messages
//		TotalUsageActions.vendorsAddedToCharts(UsageHelper.totalUsageDomesticChart, UsageHelper.categoryMessages);
//			
//		
//		// Verify that vendors selected are added to the Total Usage by Vendor chart - "Roaming Usage"
//		
//		// Unselect all vendors
//		CommonTestStepActions.UnSelectAllVendors();
//		
//		// Voice
//		TotalUsageActions.vendorsAddedToCharts(UsageHelper.totalUsageRoamingChart, UsageHelper.categoryVoice);
//		
//		// Unselect all vendors
//		CommonTestStepActions.UnSelectAllVendors();
//				
//		// Data
//		TotalUsageActions.vendorsAddedToCharts(UsageHelper.totalUsageRoamingChart, UsageHelper.categoryData);
//		
//		// Unselect all vendors
//		CommonTestStepActions.UnSelectAllVendors();
//				
//		// Messages
//		TotalUsageActions.vendorsAddedToCharts(UsageHelper.totalUsageRoamingChart, UsageHelper.categoryMessages);
		
		
		// # Unselect vendors from PoV - one by one - and verify that they are removed from graphs (tooltip) and legend,
		// - for Voice, Data and Messages 			
		
		
		
		
	}
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "Total Usage by Vendor Actions test finished. Select OK to close browser.");
		driver.close();
		driver.quit();
	}
	
}
