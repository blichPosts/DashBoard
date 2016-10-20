package testSuiteExpense;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenses.TotalExpenseByVendorCarrier;
import expenses.TotalExpenseByVendorSpendCategory;
import helperObjects.CommonTestStepActions;

public class TotalExpenseByVendorSpendCategoryVisual extends BaseClass
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
	}
	
	@Test
	public static void TotalExpenseByVendorCarrierVisualTest() throws Exception
	{
		CommonTestStepActions.GoToExpensePageDetailedWait();
		
		// #1
		// Select all the vendors in the Point of View and select all of the vendor legends.
		CommonTestStepActions.SelectAllVendors();
		
		// It's title says 'Total Expenses by Vendor and Spend Category'.
		// To the right of the horizontal bar graphs there are legends with these labels
	    // Voice
	    // Data
	    // Messages
	    // Roaming
	    // Equipment
	    // Taxes
	    // Other - this is shown if there are six or more vendors added. (TBD -  this may be error in test writing).
	    // Account
		// Each bar graph is broken into sections. The number of sections in each bar graph is equal to the number of legends 
		// listed to the right of the bar (all of the legends have been checked).
		// It there are five or more vendors selected there will be an 'other' vendor type listed.
		TotalExpenseByVendorSpendCategory.VerifyLegendsTitleAndbarGraphCount();
		TotalExpenseByVendorSpendCategory.VerifyVendorsCountries();

		// #2
		// In the 'Country/Vendor View Selector' component, select Country.
		CommonTestStepActions.SelectCountryView();
		
	    //All of the countries are listed in the horizontal bar graph.
	    //The legends listed in the above test case are shown.
	    //Each bar graph is broken into sections. The number of sections in each bar graph is equal to the number of legends listed to the right of the bar graph (all of the checkboxes have been checked).
	    //Each legend has a corresponding section in the bar.
		TotalExpenseByVendorSpendCategory.VerifyLegendsTitleAndbarGraphCount();
		TotalExpenseByVendorSpendCategory.VerifyVendorsCountries();
	}
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "Select OK. This is Ana edit. TEST");
		driver.close();
		driver.quit();
	}	
	

}