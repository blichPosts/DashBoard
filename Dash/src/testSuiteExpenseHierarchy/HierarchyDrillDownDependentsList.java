package testSuiteExpenseHierarchy;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import expenseHierarchy.HierarchyNumbersDependents;
import expenseHierarchy.HierarchyNumbersDependents.DrillDownPageType;
import expenseHierarchy.VisualPageLoad;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;
import helperObjects.ExpenseHelper.controlType;

public class HierarchyDrillDownDependentsList extends BaseClass 
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
	}
	
	@Test
	public static void HierarchyDrillDownDependentsListTest() throws Exception
	{		
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);
		CommonTestStepActions.GoToExpensePageDetailedWait(); // the expense page with all vendors selected is shown at page open. 
		ExpenseHelper.WaitForControlLegend(controlType.costPerServiceNumber);

		VisualPageLoad.SelectAndWaitForPageLoad();
		
		ExpenseHelper.SetHierarchyMaxDisplayed(100);
		Thread.sleep(1000);

		HierarchyNumbersDependents.SetChartId();
		
		// set how many levels to drill down to.
		HierarchyNumbersDependents.SetMaxNumberOfLevelsToDrillDown(8); // TODO !!!!!  -- loop through categories.

		//HierarchyNumbersDependents.SetDrillDownPageType(DrillDownPageType.expense);
		
		//HierarchyNumbersDependents.DrillDownUpDependentUnits();

		HierarchyNumbersDependents.GoToViewTop10();		
		
		HierarchyNumbersDependents.SetDrillDownPageType(DrillDownPageType.topTen);
		
		HierarchyNumbersDependents.DrillDownUpDependentUnits();
		
	}
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");	
		ExpenseHelper.SetWaitDefault();
	    JOptionPane.showMessageDialog(frame, "DONE");
		driver.close();
		driver.quit();
	}		
}
