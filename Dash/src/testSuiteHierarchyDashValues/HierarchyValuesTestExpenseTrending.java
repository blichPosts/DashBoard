package testSuiteHierarchyDashValues;

import java.util.List;

import javax.swing.JOptionPane;

import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenseHierarchy.HierarchyExpenseTrending;
import helperObjects.GeneralHelper;
import helperObjects.HierarchyHelper;
import helperObjects.HierarchyTrendData;
import helperObjects.ReadFilesHelper;



public class HierarchyValuesTestExpenseTrending extends BaseClass{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
		
	}


	@Test
	public static void HierarchyValuesTestExpenseTrendingTest() throws Exception
	{

		// Enable Start collecting data
		ReadFilesHelper.startCollectingData();
		
		// #1 Select the "VIEW BY HIERARCHY" button
		HierarchyHelper.selectHierarchyView();
		
		// #2 Select hierarchy from dropdown , run the test for each hierarchy listed on dropdown
		List<WebElement> hierarchies = HierarchyHelper.getHierarchiesFromDropdown();
		List<String> hierarchyIds = HierarchyHelper.getHierarchiesValues();
		
		for (int i = 1; i <= hierarchies.size(); i++) {
			
//			GeneralHelper.selectFirstMonth();
			
			HierarchyHelper.selectHierarchyFromDropdown(i);
			
			HierarchyHelper.waitForChartToLoad(HierarchyHelper.expenseTrendingChart);
			
//			Thread.sleep(3000);
			
			// #3 Get data from JSON
			List<HierarchyTrendData> valuesFromAjaxCall = ReadFilesHelper.getJsonDataTrend(hierarchyIds.get(i-1));
					
			
			// #4 Verify that the values displayed on the tooltips of "Usage Trending" charts are the same as the ones read from file
			// Note: Only the first month with data is selected for each vendor, since no matter which month is selected the same info
			// will be displayed on the Usage Trending charts 
			
			try {
				
				HierarchyHelper.selectCategory(HierarchyHelper.expenseTrendingChart, HierarchyHelper.categoryTotal);
				Thread.sleep(2000);
				
				HierarchyExpenseTrending.verifyExpenseTrendingChartTooltip(HierarchyHelper.expenseTrendingChart, valuesFromAjaxCall, HierarchyHelper.categoryTotal);
				
				HierarchyHelper.selectCategory(HierarchyHelper.expenseTrendingChart, HierarchyHelper.categoryOptimizable);
				Thread.sleep(2000);
				
				HierarchyExpenseTrending.verifyExpenseTrendingChartTooltip(HierarchyHelper.expenseTrendingChart, valuesFromAjaxCall, HierarchyHelper.categoryOptimizable);
				
				HierarchyHelper.selectCategory(HierarchyHelper.expenseTrendingChart, HierarchyHelper.categoryRoaming);
				Thread.sleep(2000);
				
				HierarchyExpenseTrending.verifyExpenseTrendingChartTooltip(HierarchyHelper.expenseTrendingChart, valuesFromAjaxCall, HierarchyHelper.categoryRoaming);
				
				
			} catch(NullPointerException e) {
				
				System.out.println("chart not found");
				
			}
		
		}
				
	}

	
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "Test for Usage Trending values finished. Select OK to close browser.");
		driver.close();
		driver.quit();
	}
	
	
	
}
