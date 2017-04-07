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


public class HierarchyValuesTestDrillDownExpensesTrending extends BaseClass{

	
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
 		
	}
	
	
	@Test
	public static void HierarchyValuesTestDrillDownExpensesTrendingTest() throws Exception
	{
		
		// Enable Start collecting data
		ReadFilesHelper.startCollectingData();
		
		// #1 Select the "VIEW BY HIERARCHY" button
		HierarchyHelper.selectHierarchyView();
		
		// #2 Select hierarchy from dropdown , run the test for each hierarchy listed on dropdown
		List<WebElement> hierarchies = HierarchyHelper.getHierarchiesFromDropdown();
		List<String> hierarchyIds = HierarchyHelper.getHierarchiesValues();
		
		
		for (int i = 1; i <= hierarchies.size(); i++) {
		
			System.out.println(" **** Hierarchy " + hierarchies.get(i-1).getText());
			
			List<WebElement> dependentUnits = HierarchyHelper.getDependentUnitsPoV();
			List<WebElement> breadcrumbs = HierarchyHelper.getBreadcrumbs();
			
			// If there are no more dependent units to drill down, and there are breadcrumbs to go up on hierarchy, 
			// click on the first item on the breadcrumb to return to the top level of the hierarchy.
			if (dependentUnits.isEmpty() && !breadcrumbs.isEmpty()) {
				
				ShowText("No dependent units, click on breadcrumb.");
				HierarchyHelper.clickOnBreadcrumb(1);
				Thread.sleep(2000);
				
			} 
			
			GeneralHelper.selectFirstMonth();
			
			int j = 1;
			int levelsToDrillDown = 5;  // Drill down up to 3 levels
			boolean allMonthsVerified = false; 
			
			while (j <= levelsToDrillDown && !allMonthsVerified) { 
			
				int monthIndex = 1;
//				boolean drilledDown = false;
				
				
				HierarchyHelper.selectHierarchyFromDropdown(i);
				HierarchyHelper.waitForTileMapToBeDisplayed();
//				Thread.sleep(2000);
				
				List<WebElement> monthsInSelector = CommonTestStepActions.webListPulldown;
				
				// While there are no dependent units to drill down then select the previous month
				while (HierarchyHelper.getDependentUnitsPoV().isEmpty() && monthIndex < monthsInSelector.size()) {
					
					ShowText("... no dependent units.. select a different month..");
					String monthYear = monthsInSelector.get(monthIndex).getText();
					CommonTestStepActions.selectMonthYearPulldown(monthYear);
					monthIndex++;
					System.out.println(" **** Month Year: " + monthYear);
					GeneralHelper.waitForDataToBeLoaded();
					
				} //-- TEST
					
				if (!HierarchyHelper.getDependentUnitsPoV().isEmpty() && monthIndex < monthsInSelector.size()) {  // && !drilledDown) {
					
					System.out.println(" **** Drilling down Level #" + j);
					
					HierarchyHelper.drillDownOnDependentUnitPoV(1);
					Thread.sleep(3000);
					
					// #3 Get data from JSON
					List<HierarchyTrendData> valuesFromFile = ReadFilesHelper.getJsonDataTrend(hierarchyIds.get(i-1));	
				
					// #4 Verify that the values displayed on the tooltips of "Expenses Trending" chart are the same as the ones read from ajax calls 
					// Note: Only the first month with data is selected for each vendor, since no matter which month is selected the same info
					// will be displayed on the chart 
					
					try {
						
						HierarchyHelper.selectCategory(HierarchyHelper.expenseTrendingChart, HierarchyHelper.categoryTotal);
						
						HierarchyExpenseTrending.verifyExpenseTrendingChartTooltip(HierarchyHelper.expenseTrendingChart, valuesFromFile, HierarchyHelper.categoryTotal);
						Thread.sleep(2000);
						
						HierarchyHelper.selectCategory(HierarchyHelper.expenseTrendingChart, HierarchyHelper.categoryOptimizable);
						
						HierarchyExpenseTrending.verifyExpenseTrendingChartTooltip(HierarchyHelper.expenseTrendingChart, valuesFromFile, HierarchyHelper.categoryOptimizable);
						Thread.sleep(2000);
						
						HierarchyHelper.selectCategory(HierarchyHelper.expenseTrendingChart, HierarchyHelper.categoryRoaming);
						
						HierarchyExpenseTrending.verifyExpenseTrendingChartTooltip(HierarchyHelper.expenseTrendingChart, valuesFromFile, HierarchyHelper.categoryRoaming);
						Thread.sleep(2000);
											
					} catch (NullPointerException e) {
						
						System.out.println("chart not found");
						
					}
					
					
//					drilledDown = true;
					j++;
				}
				
				if (monthIndex == monthsInSelector.size()) {
					allMonthsVerified = true;
				}
					
					
//				} // TEST
				
//				j++;
				
			}
		
		}
		
	}
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "Test for Hierarchy Expenses Trending Drill Down values finished. Select OK to close browser.");
		driver.close();
		driver.quit();
	}

	
	
}
