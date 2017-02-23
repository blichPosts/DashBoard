package testSuiteExpense;

//1/14/16 - update country view wait and verify no "visibility" attributes when testing legends/control sections.

import java.util.List;

import org.eclipse.jetty.servlets.WelcomeFilter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenses.CountOfServiceNumbersTrend;
import expenses.TotalExpensesTrend;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;
import helperObjects.UsageHelper;
import helperObjects.ExpenseHelper.expenseFilters;

public class CountOfServiceNumbersTrendVisual extends BaseClass 
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
	}
	
	@Test
	public static void CountOfServiceNumbersTrendVisualTest() throws Exception
	{
		// #1
		// Select vendors view and view the 'Count of Service Numbers' component.

		// this makes sure the most amount of expense legends are shown. the 'desiredMonth' can be found by running the commented 
		// code in 'TotalExpenseByVendorCountryActions' under under test suite 'testSuiteExpenseActions'.
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);  
		
		CommonTestStepActions.GoToExpensePageDetailedWait(); 
		
		CountOfServiceNumbersTrend.SetChartId(4); // setup unique id for this control test.
		
		// next two lines below use method in 'TotalExpensesTrend' class to test the trend tabs that were added to the 
		// count of service number trend control in 17.1.
		ExpenseHelper.SetExpenseFilter(expenseFilters.CountOfServiceNumbers); // this indicates which expense filter is being tested.
		TotalExpensesTrend.VerifyTrendValues();

		
		// The title is 'Count of Service Number by Vendor/Country'
		CountOfServiceNumbersTrend.VerifyTitle(CommonTestStepActions.ExpensesViewMode.vendor);

		// #2 
		// Add all the vendors in point of view. 
		CommonTestStepActions.SelectAllVendors();

	    // The component has vertical bar graphs for each month in the month pulldown.
	    // The legends show the same as the legends for the 'Total Expense' control 
	    // The last 13 months are shown across the bottom of a line graph matching the months in the month pulldown.
	    // There is a section in each bar chart for each legend.
		CountOfServiceNumbersTrend.VerifyLegends();
		CountOfServiceNumbersTrend.VerifyMonths();
		CountOfServiceNumbersTrend.VerifyNumLegendsMatchNumBarSections();		

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
		
		CountOfServiceNumbersTrend.VerifyTitle(CommonTestStepActions.ExpensesViewMode.country);		
		CountOfServiceNumbersTrend.VerifyLegends();
		CountOfServiceNumbersTrend.VerifyMonths();
		CountOfServiceNumbersTrend.VerifyNumLegendsMatchNumBarSections();		
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
