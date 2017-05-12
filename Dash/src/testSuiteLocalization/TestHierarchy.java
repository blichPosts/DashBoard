package testSuiteLocalization;

import org.openqa.selenium.By;
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
		//CommonTestStepActions.GoToExpensePageDetailedWait(); // the expense page with all vendors selected is shown at page open. 
		DebugTimeout(4, "wait 4 for expense page load");
		
		// ExpenseHelper.WaitForControlLegend(controlType.costPerServiceNumber);

		//ExpenseHelper.ShowSelectedMonth();
		
		// HierarchyHelper.SelectAndWaitForPageLoad();
		
		driver.findElement(By.cssSelector(".tdb-button.tdb-button--flat")).click();
		
		// .tdb-button.tdb-button--flat
		
		DebugTimeout(4, "wait 4 for page load");
		
		//CommonTestStepActions.OpenSettingsPanel();

		//FleetExpense.VerifySomeTextInSettingsPanel();

		//FleetExpense.selectLanguage("es-ES");

		//CommonTestStepActions.CloseSettingsPanel();

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
