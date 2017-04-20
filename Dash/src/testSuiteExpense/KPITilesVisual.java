package testSuiteExpense;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;


public class KPITilesVisual extends BaseClass
{
	
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
	}
	
	@Test
	public static void KpiTilesVisualTest() throws Exception
	{
		
		// this makes sure the most amount of expense legends are shown. the 'desiredMonth' can be found by running the commented 
		// code in 'TotalExpenseByVendorCountryActions' under under test suite 'testSuiteExpenseActions'.
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);  
		
		CommonTestStepActions.GoToExpensePageDetailedWait();
		
		// prereq - select all of the vendors. - this is already done in dev instance 
		// driver.findElement(By.cssSelector(".tdb-povGroup__toggle>a")).click();
		
		// #1 Observe the KPI components - There are three KPI components, 'Total Expense', 'Count of Service Numbers', and Cost Per Service Number.
		ExpenseHelper.VerifyThreeComponents();
		
		// # 2
		//Observe the first component from the left.

		//The top says 'Total Expense'		
		//The middle section shows the total expense amount with K indicator for selected vendor or country.
		ExpenseHelper.VerifyTotalExpenseCostAndTitle();
		
		//Next there is a trend icon (up/down) arrow. -- not consistent, checked in KPI numbers test.  bob - 4/19/17.
		// ExpenseHelper.TotalExpensedVerifyTrend();
		
		// 1/20/16 - removed this because it doesn't always exist. ana's test for KPI tiles will verify this.
		// Next there is a Rolling average with rolling averages for three and six months.
		// ExpenseHelper.VerifyRollingAverageTotalExpense();
		
		// # 3
		// Observe the second component from the left.
		 
	    // The top says 'Count of Service Numbers'
	    // The middle section shows the total count of service numbers managed by the selected vendors in the selected month.
		ExpenseHelper.VerifyCountOfServiceNumbersCostAndTitle();		
		
	    // Next there is a trend icon (up/down) arrow.
		// ExpenseHelper.CountServiceNumbersVerifyTrend(); // not always
		
		// 1/20/16 - removed most of this because it doesn't always exist. ana's test for KPI tiles will verify this.
		// Next there is a Rolling average with rolling averages for three and six months. 
		// ExpenseHelper.VerifyRollingAverageCountServiceNumbers(); 
		
		// #4 - Observe the third component from the left. 
	    // The top says 'Cost per Service Number'
	    // The middle section shows the average Cost per Service Number for the selected vendors/countries and for the selected month.		
		ExpenseHelper.VerifyCostPerServiceNumberCostAndTitle();		
		
		// There will be a trend icon represented by an arrow indicating direction. // not always - checked in KPI numbers test. 
		// The icon will have a percentage number indicating the percentage of change between the current amount and the benchmark
		// ExpenseHelper.CostPerServiceNumberVerifyTrend();
		
		// There is a section that says 'Rolling Averages'. It lists the 3 and 6 month rolling averages for the expenses per service number.
		// ExpenseHelper.VerifyRollingAverageCostPerServiceNumber(); // not always - tested in KPI numbers.		
	}
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    // JOptionPane.showMessageDialog(frame, "Select OK. This is Ana edit. TEST");
		driver.close();
		driver.quit();
	}
}
