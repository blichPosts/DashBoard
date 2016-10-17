package testSuiteExpense;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenses.TotalExpenseByVendorCarrier;
import helperObjects.CommonTestStepActions;


public class TotalExpenseByVendorCarrierVisual extends BaseClass
{
	
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
	}
	
	@Test
	public static void TotalExpenseByVendorCarrierVisuallTest() throws Exception
	{
		CommonTestStepActions.GoToExpensePage();

		// #1
		// Select all the vendors in the Point of View and select all of the vendor legends.
		CommonTestStepActions.SelectAllVendors();

		// The pie shows data only for the five largest vendors selected. Data for other vendors selected are shown under the “Others” slice. 
		// The “Others” slice should not appear if just five or less vendors are selected.
		TotalExpenseByVendorCarrier.getAllLegends();
		
		DebugTimeout(9999, "DONE");		
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
