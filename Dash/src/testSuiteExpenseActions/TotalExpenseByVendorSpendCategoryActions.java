package testSuiteExpenseActions;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenses.TotalExpenseByVendorSpendCategory;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;
import helperObjects.ExpenseHelper.controlType;

public class TotalExpenseByVendorSpendCategoryActions extends BaseClass 
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
	}
	
	@Test
	public static void TotalExpenseByVendorSpendCategoryActionsTest() throws Exception
	{
		// setup page for test.
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);
		CommonTestStepActions.GoToExpensePageDetailedWait(); // the expense page with all vendors selected is shown at page open. 
		
		// need to set chartId for 'expense helper' because call below uses expense helper call.
		ExpenseHelper.SetChartId(1);
		
		ExpenseHelper.WaitForControlLegend(controlType.totalExpense);
		
		// store vendor names currently shown in expense control. 
		TotalExpenseByVendorSpendCategory.InitializeTotalExpenseSpendCategoryTest(); 
		
		if(!loginType.equals(LoginType.MatrixPortal))
		{
			CommonTestStepActions.UnSelectAllVendors();
			ExpenseHelper.VerifyControlsNotPresent();
			CommonTestStepActions.selectOneVendor(TotalExpenseByVendorSpendCategory.totalExpenseExpectedLegendsList.get(0));			
		}
		
		// need chartId for 'TotalExpenseByVendorSpendCategory'.
		TotalExpenseByVendorSpendCategory.SetChartId(1);
		
		TotalExpenseByVendorSpendCategory.VerifyRemovingCategories(ViewType.vendor);
		
		// ExpenseHelper.VerifyOneControlNotPresent(ExpenseHelper.controlType.totalExpenseSpendCatergory); // verify there are no bar graphs in expense spend category.
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
