package testSuiteExpenseHierarchy;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenseHierarchy.HierarchyNumbersDependents;
import expenseHierarchy.VisualPageLoad;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;
import helperObjects.ExpenseHelper.controlType;
import helperObjects.ExpenseHelper.hierarchyPulldownSelection;

public class HierarchyNumbersTestsDrillDown extends BaseClass 
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
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);
		CommonTestStepActions.GoToExpensePageDetailedWait(); // the expense page with all vendors selected is shown at page open. 
		
		ExpenseHelper.WaitForControlLegend(controlType.costPerServiceNumber);

		VisualPageLoad.SelectAndWaitForPageLoad();
		
		HierarchyNumbersDependents.SetChartId();

		ExpenseHelper.failedtestNgTest = false;

		// this sets the number of tiles to test and the number of tiles to show.
		HierarchyNumbersDependents.SetupNumberOfTilesToTestAndShow(30, 50);
		
		ShowText("\nDefault Cost Center Hierarchy Is Set ***** \n");
		
		// the first number is how many levels to attempt to drill down to.
		// the second number is how many tiles are available to click when drilling down.
		HierarchyNumbersDependents.DrillDownAcrossCostFilters(3, 50); // TEST --------------------------------------
		
		ShowText("\nHave Set Management Hierarchy  ***** \n");
		ExpenseHelper.SelectHierarchy(hierarchyPulldownSelection.Management);
		Thread.sleep(2000);

		// the first number is how many levels to attempt to drill down to. 
		// the second number is how many tiles are available to click when drilling down.
		HierarchyNumbersDependents.DrillDownAcrossCostFilters(3, 50); // TEST --------------------------------------
		
		ShowText("\nHave Set Approval Hierarchy  ***** \n");
		ExpenseHelper.SelectHierarchy(hierarchyPulldownSelection.Approval);
		Thread.sleep(2000);

		// the first number is how many levels to attempt to drill down to.
		// the second number is how many tiles are available to click when drilling down.
		HierarchyNumbersDependents.DrillDownAcrossCostFilters(2, 50); // TEST --------------------------------------
		
		// see if there were errors and raise testNG error if there were any.
		if(ExpenseHelper.failedtestNgTest)
		{
			Assert.fail("Failure in HierarchyNumbersTest()");
		}	
	}
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");	
		ExpenseHelper.SetWaitDefault();
	    // JOptionPane.showMessageDialog(frame, "Select OK. This is Ana edit. TEST");
		driver.close();
		driver.quit();
	}	
	
	
	
}
