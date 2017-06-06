package testSuiteUsageActions;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.UsageHelper;
import usage.UsageTrending;
import usage.UsageTrendingActions;

public class UsageTrendingByCountryActions extends BaseClass{

	
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
	}
	
	
	@Test
	public static void UsageTrendingByCountryActionsTest() throws Exception
	{
		
		CommonTestStepActions.GoToUsagePageDetailedWait();
			
		// Set month selector value
		CommonTestStepActions.selectMonthYearPulldown("April 2017");
				
		Thread.sleep(2000);
		
		// #1 Select Vendor View 
		UsageHelper.selectCountryView();

		// #2 Verify the text shown on the tooltip when hovering the mouse over the bar charts, for Usage Trending Domestic chart
		UsageTrending.verifyUsageTrendingChartTooltip(UsageHelper.usageTrendingDomesticChart);
		
		
		// #3 Verify the text shown on the tooltip when hovering the mouse over the bar charts, for Usage Trending Roaming chart
		UsageTrending.verifyUsageTrendingChartTooltip(UsageHelper.usageTrendingRoamingChart);
				
		
		// #4 Verify that countries on the legends of Usage Trending Domestic chart can be switched on/off. 
		// The bars are added/removed from chart according to the countries selected on the legends.
		UsageTrending.verifyBarsCanBeSwitchedOnOff(UsageHelper.usageTrendingDomesticChart);

		
		// #5 Verify that vendors/countries on the legends of Usage Trending Roaming chart can be switched on/off.
		UsageTrending.verifyBarsCanBeSwitchedOnOff(UsageHelper.usageTrendingRoamingChart);
	
		
		// #6 Verify that countries selected are added to the Usage Trending by Country chart - "Domestic"
		
		// Unselect all vendors
		CommonTestStepActions.UnSelectAllVendors();
		
		// Voice
		UsageTrendingActions.countriesAddedToCharts(UsageHelper.usageTrendingDomesticChart, UsageHelper.categoryVoice);
	
		// Unselect all vendors
		CommonTestStepActions.UnSelectAllVendors();
		
		// Messages
		UsageTrendingActions.countriesAddedToCharts(UsageHelper.usageTrendingDomesticChart, UsageHelper.categoryMessages);
				
		// Unselect all vendors
		CommonTestStepActions.UnSelectAllVendors();
		
		// Data
		UsageTrendingActions.countriesAddedToCharts(UsageHelper.usageTrendingDomesticChart, UsageHelper.categoryData);

		// Unselect all vendors
		CommonTestStepActions.UnSelectAllVendors();
		
		// Verify that countries selected are added to the Usage Trending by Country chart - "Roaming"
		
		// Voice
		UsageTrendingActions.countriesAddedToCharts(UsageHelper.usageTrendingRoamingChart, UsageHelper.categoryVoice);
				
		// Unselect all vendors
		CommonTestStepActions.UnSelectAllVendors();
		
		// Messages
		UsageTrendingActions.countriesAddedToCharts(UsageHelper.usageTrendingRoamingChart, UsageHelper.categoryMessages);
				
		// Unselect all vendors
		CommonTestStepActions.UnSelectAllVendors();
		
		// Data
		UsageTrendingActions.countriesAddedToCharts(UsageHelper.usageTrendingRoamingChart, UsageHelper.categoryData);
	
		// #7 Verify that countries that are removed from the PoV section are removed from the charts
		UsageTrendingActions.countriesUnselectedRemovedFromChart(UsageHelper.usageTrendingDomesticChart);
		
		UsageTrendingActions.countriesUnselectedRemovedFromChart(UsageHelper.usageTrendingRoamingChart);
		
		
	}
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "'Usage Trending - Usage by Country' test finished. Select OK to close browser.");
		driver.close();
		driver.quit();
	}

	
}
