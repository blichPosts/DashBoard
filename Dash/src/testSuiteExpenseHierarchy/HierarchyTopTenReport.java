package testSuiteExpenseHierarchy;

import java.util.List;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenseHierarchy.HierarchyTopTenValues;
import helperObjects.CommonTestStepActions;
import helperObjects.GeneralHelper;
import helperObjects.HierarchyHelper;
import helperObjects.ReadFilesHelper;


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
		String hierarchyValue = ReadFilesHelper.getHierarchyValue();
		
			
		// #4 Get the last month listed on month selector
		List<String> monthsInDropdown = HierarchyHelper.getMonthsListedInDropdown(); 

		String lastMonthListedMonthSelector = monthsInDropdown.get(monthsInDropdown.size()-1);
		String monthYearToSelect = "";
		
		int indexMonth = 0;
		boolean dataForMonthSelected = true;
	
		
		do {
			
			// #5 Select month on month/year selector
			monthYearToSelect = monthsInDropdown.get(indexMonth);
			CommonTestStepActions.selectMonthYearPulldown(monthYearToSelect);
			Thread.sleep(2000);
			
			System.out.println("Month Year: " + monthYearToSelect);
			
			// #6 Verify that the values displayed on the tooltips of "Top Ten" chart are the same as the ones read from file
			
			// Wait for chart to be loaded
			dataForMonthSelected = GeneralHelper.waitForChartToLoad(HierarchyHelper.topTenChart, MediumTimeout);
						
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
	    JOptionPane.showMessageDialog(frame, "Test for Hierarchy Top Ten Report finished. Select OK to close browser.");
		driver.close();
		driver.quit();
	}

}
