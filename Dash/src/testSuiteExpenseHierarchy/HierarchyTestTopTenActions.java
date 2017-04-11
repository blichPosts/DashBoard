package testSuiteExpenseHierarchy;

import java.util.List;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenseHierarchy.HierarchyTopTenActions;
import helperObjects.CommonTestStepActions;
import helperObjects.GeneralHelper;
import helperObjects.HierarchyHelper;


public class HierarchyTestTopTenActions extends BaseClass {
	
	
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
 		
	}
	
	/*
	 * 1. Verify the hierarchy name on the main title, along with the month selected --> ** DONE **
	 * 2. Take the name of a dependent unit. Drill down. Verify the dependent unit's name gets updated above chart  --> ** DONE **
	 * 3. Verify that after drilling down, the parent's name is updated on the breadcrumb. --> ** DONE **
	 * 4. Select different categories on category selector. Verify the category name gets updated on the "Top 10 Service Numbers by Expense Amount - " label --> ** DONE **
	 * */
	
	@Test
	public static void HierarchyTestTopTenActionsTest() throws Exception
	{
		
		// #1 Select the "VIEW BY HIERARCHY" button
		HierarchyHelper.selectHierarchyView();
					
		// #2 Select the "TOP TEN VIEW" 
		HierarchyHelper.selectTopTenView(2);
		Thread.sleep(2000);
		
		// #3 Select hierarchy from dropdown
		List<WebElement> hierarchies = HierarchyHelper.getHierarchiesFromDropdown();


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
			
			while (j <= levelsToDrillDown && !HierarchyHelper.getDependentUnitsPoV().isEmpty()) {
			
				System.out.println(" **** Drilling down Level #" + j);
			
				GeneralHelper.selectFirstMonth();
				boolean monthSelected = true;
				Thread.sleep(2000);
				
				HierarchyHelper.selectHierarchyFromDropdown(i);
				Thread.sleep(2000);
			
				// Verify hierarchy name on main title
				HierarchyTopTenActions.verifyHierarchyNameOnTitle();
				
				// Verify dependent unit's name on main title
				HierarchyTopTenActions.verifyDependentUnitOnTitleAndBreadcrumbs(j);
				
				
				// #4 Get the last month listed on month selector
				List<String> monthsInDropdown = HierarchyHelper.getMonthsListedInDropdown(); 
		
				String lastMonthListedMonthSelector = monthsInDropdown.get(monthsInDropdown.size()-1);
				String monthYearToSelect = "";
				
				int indexMonth = 0;
				
				do {
					
					if (!monthSelected) {
						
						monthYearToSelect = monthsInDropdown.get(indexMonth);
						System.out.println("Month Year: " + monthYearToSelect);
						
						// #5 Select month on month/year selector
						CommonTestStepActions.selectMonthYearPulldown(monthYearToSelect);
						
						// Wait for chart to be loaded
						WaitForElementVisible(By.cssSelector("chart>div"), ExtremeTimeout);
						Thread.sleep(2000);
						
					}
					
					// Verify month and year on main title
					HierarchyTopTenActions.verifyMonthYearOnTitle();
					
					// #6 Verify categories are updated on "Top 10 Service Numbers by Expense Amount - " label
					
					// Select category "Total"
					HierarchyTopTenActions.verifyTopTenLabelCategory(HierarchyHelper.topTenChart, HierarchyHelper.categoryTotal);
					
					// Select category "Optimizable"
					HierarchyTopTenActions.verifyTopTenLabelCategory(HierarchyHelper.topTenChart, HierarchyHelper.categoryOptimizable);
										
					// Select category "Roaming"
					HierarchyTopTenActions.verifyTopTenLabelCategory(HierarchyHelper.topTenChart, HierarchyHelper.categoryRoaming);
						
					indexMonth++;
					monthSelected = false;
					
				} while (!monthYearToSelect.equals(lastMonthListedMonthSelector));
				
				j++;
		
			}	
				
		}	
		
	}
	

	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "Test for Hierarchy Top Ten Actions finished. Select OK to close browser.");
		driver.close();
		driver.quit();
	}
	

}
