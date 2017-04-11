package testSuiteExpenseHierarchy;

import java.util.Collections;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenseHierarchy.HierarchyNumbersDependents;
import expenseHierarchy.VisualPageLoad;
import helperObjects.Child;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;
import helperObjects.ExpenseHelper.controlType;
import helperObjects.ExpenseHelper.hierarchyTileMapTabSelection;

public class HierarchyNumbersTestsDependents extends BaseClass
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
	}
	
	@Test
	public static void HierarchyNumbersTestDependents() throws Exception
	{	
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);
		CommonTestStepActions.GoToExpensePageDetailedWait(); // the expense page with all vendors selected is shown at page open. 
		ExpenseHelper.WaitForControlLegend(controlType.costPerServiceNumber);
		VisualPageLoad.SelectAndWaitForPageLoad();

		ExpenseHelper.SetHierarchyMaxDisplayed(100);
		Thread.sleep(1000);
		
		HierarchyNumbersDependents.LoopThroughHierarchiesDependentUnits();
		//HierarchyNumbersDependents.LoopThroughMonths();
	}
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");	
		ExpenseHelper.SetWaitDefault();
	    JOptionPane.showMessageDialog(frame, "Test Done.");
		driver.close();
		driver.quit();
	}	
	
	
	
}
