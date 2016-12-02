package testSuiteUsageActions;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.UsageHelper;
import usage.UsageTrending;


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
			
		// #1 Select Vendor View 
		UsageHelper.selectVendorView();
		
		
		// #2 Verify the text shown on the tooltip when hovering the mouse over the bar charts, for Usage Trending Domestic chart
		UsageTrending.verifyUsageTrendingChartTooltip(UsageHelper.UsageTrendingDomesticChart);
		
		
		// #3 Verify the text shown on the tooltip when hovering the mouse over the bar charts, for Usage Trending Roaming chart
		UsageTrending.verifyUsageTrendingChartTooltip(UsageHelper.UsageTrendingRoamingChart);
				
		
		// #4 Verify that vendors on the legends of Usage Trending Domestic chart can be switched on/off. 
		// The bars are added/removed from chart according to the vendors selected on the legends.
		UsageTrending.verifyBarsCanBeSwitchedOnOff(UsageHelper.UsageTrendingDomesticChart);

		
		// #5 Verify that vendors/countries on the legends of Usage Trending Roaming chart can be switched on/off.
		UsageTrending.verifyBarsCanBeSwitchedOnOff(UsageHelper.UsageTrendingRoamingChart);
		
		
		// #4 Select only one vendor
		//CommonTestStepActions.UnSelectAllVendors(); 
		//CommonTestStepActions.selectOneVendor("AT&T Mobility");
		
		
		
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
