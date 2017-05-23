package testSuiteLocalization;

import org.junit.experimental.theories.suppliers.TestedOn;
import org.openqa.selenium.By;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenses.CostPerServiceNumberTrend;
import expenses.TotalExpensesTrend;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;
import helperObjects.ExpenseHelper.expenseFilters;
import localization.FleetExpense;
// import localization.FleetExpense.viewType;

public class TestExpense extends BaseClass 
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
	}
	
	@Test
	public static void TestExpensesTest() throws Exception
	{
		// #1


		
		// this makes sure the most amount of expense legends are shown. the 'desiredMonth' can be found by running the commented 
		// code in 'TotalExpenseByVendorCountryActions' under under test suite 'testSuiteExpenseActions'.
		// CommonTestStepActions.selectMonthYearPulldown("April 2016");  
		
		// CommonTestStepActions.GoToExpensePageDetailedWait(); // doen't work in foreign language.  ---------------- 

		//Pause("Wait for Page Load.");
		
		DebugTimeout(4, "wait 4 for page load");
		
		//FleetExpense.InitVisibilityTileMapAverages(); // find what is showing in the KPI tiles.
		
		//Pause("");
		
		//CommonTestStepActions.OpenSettingsPanel();
		
		// NOTE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! MAIN TITLE at top.

		// FleetExpense.VerifySomeTextInSettingsPanel(); // gone

		// German [MDE] -- month/year
		// Japan [ja] -- year/month
		
		FleetExpense.SetupLanguageTag("[ja]");
		
		FleetExpense.SetupInsideTag("ja");
		
		FleetExpense.SetCurrency("USD");

		//CommonTestStepActions.CloseSettingsPanel();
		
		// FleetExpense.SetViewType(viewType.vendor); // don't need.
		
		FleetExpense.LoopThroughMonths();
		
		ExpenseHelper.SetupForCountryPageWait();
		
		// move to country view.
		CommonTestStepActions.SelectCountryView();
		
		ExpenseHelper.WaitForCountryPageLoad();

		// FleetExpense.SetViewType(viewType.country); // don't need
		
		FleetExpense.LoopThroughMonths();
		

		ShowText("DONE ..... PASSED");
		
		Pause("Passed...");
		
		
		// SIDEWAYS TEXT
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
