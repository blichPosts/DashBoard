package testSuiteExpenseHierarchy;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenseHierarchy.HierarchyNumbersDependents;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;
import helperObjects.ReadFilesHelper;
import helperObjects.ExpenseHelper.controlType;
import helperObjects.HierarchyHelper;

public class DependentUnitsDrillDown extends BaseClass 
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
	}
	
	@Test
	public static void DependentUnitsDrillDownTest() throws Exception
	{		
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);
		CommonTestStepActions.GoToExpensePageDetailedWait(); // the expense page with all vendors selected is shown at page open. 
		
		ExpenseHelper.WaitForControlLegend(controlType.costPerServiceNumber);

		ExpenseHelper.ShowSelectedMonth();
		
		ReadFilesHelper.startCollectingData(); // start the JavascriptExecutor.
		
		HierarchyHelper.SelectAndWaitForPageLoad();

		ExpenseHelper.SetHierarchyMaxDisplayed(100);
		Thread.sleep(1000);
		
		// set how many levels to drill down to.
		HierarchyNumbersDependents.SetMaxNumberOfLevelsToDrillDown(5);
		
		// this sets the number of tiles to test and the number of tiles to show. ???
		// HierarchyNumbersDependents.SetupNumberOfTilesToTestAndShow(2, 100);
		
		// set which tile map test should be run in the loop below. 
		//HierarchyNumbersDependents.SetCurrrentTileMapTestType(TileMapTestType.drillDown);

		// this will do drill down tests by looping through each hierarchy and doing drill down tests in each hierarchy.  
		// HierarchyNumbersDependents.LoopThroughHierarchiesDependentUnitsDrillDown();
		
		HierarchyNumbersDependents.LoopThroughMonthsDependentUnits();
		
		Pause("Passed");
		
	}
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");	
		ExpenseHelper.SetWaitDefault();
	    JOptionPane.showMessageDialog(frame, "Test Done.");
		driver.close();
		driver.quit();
	}		
}
