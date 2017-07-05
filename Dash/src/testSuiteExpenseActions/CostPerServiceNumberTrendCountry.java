package testSuiteExpenseActions;

//1/14/16 - update country view wait. 

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import Dash.BaseClass.LoginType;
import expenses.CostPerServiceNumberTrend;
import expenses.TotalExpensesTrend;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;
import helperObjects.CommonTestStepActions.ExpensesViewMode;
import helperObjects.ExpenseHelper.controlType;

public class CostPerServiceNumberTrendCountry extends BaseClass 
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
	}
	
	@Test
	public static void CostPerServiceNumberTrendCountryTest() throws Exception
	{
		CommonTestStepActions.WaitForMonthSelectorVisible(); // wait month selector visible.
		
		// setup page for test.
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);
		CommonTestStepActions.GoToExpensePageDetailedWait(); // the expense page with all vendors selected is shown at page open. 
		
		ExpenseHelper.WaitForControlLegend(controlType.costPerServiceNumber); // wait for a control to show up.

		// need this to help the method calling 'VerifyTooltip' know which title will be in the hover title.
		ExpenseHelper.expenseViewMode = ExpensesViewMode.country;
		
		if(loginType.equals(LoginType.MatrixPortal)) // matrix ---------------- !!!
		{
			// move to country view.
			CommonTestStepActions.SelectCountryView();
			Pause("Wait for country page to load");
		}
		else
		{
			// this converts the vendor view total expense legends to a list holding what the legends will be in the country view. 
			// this list is stored in the expense helper class.
			ExpenseHelper.SetupForCountryPageWait();
			
			
			// move to country view.
			CommonTestStepActions.SelectCountryView();
			
			// this waits until a legend in the 'count of service numbers' control is found in the list of countries that was stored 
			// when method 'ExpenseHelper.SetupForCountryViewPageLoad()' was called above.
			ExpenseHelper.WaitForCountryPageLoad();
		}
		
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);
		
		CostPerServiceNumberTrend.SetupChartId();
		
		// this sets up a string list of the legends (for expected values) and a web list of legends (for clicking legends).
		CostPerServiceNumberTrend.Setup();
		
		ExpenseHelper.SetChartId(3);  // expense helper needs to know the current control because it has a method that's used.

		CostPerServiceNumberTrend.VerifyRemovingLegends(); // do verification.
		CostPerServiceNumberTrend.VerifyAddingLegends(); // do verification.
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
