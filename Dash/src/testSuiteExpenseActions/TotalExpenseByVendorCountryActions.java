package testSuiteExpenseActions;

import org.openqa.jetty.servlet.Debug;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenses.KpiTiles;
import expenses.TotalExpenseByVendorCarrier;
import helperObjects.CommonTestStepActions;
import helperObjects.Country;
import helperObjects.ExpenseHelper;

public class TotalExpenseByVendorCountryActions extends BaseClass 
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
	}
	
	@Test
	public static void KpiTilesActionsTest() throws Exception
	{

		// Un-select all of the vendors in the Point of View.
		CommonTestStepActions.GoToExpensePageDetailedWait(); 
		//CommonTestStepActions.UnSelectAllVendors();
		//ExpenseHelper.VerifyControlsNotPresent(); // wait for all controls to be empty.

		// create container that stores each country object and the vendors inside each country onto a list.   
		ExpenseHelper.SetupCountryAndVendorData();
		
		ExpenseHelper.FindMonthWithMostVendors();
		
		DebugTimeout(9999, "Freeze");
		
		
		
		
		/*
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
		
		// #3 Remove vendors one at a time. 
		// Each time a vendor is removed, the numeric indicators for the 'Total Expense' and 'Count of Service Numbers' KPIs stay the same or decrease.
		
		KpiTiles.VerifyAddingEachVendor(KpiTiles.xpathCountOfServiceNumbersKpi, KpiTiles.AscendDescend.descending);
		ExpenseHelper.VerifyControlsNotPresent(); // wait for all controls to be empty.
		
		// # 4 
		// In the 'Country/Vendor' view selector component, select Country and repeat steps 1, 2, and 3 above substituting country for vendor.
		
		// switch to the country view, un-select all vendors, and wait for all controls to be empty.  
		CommonTestStepActions.SelectCountryView();
		CommonTestStepActions.UnSelectAllVendors();
		ExpenseHelper.VerifyControlsNotPresent();

	    // All of the controls are blank.
	    // The values in the KPI tiles are 0.
	    // 'Total Expense and 'Cost per Service Number' have leading dollar signs.
		KpiTiles.VerifyInitialValues();

		// Add vendors one at a time.
		// Each time a vendor is added, the numeric indicators for the 'Total Expense' and 'Count of Service Numbers' KPIs stay the same or increase.
		
		// create container that stores each country object and the vendors inside each country onto a list.   
		ExpenseHelper.SetupCountryAndVendorData();
		
		KpiTiles.VerifyAddingEachVendor(KpiTiles.xpathTotalExpenseKpi, KpiTiles.AscendDescend.ascending);
		
		// Remove vendors one at a time. 
		// Each time a vendor is removed, the numeric indicators for the 'Total Expense' and 'Count of Service Numbers' KPIs stay the same or decrease.
		
		KpiTiles.VerifyAddingEachVendor(KpiTiles.xpathCountOfServiceNumbersKpi, KpiTiles.AscendDescend.descending);
		ExpenseHelper.VerifyControlsNotPresent(); // wait for all controls to be empty.
		*/
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
