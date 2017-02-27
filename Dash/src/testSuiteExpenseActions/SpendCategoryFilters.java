package testSuiteExpenseActions;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;
import helperObjects.ExpenseHelper.expenseFilters;

public class SpendCategoryFilters extends BaseClass 
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
	}
	
	
	@Test
	public static void SpendCategoryFiltersTest() throws Exception
	{
		// setup page for test.
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);
		CommonTestStepActions.GoToExpensePageDetailedWait(); // the expense page with all vendors selected is shown at page open. 

		ExpenseHelper.SetupExpectedCostFilters();
		
		ShowText("run");
		
		ExpenseHelper.SetExpenseFilter(expenseFilters.Expense); // this indicates which expense filter is being tested.
		
		// verify selecting expense trending 
		ExpenseHelper.VerifySpendCateoryFilter();
		
		ShowText("run");

		ExpenseHelper.SetExpenseFilter(expenseFilters.CountOfServiceNumbers); // this indicates which expense filter is being tested.
		
		// verify selecting expense trending 
		ExpenseHelper.VerifySpendCateoryFilter();

		ShowText("run");
		
		ExpenseHelper.SetExpenseFilter(expenseFilters.CostPerServiceNumber); // this indicates which expense filter is being tested.
		
		// verify selecting expense trending 
		ExpenseHelper.VerifySpendCateoryFilter();

		
		
		
		
		//DebugTimeout(999999, "99999");
		
		//ExpenseHelper.SetExpenseFilter(expenseFilters.CostPerServiceNumber); // this indicates which expense filter is being tested.
		// verify selecting cost per service number trending
		
		//ExpenseHelper.SetExpenseFilter(expenseFilters.CountOfServiceNumbers); // this indicates which expense filter is being tested.
		// verify selecting count of service number trending
		
		//Expense,
		//CostPerServiceNumber,
		//CountOfServiceNumbers,
	
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
