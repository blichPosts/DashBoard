package testSuiteHierarchyDashValues;

import java.util.List;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenseHierarchy.HierarchyExpenseTrending;
import expenseHierarchy.HierarchyTopTenValues;
import helperObjects.CommonTestStepActions;
import helperObjects.HierarchyHelper;
import helperObjects.HierarchyTrendData;
import helperObjects.ReadFilesHelper;
import helperObjects.UsageHelper;

public class HierarchyTestValuesTopTen extends BaseClass {

	
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
 		
	}
	
	
	@Test
	public static void HierarchyTestValuesTopTenTest() throws Exception
	{
		
		// #1 Select the "VIEW BY HIERARCHY" button
		HierarchyHelper.selectHierarchyView();
					
		// #2 Select the "TOP TEN VIEW" 
		HierarchyHelper.selectTopTenView();
		
		// #3 Read data from file ???
		
		// ...
		
		// #4 Select month on month/year selector
		// Month to be selected on pulldown needs to be one of the months for which there's data in the source file
//		String year = valuesFromFile.get(0).getOrdinalYear();
//		String month = valuesFromFile.get(0).getOrdinalMonth();
//		String monthYearToSelect = "";
//		
//		monthYearToSelect = CommonTestStepActions.convertMonthNumberToName(month, year);
//		System.out.println("Month Year: " + monthYearToSelect);
//		
//		CommonTestStepActions.selectMonthYearPulldown(monthYearToSelect);
//		Thread.sleep(2000);
		
		
		// #5 Verify that the values displayed on the tooltips of "Usage Trending" charts are the same as the ones read from file
		// Note: Only the first month with data is selected for each vendor, since no matter which month is selected the same info
		// will be displayed on the Usage Trending charts 
		
		try {
			
			HierarchyHelper.selectCategory(HierarchyHelper.categoryTotal);
			
			HierarchyTopTenValues.verifyTopTenValues(HierarchyHelper.topTenChart, HierarchyHelper.categoryTotal);
			Thread.sleep(2000);
			
			HierarchyHelper.selectCategory(HierarchyHelper.categoryOptimizable);
			
			HierarchyTopTenValues.verifyTopTenValues(HierarchyHelper.topTenChart, HierarchyHelper.categoryOptimizable);
			Thread.sleep(2000);
			
			HierarchyHelper.selectCategory(HierarchyHelper.categoryRoaming);
			
			HierarchyTopTenValues.verifyTopTenValues(HierarchyHelper.topTenChart, HierarchyHelper.categoryRoaming);
			Thread.sleep(2000);
			
		} catch(NullPointerException e) {
			
			System.out.println("chart not found");
			
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
