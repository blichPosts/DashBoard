package testSuiteExpense;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenses.TotalExpenseByVendorCarrier;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;


public class TotalExpenseByVendorCarrierVisual extends BaseClass
{
	
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
	}
	
	@Test
	public static void TotalExpenseByVendorCarrierVisualTest() throws Exception
	{
		
		// this makes sure the most amount of expense legends are shown. the 'desiredMonth' can be found by running the commented 
		// code in 'TotalExpenseByVendorCountryActions' under under test suite 'testSuiteExpenseActions'.
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);  

		CommonTestStepActions.GoToExpensePageDetailedWait();
		
		// #1
		// Select all the vendors in the Point of View and select all of the vendor legends.
		CommonTestStepActions.SelectAllVendors();

		// The title is 'Total Expense by Vendor'.
		// The pie shows data only for the five largest vendors selected. Data for other vendors selected are shown under the “Others” slice. 
		// The “Others” slice should not appear if just five or less vendors are selected.
		TotalExpenseByVendorCarrier.VerifyLegendsTitleAndPieCount();
		TotalExpenseByVendorCarrier.VerifyVendorView();

		// initialize collection needed to do the convert vendors to countries
		ExpenseHelper.SetupCountryAndVendorData(); 

		// this converts the vendor view total expense legends to a list holding what the legends will be in the country view. 
		// this list is stored in the expense helper class.
		ExpenseHelper.SetupForCountryViewPageLoad();  

		// #2 Select country in the 'Country/Vendor View Selector'
		CommonTestStepActions.SelectCountryView();
		
		// this waits until a legend in the 'count of service numbers' control is found in the list of countries that was stored 
		// when method 'ExpenseHelper.SetupForCountryViewPageLoad()' was called above.
		ExpenseHelper.WaitForCountryPageLoad();

		
	    // The title is 'Total Expenses by Country'.
	    // There is a slice in the pie for each country.
	    // There is a legend for each country.
	    // If there are more the five countries there is an other legend and pie element.
		TotalExpenseByVendorCarrier.VerifyLegendsTitleAndPieCount();
		TotalExpenseByVendorCarrier.VerifyCountryView();
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
