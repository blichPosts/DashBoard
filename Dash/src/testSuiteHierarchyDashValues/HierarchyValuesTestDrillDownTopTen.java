package testSuiteHierarchyDashValues;

import java.util.List;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenseHierarchy.HierarchyTopTenValues;
import helperObjects.CommonTestStepActions;
import helperObjects.GeneralHelper;
import helperObjects.HierarchyHelper;
import helperObjects.ReadFilesHelper;


public class HierarchyValuesTestDrillDownTopTen extends BaseClass {

	
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
 		
	}
	
	
	@Test
	public static void HierarchyValuesTestDrillDownTopTenTest() throws Exception
	{
		
		// Enable Start collecting data
		ReadFilesHelper.startCollectingData();
		
		// #1 Select the "VIEW BY HIERARCHY" button
		HierarchyHelper.selectHierarchyView();
					
		// #2 Select the "TOP TEN VIEW" 
		HierarchyHelper.selectTopTenView(2);
		Thread.sleep(2000);
		
		// #3 Select hierarchy from dropdown , run the test for each hierarchy listed on dropdown
		List<WebElement> hierarchies = HierarchyHelper.getHierarchiesFromDropdown();
		List<String> hierarchyIds = HierarchyHelper.getHierarchiesValues();
		
		CommonTestStepActions.initializeMonthSelector();
		List<WebElement> monthsInSelector = CommonTestStepActions.webListPulldown;
		
		for (int i = 1; i <= hierarchies.size(); i++) {
			
			System.out.println(" **** Hierarchy " + hierarchies.get(i-1).getText());
			
			HierarchyHelper.selectHierarchyFromDropdown(i);
			Thread.sleep(2000);
			
			List<WebElement> dependentUnits = HierarchyHelper.getDependentUnitsPoV();
			List<WebElement> breadcrumbs = HierarchyHelper.getBreadcrumbs();
			
			// If there are no more dependent units to drill down, and there are breadcrumbs to go up on hierarchy, 
			// click on the first item on the breadcrumb to return to the top level of the hierarchy.
			if (dependentUnits.isEmpty() && !breadcrumbs.isEmpty()) {
				
				ShowText("No dependent units, click on breadcrumb.");
				HierarchyHelper.clickOnBreadcrumb(1);
				Thread.sleep(2000);
				
			}
			
			int j = 1;
			int levelsToDrillDown = 5;  // Drill down up to 5 levels
			
			while (j <= levelsToDrillDown) {
			
				int monthIndex = 1;
				
				// While hierarchy is in the Top level, and there are no dependent units to drill down, 
				// then select the previous month to see if it has dependent units to drill down.
				while (j == 1 && HierarchyHelper.getDependentUnitsPoV().isEmpty() && monthIndex < monthsInSelector.size()) {
					
					String monthYear = monthsInSelector.get(monthIndex).getText();
					CommonTestStepActions.selectMonthYearPulldown(monthYear);
					monthIndex++;
					
					ShowText("... no dependent units.. select a different month.. --> Month Year selected: " + monthYear);
					
					GeneralHelper.waitForDataToBeLoaded();
				
				}	
					
				// #4 If there are dependent units, drill down
				if (!HierarchyHelper.getDependentUnitsPoV().isEmpty()) {
					
					System.out.println(" **** Drilling down Level #" + j);
					
					int dependentUnitToDrillDown = HierarchyHelper.getDependentUnitToDrillDown(); 
					HierarchyHelper.drillDownOnDependentUnitPoV(dependentUnitToDrillDown);
					
					// Wait for chart to be reloaded after the drilling down action
					WaitForElementPresent(By.cssSelector("chart>div"), MediumTimeout);
							
					// #5 Verify that the values displayed on the tooltips of "Top Ten" chart are the same as the ones read from file
					
					try {
						
						// Run test for "Expense" chart and category "Total"
						HierarchyTopTenValues.verifyTopTenChartValues(hierarchyIds.get(i-1), HierarchyHelper.topTenChart, HierarchyHelper.categoryTotal);
						
						// Run test for "Expense" chart and category "Optimizable"
						HierarchyTopTenValues.verifyTopTenChartValues(hierarchyIds.get(i-1), HierarchyHelper.topTenChart, HierarchyHelper.categoryOptimizable);
						
						// Run test for "Expense" chart and category "Roaming"
						HierarchyTopTenValues.verifyTopTenChartValues(hierarchyIds.get(i-1), HierarchyHelper.topTenChart, HierarchyHelper.categoryRoaming);
						
						
					} catch(NullPointerException e) {
						
						System.out.println("chart not found");
						
					}
				
				}
				
				j++;
		
			}	
			
				
		}
			
	}
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "Test for Hierarchy Top Ten Drill Down values finished. Select OK to close browser.");
		driver.close();
		driver.quit();
	}

	
}
