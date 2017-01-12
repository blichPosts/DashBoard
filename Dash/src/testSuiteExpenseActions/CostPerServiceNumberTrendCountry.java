package testSuiteExpenseActions;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenses.CostPerServiceNumberTrend;
import expenses.TotalExpensesTrend;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;

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
		// setup page for test.
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);
		CommonTestStepActions.GoToExpensePageDetailedWait(); // the expense page with all vendors selected is shown at page open. 

		// initialize collection needed to do the convert vendors to countries
		ExpenseHelper.SetupCountryAndVendorData(); 

		// this converts the vendor view total expense legends to a list holding what the legends will be in the country view. 
		// this list is stored in the expense helper class.
		ExpenseHelper.SetupForCountryViewPageLoad();  
		
		// move to country view.
		CommonTestStepActions.SelectCountryView();
		
		// this waits until a legend in the 'count of service numbers' control is found in the list of countries that was stored 
		// when method 'ExpenseHelper.SetupForCountryViewPageLoad()' was called above.
		ExpenseHelper.WaitForCountryPageLoad();
		
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);
		
		CostPerServiceNumberTrend.SetupChartId();
		
		// this sets up a string list of the legends (for expected values) and a web list of legends (for clicking legends).
		CostPerServiceNumberTrend.Setup();
		
		ExpenseHelper.SetChartId(3); // expense helper needs to know the control because it has a method that's used.

		CostPerServiceNumberTrend.VerifyRemovingLegends(); // do verification.
		
		// verify expense trending control is empty.
		ExpenseHelper.VerifyOneControlNotPresent(ExpenseHelper.controlType.costPerServiceNumber); // verify there are no bar graphs in expense spend category.
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
