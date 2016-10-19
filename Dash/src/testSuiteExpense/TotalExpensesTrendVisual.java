package testSuiteExpense;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenses.TotalExpenseByVendorCarrier;
import expenses.TotalExpenseByVendorSpendCategory;
import expenses.TotalExpensesTrend;
import helperObjects.CommonTestStepActions;

public class TotalExpensesTrendVisual extends BaseClass 
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
		
		CommonTestStepActions.SelectAllVendors();

		
		// #1		
		// In the 'Total Spend By Vendor'  component:
	    // There are these selections in a horizontal selector bar with the title 'Expense Trending'
	    // All
	    // Voice
	    // Data
	    // Messages
	    // Roaming
	    // Equipment
	    // Taxes
	    // Other
	    // Account 

	    // Below the Expense Trending bar there there is a title 'Expense by Vendor - All Categories'.
		
		// .tdb-boxSelector.tdb-align--right > div
		
		TotalExpensesTrend.VerifyTrends();
		
		DebugTimeout(9999, "Freeze");
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
