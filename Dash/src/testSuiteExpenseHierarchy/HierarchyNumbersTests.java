package testSuiteExpenseHierarchy;

import Dash.BaseClass;
import expenseHierarchy.HierarchyNumbersDependents;
import expenseHierarchy.VisualPageLoad;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;
import helperObjects.ExpenseHelper.controlType;
import helperObjects.ExpenseHelper.hierarchyPulldownSelection;

public class HierarchyNumbersTests extends BaseClass
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
	}
	
	@Test
	public static void HierarchyNumbersTest() throws Exception
	{	
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);
		CommonTestStepActions.GoToExpensePageDetailedWait(); // the expense page with all vendors selected is shown at page open. 
		
		ExpenseHelper.WaitForControlLegend(controlType.costPerServiceNumber);

		VisualPageLoad.SelectAndWaitForPageLoad();
		
		HierarchyNumbersDependents.SetChartId();

		ExpenseHelper.failedtestNgTest = false;

		// this sets the number of tiles to test and the number of tiles to show.
		HierarchyNumbersDependents.SetupNumberOfTilesToTestAndShow(45, 50);
		
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
		
		/*
		// ////////////////////////////////////////////////////////////////////////
		// 							GO THROUGH MONTHS
		// ////////////////////////////////////////////////////////////////////////

		// this sets the number of tiles to test and the number of tiles to show.
		HierarchyNumbersDependents.SetupNumberOfTilesToTestAndShow(3, 50);
		
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
		*/
		
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
