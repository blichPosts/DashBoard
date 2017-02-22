package testSuiteExpenseHierarchy;

import Dash.BaseClass;
import expenseHierarchy.HierarchyNumbersDependents;
import expenseHierarchy.VisualPageLoad;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;
import helperObjects.ExpenseHelper.controlType;

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
		HierarchyNumbersDependents.SetupNumberOfTilesToTestAndShow(10, 50);
		
		Thread.sleep(3000);

		ShowText("Default Cost Center Hierarchy Is Set *****");
		HierarchyNumbersDependents.TestPhaseOne(); // TEST --------------------------------------
		/*
		ShowText("Have Set Management Hierarchy  *****");
		ExpenseHelper.SelectHierarchy(hierarchyPulldownSelection.Management);
		Thread.sleep(2000);
		
		HierarchyNumbersDependents.TestPhaseOne(); // TEST --------------------------------------

		ShowText("Have Set Approval Hierarchy  *****");
		ExpenseHelper.SelectHierarchy(hierarchyPulldownSelection.Approval);
		Thread.sleep(2000);
		
		HierarchyNumbersDependents.TestPhaseOne(); // TEST --------------------------------------
		
		// see if there were errors and raise testNG error if there were any.
		if(ExpenseHelper.failedtestNgTest)
		{
			Assert.fail("Failure in HierarchyNumbersTest()");
		}
		*/
		
		// ///////////////////////////////  MONTHS ////////////////////////////////////////////////////////////////////////
		// ///////////////////////////////  MONTHS ////////////////////////////////////////////////////////////////////////
		// ///////////////////////////////  MONTHS ////////////////////////////////////////////////////////////////////////
		/*
		// this sets the number of tiles to test and the number of tiles to show.
		HierarchyNumbersDependents.SetupNumberOfTilesToTestAndShow(4, 50);
		
		ShowText("Have Set Cost Center Hierarchy  *****");
		ExpenseHelper.SelectHierarchy(hierarchyPulldownSelection.CostCenter);
		Thread.sleep(2000);
		
		HierarchyNumbersDependents.TestPhaseWithMonths(); // TEST --------------------------------------
		
		
		ShowText("Have Management Hierarchy  *****");
		ExpenseHelper.SelectHierarchy(hierarchyPulldownSelection.Management);
		Thread.sleep(2000);
		
		HierarchyNumbersDependents.TestPhaseWithMonths(); // TEST --------------------------------------
		
		ShowText("Have Set Cost Center Approval  *****");
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
	    // JOptionPane.showMessageDialog(frame, "Select OK. This is Ana edit. TEST");
		driver.close();
		driver.quit();
	}	
}
