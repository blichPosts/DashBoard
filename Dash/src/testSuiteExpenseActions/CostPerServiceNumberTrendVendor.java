package testSuiteExpenseActions;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenses.CostPerServiceNumberTrend;
import expenses.TotalExpensesTrend;
import helperObjects.CommonTestStepActions;
import helperObjects.CommonTestStepActions.ExpensesViewMode;
import helperObjects.ExpenseHelper;
import helperObjects.ExpenseHelper.controlType;

public class CostPerServiceNumberTrendVendor extends BaseClass 
{
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
	}
	
	@Test
	public static void CostPerServiceNumberTrendVendorTest() throws Exception
	{
		// setup page for test.
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);
		CommonTestStepActions.GoToExpensePageDetailedWait(); // the expense page with all vendors selected is shown at page open. 
		
		ExpenseHelper.WaitForControlLegend(controlType.costPerServiceNumber); // wait for a control to show up.
		
		Thread.sleep(2000); // without this web element with legends gets bad info.
		
		// need this to help the method calling 'VerifyTooltip' know which title will be in the hover title.
		ExpenseHelper.expenseViewMode = ExpensesViewMode.vendor; 
		
		CostPerServiceNumberTrend.SetupChartId();
		
		// this sets up a string list of the legends (for expected values) and a web list of legends (for clicking legends).
		CostPerServiceNumberTrend.Setup();

		ExpenseHelper.SetChartId(3);  // expense helper needs to know the current control because it has a method that's used.
		
		CostPerServiceNumberTrend.VerifyRemovingLegends();
		
		// verify expense trending control is empty.
		ExpenseHelper.VerifyOneControlNotPresent(ExpenseHelper.controlType.costPerServiceNumber); // verify there are no bar graphs in expense spend category.
		
	}
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");	
		ExpenseHelper.SetWaitDefault();
//	    JOptionPane.showMessageDialog(frame, "Select OK. This is Ana edit. TEST");
		driver.close();
		driver.quit();
	}	
}
