package testSuiteExpenseActions;

//1/14/16 - update country view wait

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenses.TotalExpensesTrend;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;

public class TotalExpensesTrendCountryActions extends BaseClass 
{
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
	}
	
	@Test
	public static void TotalExpensesTrendVendorActionsTest() throws Exception
	{
		// setup page for test.
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);
		CommonTestStepActions.GoToExpensePageDetailedWait(); // the expense page with all vendors selected is shown at page open. 

		// this converts the vendor view total expense legends to a list holding what the legends will be in the country view. 
		// this list is stored in the expense helper class.
		ExpenseHelper.SetupForCountryPageWait();
		
		// move to country view.
		CommonTestStepActions.SelectCountryView();
		
		// this waits until a legend in the 'count of service numbers' control is found in the list of countries that was stored 
		// when method 'ExpenseHelper.SetupForCountryViewPageLoad()' was called above.
		ExpenseHelper.WaitForCountryPageLoad();
		
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);
		
		TotalExpensesTrend.SetupChartId(); // expense helper needs to know the current control because it has a method that's used.
		
		// this sets up a string list of the legends (for expected values) and a web list of legends (for clicking legends).
		TotalExpensesTrend.Setup();
		
		ExpenseHelper.SetChartId(2); // expense helper needs to know the control because it has a method that's used.

		TotalExpensesTrend.VerifyRemovingLegends(); // do verification.
		
		// verify expense trending control is empty.
		ExpenseHelper.VerifyOneControlNotPresent(ExpenseHelper.controlType.expenseTrending); // verify there are no bar graphs in expense spend category.
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
