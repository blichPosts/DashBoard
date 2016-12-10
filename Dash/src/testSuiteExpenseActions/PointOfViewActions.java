package testSuiteExpenseActions;

import org.bouncycastle.cms.PasswordRecipientInformation;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenses.PointOfView;
import helperObjects.CommonTestStepActions;
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

		/*
		// #2) Select the none selection to un-select all the vendors, select the recorded vendors one at a time, then select all the vendors.
		CommonTestStepActions.UnSelectAllVendors();

	    // When the none selector is selected there are no controls shown.
		PointOfView.VerifyControlsNotPresent();
		
		// As the vendors are added they show up in each control.
	    // When all of the vendors are selected, an other legend shows up in all the controls if it existed in step one.
		PointOfView.VerifyAddingVendors();
		
		// # 3 
	    // Un-select all of the vendors
	    // Add back the ones from step one.
	    // Un-select each vendor one at a time.
		PointOfView.SetupForRemoveVendorsTest();	


	    // As each vendor is removed the corresponding legend is removed from each control
	    // When all the vendors are unselected the controls are blank.
		PointOfView.RemoveVendorsAndVerify();		
		

		// # 4
		// Switch the month pull down through each month.
    	// The Total Expense control shows the selected month/year.
    	// The month/year shown in the title (top-left) is the slected month/year.
		PointOfView.VerifyMonthPulldownSelectionsInControls();
		*/
		
		// at this point switch to country view and get countries related to each vendor that was saved from 
		
		PointOfView.StoreExpectedCountries();
		
		
		
		
		
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
