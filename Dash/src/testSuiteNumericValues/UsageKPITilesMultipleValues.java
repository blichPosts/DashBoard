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
import usage.UsageKPITilesActions;

public class UsageKPITilesMultipleValues extends BaseClass{

	
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
 		
	}
	
	
	@Test
	public static void UsageKPITilesMultipleValuesTest() throws Exception
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
		
		List<List<UsageOneMonth>> listVendorsSelectedData = FleetHelper.getExpenseUsageDataForTest();

		// When more than one vendor is selected, the data needs to be summarized 
		List<UsageOneMonth> valuesSummarizedVendors = UsageHelper.summarizeDataUsageVendorsSelected(listVendorsSelectedData);
					
		
		// Get the last month listed on month selector 
		String lastMonthListedMonthSelector = GeneralHelper.getLastMonthFromSelector();
		String monthYearToSelect = "";
		
		int indexMonth = 0;
				
		do {
		
			// Get the data for the month indicated by "indexMonth"
			UsageOneMonth oneMonthData = valuesSummarizedVendors.get(indexMonth);
			
			String[] monthYear = UsageHelper.getMonthYearToSelect(oneMonthData);
			String month = monthYear[0];
			String year = monthYear[1];
			
			monthYearToSelect = CommonTestStepActions.convertMonthNumberToName(month, year);
			System.out.println("Month Year: " + monthYearToSelect);
			
			// #4 Select month on month/year selector
			CommonTestStepActions.selectMonthYearPulldown(monthYearToSelect);
			
			Thread.sleep(2000);
			
			String domesticVoiceUsage = oneMonthData.getDomesticVoice();
			String domesticVoiceOverageUsage = oneMonthData.getDomesticOverageVoice();
			String domesticMessagesUsage = oneMonthData.getDomesticMessages();
			String domesticDataUsage = oneMonthData.getDomesticDataUsageKb();
			String roamingDataUsage = oneMonthData.getRoamingDataUsageKb();
							
			// #5 Compare the values displayed on the KPIs to the values from spreadsheet
			UsageKPITilesActions.verifyKPItileValues(domesticVoiceUsage, domesticVoiceOverageUsage, domesticMessagesUsage, domesticDataUsage, roamingDataUsage);
			
			
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
			 
				UsageKPITilesActions.verifyThreeMonthRollingAverageAndTrendingValues(valuesForTrendingValue);
				
			}
			
			
			// #7 Calculate 6 Month Rolling Averages
			// Get the values needed to calculate the 6 month Rolling Averages
			// ONLY if there's data for five months before the current month. 
			// E.g.: Last month with data: January 2016, then June 2016 is the last month that will have the 6 month rolling average calculated
			if(indexMonth < valuesSummarizedVendors.size()-5){
				
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
				
				UsageKPITilesActions.verifySixMonthRollingAverage(valuesForSixMonthAverage);
				
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
