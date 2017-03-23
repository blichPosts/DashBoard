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
		
		for (int i = 1; i <= hierarchies.size(); i++) {
			
			System.out.println(" **** Hierarchy " + hierarchies.get(i-1).getText());
			
			List<WebElement> dependentUnits = HierarchyHelper.getDependentUnitsPoV();
			List<WebElement> breadcrumbs = HierarchyHelper.getBreadcrumbs();
			
			// If there are no more dependent units to drill down, and there are breadcrumbs to go up on hierarchy, 
			// click on the first item on the breadcrumb to return to the top level of the hierarchy.
			if (dependentUnits.isEmpty() && !breadcrumbs.isEmpty()) {
				
				HierarchyHelper.clickOnBreadcrumb(1);
				Thread.sleep(2000);
				
			}
			
			int j = 1;
			int levelsToDrillDown = 3;  // Drill down up to 3 levels
			
			int monthIndex = 1;
						
			while (j <= levelsToDrillDown) {
			
				System.out.println(" **** Drilling down Level #" + j);
			
				GeneralHelper.selectFirstMonth();
				boolean monthSelected = true;
				Thread.sleep(2000);
				
				HierarchyHelper.selectHierarchyFromDropdown(i);
				Thread.sleep(2000);
			
				List<WebElement> monthsInSelector = CommonTestStepActions.webListPulldown;
				
				// While there are no dependent units to drill down then select the previous month
				while (HierarchyHelper.getDependentUnitsPoV().isEmpty() && monthIndex < monthsInSelector.size()) {
					
					String monthYear = monthsInSelector.get(monthIndex).getText();
					CommonTestStepActions.selectMonthYearPulldown(monthYear);
					monthIndex++;
					System.out.println(" **** Month Year: " + monthYear);
					
					GeneralHelper.waitForDataToBeLoaded();
				}	
					
				if (!HierarchyHelper.getDependentUnitsPoV().isEmpty() && monthIndex < monthsInSelector.size()) {
				
					HierarchyHelper.drillDownOnDependentUnitPoV(1);
					
					// Wait for 5 seconds to give time to the KPI tile to load data after the drilling down action
					Thread.sleep(5000);
					
				
					// #4 Get the last month listed on month selector
					List<String> monthsInDropdown = HierarchyHelper.getMonthsListedInDropdown(); 
			
					String lastMonthListedMonthSelector = monthsInDropdown.get(monthsInDropdown.size()-1);
					String monthYearToSelect = "";
					
					int indexMonth = monthIndex - 1;
					
//						do {
						
//							if (!monthSelected) {
//								
//								monthYearToSelect = monthsInDropdown.get(indexMonth);
//								System.out.println("Month Year: " + monthYearToSelect);
//								
//								// #5 Select month on month/year selector
//								CommonTestStepActions.selectMonthYearPulldown(monthYearToSelect);
//								
//								// Wait for chart to be loaded
//								WaitForElementVisible(By.cssSelector("chart>div"), ExtremeTimeout);
//								
//							}
						
						
						// #6 Verify that the values displayed on the tooltips of "Top Ten" chart are the same as the ones read from file
						
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
						
						indexMonth++;
						monthSelected = false;
						
//						} while (!monthYearToSelect.equals(lastMonthListedMonthSelector));
			
				}
					
//				}
				
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
