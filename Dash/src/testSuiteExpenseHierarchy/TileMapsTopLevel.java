package testSuiteExpenseHierarchy;

import Dash.BaseClass;
import expenseHierarchy.HierarchyNumbersDependents;
import expenseHierarchy.HierarchyNumbersDependents.TileMapTestType;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;
import helperObjects.HierarchyHelper;
import helperObjects.ExpenseHelper.controlType;

public class TileMapsTopLevel extends BaseClass
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
	}
	
	@Test
	public static void TileMapsTopLevelTest() throws Exception
	{	
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);
		CommonTestStepActions.GoToExpensePageDetailedWait(); // the expense page with all vendors selected is shown at page open. 
		
		ExpenseHelper.WaitForControlLegend(controlType.costPerServiceNumber);

		HierarchyHelper.SelectAndWaitForPageLoad();
		
		HierarchyNumbersDependents.SetChartId();

		ExpenseHelper.failedtestNgTest = false;

		// this sets the number of tiles to test and the number of tiles to show.
		// note: below only sets number of tiles to show.
		// HierarchyNumbersDependents.SetupNumberOfTilesToTestAndShow(0, 100);
		
		ExpenseHelper.SetHierarchyMaxDisplayed(100);	
	
		// HierarchyNumbersDependents.RunAllTilesInDash();

		//HierarchyNumbersDependents.SetCurrrentTileMapTestType(TileMapTestType.commandSmoke);
		
		//HierarchyNumbersDependents.LoopThroughHierarchiesTileMapTests();
		
		// HierarchyNumbersDependents.SetMaxNumberOfLevelsToDrillDown(3);
		
		HierarchyNumbersDependents.SetCurrrentTileMapTestType(TileMapTestType.commandAllCatergories);
		
		HierarchyNumbersDependents.LoopThroughTileMapTests();

		
		
		// ////////////////////////////////////////////////////////////////
		// BELOW FOR reference application.
		// ////////////////////////////////////////////////////////////////
		/*
		// set which tile map test should be run in the loop below. 
		HierarchyNumbersDependents.SetCurrrentTileMapTestType(TileMapTestType.phaseOne);
		
		// run selected test.
		HierarchyNumbersDependents.LoopThroughHierarchiesTileMapTests();
		
		// set which tile map test should be run in the loop below. 
		HierarchyNumbersDependents.SetCurrrentTileMapTestType(TileMapTestType.months);
		
		// run selected test.
		HierarchyNumbersDependents.LoopThroughHierarchiesTileMapTests();
		 */
		
		/*
		// this sets the number of tiles to test and the number of tiles to show.
		// HierarchyNumbersDependents.SetupNumberOfTilesToTestAndShow(45, 50);
		
		Thread.sleep(3000);

		ShowText("Default Cost Center Hierarchy Is Set *****");
		HierarchyNumbersDependents.TestPhaseOne(); // TEST --------------------------------------  

		ShowText("Have Set Management Hierarchy  *****");
		ExpenseHelper.SelectHierarchy(hierarchyPulldownSelection.Management);
		Thread.sleep(2000);
		
		HierarchyNumbersDependents.TestPhaseOne(); // TEST --------------------------------------

		ShowText("Have Set Approval Hierarchy  *****");
		ExpenseHelper.SelectHierarchy(hierarchyPulldownSelection.Approval);
		Thread.sleep(2000);
		
		HierarchyNumbersDependents.TestPhaseOne(); // TEST --------------------------------------
		*/
		
		/*
		// ////////////////////////////////////////////////////////////////////////
		// 							GO THROUGH MONTHS
		// ////////////////////////////////////////////////////////////////////////

		// this sets the number of tiles to test and the number of tiles to show.
		// HierarchyNumbersDependents.SetupNumberOfTilesToTestAndShow(30, 50);
		
		ShowText("Have Set Cost Center Hierarchy Is Set *****");
		ExpenseHelper.SelectHierarchy(hierarchyPulldownSelection.CostCenter);
		
		HierarchyNumbersDependents.TestPhaseWithMonths(); // TEST --------------------------------------

		ShowText("Have Set Management Hierarchy  *****");
		ExpenseHelper.SelectHierarchy(hierarchyPulldownSelection.Management);
		Thread.sleep(2000);
		
		HierarchyNumbersDependents.TestPhaseWithMonths(); // TEST --------------------------------------

		ShowText("Have Set Approval Hierarchy  *****");
		ExpenseHelper.SelectHierarchy(hierarchyPulldownSelection.Approval);
		Thread.sleep(2000);
		
		HierarchyNumbersDependents.TestPhaseWithMonths(); // TEST --------------------------------------

		
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
	    JOptionPane.showMessageDialog(frame, "Test Complete.");
		driver.close();
		driver.quit();
	}	
}
