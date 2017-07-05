package testSuiteExpenseActions;

import javax.swing.JOptionPane;

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
		CommonTestStepActions.WaitForMonthSelectorVisible(); // wait month selector visible.
		
		// setup page for test.
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);
		CommonTestStepActions.GoToExpensePageDetailedWait(); // the expense page with all vendors selected is shown at page open. 
		
		// need to set chartId for 'expense helper' because call below uses expense helper call.
		ExpenseHelper.SetChartId(1);
		
		ExpenseHelper.WaitForControlLegend(controlType.totalExpense);
		
		// store vendor names currently shown in expense control. 
		TotalExpenseByVendorSpendCategory.InitializeTotalExpenseSpendCategoryTest(); 
		
		// if not using matrix portal, select the first vendor in the stored vendor list.
		if(!loginType.equals(LoginType.MatrixPortal))
		{
			CommonTestStepActions.UnSelectAllVendors();
			ExpenseHelper.VerifyControlsNotPresent();
			CommonTestStepActions.selectOneVendor(TotalExpenseByVendorSpendCategory.totalExpenseExpectedLegendsList.get(0));			
		}
		
		// need chartId for 'TotalExpenseByVendorSpendCategory'.
		TotalExpenseByVendorSpendCategory.SetChartId(1);
		
		TotalExpenseByVendorSpendCategory.VerifyRemovingCategories(ViewType.vendor);
		TotalExpenseByVendorSpendCategory.VerifyAddingCategories(ViewType.vendor);
	}
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");	
		ExpenseHelper.SetWaitDefault();
	    JOptionPane.showMessageDialog(frame, "DONE");
		driver.close();
		driver.quit();
	}	
}
