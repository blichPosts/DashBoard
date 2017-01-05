package testSuiteExpenseActions;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import Dash.BaseClass.ViewType;
import expenses.TotalExpenseByVendorCarrier;
import expenses.TotalExpenseByVendorSpendCategory;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;
import helperObjects.ExpenseHelper.enableDisableActionsType;

public class TotalExpenseByVendorSpendCategoryActions extends BaseClass 
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
	}
	
	@Test
	public static void TotalExpenseByVendorSpendCategoryActionsTest() throws Exception
	{
		
		// setup page for test.
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);
		CommonTestStepActions.GoToExpensePageDetailedWait(); // the expense page with all vendors selected is shown at page open. 

		// need to
		ExpenseHelper.SetChartId(1);
		
		// store vendors currently shown. 
		TotalExpenseByVendorSpendCategory.StoreAllLegendsInTotalExpenseSpendCategory(); 
		
		CommonTestStepActions.UnSelectAllVendors();
		ExpenseHelper.VerifyControlsNotPresent();
		
		CommonTestStepActions.selectOneVendor(TotalExpenseByVendorSpendCategory.totalExpenseExpectedLegendsList.get(0));
		
		// need to 
		TotalExpenseByVendorSpendCategory.SetChartId(1);
		
		TotalExpenseByVendorSpendCategory.VerifyRemovingCategories(); 
		
		/*
		driver.navigate().refresh();

		DebugTimeout(10, "wait ten seconds wait.");
		
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
