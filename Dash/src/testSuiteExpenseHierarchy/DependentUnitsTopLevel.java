package testSuiteExpenseHierarchy;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenseHierarchy.HierarchyNumbersDependents;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;
import helperObjects.HierarchyHelper;
import helperObjects.ReadFilesHelper;
import helperObjects.ExpenseHelper.controlType;

public class DependentUnitsTopLevel extends BaseClass
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
	}
	
	@Test
	public static void DependentsTopLevelTest() throws Exception
	{	
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);
		CommonTestStepActions.GoToExpensePageDetailedWait(); // the expense page with all vendors selected is shown at page open. 
		ExpenseHelper.WaitForControlLegend(controlType.costPerServiceNumber);
		
		ReadFilesHelper.startCollectingData(); // start the JavascriptExecutor.
		
		HierarchyHelper.SelectAndWaitForPageLoad();

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
