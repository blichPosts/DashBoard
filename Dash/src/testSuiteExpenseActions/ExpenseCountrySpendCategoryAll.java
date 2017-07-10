package testSuiteExpenseActions;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import expenses.TotalExpenseByVendorSpendCategory;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;
import helperObjects.ExpenseHelper.controlType;
import Dash.BaseClass;
import Dash.BaseClass.LoginType;
import Dash.BaseClass.ViewType;

public class ExpenseCountrySpendCategoryAll extends BaseClass
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
	}
	
	@Test
	public static void ExpenseVendorSpendCategoryAllTest() throws Exception
	{
		CommonTestStepActions.WaitForMonthSelectorVisible(); // wait month selector visible.
		
		// setup page for test.
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);
		CommonTestStepActions.GoToExpensePageDetailedWait(); // the expense page with all vendors selected is shown at page open. 
		
		// need to set chartId for 'expense helper'.
		ExpenseHelper.SetChartId(1);
		
		// need to set chartId for 'TotalExpenseByVendorSpendCategory'.
		TotalExpenseByVendorSpendCategory.SetChartId(1);
		
		ExpenseHelper.WaitForControlLegend(controlType.totalExpense);
		
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
		
		TotalExpenseByVendorSpendCategory.VerifyRemovingCategoriesMultipleVendors(ViewType.country);
		TotalExpenseByVendorSpendCategory.VerifyAddingCategoriesMultipleVendors(ViewType.country);
	}
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");	
		ExpenseHelper.SetWaitDefault();
	    // JOptionPane.showMessageDialog(frame, "DONE");
		driver.close();
		driver.quit();
	}	
		
	
}
