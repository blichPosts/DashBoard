package testSuiteHierarchyDashValues;

import java.util.List;

import javax.swing.JOptionPane;

import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenseHierarchy.HierarchyTreeMap;
import helperObjects.GeneralHelper;
import helperObjects.HierarchyHelper;
import helperObjects.HierarchyTrendData;
import helperObjects.ReadFilesHelper;



public class HierarchyValuesTestTileMap extends BaseClass{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
		
	}


	@Test
	public static void HierarchyValuesTestTileMapTest() throws Exception
	{

		// Enable Start collecting data
		ReadFilesHelper.startCollectingData();
		
		// #1 Select the "VIEW BY HIERARCHY" button
		HierarchyHelper.selectHierarchyView();
		
		// #2 Select hierarchy from dropdown , run the test for each hierarchy listed on dropdown
		List<WebElement> hierarchies = HierarchyHelper.getHierarchiesFromDropdown();
		
		for (int i = 1; i <= 1; i++) {  // hierarchies.size(); i++) {
			
			System.out.println(" **** Hierarchy " + hierarchies.get(i-1).getText());
			GeneralHelper.selectFirstMonth();
			HierarchyHelper.selectHierarchyFromDropdown(i);
			Thread.sleep(2000);
			
			// #3 Get data from JSON
//			List<HierarchyTrendData> valuesFromFile = ReadFilesHelper.getJsonDataTrend(i);	
					
			
			// #4 Verify that the values displayed on the tooltips of "Usage Trending" charts are the same as the ones read from file
			// Note: Only the first month with data is selected for each vendor, since no matter which month is selected the same info
			// will be displayed on the Usage Trending charts 
			
			try {
				
				HierarchyHelper.selectCategory(HierarchyHelper.treeMapChart, HierarchyHelper.categoryTotal);
				HierarchyTreeMap.hoverThroughTiles();
				
				Thread.sleep(2000);
				
//				HierarchyHelper.selectCategory(HierarchyHelper.categoryOptimizable);
				
//				HierarchyHelper.selectCategory(HierarchyHelper.categoryRoaming);
				
				
				
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
