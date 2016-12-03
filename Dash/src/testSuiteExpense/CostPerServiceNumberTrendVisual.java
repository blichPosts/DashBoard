package testSuiteExpense;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenses.CostPerServiceNumberTrend;
import expenses.CountOfServiceNumbersTrend;
import helperObjects.CommonTestStepActions;

public class CostPerServiceNumberTrendVisual extends BaseClass 
{
	
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
	}
	
	@Test
	public static void CostPerServiceNumbersTrendVisualTest() throws Exception
	{
		// #1
		// Select vendors view and view the 'Count of Service Numbers' component.

		CommonTestStepActions.GoToExpensePageDetailedWait(); 
		
		CostPerServiceNumberTrend.SetupChartId(); // setup unique id for this control test.
		
		// #1 View the 'Cost per Line Trend for Top 5 Spend' component.

		// The title is 'Cost per Service Number - All Categories'.
	    // With no vendors selected the control is blank. (NOTE: will be done in actions).
		CostPerServiceNumberTrend.VerifyTitle(CommonTestStepActions.ExpensesViewMode.vendor);

		// #2 
		// Select all the vendors. 
		CommonTestStepActions.SelectAllVendors();


    	// The legends match the legends in the 'Total Expense' control.
    	// The last 13 months are shown across the bottom of a line graph matching the months in the month pulldown.
    	// There is a line for each legend (shown in hover).
		CostPerServiceNumberTrend.VerifyLegends();
		CostPerServiceNumberTrend.VerifyMonths();
		CostPerServiceNumberTrend.VerifyNumLegendsMatchNumBarSections();		
		
		// #3
		// Switch to the 'Country View'.
		// The results are the same as the above steps except countries are shown instead of vendors.
		CommonTestStepActions.SelectCountryView();
		CostPerServiceNumberTrend.VerifyTitle(CommonTestStepActions.ExpensesViewMode.country);
		CostPerServiceNumberTrend.VerifyLegends();
		CostPerServiceNumberTrend.VerifyMonths();
		CostPerServiceNumberTrend.VerifyNumLegendsMatchNumBarSections();		
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