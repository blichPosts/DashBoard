package testSuiteLocalization;

import javax.swing.JOptionPane;

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
		
		DebugTimeout(6, "wait 6 for page load");
		
		// German MDE
		
		Hierarchy.SetupLanguageTag("[MDE]");
		
		Hierarchy.SetupInsideTag("MDE");
		
		Hierarchy.SetCurrency("USD");
		
		Hierarchy.LoopThroughMonthsTwo();
		
		//Hierarchy.LoopThroughMonths();
		
		// Hierarchy.LoopThroughHierarchies(); // hierarchies to category selectors to maybe driil down 
		
		
		Pause("Test Passed.");
		// miss step.
		
	
	}
	
	@AfterClass
	public static void closeDriver() throws Exception
	{

		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "At End Before Browser Close");
		driver.close();
		driver.quit();
	}		
	
	
}
