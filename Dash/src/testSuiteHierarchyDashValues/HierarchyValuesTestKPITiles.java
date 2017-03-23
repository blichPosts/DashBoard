package testSuiteHierarchyDashValues;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenseHierarchy.HierarchyKPITilesValues;
import helperObjects.CommonTestStepActions;
import helperObjects.GeneralHelper;
import helperObjects.HierarchyHelper;
import helperObjects.HierarchyTrendData;
import helperObjects.ReadFilesHelper;


public class HierarchyValuesTestKPITiles extends BaseClass{

	
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
 		
	}
	
	
	@Test
	public static void HierarchyValuesTestKPITilesTest() throws Exception
	{
		
		// Enable Start collecting data
		ReadFilesHelper.startCollectingData();
		
		// #1 Select the "VIEW BY HIERARCHY" button
		HierarchyHelper.selectHierarchyView();
		
		// #2 Select hierarchy from dropdown , run the test for each hierarchy listed on dropdown
		List<WebElement> hierarchies = HierarchyHelper.getHierarchiesFromDropdown();
		List<String> hierarchyIds = HierarchyHelper.getHierarchiesValues();
		
		for (int i = 1; i <= hierarchies.size(); i++) {
			
			GeneralHelper.selectFirstMonth();
			boolean monthSelected = true;
			
			HierarchyHelper.selectHierarchyFromDropdown(i);
			System.out.println(" **** Hierarchy " + hierarchies.get(i-1).getText());
			
			Thread.sleep(3000);
			
			// #3 Get data from JSON
			List<HierarchyTrendData> valuesFromFile = ReadFilesHelper.getJsonDataTrend(hierarchyIds.get(i-1));	
					
					
			// #4 Get the last month listed on month selector 
			String lastMonthListedMonthSelector = driver.findElement(By.cssSelector(".tdb-pov__monthPicker>div>select>option:last-of-type")).getText();
			
			HierarchyTrendData trendData;
			String year = "";
			String month = "";
			String monthYearToSelect = "";
			
			String totalExpense;
			String optimizableExpense;
			String roamingExpense;
			String totalExpenseRollup;
			String optimizableExpenseRollup;
			String roamingExpenseRollup;
			String numberOfLinesRollUp;
			
			
			int indexMonth = 0;
			
			do {
						
				// Get the data for the month indicated by "indexMonth"
				trendData = valuesFromFile.get(indexMonth);
				
				if (!monthSelected) {
					
					month = trendData.getOrdinalMonth();
					year = trendData.getOrdinalYear();
					
					monthYearToSelect = CommonTestStepActions.convertMonthNumberToName(month, year);
					System.out.println("Month Year: " + monthYearToSelect);
					
					// #5 Select month on month/year selector
					CommonTestStepActions.selectMonthYearPulldown(monthYearToSelect);
					
					// Wait for KPI tiles to be loaded
					Thread.sleep(2000);
					
				}
				
				totalExpense = trendData.getTotalExpense();
				optimizableExpense = trendData.getOptimizableExpense();
				roamingExpense = trendData.getRoamingExpense();
				totalExpenseRollup = trendData.getTotalExpenseRollup();
				optimizableExpenseRollup = trendData.getOptimizableExpenseRollup();
				roamingExpenseRollup = trendData.getRoamingExpenseRollup();
				numberOfLinesRollUp = trendData.getNumberOfLinesRollup();
			
				
				// #6 Compare the values displayed on the KPIs to the values from spreadsheet
				HierarchyKPITilesValues.verifyKPItileValues(totalExpense, optimizableExpense, roamingExpense, totalExpenseRollup, 
						optimizableExpenseRollup, roamingExpenseRollup, numberOfLinesRollUp);
				
				
				// #7 Calculate 3 Month Rolling Averages and Trending Percentage
				// Get the values needed to calculate the 3 month Rolling Averages, and the Trending values
				// ONLY if there's data for two months before the current month. 
				// E.g.: Last month with data: January 2016, then March 2016 is the last month that will have the 3 month rolling average and trending values calculated
				if(indexMonth < valuesFromFile.size() - 2){
					
					List<HierarchyTrendData> valuesForThreeMonthAverage = new ArrayList<HierarchyTrendData>();
					
					// Adds the current month values to the list
					valuesForThreeMonthAverage.add(valuesFromFile.get(indexMonth));
					// Adds the previous month values to the list
					valuesForThreeMonthAverage.add(valuesFromFile.get(indexMonth+1));
					// Adds values from 2 months ago to the list
					valuesForThreeMonthAverage.add(valuesFromFile.get(indexMonth+2));
				 
					HierarchyKPITilesValues.verifyThreeMonthRollingAverageAndTrendingValues(valuesForThreeMonthAverage);
					
				}
				
				
				// #8 Calculate 6 Month Rolling Averages
				// Get the values needed to calculate the 6 month Rolling Averages
				// ONLY if there's data for five months before the current month. 
				// E.g.: Last month with data: January 2016, then June 2016 is the last month that will have the 6 month rolling average calculated
				if(indexMonth < valuesFromFile.size() - 5){
					
					List<HierarchyTrendData> valuesForSixMonthAverage = new ArrayList<HierarchyTrendData>();
					
					// Adds the current month values to the list
					valuesForSixMonthAverage.add(valuesFromFile.get(indexMonth));
					// Adds the previous month values to the list
					valuesForSixMonthAverage.add(valuesFromFile.get(indexMonth+1));
					// Adds values from 2 months ago to the list
					valuesForSixMonthAverage.add(valuesFromFile.get(indexMonth+2));
					// Adds values from 3 months ago to the list
					valuesForSixMonthAverage.add(valuesFromFile.get(indexMonth+3));
					// Adds values from 4 months ago to the list
					valuesForSixMonthAverage.add(valuesFromFile.get(indexMonth+4));
					// Adds values from 5 months ago to the list
					valuesForSixMonthAverage.add(valuesFromFile.get(indexMonth+5));
					
					HierarchyKPITilesValues.verifySixMonthRollingAverage(valuesForSixMonthAverage);
					
				}
				
				// #9 Verify the amount of service numbers and the amount of invoices loaded
				// ** The amount of service numbers and the amount of invoices loaded will be displayed only if there's data for the month
				if (trendData.getHasData()) {
					
					HierarchyKPITilesValues.verifyServiceNumbersAmount(trendData.getNumberOfLinesRollup());
					HierarchyKPITilesValues.verifyNumberInvoices(trendData.getTotalNumberOfInvoices());
					
				}
				
				indexMonth++;
				monthSelected = false;
				
			} while (!monthYearToSelect.equals(lastMonthListedMonthSelector));
			
		}
					
	}
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "Test for Hierarchy KPI Tiles values finished. Select OK to close browser.");
		driver.close();
		driver.quit();
	}

	
	
}
