package testSuiteExpenseActions;

import org.openqa.jetty.servlet.Debug;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenses.KpiTiles;
import expenses.TotalExpenseByVendorCarrier;
import helperObjects.CommonTestStepActions;
import helperObjects.Country;
import helperObjects.ExpenseHelper;
import helperObjects.ExpenseHelper.controlType;
import helperObjects.ExpenseHelper.enableDisableActionsType;

public class TotalExpenseByVendorCountryActions extends BaseClass 
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
	}
	
	@Test
	public static void TotalExpenseByVendorCountryActionsTest() throws Exception
	{
		
		// #1 - this step isn't done because the spread sheet is needed. 
		
		// setup page for test.
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);
		CommonTestStepActions.GoToExpensePageDetailedWait(); // the expense page with all vendors selected is shown at page open. 

		// setup for tests.
		ExpenseHelper.SetChartId(0);
		
		// this verifies selecting/un-selecting legends and the correct slices shown on each selection.
		ExpenseHelper.SetupExpenseControSliceTesting(); // set up containers in expense helper to be used for testing 'total expense' control slices. 
		TotalExpenseByVendorCarrier.StoreAllLegendsInTotalExpense(); // setup containers in 'TotalExpenseByVendorCarrier' for testing 'total expense' control slices.
		TotalExpenseByVendorCarrier.VerifyUnSelectingLegendsAndSlices();
		
		ExpenseHelper.VerifyOneControlNotPresent(controlType.totalExpense);
		
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
