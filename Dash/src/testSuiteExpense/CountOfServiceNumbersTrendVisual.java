package testSuiteExpense;

import java.util.List;

import org.eclipse.jetty.servlets.WelcomeFilter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenses.CountOfServiceNumbersTrend;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;
import helperObjects.UsageHelper;

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

		// #3
		// Switch to the 'Country View'.
		// The results are the same as the above steps except countries are shown instead of vendors.
		CommonTestStepActions.SelectCountryView();
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
