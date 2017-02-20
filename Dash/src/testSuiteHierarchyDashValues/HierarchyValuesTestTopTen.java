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
import helperObjects.HierarchyTopTenData;
import helperObjects.ReadFilesHelper;


public class HierarchyValuesTestTopTen extends BaseClass {

	
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
 		
	}
	
	
	@Test
	public static void HierarchyTestValuesTopTenTest() throws Exception
	{
		
		// Enable Start collecting data
		ReadFilesHelper.startCollectingData();
		
		// #1 Select the "VIEW BY HIERARCHY" button
		HierarchyHelper.selectHierarchyView();
					
		// #2 Select the "TOP TEN VIEW" 
		HierarchyHelper.selectTopTenView();
		Thread.sleep(3000);
		
		// #2 Select hierarchy from dropdown , run the test for each hierarchy listed on dropdown
		List<WebElement> hierarchies = HierarchyHelper.getHierarchiesFromDropdown();
		
		for (int i = 1; i <= hierarchies.size(); i++) {
			
			GeneralHelper.selectFirstMonth();
			HierarchyHelper.selectHierarchyFromDropdown(i);
			boolean monthSelected = true;
			Thread.sleep(3000);
		
		
			// #3 Get the last month listed on month selector
			List<String> monthsInDropdown = HierarchyHelper.getMonthsListedInDropdown(); 
	
			String lastMonthListedMonthSelector = monthsInDropdown.get(monthsInDropdown.size()-1);  // driver.findElement(By.cssSelector(".tdb-pov__monthPicker>div>select>option:last-of-type")).getText();
			String monthYearToSelect = "";
			
			int indexMonth = 0;
			
			do {
				
				if (!monthSelected) {
					
					monthYearToSelect = monthsInDropdown.get(indexMonth);
					System.out.println("Month Year: " + monthYearToSelect);
					
					// #4 Select month on month/year selector
					CommonTestStepActions.selectMonthYearPulldown(monthYearToSelect);
					
					// Wait for chart to be loaded
					WaitForElementVisible(By.cssSelector("chart>div"), ExtremeTimeout);
					
				}
				
				List<HierarchyTopTenData> topTenValuesExpected; 
				
				// #5 Verify that the values displayed on the tooltips of "Top Ten" chart are the same as the ones read from file
				
				try {
					
					// Select category "Total Expense"
					HierarchyHelper.selectCategory(HierarchyHelper.categoryTotal);
	
					// Wait for the data to be updated on chart
					Thread.sleep(3000);
				
					// Get data from JSON
					topTenValuesExpected = ReadFilesHelper.getJsonDataTopTen(HierarchyHelper.categoryTotal, i); 
					
					// Verify values on Top Ten chart for category "Total Expense"
					HierarchyTopTenValues.verifyTopTenValues(topTenValuesExpected, HierarchyHelper.topTenChart, HierarchyHelper.categoryTotal);
	
					// Select category "Optimizable Expense"
					HierarchyHelper.selectCategory(HierarchyHelper.categoryOptimizable);
					
					// Wait for the data to be updated on chart
					Thread.sleep(3000);
					
					// Get data from JSON
					topTenValuesExpected = ReadFilesHelper.getJsonDataTopTen(HierarchyHelper.categoryOptimizable, i); 
					
					// Verify values on Top Ten chart for category "Optimizable Expense"
					HierarchyTopTenValues.verifyTopTenValues(topTenValuesExpected, HierarchyHelper.topTenChart, HierarchyHelper.categoryOptimizable);
	
					// Select category "Roaming Expense"
					HierarchyHelper.selectCategory(HierarchyHelper.categoryRoaming);
					
					// Wait for the data to be updated on chart
					Thread.sleep(3000);
	
					// Get data from JSON
					topTenValuesExpected = ReadFilesHelper.getJsonDataTopTen(HierarchyHelper.categoryRoaming, i); 
	
					// Verify values on Top Ten chart for category "Roaming Expense"				
					HierarchyTopTenValues.verifyTopTenValues(topTenValuesExpected, HierarchyHelper.topTenChart, HierarchyHelper.categoryRoaming);
					
				} catch(NullPointerException e) {
					
					System.out.println("chart not found");
					
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
	    JOptionPane.showMessageDialog(frame, "Test for Hierarchy Top Ten values finished. Select OK to close browser.");
		driver.close();
//		driver.quit();
	}

	
}
