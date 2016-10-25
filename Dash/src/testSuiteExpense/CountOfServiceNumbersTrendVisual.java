package testSuiteExpense;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;

public class CountOfServiceNumbersTrendVisual extends BaseClass 
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
	}
	
	@Test
	public static void CountOfServiceNumbersTrendVisualTest() throws Exception
	{
		// #1
		// Select  vendors view and view the 'Count of Service Numbers' component.
		CommonTestStepActions.GoToExpensePageDetailedWait();
		

	    //The title is 'Cost per Service Number by Vendor - All Categories'
	    //The last twelve months are shown.

		// (//span[text()='Vendor'])[4]
		
		
		
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
