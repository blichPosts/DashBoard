package testSuiteHierarchyDashValues;

import java.util.List;

import javax.swing.JOptionPane;

import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenseHierarchy.HierarchyExpenseTrending;
import helperObjects.CommonTestStepActions;
import helperObjects.GeneralHelper;
import helperObjects.HierarchyHelper;
import helperObjects.HierarchyTrendData;
import helperObjects.ReadFilesHelper;


public class HierarchyValuesTestExpenseTrending extends BaseClass{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
		
	}


	@Test
	public static void HierarchyValuesTestExpenseTrendingTest() throws Exception
	{

		// *** Needed for Firefox *** :| 
		GeneralHelper.waitForHeaderVisible();
		
		// Enable Start collecting data
		ReadFilesHelper.startCollectingData();
		
		// #1 Select the "VIEW BY HIERARCHY" button
		HierarchyHelper.selectHierarchyView();
		
		// #2 Select hierarchy from dropdown , run the test for each hierarchy listed on dropdown
		List<WebElement> hierarchies = HierarchyHelper.getHierarchiesFromDropdown();
		List<String> monthsInDropdown = HierarchyHelper.getMonthsListedInDropdown();
		
		// If the test is run for a tenant that has only one hierarchy, then the Hierarchy dropdown list won't be displayed. 
		// 'amountHierarchies' is initialized in '1' for the test not to fail if the dropdown is not present.   
		int amountHierarchies = 1;
		
		if (hierarchies.size() > amountHierarchies) {
			amountHierarchies = hierarchies.size();
		}
		
		
		for (int i = 0; i < amountHierarchies; i++) {
		
			int monthIndex = 0;
			String hierarchyValue = HierarchyHelper.getHierarchyValue(i);
			
			while (!GeneralHelper.waitForChartToLoad(HierarchyHelper.expenseTrendingChart, MediumTimeout) && monthIndex < monthsInDropdown.size()) {
				
				String monthYearToSelect = monthsInDropdown.get(monthIndex);
				CommonTestStepActions.selectMonthYearPulldown(monthYearToSelect);
				Thread.sleep(2000);
				monthIndex++;
				
				ShowText("... no data for this chart... select a different month.. --> Month Year selected: " + monthYearToSelect);
			} 
			
			
			HierarchyHelper.waitForChartToLoad(HierarchyHelper.expenseTrendingChart);  // <-- seems that is not useful :| 
			Thread.sleep(3000);
			
			// #3 Get data from JSON
			List<HierarchyTrendData> valuesFromAjaxCall = ReadFilesHelper.getJsonDataTrend(hierarchyValue);
			

		
			// #4 Verify that the values displayed on the tooltips of "Expense Trending" chart are the same as the ones read from file
			// Note: Only the first month with data is selected for each vendor, since no matter which month is selected the same info
			// will be displayed on the Expense Trending chart. 
			
			try {
								
				HierarchyExpenseTrending.verifyExpenseTrendingChartTooltip(HierarchyHelper.expenseTrendingChart, valuesFromAjaxCall, HierarchyHelper.categoryTotal);
				
				HierarchyExpenseTrending.verifyExpenseTrendingChartTooltip(HierarchyHelper.expenseTrendingChart, valuesFromAjaxCall, HierarchyHelper.categoryOptimizable);
				
				HierarchyExpenseTrending.verifyExpenseTrendingChartTooltip(HierarchyHelper.expenseTrendingChart, valuesFromAjaxCall, HierarchyHelper.categoryRoaming);
				
			} catch (NullPointerException e) {
				
				System.out.println("chart not found or value found is null");
				
			}
		
		}
				
	}

	
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "Test for Expenses Trending values finished. Select OK to close browser.");
		driver.close();
		driver.quit();
	}
	
	
	
}
