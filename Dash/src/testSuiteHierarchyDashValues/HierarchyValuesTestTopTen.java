package testSuiteHierarchyDashValues;

import java.util.List;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
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
import helperObjects.UsageHelper;


public class HierarchyValuesTestTopTen extends BaseClass {

	
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
 		
	}
	
	
	@Test
	public static void HierarchyValuesTestTopTenTest() throws Exception
	{
		
		// Enable Start collecting data
		ReadFilesHelper.startCollectingData();
		
		// #1 Select the "VIEW BY HIERARCHY" button
		HierarchyHelper.selectHierarchyView();
					
		// #2 Select the "TOP TEN VIEW" 
		HierarchyHelper.selectTopTenView(2);
		Thread.sleep(2000);
		
		// #2 Select hierarchy from dropdown , run the test for each hierarchy listed on dropdown
		List<WebElement> hierarchies = HierarchyHelper.getHierarchiesFromDropdown();
		List<String> hierarchyIds = HierarchyHelper.getHierarchiesValues();
		
		for (int i = 1; i <= hierarchies.size(); i++) {
			
			GeneralHelper.selectFirstMonth();
			HierarchyHelper.selectHierarchyFromDropdown(i);
			
			boolean monthSelected = true;
			Thread.sleep(2000);
		
		
			// #3 Get the last month listed on month selector
			List<String> monthsInDropdown = HierarchyHelper.getMonthsListedInDropdown(); 
	
			String lastMonthListedMonthSelector = monthsInDropdown.get(monthsInDropdown.size()-1);
			String monthYearToSelect = "";
			
			int indexMonth = 0;
			
			do {
				
				if (!monthSelected) {
					
					monthYearToSelect = monthsInDropdown.get(indexMonth);
					System.out.println("Month Year: " + monthYearToSelect);
					
					// #4 Select month on month/year selector
					CommonTestStepActions.selectMonthYearPulldown(monthYearToSelect);
					
					// Wait for chart to be loaded
					WaitForElementVisible(By.cssSelector("chart>div"), MediumTimeout);
					
				}
				
								
				// #5 Verify that the values displayed on the tooltips of "Top Ten" chart are the same as the ones read from file
				
				boolean dataForMonthSelected = true;
				
				try {
					
					String cssSelector = "#" + UsageHelper.getChartId(HierarchyHelper.topTenChart) + ">svg>g>g>rect.highcharts-point:nth-child(1)";
					driver.findElement(By.cssSelector(cssSelector));
					
				} catch (NoSuchElementException e) {
					
					System.out.println("No data for the selected month");
					dataForMonthSelected = false;
					
				}
				
				if (dataForMonthSelected) {
					
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
		driver.quit();
	}

	
}
