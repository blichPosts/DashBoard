package testSuiteNumericValues;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenses.ExpensesKPITilesValues;
import helperObjects.CommonTestStepActions;
import helperObjects.FleetHelper;
import helperObjects.GeneralHelper;
import helperObjects.ReadFilesHelper;
import helperObjects.UsageHelper;
import helperObjects.UsageOneMonth;


public class ExpensesKPITilesMultipleValues extends BaseClass{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
 		
	}
	
	
	@Test
	public static void ExpensesKPITilesMultipleValuesTest() throws Exception
	{
		
		// Enable Start collecting data
		ReadFilesHelper.startCollectingData();
		Thread.sleep(2000);
		
		// Reload Fleet data
		ReadFilesHelper.reloadFleetData();
		Thread.sleep(2000);
		
		// Wait for countries and vendors to be loaded on PoV section
		FleetHelper.waitForPoVSectionToBeLoaded(); 
					
		CommonTestStepActions.GoToExpensePageDetailedWait();

		
		// #1 Select Vendor View and Unselect all vendors  
		CommonTestStepActions.SelectVendorView();
		//CommonTestStepActions.UnSelectAllVendors();
					
						
		List<List<UsageOneMonth>> listVendorsSelectedData = FleetHelper.getExpenseUsageDataForTest();
		List<UsageOneMonth> valuesSummarizedVendors = UsageHelper.summarizeDataExpensesVendorsSelected(listVendorsSelectedData);
		
		String lastMonthListedMonthSelector = GeneralHelper.getLastMonthFromSelector();
		String monthYearToSelect = "";

		int indexMonth = 0;
		
		do {
		
			UsageOneMonth oneMonthData = valuesSummarizedVendors.get(indexMonth);
			 
			String[] monthYear = UsageHelper.getMonthYearToSelect(oneMonthData);
			String month = monthYear[0];
			String year = monthYear[1];
			
			monthYearToSelect = CommonTestStepActions.convertMonthNumberToName(month, year);
			System.out.println("Month Year: " + monthYearToSelect);
			
			// #4 Select month on month/year selector
			CommonTestStepActions.selectMonthYearPulldown(monthYearToSelect);
			
			Thread.sleep(2000);
			
			String totalCharge = oneMonthData.getTotalCharge();
			String numberOfLines = oneMonthData.getNumberOfLines();
							
			// #5 Compare the values displayed on the KPIs to the values from spreadsheet
			ExpensesKPITilesValues.verifyKPItileValues(totalCharge, numberOfLines);
			
			
			// #6 Calculate 3 Month Rolling Averages and Trending Percentage
			// Get the values needed to calculate the 3 month Rolling Averages, and the Trending values
			// ONLY if there's data for two months before the current month. 
			// E.g.: Last month with data: January 2016, then March 2016 is the last month that will have the 3 month rolling average and trending values calculated
			if(indexMonth < valuesSummarizedVendors.size()-2){
				
				List<UsageOneMonth> valuesForTrendingValue = new ArrayList<UsageOneMonth>();
				
				// Adds the current month values to the list
				valuesForTrendingValue.add(valuesSummarizedVendors.get(indexMonth));
				// Adds the previous month values to the list
				valuesForTrendingValue.add(valuesSummarizedVendors.get(indexMonth+1));
				// Adds values from 2 months ago to the list
				valuesForTrendingValue.add(valuesSummarizedVendors.get(indexMonth+2));
			 	
				ExpensesKPITilesValues.verifyThreeMonthRollingAverageAndTrendingValues(valuesForTrendingValue);
				
			}
			
			
			// #7 Calculate 6 Month Rolling Averages
			// Get the values needed to calculate the 6 month Rolling Averages
			// ONLY if there's data for five months before the current month. 
			// E.g.: Last month with data: January 2016, then June 2016 is the last month that will have the 6 month rolling average calculated
			if (indexMonth < valuesSummarizedVendors.size()-5) {
				
				List<UsageOneMonth> valuesForSixMonthAverage = new ArrayList<UsageOneMonth>();
				
				// Adds the current month values to the list
				valuesForSixMonthAverage.add(valuesSummarizedVendors.get(indexMonth));
				// Adds the previous month values to the list
				valuesForSixMonthAverage.add(valuesSummarizedVendors.get(indexMonth+1));
				// Adds values from 2 months ago to the list
				valuesForSixMonthAverage.add(valuesSummarizedVendors.get(indexMonth+2));
				// Adds values from 3 months ago to the list
				valuesForSixMonthAverage.add(valuesSummarizedVendors.get(indexMonth+3));
				// Adds values from 4 months ago to the list
				valuesForSixMonthAverage.add(valuesSummarizedVendors.get(indexMonth+4));
				// Adds values from 5 months ago to the list
				valuesForSixMonthAverage.add(valuesSummarizedVendors.get(indexMonth+5));
				
				ExpensesKPITilesValues.verifySixMonthRollingAverage(valuesForSixMonthAverage);
				
			}
			
			indexMonth++;
			
		} while (!monthYearToSelect.equals(lastMonthListedMonthSelector));
		
		Thread.sleep(2000);		
		
	}
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "Test for KPI tiles values finished. Select OK to close browser.");
		driver.close();
		driver.quit();
		
	}

}

