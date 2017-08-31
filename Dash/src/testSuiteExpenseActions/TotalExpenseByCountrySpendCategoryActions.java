package testSuiteExpenseActions;

//1/14/16 - update country view wait

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import Dash.BaseClass.ViewType;
import expenses.TotalExpenseByVendorSpendCategory;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;
import helperObjects.ExpenseHelper.controlType;

public class TotalExpenseByCountrySpendCategoryActions extends BaseClass 
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
	}
	
	@Test
	public static void TotalExpenseByCountrySpendCategoryActionsTest() throws Exception
	{
		CommonTestStepActions.WaitForMonthSelectorVisible(); // wait month selector visible.
		
		// setup page for test.
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);
		CommonTestStepActions.GoToExpensePageDetailedWait(); // the expense page with all vendors selected is shown at page open. 

		ExpenseHelper.WaitForControlLegend(controlType.costPerServiceNumber); // wait for a control to show up.
		
		// need chartId in helper.
		ExpenseHelper.SetChartId(1);
		
		// store country names currently shown in expense control. 
		TotalExpenseByVendorSpendCategory.InitializeTotalExpenseSpendCategoryTest(); 
		
		// this is for page switch.
		if(loginType.equals(LoginType.MatrixPortal)) // matrix ---------------- !!!
		{
			// move to country view.
			CommonTestStepActions.SelectCountryView();
			Pause("Wait for country page to load.");
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
	
		// if not using matrix portal, select the first country in the stored country list.
		if(!loginType.equals(LoginType.MatrixPortal)) // matrix ---------------- !!!
		{
			CommonTestStepActions.UnSelectAllVendors();
			ExpenseHelper.VerifyControlsNotPresent();
			CommonTestStepActions.selectOneVendor(TotalExpenseByVendorSpendCategory.totalExpenseExpectedLegendsList.get(0));
			ExpenseHelper.SetupCountryAndVendorData(); 
		}
		
		TotalExpenseByVendorSpendCategory.SetChartId(1);
		
		// need chartId for 'TotalExpenseByVendorSpendCategory'.
		TotalExpenseByVendorSpendCategory.VerifyRemovingCategories(ViewType.country);
		TotalExpenseByVendorSpendCategory.VerifyAddingCategories(ViewType.country);
		//Pause("Passed");
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
