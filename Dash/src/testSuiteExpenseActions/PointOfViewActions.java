package testSuiteExpenseActions;

import javax.swing.JOptionPane;

import org.bouncycastle.cms.PasswordRecipientInformation;
import org.openqa.jetty.servlet.Debug;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenses.PointOfView;
import helperObjects.CommonTestStepActions;
import helperObjects.CommonTestStepActions.ExpensesViewMode;
import helperObjects.Country;
import helperObjects.ExpenseHelper;
import helperObjects.ExpenseHelper.controlType;

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
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);
		CommonTestStepActions.GoToExpensePageDetailedWait(); 
		CommonTestStepActions.SelectAllVendors();
		ExpenseHelper.WaitForControlLegend(controlType.totalExpense);
		PointOfView.StoreAllLegendsInTotalExpense();

		// #2) Select the none selection to un-select all the vendors, select the recorded vendors one at a time, then select all the vendors.
		CommonTestStepActions.UnSelectAllVendors();

	    // When the none selector is selected there are no controls shown.
		PointOfView.VerifyControlsNotPresent();

		// As the vendors are added they show up in each control.
	    // When all of the vendors are selected, an other legend shows up in all the controls if it existed in step one.
		// PointOfView.VerifyAddingVendors();
		PointOfView.VerifyAddingVendorsTwo(ExpensesViewMode.vendor);
		
		// # 3 
	    // Un-select all of the vendors
	    // Add back the ones from step one.
	    // Un-select each vendor one at a time.
		PointOfView.SetupForRemoveVendorsTest();	

	    // As each vendor is removed the corresponding legend is removed from each control
	    // When all the vendors are unselected the controls are blank.
		PointOfView.RemoveVendorsAndVerifyTwo(ExpensesViewMode.vendor);		

		// # 4
		// Switch the month pull down through each month.
    	// The Total Expense control shows the selected month/year.
    	// The month/year shown in the title (top-left) is the selected month/year.
		PointOfView.VerifyMonthPulldownSelectionsInControls();
		
		// # 5
		// In the 'Country/Vendor View Selector' component, select Country and repeat the above steps.
		// Expected results pass.
		
		// create container that stores each country object and the vendors inside each country onto a list. this is needed to  
		ExpenseHelper.SetupCountryAndVendorData();
		
		// switch to the country view, un-select all vendors, and wait for all controls to be empty.  
		CommonTestStepActions.SelectCountryView();
		CommonTestStepActions.UnSelectAllVendors();
		PointOfView.VerifyControlsNotPresent();

		// # 2 
		// add vendors one at a time and verify expected country legends. 
		PointOfView.VerifyAddingVendorsTwo(ExpensesViewMode.country);

		
		// # 3 
	    // Un-select all of the vendors
	    // Add back the ones from step one.
	    // Un-select each vendor one at a time.
		PointOfView.SetupForRemoveVendorsTest();	
		
	    // As each vendor is removed the corresponding legend is removed from each control
	    // When all the vendors are unselected the controls are blank.
		PointOfView.RemoveVendorsAndVerifyTwo(ExpensesViewMode.country);		
		
		// # 4
		// Switch the month pull down through each month.
		PointOfView.VerifyMonthPulldownSelectionsInControls();
		
		// .md-checkbox-inner-container>#input-md-checkbox-1 /'/ this is css for the first vendor check box css=.md-checkbox-inner-container>#input-md-checkbox-1 
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
