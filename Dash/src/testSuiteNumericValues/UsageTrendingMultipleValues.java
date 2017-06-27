package testSuiteNumericValues;

import java.util.List;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.FleetHelper;
import helperObjects.GeneralHelper;
import helperObjects.ReadFilesHelper;
import helperObjects.UsageHelper;
import helperObjects.UsageOneMonth;
import usage.UsageTrendingValues;

public class UsageTrendingMultipleValues extends BaseClass{

	
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
		
	}


	@Test
	public static void UsageTrendingMultipleValuesTest() throws Exception
	{

		// Enable Start collecting data
		ReadFilesHelper.startCollectingData();
		Thread.sleep(2000);
		
		// Reload Fleet data
		ReadFilesHelper.reloadFleetData();
		Thread.sleep(2000);
			
		// Wait for countries and vendors to be loaded on PoV section
		FleetHelper.waitForPoVSectionToBeLoaded(); 

		CommonTestStepActions.GoToUsagePageDetailedWait();
		
		
		// #1 Select Vendor View and Unselect all vendors  
		UsageHelper.selectVendorView();
		//CommonTestStepActions.UnSelectAllVendors();
			
		// #2 Get the data for each of the selected vendors for all months.
		List<List<UsageOneMonth>> listSelectedDataForMonthListUnified = FleetHelper.getExpenseUsageDataForTest();
		
		
		// #3 Some vendors might not have information for all last 13 months; in that case the value displayed on the Trending chart is zero
		//    So to have a valid value to compare with, the info for those missing months (on source file) is created and values are set to zero.  
		List<List<UsageOneMonth>> listVendorsSelectedData = FleetHelper.getExpenseUsageDataAllMonths(listSelectedDataForMonthListUnified);
		
		
		// #4 Each list in dataForExpenseTrending will have the data for a specific month, for all the vendors previously selected
		List<List<UsageOneMonth>> dataForUsageTrending = FleetHelper.getListsWithDataPerMonth(listVendorsSelectedData);
		
			
		// Get months listed on the dropdown list
		List<String> monthsToSelect = UsageHelper.getMonthYearListString();
						
		String lastMonthListedMonthSelector = GeneralHelper.getLastMonthFromSelector(); 
		int indexMonthToSelect = 0;
		String monthYearToSelect = "";
		List<String> monthsWithDataToSelectPulldown = UsageHelper.getMonthListUnifiedForVendorsSelected(listSelectedDataForMonthListUnified);
		
									
		do {
					
			monthYearToSelect = monthsWithDataToSelectPulldown.get(indexMonthToSelect);  
			System.out.println("Month Year: " + monthYearToSelect);
			
			CommonTestStepActions.selectMonthYearPulldown(monthYearToSelect);
			Thread.sleep(2000);
			
			
			// #5 Verify that the values displayed on the tooltips of "Usage Trending" charts are the same as the ones read from file
			
			try {
				
				// Domestic chart
				UsageHelper.selectCategory(UsageHelper.usageTrendingSection, UsageHelper.categoryVoice);
				
				UsageTrendingValues.verifyUsageTrendingChartTooltipByVendor(UsageHelper.usageTrendingDomesticChart, dataForUsageTrending, UsageHelper.categoryVoice);
				Thread.sleep(2000);
				
				UsageHelper.selectCategory(UsageHelper.usageTrendingSection, UsageHelper.categoryData);
				
				UsageTrendingValues.verifyUsageTrendingChartTooltipByVendor(UsageHelper.usageTrendingDomesticChart, dataForUsageTrending, UsageHelper.categoryData);
				Thread.sleep(2000);
				
				UsageHelper.selectCategory(UsageHelper.usageTrendingSection, UsageHelper.categoryMessages);
				
				UsageTrendingValues.verifyUsageTrendingChartTooltipByVendor(UsageHelper.usageTrendingDomesticChart, dataForUsageTrending, UsageHelper.categoryMessages);
				Thread.sleep(2000);
				
				// Roaming chart
				UsageHelper.selectCategory(UsageHelper.usageTrendingSection, UsageHelper.categoryVoice);
				
				UsageTrendingValues.verifyUsageTrendingChartTooltipByVendor(UsageHelper.usageTrendingRoamingChart, dataForUsageTrending, UsageHelper.categoryVoice);
				Thread.sleep(2000);	
				
				UsageHelper.selectCategory(UsageHelper.usageTrendingSection, UsageHelper.categoryData);
				
				UsageTrendingValues.verifyUsageTrendingChartTooltipByVendor(UsageHelper.usageTrendingRoamingChart, dataForUsageTrending, UsageHelper.categoryData);
				Thread.sleep(2000);	
				
				UsageHelper.selectCategory(UsageHelper.usageTrendingSection, UsageHelper.categoryMessages);
				
				UsageTrendingValues.verifyUsageTrendingChartTooltipByVendor(UsageHelper.usageTrendingRoamingChart, dataForUsageTrending, UsageHelper.categoryMessages);
				Thread.sleep(2000);
								
			} catch(NullPointerException e) {
				
				System.out.println("chart not found or value found is null");
				
			}
			
			indexMonthToSelect++;
			
		} while (!monthYearToSelect.equals(lastMonthListedMonthSelector) && indexMonthToSelect < monthsToSelect.size());
		
			
	}
	
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "Test for Usage Trending values finished. Select OK to close browser.");
		driver.close();
		driver.quit();
	}

}
