package testSuiteNumericValues;

import java.util.List;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenses.ExpenseTrendingMultipleValues;
import helperObjects.CommonTestStepActions;
import helperObjects.FleetHelper;
import helperObjects.GeneralHelper;
import helperObjects.ReadFilesHelper;
import helperObjects.UsageHelper;
import helperObjects.UsageOneMonth;


public class CostPerServiceNumberMultipleValues extends BaseClass{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
	}


	@Test
	public static void CostPerServiceNumberMultipleValuesTest() throws Exception
	{
		
		// Enable Start collecting data
		ReadFilesHelper.startCollectingData();
		Thread.sleep(2000);
		
		// Reload Fleet data
		ReadFilesHelper.reloadFleetData();
		Thread.sleep(2000);
				
		// Wait for countries and vendors to be loaded on PoV section
		FleetHelper.waitForPoVSectionToBeLoaded(); 
			
		
		// #1 Select Vendor View and Unselect all vendors  
		CommonTestStepActions.SelectVendorView();
		CommonTestStepActions.UnSelectAllVendors();
		
		CommonTestStepActions.GoToExpensePageDetailedWait();
		
		// #2 Get the data for each of the selected vendors for all months.
		List<List<UsageOneMonth>> listSelectedDataForMonthListUnified = FleetHelper.getExpenseUsageDataForTest();
		
		
		// #3 Some vendors might not have information for all last 13 months; in that case the value displayed on the Trending chart is zero
		//    So to have a valid value to compare with, the info for those missing months (on source file) is created and values are set to zero.  
		List<List<UsageOneMonth>> listVendorsSelectedData = FleetHelper.getExpenseUsageDataAllMonths(listSelectedDataForMonthListUnified);
		
			
		// Get months listed on the dropdown list
		List<String> monthsToSelect = UsageHelper.getMonthYearListString();
		
					
		// #4 Each list in dataForExpenseTrending will have the data for a specific month, for all the vendors previously selected
		List<List<UsageOneMonth>> dataForExpenseTrending = FleetHelper.getListsWithDataPerMonth(listVendorsSelectedData);
		
			
		String lastMonthListedMonthSelector = GeneralHelper.getLastMonthFromSelector(); 
		int indexMonthToSelect = 0;
		String monthYearToSelect = "";
		List<String> monthsWithDataToSelectPulldown = UsageHelper.getMonthListUnifiedForVendorsSelected(listSelectedDataForMonthListUnified);
		
		
		do {
					
			monthYearToSelect = monthsWithDataToSelectPulldown.get(indexMonthToSelect);  
			System.out.println(" ** Month Year: " + monthYearToSelect);
			
			CommonTestStepActions.selectMonthYearPulldown(monthYearToSelect);
			Thread.sleep(2000);
			
			// #5 Verify that the values displayed on the tooltips of "Expense Trending" charts are the same as the ones read from file
			
			try {
				
				FleetHelper.selectCategory(FleetHelper.expenseCategoryAll);
				
				ExpenseTrendingMultipleValues.verifyExpenseTrendingChartTooltip(FleetHelper.costPerServiceNumberChart, dataForExpenseTrending, FleetHelper.expenseCategoryAll);
				Thread.sleep(2000);
				
				FleetHelper.selectCategory(FleetHelper.expenseCategoryVoice);
				
				ExpenseTrendingMultipleValues.verifyExpenseTrendingChartTooltip(FleetHelper.costPerServiceNumberChart, dataForExpenseTrending, FleetHelper.expenseCategoryVoice);
				Thread.sleep(2000);
				
				FleetHelper.selectCategory(FleetHelper.expenseCategoryData);
				
				ExpenseTrendingMultipleValues.verifyExpenseTrendingChartTooltip(FleetHelper.costPerServiceNumberChart, dataForExpenseTrending, FleetHelper.expenseCategoryData);
				Thread.sleep(2000);
				
				FleetHelper.selectCategory(FleetHelper.expenseCategoryMessages);
				
				ExpenseTrendingMultipleValues.verifyExpenseTrendingChartTooltip(FleetHelper.costPerServiceNumberChart, dataForExpenseTrending, FleetHelper.expenseCategoryMessages);
				Thread.sleep(2000);
				
				FleetHelper.selectCategory(FleetHelper.expenseCategoryRoaming);
				
				ExpenseTrendingMultipleValues.verifyExpenseTrendingChartTooltip(FleetHelper.costPerServiceNumberChart, dataForExpenseTrending, FleetHelper.expenseCategoryRoaming);
				Thread.sleep(2000);
				 
				FleetHelper.selectCategory(FleetHelper.expenseCategoryEquipment);
				
				ExpenseTrendingMultipleValues.verifyExpenseTrendingChartTooltip(FleetHelper.costPerServiceNumberChart, dataForExpenseTrending, FleetHelper.expenseCategoryEquipment);
				Thread.sleep(2000);
				 
				FleetHelper.selectCategory(FleetHelper.expenseCategoryTaxes);
				
				ExpenseTrendingMultipleValues.verifyExpenseTrendingChartTooltip(FleetHelper.costPerServiceNumberChart, dataForExpenseTrending, FleetHelper.expenseCategoryTaxes);
				Thread.sleep(2000);
			 
				FleetHelper.selectCategory(FleetHelper.expenseCategoryOther);
				
				ExpenseTrendingMultipleValues.verifyExpenseTrendingChartTooltip(FleetHelper.costPerServiceNumberChart, dataForExpenseTrending, FleetHelper.expenseCategoryOther);
				Thread.sleep(2000);
				 
				FleetHelper.selectCategory(FleetHelper.expenseCategoryAccount);
				
				ExpenseTrendingMultipleValues.verifyExpenseTrendingChartTooltip(FleetHelper.costPerServiceNumberChart, dataForExpenseTrending, FleetHelper.expenseCategoryAccount);
				Thread.sleep(2000);
				
				
			} catch (NullPointerException e) {
				
				System.out.println("chart not found or value found is null");
				
			}
			
			indexMonthToSelect++;
			
		} while (!monthYearToSelect.equals(lastMonthListedMonthSelector) && indexMonthToSelect < monthsToSelect.size());
			
	
	}
	
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "Test for Cost Per Service Number values finished. Select OK to close browser.");
		driver.close();
		driver.quit();
	}

}
