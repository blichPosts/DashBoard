package testSuiteExpenseActions;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenses.TotalExpenseByVendorSpendCategory;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;
import helperObjects.ExpenseHelper.controlType;

public class ExpenseVendorSpendCategoryAll extends BaseClass
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
		
		TotalExpenseByVendorSpendCategory.VerifyRemovingCategoriesMultipleVendors(ViewType.vendor);
		TotalExpenseByVendorSpendCategory.VerifyAddingCategoriesMultipleVendors(ViewType.vendor);
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
