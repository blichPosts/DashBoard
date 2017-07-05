package testSuiteExpenseActions;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenses.CostPerServiceNumberTrend;
import expenses.CountOfServiceNumbersTrend;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;
import helperObjects.CommonTestStepActions.ExpensesViewMode;
import helperObjects.ExpenseHelper.controlType;

public class CountOfServiceNumbersVendor extends BaseClass 
{
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
	}
	
	@Test
	public static void CountOfServiceNumbersTrendVendorTest() throws Exception
	{
		CommonTestStepActions.WaitForMonthSelectorVisible(); // wait month selector visible.
		
		// setup page for test.
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);
		CommonTestStepActions.GoToExpensePageDetailedWait(); // the expense page with all vendors selected is shown at page open. 
		
		ExpenseHelper.WaitForControlLegend(controlType.countOfServiceNumbers); // wait for a control to show up.
		
		Thread.sleep(2000); // without this web element with legends gets bad info.
		
		// need this to help the method calling 'VerifyTooltip' know which title will be in the hover title.
		ExpenseHelper.expenseViewMode = ExpensesViewMode.vendor;
		
		CountOfServiceNumbersTrend.SetupChartId();
		
		// this sets up a string list of the legends (for expected values) and a web list of legends (for clicking legends).
		CountOfServiceNumbersTrend.Setup();
		
		ExpenseHelper.SetChartId(4);  // expense helper needs to know the current control because it has a method that's used.
		
		CountOfServiceNumbersTrend.VerifyRemovingLegends(); // do verification.
		CountOfServiceNumbersTrend.VerifyAddingLegends(); // do verification.
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
