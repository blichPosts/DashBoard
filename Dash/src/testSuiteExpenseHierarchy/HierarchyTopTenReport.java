package testSuiteExpenseHierarchy;

import java.util.List;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenseHierarchy.HierarchyTopTenValues;
import helperObjects.CommonTestStepActions;
import helperObjects.HierarchyHelper;
import helperObjects.ReadFilesHelper;
import helperObjects.UsageHelper;


public class HierarchyTopTenReport extends BaseClass {

	
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
 		
	}
	
	
	@Test
	public static void HierarchyTopTenReportTest() throws Exception
	{
		
		// Enable Start collecting data
		ReadFilesHelper.startCollectingData();
		
		
		// #1 Select the "VIEW BY HIERARCHY" button
		HierarchyHelper.selectHierarchyView();
					
		
		// #2 Select the "TOP TEN VIEW" 
		HierarchyHelper.selectTopTenView(2);
		Thread.sleep(2000);
		

		// #3 Get the hierarchy's value
		String hierarchyValue = "";
		hierarchyValue = ReadFilesHelper.getHierarchyValue();
		
			
		// #4 Get the last month listed on month selector
		List<String> monthsInDropdown = HierarchyHelper.getMonthsListedInDropdown(); 

		String lastMonthListedMonthSelector = monthsInDropdown.get(monthsInDropdown.size()-1);
		String monthYearToSelect = "";
		
		int indexMonth = 0;
		boolean dataForMonthSelected = true;
	
		String cssSelector = "#" + UsageHelper.getChartId(HierarchyHelper.topTenChart) + ">svg>g.highcharts-grid.highcharts-yaxis-grid>path:nth-child(2)";
		
		
		do {
			
			// #5 Select month on month/year selector
			monthYearToSelect = monthsInDropdown.get(indexMonth);
			CommonTestStepActions.selectMonthYearPulldown(monthYearToSelect);
			Thread.sleep(2000);
			
			System.out.println("Month Year: " + monthYearToSelect);
			
			// #6 Verify that the values displayed on the tooltips of "Top Ten" chart are the same as the ones read from file
			
			try {
				
				// Wait for chart to be loaded
				dataForMonthSelected = WaitForElementVisibleNoThrow(By.cssSelector(cssSelector), TenTimeout);
				
			} catch (TimeoutException e) {
				
				System.out.println("No data for the selected month");
				
			}
			
			if (dataForMonthSelected) {
					
				// Run test for "Expense" chart and category "Total"
				HierarchyTopTenValues.verifyTopTenChartReport(hierarchyValue, HierarchyHelper.topTenChart, HierarchyHelper.categoryTotal);
								
			}
			
			indexMonth++;

		} while (!monthYearToSelect.equals(lastMonthListedMonthSelector) && !dataForMonthSelected);
		
		
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
