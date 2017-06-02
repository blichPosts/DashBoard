package testSuiteNumericValues;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.FleetHelper;
import helperObjects.ReadFilesHelper;
import helperObjects.UsageHelper;
import helperObjects.UsageOneMonth;
import usage.UsageTrendingValues;


public class UsageTrendingTestValuesOneVendor extends BaseClass{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
		
	}


	@Test
	public static void UsageTrendingTestValuesOneVendorTest() throws Exception
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
		
		// #1 Select Vendor View 
		UsageHelper.selectVendorView();
					
		
		List<WebElement> vendors = CommonTestStepActions.getAllVendorNames();
		List<String> vendorNames = new ArrayList<>();
		
		for(WebElement vendor: vendors){
			vendorNames.add(vendor.getText());
		}

		
		// Run the test for each vendor 
		for (String vendorSelected: vendorNames) {
			
			String vendor = vendorSelected;
			
			// #2 Get the data from the AJAX call
			List<UsageOneMonth> valuesTmp = ReadFilesHelper.getJsonDataExpenseUsage(vendor);
						
			// Some vendors might not have information for all last 13 months; in that case the value displayed on the Usage Trending chart is zero
			// So to have a valid value to compare with, the info for those missing months (on source file) is created and values are set to zero.  
			List<UsageOneMonth> values = UsageHelper.addMissingMonthsForVendor(valuesTmp);
			
			
			// #3 Select only one vendor
			CommonTestStepActions.UnSelectAllVendors();
			CommonTestStepActions.selectOneVendor(vendor);
			
			
			// #4 Select month on month/year selector
			// Month to be selected on pulldown needs to be one of the months for which there's data in the source file
			UsageOneMonth oneMonthData = valuesTmp.get(0);
						
			String[] monthYear = UsageHelper.getMonthYearToSelect(oneMonthData);
			
			String year = monthYear[1];
			String month = monthYear[0];
			
			String monthYearToSelect = CommonTestStepActions.convertMonthNumberToName(month, year);
			System.out.println("Month Year: " + monthYearToSelect);
			
			CommonTestStepActions.selectMonthYearPulldown(monthYearToSelect);
			Thread.sleep(2000);
			
			
			// #5 Verify that the values displayed on the tooltips of "Usage Trending" charts are the same as the ones read from file
			// Note: Only the first month with data is selected for each vendor, since no matter which month is selected the same info
			// will be displayed on the Usage Trending charts 
			
			List<List<UsageOneMonth>> listSelectedDataForMonthListUnified = new ArrayList<>();
			listSelectedDataForMonthListUnified.add(values);
			

			// #6 Some vendors might not have information for all last 13 months; in that case the value displayed on the Trending chart is zero
			//    So to have a valid value to compare with, the info for those missing months (on source file) is created and values are set to zero.  
			List<List<UsageOneMonth>> listVendorsSelectedData = FleetHelper.getExpenseUsageDataAllMonths(listSelectedDataForMonthListUnified);
			
			
			// #7 Each list in dataForExpenseTrending will have the data for a specific month, for all the vendors previously selected
			List<List<UsageOneMonth>> dataForUsageTrending = FleetHelper.getListsWithDataPerMonth(listVendorsSelectedData);
			
			
			
			try {
				
				// Domestic chart
				UsageHelper.selectCategory(UsageHelper.usageTrendingSection, UsageHelper.categoryVoice);
				
				UsageTrendingValues.verifyUsageTrendingChartTooltip(UsageHelper.usageTrendingDomesticChart, dataForUsageTrending, UsageHelper.categoryVoice);
				Thread.sleep(2000);
				
				UsageHelper.selectCategory(UsageHelper.usageTrendingSection, UsageHelper.categoryData);
				
				UsageTrendingValues.verifyUsageTrendingChartTooltip(UsageHelper.usageTrendingDomesticChart, dataForUsageTrending, UsageHelper.categoryData);
				Thread.sleep(2000);
				
				UsageHelper.selectCategory(UsageHelper.usageTrendingSection, UsageHelper.categoryMessages);
				
				UsageTrendingValues.verifyUsageTrendingChartTooltip(UsageHelper.usageTrendingDomesticChart, dataForUsageTrending, UsageHelper.categoryMessages);
				Thread.sleep(2000);
				
				// Roaming chart
				UsageHelper.selectCategory(UsageHelper.usageTrendingSection, UsageHelper.categoryVoice);
				
				UsageTrendingValues.verifyUsageTrendingChartTooltip(UsageHelper.usageTrendingRoamingChart, dataForUsageTrending, UsageHelper.categoryVoice);
				Thread.sleep(2000);	
				
				UsageHelper.selectCategory(UsageHelper.usageTrendingSection, UsageHelper.categoryData);
				
				UsageTrendingValues.verifyUsageTrendingChartTooltip(UsageHelper.usageTrendingRoamingChart, dataForUsageTrending, UsageHelper.categoryData);
				Thread.sleep(2000);	
				
				UsageHelper.selectCategory(UsageHelper.usageTrendingSection, UsageHelper.categoryMessages);
				
				UsageTrendingValues.verifyUsageTrendingChartTooltip(UsageHelper.usageTrendingRoamingChart, dataForUsageTrending, UsageHelper.categoryMessages);
				Thread.sleep(2000);
								
			} catch(NullPointerException e) {
				
				System.out.println("chart not found or value found is null");
				
			}
						
		}
	
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
