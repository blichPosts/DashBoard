package testSuiteLocalization;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;
import helperObjects.HierarchyHelper;
import helperObjects.ReadFilesHelper;
import helperObjects.ExpenseHelper.controlType;
import localization.FleetExpense;
import localization.Hierarchy;

public class TestHierarchy extends BaseClass 
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
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);
		CommonTestStepActions.GoToExpensePageDetailedWait(); // the expense page with all vendors selected is shown at page open. 
		
		ExpenseHelper.WaitForControlLegend(controlType.costPerServiceNumber);

		ExpenseHelper.ShowSelectedMonth();
		
		HierarchyHelper.SelectAndWaitForPageLoad();
		
		CommonTestStepActions.OpenSettingsPanel();

		//FleetExpense.VerifySomeTextInSettingsPanel();

		//FleetExpense.selectLanguage("es-ES");

		CommonTestStepActions.CloseSettingsPanel();

		Hierarchy.RunExpenseLocalizationTagTests();
		
		Pause("");
		// miss step.
		
	
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
