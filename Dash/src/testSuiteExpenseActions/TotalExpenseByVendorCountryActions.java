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
import helperObjects.ExpenseHelper.enableDisableActionsType;

public class TotalExpenseByVendorCountryActions extends BaseClass 
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
	}
	
	@Test
	public static void TotalExpenseByVendorCountryActionsTest() throws Exception
	{

		// NOTE: - this is for finding a month with the largest number of legends.  this month should be what 'ExpenseHelper.desiredMonth' is set to.
		//ShowText("Start finding desired month." );
		//CommonTestStepActions.GoToExpensePageDetailedWait(); // the expense page with all vendors selected is shown at page open. 
		//ExpenseHelper.FindMonthWithMostVendors(); 
		//DebugTimeout(9999,"Desired month has been found. Stop Program" );
		
		
		// #1 - this step isn't done because the spread sheet is needed. 
		
		// setup page for test.
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);
		CommonTestStepActions.GoToExpensePageDetailedWait(); // the expense page with all vendors selected is shown at page open. 

		// #2
		// #3
		// Un-select the legends one at a time.
		// Select the legends one at a time.
		// The legend that is un-selected is disabled.
		// The corresponding slice for the legend is removed from the pie.
		
		// setup for tests.
		ExpenseHelper.SetChartId(0);
		
		// this verifies selecting/un-selecting legends and the correct slices shown on each selection.
		ExpenseHelper.SetupExpenseControSliceTesting(); // set up containers in expense helper to be used for testing 'total expense' control slices. 
		TotalExpenseByVendorCarrier.StoreAllLegendsInTotalExpense(); // setup containers in 'TotalExpenseByVendorCarrier' for testing 'total expense' control slices.
		TotalExpenseByVendorCarrier.VerifyUnSelectingLegendsAndSlices();

		driver.navigate().refresh();

		DebugTimeout(10, "wait ten");
		
		// setup page for test.
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);
		CommonTestStepActions.GoToExpensePageDetailedWait(); // the expense page with all vendors selected is shown at page open. 

		
		
		ExpenseHelper.SetupExpenseControSliceTesting(); // set up containers in expense helper to be used for testing 'total expense' control slices. 
		TotalExpenseByVendorCarrier.StoreAllLegendsInTotalExpense(); // setup containers in 'TotalExpenseByVendorCarrier' for testing 'total expense' control slices.

		
		
		CommonTestStepActions.GoToExpensePageDetailedWait();
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);		
		
		CommonTestStepActions.UnSelectAllVendors();
		ExpenseHelper.VerifyControlsNotPresent();
		
		TotalExpenseByVendorCarrier.VerifySelectUnselectVendors(ViewType.vendor); // vendors		
		
		CommonTestStepActions.SelectCountryView();
		CommonTestStepActions.UnSelectAllVendors();
		ExpenseHelper.VerifyControlsNotPresent();

		ExpenseHelper.SetupCountryAndVendorData(); // this sets up 
		
		ExpenseHelper.SetChartId(0);
		
		TotalExpenseByVendorCarrier.VerifySelectUnselectVendors(ViewType.country); // countries		
		
		CommonTestStepActions.SelectAllVendors();
		CommonTestStepActions.GoToExpensePageDetailedWait();
		
		Thread.sleep(3000); // saving time. spent too much time on this test already. wait for six vendors in each control needs written.

		ExpenseHelper.SetChartId(0);

		ExpenseHelper.SetTempLocator(ExpenseHelper.partialXpathToLegendsListInControls);
		ExpenseHelper.VerifySelectUnselect(enableDisableActionsType.disabling); // vendor
		ExpenseHelper.VerifySelectUnselect(enableDisableActionsType.enabling); // vendor

		CommonTestStepActions.SelectAllVendors();
		CommonTestStepActions.GoToExpensePageDetailedWait();

		CommonTestStepActions.SelectVendorView();
		CommonTestStepActions.UnSelectAllVendors();
		ExpenseHelper.VerifyControlsNotPresent();
		
		CommonTestStepActions.SelectAllVendors();
		CommonTestStepActions.GoToExpensePageDetailedWait();
		
		Thread.sleep(3000); // saving time. spent too much time on this test already. wait for six vendors in each control needs written.

		ExpenseHelper.SetChartId(0);
		
		ExpenseHelper.SetTempLocator(ExpenseHelper.partialXpathToLegendsListInControls);
		ExpenseHelper.VerifySelectUnselect(enableDisableActionsType.disabling); // country
		ExpenseHelper.VerifySelectUnselect(enableDisableActionsType.enabling); // country
		
		// example of how to  
		//ExpenseHelper.SetupCountryAndVendorData();
		// TotalExpenseByVendorCarrier.SetupCountryListFromVendorList();

		// clear all legends and switch to country view.
		//CommonTestStepActions.UnSelectAllVendors();
		//ExpenseHelper.VerifyControlsNotPresent();
		//CommonTestStepActions.SelectCountryView();
		//CommonTestStepActions.SelectAllVendors();
		//DebugTimeout(5, "Five");
		//ExpenseHelper.SetChartId(0);
		//ExpenseHelper.SetupExpenseControSliceTesting(); // set up containers in expense helper to be used for testing 'total expense' control slices. 
		//TotalExpenseByVendorCarrier.StoreAllLegendsInTotalExpense(); // setup containers in 'TotalExpenseByVendorCarrier' for testing 'total expense' control slices.
		//TotalExpenseByVendorCarrier.VerifyUnSelectingLegendsAndSlices();
		
		
		
		DebugTimeout(9999, "Freeze");


		// this takes care of the enable/disable testing for legends.
		/*
		ExpenseHelper.SetChartId(0);
		ExpenseHelper.SetTempLocator(ExpenseHelper.partialXpathToLegendsListInControls);
		ExpenseHelper.VerifySelectUnselect(enableDisableActionsType.disabling); 
		ExpenseHelper.VerifySelectUnselect(enableDisableActionsType.enabling);
		
		CommonTestStepActions.SelectCountryView();
		
		ExpenseHelper.VerifySelectUnselect(enableDisableActionsType.disabling); 
		ExpenseHelper.VerifySelectUnselect(enableDisableActionsType.enabling);
		*/
		// this is for clicking legends and verifying slice section names.
		
		
		
		
		/*		
		CommonTestStepActions.SelectVendorView();
		
		ExpenseHelper.SetChartId(1);
		ExpenseHelper.SetTempLocator(ExpenseHelper.partialXpathForLegendsInTotalSpendCategoryCategories);

		ExpenseHelper.VerifySelectUnselect(enableDisableActionsType.disabling); 
		ExpenseHelper.VerifySelectUnselect(enableDisableActionsType.enabling);
		
		CommonTestStepActions.SelectCountryView();
		
		ExpenseHelper.VerifySelectUnselect(enableDisableActionsType.disabling); 
		ExpenseHelper.VerifySelectUnselect(enableDisableActionsType.enabling);
		*/
		
		
		
		
		//CommonTestStepActions.SelectVendorView();

		//ExpenseHelper.SetChartId(2);
		//ExpenseHelper.SetTempLocator(ExpenseHelper.partialXpathToLegendsListInControls);

		//ExpenseHelper.VerifySelectUnselect(enableDisableActionsType.disabling); 
		//ExpenseHelper.VerifySelectUnselect(enableDisableActionsType.enabling);
		
		//CommonTestStepActions.SelectCountryView();
		
		//ExpenseHelper.VerifySelectUnselect(enableDisableActionsType.disabling); 
		//ExpenseHelper.VerifySelectUnselect(enableDisableActionsType.enabling);
		
		
		
		
		

		
		//CommonTestStepActions.UnSelectAllVendors();
		//ExpenseHelper.VerifyControlsNotPresent(); // wait for all controls to be empty.
		
		
		
		//       OLD KPI code.
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
		ExpenseHelper.SetWaitDefault();
	    // JOptionPane.showMessageDialog(frame, "Select OK. This is Ana edit. TEST");
		driver.close();
		driver.quit();
	}	
	
	
}
