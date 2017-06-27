package testSuiteHierarchyDashValues;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

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


public class HierarchyValuesTestDrillDownKPITiles extends BaseClass{

	
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
 		
	}
	
	
	@Test
	public static void HierarchyValuesTestDrillDownKPITilesTest() throws Exception
	{
		
		// Enable Start collecting data
		ReadFilesHelper.startCollectingData();
		
		// #1 Select the "VIEW BY HIERARCHY" button
		HierarchyHelper.selectHierarchyView();
		
		// #2 Select hierarchy from dropdown , run the test for each hierarchy listed on dropdown
		List<WebElement> hierarchies = HierarchyHelper.getHierarchiesFromDropdown();
		
		int amountHierarchies = 1;
		
		if (hierarchies.size() > amountHierarchies) {
			amountHierarchies = hierarchies.size();
		}
		
		for (int i = 0; i < amountHierarchies; i++) {
		
			GeneralHelper.selectFirstMonth();
			
			String hierarchyValue = HierarchyHelper.getHierarchyValue(i);
			
			List<WebElement> dependentUnits = HierarchyHelper.getDependentUnitsPoV();
			List<WebElement> breadcrumbs = HierarchyHelper.getBreadcrumbs();
			
			// If there are no more dependent units to drill down, and there are breadcrumbs to go up on hierarchy, 
			// click on the first item on the breadcrumb to return to the top level of the hierarchy.
			if (dependentUnits.isEmpty() && !breadcrumbs.isEmpty()) {
				
				HierarchyHelper.clickOnBreadcrumb(1);
				Thread.sleep(5000);
				
			}
			
			
			int numLevel = 1;
			int levelsToDrillDown = 3;  // Drill down up to <levelsToDrillDown> levels
			int monthIndex = 0;
			
			
			while (numLevel <= levelsToDrillDown) {
			
				ShowText(" **** Drilling down Level #" + numLevel);
				
				List<String> monthsInDropdown = HierarchyHelper.getMonthsListedInDropdown();
				int indexMonth = 0;
				
				CommonTestStepActions.selectMonthYearPulldown(monthsInDropdown.get(indexMonth));
				
				// While there are no dependent units to drill down then select the previous month
				while (HierarchyHelper.getDependentUnitsPoV().isEmpty() && monthIndex < monthsInDropdown.size()) {
					
					String monthYear = monthsInDropdown.get(monthIndex);
					indexMonth = monthIndex;
					CommonTestStepActions.selectMonthYearPulldown(monthYear);
					monthIndex++;
										
					GeneralHelper.waitForDataToBeLoaded();
					
				}	
				
				ShowText(" **** Month year selected: " + CommonTestStepActions.GetPulldownTextSelected());
				
				if (!HierarchyHelper.getDependentUnitsPoV().isEmpty() && monthIndex < monthsInDropdown.size()) {
				
					int numDependentUnit = HierarchyHelper.getDependentUnitToDrillDown();
					HierarchyHelper.drillDownOnDependentUnitPoV(numDependentUnit);
					
					// Wait for 5 seconds to give time to the KPI tile to load data after the drilling down action
					Thread.sleep(5000);
								
					// #3 Get data from JSON
					List<HierarchyTrendData> valuesFromAjaxCall = ReadFilesHelper.getJsonDataTrend(hierarchyValue);
				
				
					// Get the data for the month indicated by "indexMonth"
					HierarchyTrendData trendData = valuesFromAjaxCall.get(indexMonth);
										
					ShowText("month/year: " + trendData.getOrdinalMonth() + "/" + trendData.getOrdinalYear());
					
					String totalExpense = trendData.getTotalExpense();
					String optimizableExpense = trendData.getOptimizableExpense();
					String roamingExpense = trendData.getRoamingExpense();
					String totalExpenseRollup = trendData.getTotalExpenseRollup();
					String optimizableExpenseRollup = trendData.getOptimizableExpenseRollup();
					String roamingExpenseRollup = trendData.getRoamingExpenseRollup();
					String numberOfLinesRollUp = trendData.getNumberOfLinesRollup();
				
					
					// #6 Compare the values displayed on the KPIs to the values from spreadsheet
					HierarchyKPITilesValues.verifyKPItileValues(totalExpense, optimizableExpense, roamingExpense, totalExpenseRollup, 
							optimizableExpenseRollup, roamingExpenseRollup, numberOfLinesRollUp);
					
					
					// #7 Calculate 3 Month Rolling Averages and Trending Percentage
					// Get the values needed to calculate the 3 month Rolling Averages, and the Trending values
					// ONLY if there's data for two months before the current month. 
					// E.g.: Last month with data: January 2016, then March 2016 is the last month that will have the 3 month rolling average and trending values calculated
					if(indexMonth < valuesFromAjaxCall.size() - 2){
						
						List<HierarchyTrendData> valuesForThreeMonthAverage = new ArrayList<HierarchyTrendData>();
						
						// Adds the current month values to the list
						valuesForThreeMonthAverage.add(valuesFromAjaxCall.get(indexMonth));
						// Adds the previous month values to the list
						valuesForThreeMonthAverage.add(valuesFromAjaxCall.get(indexMonth+1));
						// Adds values from 2 months ago to the list
						valuesForThreeMonthAverage.add(valuesFromAjaxCall.get(indexMonth+2));
					 
						HierarchyKPITilesValues.verifyThreeMonthRollingAverageAndTrendingValues(valuesForThreeMonthAverage);
						
					}
					
					
					// #8 Calculate 6 Month Rolling Averages
					// Get the values needed to calculate the 6 month Rolling Averages
					// ONLY if there's data for five months before the current month. 
					// E.g.: Last month with data: January 2016, then June 2016 is the last month that will have the 6 month rolling average calculated
					if(indexMonth < valuesFromAjaxCall.size() - 5){
						
						List<HierarchyTrendData> valuesForSixMonthAverage = new ArrayList<HierarchyTrendData>();
						
						// Adds the current month values to the list
						valuesForSixMonthAverage.add(valuesFromAjaxCall.get(indexMonth));
						// Adds the previous month values to the list
						valuesForSixMonthAverage.add(valuesFromAjaxCall.get(indexMonth+1));
						// Adds values from 2 months ago to the list
						valuesForSixMonthAverage.add(valuesFromAjaxCall.get(indexMonth+2));
						// Adds values from 3 months ago to the list
						valuesForSixMonthAverage.add(valuesFromAjaxCall.get(indexMonth+3));
						// Adds values from 4 months ago to the list
						valuesForSixMonthAverage.add(valuesFromAjaxCall.get(indexMonth+4));
						// Adds values from 5 months ago to the list
						valuesForSixMonthAverage.add(valuesFromAjaxCall.get(indexMonth+5));
						
						HierarchyKPITilesValues.verifySixMonthRollingAverage(valuesForSixMonthAverage);
						
					}
					
					indexMonth++;
					
				}
				
				numLevel++;
				
			}
		
		}
		
	}
	
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		ShowText("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "Test for Hierarchy KPI Tiles Drill Down values finished. Select OK to close browser.");
		driver.close();
		driver.quit();
	}

	
	
}
