package testSuiteUsage;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.UsageHelper;
import usage.UsageTrending;

public class UsageTrendingByCountry extends BaseClass{

	
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
	}
	
	
	@Test
	public static void UsageTrendingByCountryTest() throws Exception
	{
		
		CommonTestStepActions.GoToUsagePageDetailedWait();
			
		// #1 Select Country View 
		UsageHelper.selectCountryView();
		
		
		// #2 Verify that title "Usage by Country - ... " changes according to what Category was selected
		UsageTrending.verifyUsageTrendingTitle(UsageHelper.usageTrendingByCountry);
		
					
		// #3 Verify bar charts titles
		UsageTrending.verifyBarChartTitlesUsageTrending();
		
		
		// #4 Verify that the legends of the Usage Trending charts show selected vendors 
		UsageTrending.verifyCountriesInLegend_ChartsUsageTrending();
		
		
		// #5 Verify that 13 months are listed in the Usage Trending charts 
		UsageTrending.verifyMonthYearInUsageTrendingCharts();
		
			
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
