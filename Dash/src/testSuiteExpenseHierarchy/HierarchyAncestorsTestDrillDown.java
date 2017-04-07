package testSuiteExpenseHierarchy;

import java.util.List;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenseHierarchy.HierarchyAncestors;
import helperObjects.AncestorsInfo;
import helperObjects.CommonTestStepActions;
import helperObjects.GeneralHelper;
import helperObjects.HierarchyHelper;
import helperObjects.ReadFilesHelper;

public class HierarchyAncestorsTestDrillDown extends BaseClass{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
		
	}


	@Test
	public static void HierarchyAncestorsTestDrillDownTest() throws Exception
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
			HierarchyHelper.selectHierarchyFromDropdown(i);
			Thread.sleep(2000);
				
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
			int levelsToDrillDown = 5;  // Drill down up to 5 levels
			int monthIndex = 1;
			
			while (j <= levelsToDrillDown) {
			
				System.out.println(" **** Drilling down Level #" + j);
				
				GeneralHelper.selectFirstMonth();
				boolean monthSelected = true;
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
					
					// Wait for 3 seconds to give time to load data after the drilling down action
					Thread.sleep(3000);
					
					// #3 Get data from JSON
					List<AncestorsInfo> valuesFromFile = ReadFilesHelper.getJsonDataAncestors(hierarchyIds.get(i-1));
					
					
					// #4 Get the last month listed on month selector 
					String lastMonthListedMonthSelector = driver.findElement(By.cssSelector(".tdb-pov__monthPicker>div>select>option:last-of-type")).getText();
					
					AncestorsInfo ancestorsData;
					String year = "";
					String month = "";
					String monthYearToSelect = "";
											
					int indexMonth = monthIndex - 1;
					
					
					do {
								
						// Get the data for the month indicated by "indexMonth"
						ancestorsData = valuesFromFile.get(indexMonth);
						
						if (!monthSelected) {
							
							month = ancestorsData.getOrdinalMonth();
							year = ancestorsData.getOrdinalYear();
							
							monthYearToSelect = CommonTestStepActions.convertMonthNumberToName(month, year);
							System.out.println("Month Year: " + monthYearToSelect);
							
							// #5 Select month on month/year selector
							CommonTestStepActions.selectMonthYearPulldown(monthYearToSelect);
							
							// Wait for data to be loaded
							Thread.sleep(2000);
							
						}
						
						HierarchyAncestors.verifyAncestorsAndBreadcrumbs(ancestorsData);
						
						indexMonth++;
						monthSelected = false;
						
					} while (!monthYearToSelect.equals(lastMonthListedMonthSelector) && indexMonth < valuesFromFile.size());
				
				}
				
				j++;
				
			}
		
		}
				
	}

	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "Test for Ancestors on Hierarchy Dashboard finished. Select OK to close browser.");
		driver.close();
		driver.quit();
	}
	
}
