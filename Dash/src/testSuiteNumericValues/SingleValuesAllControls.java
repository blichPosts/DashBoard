package testSuiteNumericValues;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.CommonTestStepActions.ExpensesViewMode;
import helperObjects.ExpenseHelper;
import helperObjects.ExpenseHelper.controlType;
import helperObjects.ExpenseValuesHelper;

public class SingleValuesAllControls extends BaseClass 
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
	}
	
	
	@Test
	public static void SingleValuesAllControlsTest() throws Exception
	{
		// clear all the controls.
		CommonTestStepActions.GoToExpensePage();
		CommonTestStepActions.UnSelectAllVendors();
		ExpenseHelper.VerifyControlsNotPresent();

		// setup the list of lists to be used for testing AT & T.
		ExpenseValuesHelper.ReadFileAndBuildLists("AT&T Mobility.txt");
		CommonTestStepActions.SelectSingleVendor("AT&T Mobility");
		ExpenseValuesHelper.SetupChartIdForExpense();

		// total expense control.
		for(int x = 0; x < ExpenseValuesHelper.rowsOfValues; x++)
		{
			ExpenseValuesHelper.SelectMonth(x);
			// Thread.sleep(1000);
			ExpenseHelper.WaitForControlLegend(controlType.totalExpense);
			ExpenseValuesHelper.VerifyOneVendorTotalExpense();
		}
		
		DebugTimeout(9999, "9999");
		
	}
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
		driver.close();
		driver.quit();
	}

	
	
	
	
}
