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
import helperObjects.GeneralHelper;
import helperObjects.ReadFilesHelper;
import helperObjects.UsageHelper;
import helperObjects.UsageOneMonth;
import usage.TotalUsageValues;


public class UsageTotalUsageTestValuesOneVendor extends BaseClass{

	
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
 		
	}
	
	
	@Test 
	public static void UsageTotalUsageTestValuesOneVendorTest() throws Exception
	{

		// *** Needed for Firefox *** :|
		GeneralHelper.waitForHeaderVisible();	
				
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
		CommonTestStepActions.UnSelectAllVendors();
		
		
		List<WebElement> vendors = CommonTestStepActions.getAllVendorsInPoV();
		List<String> vendorNames = new ArrayList<>();
		
		
		// Get all the vendor names listed on the PoV section
		for (WebElement vendor: vendors) {
			vendorNames.add(vendor.getText());
		}
		
	
		// Run the test for each vendor 
		for (String vendor: vendorNames) {
			
			// #2 Read data from the ajax call response
			List<UsageOneMonth> valuesFromAjaxCall = ReadFilesHelper.getJsonDataExpenseUsage(vendor);
			
				
			// #3 Select only one vendor
			CommonTestStepActions.selectOneVendor(vendor);
			
			String lastMonthListedMonthSelector = GeneralHelper.getLastMonthFromSelector();
			
			UsageOneMonth oneMonthData;
			String year = "";
			String month = "";
			String monthYearToSelect = "";
						
			int indexMonth = 0;
			
			do {
			
				// Get the data for the selected vendor and the month indicated by indexMonth
				oneMonthData = valuesFromAjaxCall.get(indexMonth);   
				
				String[] monthYear = UsageHelper.getMonthYearToSelect(oneMonthData);
				month = monthYear[0];
				year = monthYear[1];
				
				monthYearToSelect = CommonTestStepActions.convertMonthNumberToName(month, year);
				System.out.println("Month Year: " + monthYearToSelect);
				
				// #4 Select month on month/year selector
				CommonTestStepActions.selectMonthYearPulldown(monthYearToSelect);
				Thread.sleep(2000);
				
				
				List<UsageOneMonth> listOneMonthSortedByVendor = new ArrayList<>();
				listOneMonthSortedByVendor.add(oneMonthData);
				
				// #5 Verify that the values displayed on the tooltips of Total Usage charts are the same as the ones read from file  
				
				UsageHelper.selectCategory(UsageHelper.totalUsageSection, UsageHelper.categoryVoice);

				TotalUsageValues.verifyTotalUsageChartTooltipByVendor(UsageHelper.totalUsageDomesticChart, listOneMonthSortedByVendor, UsageHelper.categoryVoice);
				Thread.sleep(2000);
				
				TotalUsageValues.verifyTotalUsageChartTooltipByVendor(UsageHelper.totalUsageRoamingChart, listOneMonthSortedByVendor, UsageHelper.categoryVoice);
				Thread.sleep(2000);				
				
				UsageHelper.selectCategory(UsageHelper.totalUsageSection, UsageHelper.categoryData);
				
				TotalUsageValues.verifyTotalUsageChartTooltipByVendor(UsageHelper.totalUsageDomesticChart, listOneMonthSortedByVendor, UsageHelper.categoryData);
				Thread.sleep(2000);
				
				TotalUsageValues.verifyTotalUsageChartTooltipByVendor(UsageHelper.totalUsageRoamingChart, listOneMonthSortedByVendor, UsageHelper.categoryData);
				Thread.sleep(2000);				
				
				UsageHelper.selectCategory(UsageHelper.totalUsageSection, UsageHelper.categoryMessages);
				
				TotalUsageValues.verifyTotalUsageChartTooltipByVendor(UsageHelper.totalUsageDomesticChart, listOneMonthSortedByVendor, UsageHelper.categoryMessages);
				Thread.sleep(2000);
				
				TotalUsageValues.verifyTotalUsageChartTooltipByVendor(UsageHelper.totalUsageRoamingChart, listOneMonthSortedByVendor, UsageHelper.categoryMessages);
				Thread.sleep(2000);
								
				indexMonth++;
				
			} while (!monthYearToSelect.equals(lastMonthListedMonthSelector));
						
		}
	
	}
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "Test for Total Usage values finished. Select OK to close browser.");
		driver.close();
		driver.quit();
	}
	
}
