package testSuiteExpenseActions;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenses.KpiTiles;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;

public class KPITilesActions extends BaseClass 
{
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
	}
	
	@Test
	public static void KpiTilesActionsTest() throws Exception
	{
		// #1
		// Un-select all of the vendors in the Point of View.
		CommonTestStepActions.GoToExpensePageDetailedWait(); 
		CommonTestStepActions.UnSelectAllVendors();
		ExpenseHelper.VerifyControlsNotPresent(); // wait for all controls to be empty.

	    // All of the controls are blank.
	    // The values in the KPI tiles are 0.
	    // 'Total Expense and 'Cost per Service Number' have leading dollar signs.
		KpiTiles.VerifyInitialValues();
		

		// #2
		// Add vendors one at a time.
		// Each time a vendor is added, the numeric indicators for the 'Total Expense' and 'Count of Service Numbers' KPIs stay the same or increase.
		
		// create container that stores each country object and the vendors inside each country onto a list.   
		ExpenseHelper.SetupCountryAndVendorData();
		
		KpiTiles.VerifyAddingEachVendor(KpiTiles.xpathTotalExpenseKpi, KpiTiles.AscendDescend.ascending);
		KpiTiles.VerifyAddingEachVendor(KpiTiles.xpathCountOfServiceNumbersKpi, KpiTiles.AscendDescend.descending);
		ExpenseHelper.VerifyControlsNotPresent(); // wait for all controls to be empty.
		
		DebugTimeout(9999, "Freeze");
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
