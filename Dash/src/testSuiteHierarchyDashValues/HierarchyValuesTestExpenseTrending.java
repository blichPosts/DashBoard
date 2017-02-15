package testSuiteHierarchyDashValues;

import java.util.List;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenseHierarchy.HierarchyExpenseTrending;
import helperObjects.CommonTestStepActions;
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
	public static void HierarchyExpenseTrendingTestValuesTest() throws Exception
	{

		// Enable Start collecting data
		ReadFilesHelper.startCollectingData();
		
		// #1 Select the "VIEW BY HIERARCHY" button
		HierarchyHelper.selectHierarchyView();
		
		// #2 Select hierarchy from dropdown
		HierarchyHelper.selectHierarchyFromDropdown(1);
		
		// #3 Select first month from dropdown
		GeneralHelper.selectFirstMonth();
		
		// #4 Get data from JSON
		List<HierarchyTrendData> valuesFromFile = ReadFilesHelper.getJsonDataTrend(false);   //getHierarchyTrendData(completePath); // new ArrayList<>();
		
		
		// #4 Select month on month/year selector
		// Month to be selected on pulldown needs to be one of the months for which there's data in the source file
		String year = valuesFromFile.get(0).getOrdinalYear();
		String 	month = valuesFromFile.get(0).getOrdinalMonth();
		String monthYearToSelect = "";
		
		monthYearToSelect = CommonTestStepActions.convertMonthNumberToName(month, year);
		System.out.println("Month Year: " + monthYearToSelect);
		
		CommonTestStepActions.selectMonthYearPulldown(monthYearToSelect);
		Thread.sleep(2000);
		 		
		
		// #5 Verify that the values displayed on the tooltips of "Usage Trending" charts are the same as the ones read from file
		// Note: Only the first month with data is selected for each vendor, since no matter which month is selected the same info
		// will be displayed on the Usage Trending charts 
		
		try {
			
			HierarchyHelper.selectCategory(HierarchyHelper.categoryTotal);
			
			HierarchyExpenseTrending.verifyExpenseTrendingChartTooltip(HierarchyHelper.expenseTrendingChart, valuesFromFile, HierarchyHelper.categoryTotal);
			Thread.sleep(2000);
			
			HierarchyHelper.selectCategory(HierarchyHelper.categoryOptimizable);
			
			HierarchyExpenseTrending.verifyExpenseTrendingChartTooltip(HierarchyHelper.expenseTrendingChart, valuesFromFile, HierarchyHelper.categoryOptimizable);
			Thread.sleep(2000);
			
			HierarchyHelper.selectCategory(HierarchyHelper.categoryRoaming);
			
			HierarchyExpenseTrending.verifyExpenseTrendingChartTooltip(HierarchyHelper.expenseTrendingChart, valuesFromFile, HierarchyHelper.categoryRoaming);
			Thread.sleep(2000);
			
		} catch(NullPointerException e) {
			
			System.out.println("chart not found");
			
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
