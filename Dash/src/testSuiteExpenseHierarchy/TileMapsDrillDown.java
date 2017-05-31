package testSuiteExpenseHierarchy;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenseHierarchy.HierarchyNumbersDependents;
import expenseHierarchy.HierarchyNumbersDependents.TileMapTestType;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;
import helperObjects.HierarchyHelper;
import helperObjects.ReadFilesHelper;
import helperObjects.ExpenseHelper.controlType;

public class TileMapsDrillDown extends BaseClass 
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
	}
	
	@Test
	public static void HierarchyNumbersTestDrillDown() throws Exception
	{	
		//CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth); 
		
		CommonTestStepActions.GoToExpensePageDetailedWait(); // the expense page with all vendors selected is shown at page open. 
		
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth); 
		
		//Pause("After month select.");
		
		ExpenseHelper.WaitForControlLegend(controlType.costPerServiceNumber);
		
		HierarchyHelper.SelectAndWaitForPageLoad();

		ExpenseHelper.ShowSelectedMonth();
		
		ReadFilesHelper.startCollectingData();
		
		HierarchyNumbersDependents.SetChartId();

		ExpenseHelper.failedtestNgTest = false;

		// set how many levels to drill down to.
		HierarchyNumbersDependents.SetMaxNumberOfLevelsToDrillDown(7);

		
		// this sets the number of tiles to test and the number of tiles to show.
		HierarchyNumbersDependents.SetupNumberOfTilesToTestAndShow(1, 100);
		
		// set which tile map test should be run in the loop below. 
		HierarchyNumbersDependents.SetCurrrentTileMapTestType(TileMapTestType.drillDownCommand);

		HierarchyNumbersDependents.LoopThroughTileMapTests();
		
		
		/*
		ShowText("\nDefault Cost Center Hierarchy Is Set ***** \n");
		
		// the first number is how many levels to attempt to drill down to.
		// the second number is how many tiles are available to click when drilling down.
		HierarchyNumbersDependents.DrillDownAcrossCostFiltersTileMap(5, 50); // TEST --------------------------------------
		
		//ShowText("\nHave Set Management Hierarchy  ***** \n");
		ExpenseHelper.SelectHierarchy(hierarchyPulldownSelection.Management);
		Thread.sleep(2000);

		// the first number is how many levels to attempt to drill down to. 
		// the second number is how many tiles are available to click when drilling down.
		HierarchyNumbersDependents.DrillDownAcrossCostFiltersTileMap(5, 50); // TEST --------------------------------------
		
		ShowText("\nHave Set Approval Hierarchy  ***** \n");
		ExpenseHelper.SelectHierarchy(hierarchyPulldownSelection.Approval);
		Thread.sleep(2000);

		// the first number is how many levels to attempt to drill down to.
		// the second number is how many tiles are available to click when drilling down.
		HierarchyNumbersDependents.DrillDownAcrossCostFiltersTileMap(5, 50); // TEST --------------------------------------
		
		// see if there were errors and raise testNG error if there were any.
		if(ExpenseHelper.failedtestNgTest)
		{
			Assert.fail("Failure in HierarchyNumbersTest()");
		}
		*/	
	}
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");	
		ExpenseHelper.SetWaitDefault();
	    JOptionPane.showMessageDialog(frame, "TestComplete.");
		driver.close();
		driver.quit();
	}	
	
	
	
}
