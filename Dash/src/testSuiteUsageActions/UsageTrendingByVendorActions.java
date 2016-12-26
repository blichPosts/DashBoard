package testSuiteUsageActions;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.UsageHelper;
import usage.TotalUsageActions;
import usage.UsageTrending;
import usage.UsageTrendingActions;


public class UsageTrendingByVendorActions extends BaseClass{

	
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
	}
	
	
	@Test
	public static void UsageTrendingByVendorActionsTest() throws Exception
	{
		
		CommonTestStepActions.GoToUsagePageDetailedWait();
		
		// Set month selector value
		CommonTestStepActions.selectMonthYearPulldown("August 2016");
						
		Thread.sleep(2000);
				
		
		// #1 Select Vendor View 
		UsageHelper.selectVendorView();
		
/*		
		// #2 Verify the text shown on the tooltip when hovering the mouse over the bar charts, for Usage Trending Domestic chart
		UsageTrending.verifyUsageTrendingChartTooltip(UsageHelper.UsageTrendingDomesticChart);
		
		
		// #3 Verify the text shown on the tooltip when hovering the mouse over the bar charts, for Usage Trending Roaming chart
		UsageTrending.verifyUsageTrendingChartTooltip(UsageHelper.UsageTrendingRoamingChart);
				
		
		// #4 Verify that vendors on the legends of Usage Trending Domestic chart can be switched on/off. 
		// The bars are added/removed from chart according to the vendors selected on the legends.
		UsageTrending.verifyBarsCanBeSwitchedOnOff(UsageHelper.UsageTrendingDomesticChart);

		
		// #5 Verify that vendors/countries on the legends of Usage Trending Roaming chart can be switched on/off.
		UsageTrending.verifyBarsCanBeSwitchedOnOff(UsageHelper.UsageTrendingRoamingChart);
		
	
		// #6 Verify that vendors selected are added to the Usage Trending by Vendor chart - "Domestic"
		// Unselect all vendors
		CommonTestStepActions.UnSelectAllVendors();
			
		// Voice
		UsageTrendingActions.vendorsAddedToCharts(UsageHelper.UsageTrendingDomesticChart, UsageHelper.categoryVoice);

		// Unselect all vendors
		CommonTestStepActions.UnSelectAllVendors();
		
		// Messages
		UsageTrendingActions.vendorsAddedToCharts(UsageHelper.UsageTrendingDomesticChart, UsageHelper.categoryMessages);
				
		// Unselect all vendors
		CommonTestStepActions.UnSelectAllVendors();
		
		// Data
		UsageTrendingActions.vendorsAddedToCharts(UsageHelper.UsageTrendingDomesticChart, UsageHelper.categoryData);

			
		// Verify that vendors selected are added to the Usage Trending by Vendor chart - "Roaming"
		// Unselect all vendors
		CommonTestStepActions.UnSelectAllVendors();
		
		// Voice
		UsageTrendingActions.vendorsAddedToCharts(UsageHelper.UsageTrendingRoamingChart, UsageHelper.categoryVoice);
					
		// Unselect all vendors
		CommonTestStepActions.UnSelectAllVendors();
		
		// Messages
		UsageTrendingActions.vendorsAddedToCharts(UsageHelper.UsageTrendingRoamingChart, UsageHelper.categoryMessages);
				
		// Unselect all vendors
		CommonTestStepActions.UnSelectAllVendors();
		
		// Data
		UsageTrendingActions.vendorsAddedToCharts(UsageHelper.UsageTrendingRoamingChart, UsageHelper.categoryData);
*/		

		UsageTrendingActions.allVendorsAddedToCharts(UsageHelper.UsageTrendingRoamingChart, UsageHelper.categoryVoice);
		
		// #7 Verify that vendors that are removed from the PoV section are removed from the charts
		UsageTrendingActions.vendorsUnselectedRemovedFromChart(UsageHelper.UsageTrendingDomesticChart);
		
		UsageTrendingActions.vendorsUnselectedRemovedFromChart(UsageHelper.UsageTrendingRoamingChart);
		
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
