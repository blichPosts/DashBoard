package testSuiteNumericValues;

import java.util.ArrayList;
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
import usage.TotalUsageValues;



public class UsageTotalUsageMultipleValues extends BaseClass{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
 		
	}
	
	
	@Test
	public static void UsageTotalUsageMultipleValuesTest() throws Exception
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
		CommonTestStepActions.UnSelectAllVendors();
		
		// #2 Get the data for each of the selected vendors for all months.
		List<List<UsageOneMonth>> listVendorsSelectedData = FleetHelper.getExpenseUsageDataForTest();
				
				
		String lastMonthListedMonthSelector = GeneralHelper.getLastMonthFromSelector();
		String monthYearToSelect = "";
					
		int indexMonth = 0;
	
		List<String> monthsToSelect = UsageHelper.getMonthListUnifiedForVendorsSelected(listVendorsSelectedData);
		
		do {
		
			// List listOneMonthData will have the data for a specific month, for all the vendors previously selected
			List<UsageOneMonth> listOneMonthData = new ArrayList<>();
			
			for(List<UsageOneMonth> values: listVendorsSelectedData){
				
				int indexMonthForVendorSelected = 0;
				boolean dataFoundForMonth = false;
				
				while(indexMonthForVendorSelected < values.size()) { // && !dataFoundForMonth){
					
					String monthYear = CommonTestStepActions.convertMonthNumberToName(values.get(indexMonthForVendorSelected).getOrdinalMonth(), values.get(indexMonthForVendorSelected).getOrdinalYear()); 
					
					if(monthsToSelect.get(indexMonth).equals(monthYear)){
 
						listOneMonthData.add(values.get(indexMonthForVendorSelected));
						dataFoundForMonth = true;
						
					}
						
					indexMonthForVendorSelected++;
				}
					
				
			}
			
			// Vendors are listed in alphabetical order in the vertical axis
			List<UsageOneMonth> listOneMonthSortedByVendor = UsageHelper.sortVendorsAlphabetically(listOneMonthData);
			
			
			// #4 Select month on month/year selector
			CommonTestStepActions.selectMonthYearPulldown(monthsToSelect.get(indexMonth));
			System.out.println("monthYear: " + monthsToSelect.get(indexMonth)); 
			
			Thread.sleep(2000);
			
			// #5 Verify that the values displayed on the tooltips of Total Usage charts are the same as the ones read from file  
			
			UsageHelper.selectCategory(UsageHelper.totalUsageSection, UsageHelper.categoryVoice);
			
			TotalUsageValues.verifyTotalUsageChartTooltip(UsageHelper.totalUsageDomesticChart, listOneMonthSortedByVendor, UsageHelper.categoryVoice);
			Thread.sleep(2000);
			
			TotalUsageValues.verifyTotalUsageChartTooltip(UsageHelper.totalUsageRoamingChart, listOneMonthSortedByVendor, UsageHelper.categoryVoice);
			Thread.sleep(2000);				
			
			UsageHelper.selectCategory(UsageHelper.totalUsageSection, UsageHelper.categoryData);
			
			TotalUsageValues.verifyTotalUsageChartTooltip(UsageHelper.totalUsageDomesticChart, listOneMonthSortedByVendor, UsageHelper.categoryData);
			Thread.sleep(2000);
			
			TotalUsageValues.verifyTotalUsageChartTooltip(UsageHelper.totalUsageRoamingChart, listOneMonthSortedByVendor, UsageHelper.categoryData);
			Thread.sleep(2000);				
			
			UsageHelper.selectCategory(UsageHelper.totalUsageSection, UsageHelper.categoryMessages);
			
			TotalUsageValues.verifyTotalUsageChartTooltip(UsageHelper.totalUsageDomesticChart, listOneMonthSortedByVendor, UsageHelper.categoryMessages);
			Thread.sleep(2000);
			
			TotalUsageValues.verifyTotalUsageChartTooltip(UsageHelper.totalUsageRoamingChart, listOneMonthSortedByVendor, UsageHelper.categoryMessages);
			Thread.sleep(2000);
						
			indexMonth++;

			
		} while (!monthYearToSelect.equals(lastMonthListedMonthSelector) && indexMonth < monthsToSelect.size());
		
				
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
