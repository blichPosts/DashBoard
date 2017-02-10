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
import helperObjects.HierarchyHelper;
import helperObjects.HierarchyTrendData;
import helperObjects.ReadFilesHelper;
import helperObjects.UsageHelper;
import helperObjects.UsageOneMonth;
import usage.UsageKPITilesActions;

public class HierarchyTestKPITiles extends BaseClass{

	
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
 		
	}
	
	
	@Test
	public static void HierarchyKPITilesTest() throws Exception
	{
		
		
		// Select the "VIEW BY HIERARCHY" button
		HierarchyHelper.selectHierarchyView();
		

			
		String path = "D:\\Documents\\CMD Dashboard\\Hierarchy Dashboard\\";   //UsageHelper.path;
		String fileName = "testJsonFile";
		
		String file = fileName + ".txt";
		String completePath = path + file;
			
		// #2 Read data from file
		List<HierarchyTrendData> valuesFromFile = ReadFilesHelper.getHierarchyTrendData(completePath); // new ArrayList<>();
			
		
		// Get the last month listed on month selector 
		String lastMonthListedMonthSelector = driver.findElement(By.cssSelector(".tdb-pov__monthPicker>div>select>option:last-of-type")).getText();
		
		System.out.println("lastMonthListedMonthSelector: " + lastMonthListedMonthSelector);
		
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
		
		// index will be initialized with the last index of the list since the month info read from JSON is in chronological order;
		// and the months in the month selector are sorted the other way around 
		int indexMonth = valuesFromFile.size() - 1;
				
		
		do {
					
			// Get the data for the month indicated by "indexMonth"
			trendData = valuesFromFile.get(indexMonth);
			 
//			String[] monthYear = HierarchyHelper.getMonthYearToSelect(trendData);
			month = trendData.getOrdinalMonth();  //monthYear[0];
			year = trendData.getOrdinalYear();  //monthYear[1];
			
			
			monthYearToSelect = CommonTestStepActions.convertMonthNumberToName(month, year);
			System.out.println("Month Year: " + monthYearToSelect);
			
			// #4 Select month on month/year selector
			CommonTestStepActions.selectMonthYearPulldown(monthYearToSelect);
			
			// Wait for KPI tiles to be loaded
			HierarchyHelper.waitForKPIsToLoad(); // --works :D
						
//			month = Integer.toString(Integer.parseInt(month)-1);
			
//			totalExpense = trendData.getTotalExpense();
//			optimizableExpense = trendData.getOptimizableExpense();
//			roamingExpense = trendData.getRoamingExpense();
//			totalExpenseRollup = trendData.getTotalExpenseRollup();
//			optimizableExpenseRollup = trendData.getOptimizableExpenseRollup();
//			roamingExpenseRollup = trendData.getRoamingExpenseRollup();
//			numberOfLinesRollUp = trendData.getNumberOfLinesRollup();
							
			
			totalExpense = "0";
			optimizableExpense = "0";
			roamingExpense = "0";
			totalExpenseRollup = "0";
			optimizableExpenseRollup = "0";
			roamingExpenseRollup = "0";
			numberOfLinesRollUp = "0";
			
			
			// #5 Compare the values displayed on the KPIs to the values from spreadsheet
			HierarchyKPITilesValues.verifyKPItileValues(totalExpense, optimizableExpense, roamingExpense, totalExpenseRollup, 
					optimizableExpenseRollup, roamingExpenseRollup, numberOfLinesRollUp);
			
			
			// #6 Calculate 3 Month Rolling Averages and Trending Percentage
			// Get the values needed to calculate the 3 month Rolling Averages, and the Trending values
			// ONLY if there's data for two months before the current month. 
			// E.g.: Last month with data: January 2016, then March 2016 is the last month that will have the 3 month rolling average and trending values calculated
			if(indexMonth > 2){
				
				List<HierarchyTrendData> valuesForThreeMonthAverage = new ArrayList<HierarchyTrendData>();
				
				// Adds the current month values to the list
				valuesForThreeMonthAverage.add(valuesFromFile.get(indexMonth));
				// Adds the previous month values to the list
				valuesForThreeMonthAverage.add(valuesFromFile.get(indexMonth-1));
				// Adds values from 2 months ago to the list
				valuesForThreeMonthAverage.add(valuesFromFile.get(indexMonth-2));
			 
				HierarchyKPITilesValues.verifyThreeMonthRollingAverageAndTrendingValues(valuesForThreeMonthAverage);
				
			}
			
			
			// #7 Calculate 6 Month Rolling Averages
			// Get the values needed to calculate the 6 month Rolling Averages
			// ONLY if there's data for five months before the current month. 
			// E.g.: Last month with data: January 2016, then June 2016 is the last month that will have the 6 month rolling average calculated
			if(indexMonth > 5){
				
				List<HierarchyTrendData> valuesForSixMonthAverage = new ArrayList<HierarchyTrendData>();
				
				// Adds the current month values to the list
				valuesForSixMonthAverage.add(valuesFromFile.get(indexMonth));
				// Adds the previous month values to the list
				valuesForSixMonthAverage.add(valuesFromFile.get(indexMonth-1));
				// Adds values from 2 months ago to the list
				valuesForSixMonthAverage.add(valuesFromFile.get(indexMonth-2));
				// Adds values from 3 months ago to the list
				valuesForSixMonthAverage.add(valuesFromFile.get(indexMonth-3));
				// Adds values from 4 months ago to the list
				valuesForSixMonthAverage.add(valuesFromFile.get(indexMonth-4));
				// Adds values from 5 months ago to the list
				valuesForSixMonthAverage.add(valuesFromFile.get(indexMonth-5));
				
				HierarchyKPITilesValues.verifySixMonthRollingAverage(valuesForSixMonthAverage);
				
			}
			
			indexMonth--;
			
		} while (!monthYearToSelect.equals(lastMonthListedMonthSelector));
		
		Thread.sleep(2000);		
		
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
