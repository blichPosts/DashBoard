package testSuiteExpenseHierarchy;

import java.util.Collections;

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
	public static void HierarchyNumbersTest() throws Exception
	{	
		CommonTestStepActions.selectMonthYearPulldown(ExpenseHelper.desiredMonth);
		CommonTestStepActions.GoToExpensePageDetailedWait(); // the expense page with all vendors selected is shown at page open. 
		
		ExpenseHelper.WaitForControlLegend(controlType.costPerServiceNumber);

		VisualPageLoad.SelectAndWaitForPageLoad();

		new Select(driver.findElement(By.xpath("(//span[text()='Maximum Displayed:'])[2]/following::select"))).selectByVisibleText("100");

		// /////////////////////////////
		ExpenseHelper.SetHierarchyCostFilter(hierarchyTileMapTabSelection.Total); // set category filter.
		
		HierarchyNumbersDependents.BuildDependentChildObjects(); // create list of dependent units from Json call.
		Collections.sort(HierarchyNumbersDependents.childList, new Child()); // sort list of dependent units from Json call.
		
		// HierarchyNumbersDependents.ShowChildList();
		
		HierarchyNumbersDependents.VerifyActualExpectedDependentUnits();
		

		// /////////////////////////////
		ExpenseHelper.SetHierarchyCostFilter(hierarchyTileMapTabSelection.Optimizable); // set category filter.
		
		DebugTimeout(3, "Three");
		
		HierarchyNumbersDependents.BuildDependentChildObjects(); // create list of dependent units from Json call.
		Collections.sort(HierarchyNumbersDependents.childList, new Child()); // sort list of dependent units from Json call.
		HierarchyNumbersDependents.VerifyActualExpectedDependentUnits();
		
		// DebugTimeout(9999, "9999");
	
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
