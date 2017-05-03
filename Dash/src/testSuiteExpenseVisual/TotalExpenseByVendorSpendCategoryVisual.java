package testSuiteExpenseVisual;

//1/14/16 - update country view wait and verify no "visibility" attributes when testing legends/control sections.

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenses.TotalExpenseByVendorCarrier;
import expenses.TotalExpenseByVendorSpendCategory;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;

public class TotalExpenseByVendorSpendCategoryVisual extends BaseClass
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
	}
	
	@Test
	public static void TotalExpenseByVendorSpendVisualTest() throws Exception
	{

		// this makes sure the most amount of expense legends are shown. the 'desiredMonth' can be found by running the commented 
		// code in 'TotalExpenseByVendorCountryActions' under under test suite 'testSuiteExpenseActions'.
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);  
		
		CommonTestStepActions.GoToExpensePageDetailedWait();
		
		// #1
		// Select all the vendors in the Point of View and select all of the vendor legends.
		CommonTestStepActions.SelectAllVendors();
		
		// set chart id for this test case's corresponding class 'TotalExpenseByVendorSpendCategory'.
		TotalExpenseByVendorSpendCategory.SetupChartId();

	    // It's title says 'Total Expenses by Vendor and Spend Category'.
	    // The vendors and possible 'other' item are listed to the left of the horizontal bar charts.
	    // The vendors and possible 'other' item match the 'Total Expense' legends.
		TotalExpenseByVendorSpendCategory.VerifyVendors();
		
	    // Below the horizontal bar graphs there are legends with these labels
	    // Voice
	    // Data
	    // Messages
	    // Roaming
	    // Equipment
	    // Taxes
	    // Other - this is shown if there are six or more vendors added. (TBD -  this may be error in test writing).
	    // Account
	    
		// NOTE: next three can't be tested in the visual test. 1 and 2 may be tested in actions.
		// 1) Each bar graph is broken into sections/values when hovered over. The values that aren't = 0 are shown in the bar graph.  
	    // 2) Each legend has a corresponding section in the hover.
	    // 3) There is a scale at the bottom of the bar graph.

		// The vendors listed match the vendors listed in the 'Total Expense' pie control.  
	    // It there are five or more vendors selected there will be an 'other' vendor type listed, the same as in the 'Total Expense' pie control.  
		TotalExpenseByVendorSpendCategory.VerifyLegendsTitleAndbarGraphCount(ViewType.vendor);
		TotalExpenseByVendorSpendCategory.VerifyVendorsCountries();

		// this converts the vendor view total expense legends to a list holding what the legends will be in the country view. 
		// this list is stored in the expense helper class.
		ExpenseHelper.SetupForCountryPageWait();
		
		// #2
		// In the 'Country/Vendor View Selector' component, select Country.
		CommonTestStepActions.SelectCountryView();
		
		// this waits until a legend in the 'count of service numbers' control is found in the list of countries that was stored 
		// when method 'ExpenseHelper.SetupForCountryViewPageLoad()' was called above.
		ExpenseHelper.WaitForCountryPageLoad();
		
	    // All of the countries are listed next to the horizontal bar graph in the same fashion as they are listed in the 'Total Expense' control.
	    // The hover behavior matches the behavior in step one.
		TotalExpenseByVendorSpendCategory.VerifyLegendsTitleAndbarGraphCount(ViewType.country);
		TotalExpenseByVendorSpendCategory.VerifyVendorsCountries();
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
