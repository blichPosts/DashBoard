package testSuiteExpenseActions;

import org.bouncycastle.cms.PasswordRecipientInformation;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenses.PointOfView;
import helperObjects.CommonTestStepActions;

public class PointOfViewActions extends BaseClass 
{
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
	}
	
	@Test
	public static void PointOfViewActionsTest() throws Exception
	{
		// #1) Select all the vendors using the all selection and record the vendors (and possible 'other legend) in the 'total expense'  control.
		CommonTestStepActions.GoToExpensePageDetailedWait(); 
		CommonTestStepActions.SelectAllVendors();
		PointOfView.StoreAllLegendsInTotalExpense();
		PointOfView.BuildListOfVendorsForVendorSelection();

		
		// #2) Select the none selection to un-select all the vendors, select the recorded vendors one at a time, then select all the vendors.
		CommonTestStepActions.UnSelectAllVendors();

	    // When the none selector is selected there are no controls shown.
		PointOfView.VerifyControlsNotPresent();

		
		// As the vendors are added they show up in each control.
	    // When all of the vendors are selected, an other legend shows up in all the controls if it existed in step one.

		
		// get the legends in the expense control.
		// legendsList = ExpenseHelper.GetTotalExpenseLegends();
		

		
		
		// .md-checkbox-inner-container>#input-md-checkbox-1 /'/ this is css for the first vendor check box 
		// .md-checkbox-label
		
		
		// **************
		
		
		// initialize month selector variable.
		//CommonTestStepActions.initializeMonthSelector(); // bladd
		
		// #1 Observe the month selector in the left pane described in the summary - The month is displayed with MMM-YYYY format.		
		// #2 In the 'Overview Dash board Selector' component, select the 'View Total Expense' tab.
		//CommonTestStepActions.GoToExpensePageDetailedWait(); // #2
		//CommonTestStepActions.VerifyMonthPullDownFormat(); // #1
		
		// #3  Switch the month selector through various selections - Thirteen descending months are available for selection, starting with the current month/year. 
		//CommonTestStepActions.VerifyMonthPullDownSelectionsAndContent();
		//CommonTestStepActions.UsePulldownList();

		
		// #4
		// Observe the Country and the Vendor (has check box selectors) in the 'Country Vendor' selector control in the 'Point Of View'.
		
		// The elements of this selector are arranged hierarchically showing countries and vendors, with the vendors corresponding to each country indented inside the corresponding country
		// Each vendor has check box menu items to allow the user to select/de-select the vendors -- NOTE: in automation - this will be tested in later tests. 
		// Each country has a select/un-select “All” menu item to allow the user to select/de-select all vendors under this country at the same time. 
		// The countries are listed in alphabetical order. The Vendors within the countries are listed in alphabetical order within each country.
		//CommonTestStepActions.VerifyCountryAndVendorListSorted();
		//CommonTestStepActions.VerifyMonthPulldownDetail();
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
