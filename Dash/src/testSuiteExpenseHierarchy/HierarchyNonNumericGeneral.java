package testSuiteExpenseHierarchy;

import Dash.BaseClass;
import expenseHierarchy.HierarchyGeneral;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;
import helperObjects.ExpenseHelper.controlType;
import helperObjects.ExpenseHelper.expenseFilters;
import helperObjects.HierarchyHelper;

public class HierarchyNonNumericGeneral extends BaseClass
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
	}
	
	@Test
	public static void HierarchyNonNumericGeneralTest() throws Exception
	{
		// setup page for test.
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);
		CommonTestStepActions.GoToExpensePageDetailedWait(); // the expense page with all vendors selected is shown at page open. 
		
		ExpenseHelper.WaitForControlLegend(controlType.costPerServiceNumber);
		
		HierarchyHelper.SelectAndWaitForPageLoad();

		// ///////////////////////////////////////////////////////////////////////////////
		// below is test for expense category selectors.
		// ///////////////////////////////////////////////////////////////////////////////
		
		HierarchyGeneral.SetupExpectedCostFilters();

		HierarchyGeneral.SetExpenseFilter(expenseHierarchy.HierarchyGeneral.expenseFiltersLocation.TileMap); // click through selectors that are above the tile map. 
		HierarchyGeneral.VerifySpendCateoryFilter(); // verify both selector tabs.
		
		HierarchyGeneral.SetExpenseFilter(expenseHierarchy.HierarchyGeneral.expenseFiltersLocation.ExpenseTrend); // click through selectors that are above the trend graph.
		HierarchyGeneral.VerifySpendCateoryFilter(); // verify both selector tabs.
		
		// VisualPageLoad.VerifyInitialStatesAfterPageLoad();
	
		// ///////////////////////////////////////////////////////////////////////////////
		// 							MANUAL TEST
		// ///////////////////////////////////////////////////////////////////////////////
		// VisualPageLoad.ManualDependencyUnits();
		
		
		// this is used if need to output the values found in the UI to console.
		// VisualPageLoad.ConsoleOutForFileDiffActualValues(); 
		
		// DebugTimeout(9999, "9999");
		
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
