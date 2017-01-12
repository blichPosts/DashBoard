package testSuiteExpenseActions;

import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import Dash.BaseClass.ViewType;
import expenses.TotalExpenseByVendorSpendCategory;
import expenses.TotalExpensesTrend;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;

public class TotalExpensesTrendVendorActions extends BaseClass 
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
		
		TotalExpensesTrend.SetupChartId();
		
		// this sets up a string list of the legends (for expected values) and a web list of legends (for clicking legends).
		TotalExpensesTrend.Setup();

		ExpenseHelper.SetChartId(2); // expense helper needs to know the control because it has a method that's used.
		
		TotalExpensesTrend.VerifyRemovingLegends();
		
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
