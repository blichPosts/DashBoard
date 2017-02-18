package testSuiteExpenseHierarchy;

import Dash.BaseClass;
import expenseHierarchy.VisualPageLoad;

import org.openqa.selenium.By;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import expenses.CostPerServiceNumberTrend;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;
import helperObjects.ExpenseHelper.controlType;

public class HierarchyVisualPageLoad extends BaseClass
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
	}
	
	@Test
	public static void HierarchyTest() throws Exception
	{
		// setup page for test.
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);
		CommonTestStepActions.GoToExpensePageDetailedWait(); // the expense page with all vendors selected is shown at page open. 
		
		ExpenseHelper.WaitForControlLegend(controlType.costPerServiceNumber);

		VisualPageLoad.SelectAndWaitForPageLoad();
		
		// VisualPageLoad.VerifyInitialStatesAfterPageLoad();
		
		// DebugTimeout(3, "start");
		
		//VisualPageLoad.foo();
		
		VisualPageLoad.Hover();
		
		// DebugTimeout(9999, "9999");
		
		// this is used if need to output the values found in the UI to console.
		// VisualPageLoad.ConsoleOutForFileDiffActualValues(); 
		
		// DebugTimeout(9999, "9999");
		
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
