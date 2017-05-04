package testSuiteLocalization;

import org.openqa.selenium.By;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenses.CostPerServiceNumberTrend;
import expenses.TotalExpensesTrend;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;
import helperObjects.ExpenseHelper.expenseFilters;
import localization.FleetExpense;

public class TestExpense extends BaseClass 
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
	}
	
	@Test
	public static void TestExpensesTest() throws Exception
	{
		// #1
		// Select vendors view and view the 'Count of Service Numbers' component.

		// this makes sure the most amount of expense legends are shown. the 'desiredMonth' can be found by running the commented 
		// code in 'TotalExpenseByVendorCountryActions' under under test suite 'testSuiteExpenseActions'.
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);  
		
		CommonTestStepActions.GoToExpensePageDetailedWait(); 
		
		CommonTestStepActions.OpenSettingsPanel();
		


		FleetExpense.VerifySomeTextInSettingsPanel();

		FleetExpense.selectLanguage("es-ES");

		CommonTestStepActions.CloseSettingsPanel();
		
		//FleetExpense.KpiTitleTitle();
		//FleetExpense.KpiTileRolling();
		//FleetExpense.KpiTileThreeMonth();
		//FleetExpense.KpiTileSixMonth();
		//FleetExpense.TwoMainTitles();
		FleetExpense.SubTitles();
		Pause("");
		
		
		/*
		// next two lines below use method in 'TotalExpensesTrend' class to test the trend tabs that were added to the 
		// cost per service number trend control in 17.1
		ExpenseHelper.SetExpenseFilter(expenseFilters.CostPerServiceNumber); // this indicates which expense filter is being tested.
		TotalExpensesTrend.VerifyTrendValues();
		
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
		
		// this converts the vendor view total expense legends to a list holding what the legends will be in the country view. 
		// this list is stored in the expense helper class.
		ExpenseHelper.SetupForCountryPageWait();
		
		// #3
		// Switch to the 'Country View'.
		// The results are the same as the above steps except countries are shown instead of vendors.
		CommonTestStepActions.SelectCountryView();
		
		// this waits until a legend in the 'count of service numbers' control is found in the list of countries that was stored 
		// when method 'ExpenseHelper.SetupForCountryViewPageLoad()' was called above.
		ExpenseHelper.WaitForCountryPageLoad();
		
		CostPerServiceNumberTrend.VerifyTitle(CommonTestStepActions.ExpensesViewMode.country);
		CostPerServiceNumberTrend.VerifyLegends();
		CostPerServiceNumberTrend.VerifyMonths();
		CostPerServiceNumberTrend.VerifyNumLegendsMatchNumBarSections();
		*/
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
