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
import localization.FleetExpense.viewType;

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
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);  
		
		CommonTestStepActions.GoToExpensePageDetailedWait(); 
		
		CommonTestStepActions.OpenSettingsPanel();
		
		// NOTE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! MAIN TITLE at top.

		FleetExpense.VerifySomeTextInSettingsPanel();

		// FleetExpense.selectLanguage("es-ES");

		CommonTestStepActions.CloseSettingsPanel();
		
		//FleetExpense.KpiTitleTitle();
		//FleetExpense.KpiTileRolling();
		//FleetExpense.KpiTileThreeMonth();
		//FleetExpense.KpiTileSixMonth();
		//FleetExpense.TwoMainTitles();
		// FleetExpense.SubTitles();
		// Above are complete.
		
		//FleetExpense.TopSelectors();
		//FleetExpense.BottomSelectors();
		//FleetExpense.PieLegends();
		//FleetExpense.VendorSpendLegends();
		// FleetExpense.AllTrendLegends();
		
		FleetExpense.SetViewType(viewType.vendor);
		
		FleetExpense.LoopThroughMonths();
		
		Pause("");
		
		ExpenseHelper.SetupForCountryPageWait();
		
		// move to country view.
		CommonTestStepActions.SelectCountryView();
		
		ExpenseHelper.WaitForCountryPageLoad();

		FleetExpense.SetViewType(viewType.country);
		
		FleetExpense.LoopThroughMonths();
		


		
		Pause("");
		
		
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
