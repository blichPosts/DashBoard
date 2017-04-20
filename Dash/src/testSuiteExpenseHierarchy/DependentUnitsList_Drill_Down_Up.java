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

public class DependentUnitsList_Drill_Down_Up extends BaseClass 
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
	}
	
	@Test
	public static void DependentUnitsList_Drill_Down_Up_Test() throws Exception
	{		
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);
		CommonTestStepActions.GoToExpensePageDetailedWait(); // the expense page with all vendors selected is shown at page open. 
		ExpenseHelper.WaitForControlLegend(controlType.costPerServiceNumber);
		VisualPageLoad.SelectAndWaitForPageLoad();

		ExpenseHelper.SetHierarchyMaxDisplayed(100);
		Thread.sleep(1000);

		HierarchyNumbersDependents.SetChartId();
		
		// set how many levels to drill down to.
		HierarchyNumbersDependents.SetMaxNumberOfLevelsToDrillDown(5); 

		HierarchyNumbersDependents.SetDrillDownPageType(DrillDownPageType.expense);
		
		//HierarchyNumbersDependents.GoToViewTop10();		
		
		//HierarchyNumbersDependents.SetDrillDownPageType(DrillDownPageType.topTen);
		
		// HierarchyNumbersDependents.DrillDown_Up_DependentUnits();
		
		HierarchyNumbersDependents.LoopThroughHierarchiesDependentUnitsDrill_Down_Up(); 
		
		// HierarchyNumbersDependents.LoopThroughCatergoriesFor_Lists_Up_Down(); 
		
		
		
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
