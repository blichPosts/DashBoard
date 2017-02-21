package testSuiteExpenseHierarchy;

import Dash.BaseClass;
import expenseHierarchy.HierarchyNumbersDependents;
import expenseHierarchy.VisualPageLoad;

import javax.swing.JOptionPane;
import javax.xml.xpath.XPath;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import helperObjects.CommonTestStepActions;
import helperObjects.CommonTestStepActions.ExpensesViewMode;
import helperObjects.ExpenseHelper;
import helperObjects.ExpenseHelper.controlType;
import helperObjects.ExpenseHelper.hierarchyPulldownSelection;
import helperObjects.ExpenseHelper.hierarchyTileMapTabSelection;

public class HierarchyNumbersTests extends BaseClass
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
	}
	
	@Test
	public static void HierarchyNumbersTest() throws Exception
	{	
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);
		CommonTestStepActions.GoToExpensePageDetailedWait(); // the expense page with all vendors selected is shown at page open. 
		
		ExpenseHelper.WaitForControlLegend(controlType.costPerServiceNumber);

		VisualPageLoad.SelectAndWaitForPageLoad();
		
		HierarchyNumbersDependents.SetChartId();

		// this sets the number of tiles to test and the number of tiles to show.
		HierarchyNumbersDependents.SetupNumberOfTilesToTestAndShow(2, 50);
		
		ShowText("Have Set Cost Center");
		
		HierarchyNumbersDependents.TestPhaseOne(); // TEST --------------------------------------
		
		//ShowText("Have Set Management");
		
		//ExpenseHelper.SelectHierarchy(hierarchyPulldownSelection.Management);
		
		//Thread.sleep(2000);
		
		//HierarchyNumbersDependents.TestPhaseOne(); // TEST --------------------------------------

		//ExpenseHelper.SelectHierarchy(hierarchyPulldownSelection.Approval);
		
		//ShowText("Have Set Approval");
		
		//Thread.sleep(2000);
		
		//HierarchyNumbersDependents.TestPhaseOne(); // TEST --------------------------------------
		
		

		
		//ExpenseHelper.SelectHierarchy(hierarchyPulldownSelection.CostCenter);
		
		
		
		
		
		
		//JOptionPane.showMessageDialog(frame, "Select OK. This is Ana edit. TEST");
		
		//ExpenseHelper.SelectHierarchy(hierarchyPulldownSelection.Management);
		
		//JOptionPane.showMessageDialog(frame, "Select OK. This is Ana edit. TEST");
		
		//ExpenseHelper.SelectHierarchy(hierarchyPulldownSelection.Approval);
		
		//JOptionPane.showMessageDialog(frame, "Select OK. This is Ana edit. TEST");
		
		// HierarchyNumbersDependents.TestPhaseOne();

		
		
		
		
		/*
		// HierarchyNumbersDependents.SetLoopTestStartTile(10); // needs work

		ExpenseHelper.SetHierarchyCostFilter(hierarchyTileMapTabSelection.Total);
		
		Thread.sleep(1000);
		
		HierarchyNumbersDependents.RunAllTiles();
		//HierarchyNumbersDependents.RunThroughMonths();

		ExpenseHelper.SetHierarchyCostFilter(hierarchyTileMapTabSelection.Optimizable);
		
		Thread.sleep(1000);
		
		HierarchyNumbersDependents.RunAllTiles();
		
		ExpenseHelper.SetHierarchyCostFilter(hierarchyTileMapTabSelection.Roaming);
		
		Thread.sleep(1000);
		
		HierarchyNumbersDependents.RunAllTiles();
		
		Thread.sleep(1000);
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
