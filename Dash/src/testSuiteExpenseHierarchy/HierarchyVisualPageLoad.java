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
	public static void CostPerServiceNumberTrendVendorTest() throws Exception
	{
		// setup page for test.
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);
		CommonTestStepActions.GoToExpensePageDetailedWait(); // the expense page with all vendors selected is shown at page open. 
		
		ExpenseHelper.WaitForControlLegend(controlType.costPerServiceNumber);

		VisualPageLoad.SelectAndWaitForPageLoad();
		
		VisualPageLoad.VerifyInitialStatesAfterPageLoad();
		
		// VisualPageLoad.ConsoleOutForFileDiffActualValues(); // this is used if need to output the values found in the UI to console.
		
		// verify for desired month in  pulldown is desired month
		// String selectedOption = new Select(driver.findElement(By.xpath("Type the xpath of the drop-down element"))).getFirstSelectedOption().getText(); 
		//
		// verify --- above KPI tiles "Expenses for 0:0 Hewson, Ed:3579086421 and its dependent units". 
		// verify -- pulldown value - maximum displayed is 10 in 2 places. 
		// verify in tiles - "Top 10 (out of 100) dependent units of 0:0 Hewson, Ed:3579086421 - Total Expense"
		// verify css '.tdb-inlineBlock.tdb-boxSelector__option.tdb-boxSelector__option--selected' is enabled  -- this is one of 3 tabs default value.
		
		
		
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
