package testSuiteExpense;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenses.TotalExpenseByVendorCarrier;
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
