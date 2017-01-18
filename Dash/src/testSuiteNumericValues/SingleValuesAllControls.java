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

		ExpenseValuesHelper.ReadFileAndBuildLists("AT&T Mobility.txt"); // setup the list of lists to be used for testing. the list holds expected values.
		CommonTestStepActions.SelectSingleVendor("AT&T Mobility"); // select the vendor
		ExpenseValuesHelper.SetupChartIdForExpense();

		// total expense control test. loop through each month for the selected vendor.
		for(int x = 0; x < ExpenseValuesHelper.rowsOfValues; x++)
		{
			// this selects month to run test on. it has a built in wait for the selection to complete
			ExpenseValuesHelper.SelectMonth(x);  
			
			if(x == 0) // wait for expense control to load on first time through loop.
			{
				ExpenseHelper.WaitForControlLegend(controlType.totalExpense);				
			}
			
			ExpenseValuesHelper.VerifyOneVendorTotalExpense(); // verify total expense value for current month selection. 
		}
		
		// total expense Spend Category control test. loop through each month for the selected vendor.
		for(int x = 0; x < ExpenseValuesHelper.rowsOfValues; x++)
		{
			// this selects month to run test on. it has a built in wait for the selection to complete
			ExpenseValuesHelper.SelectMonth(x);  
			
			if(x == 0) // wait for expense control to load on first time through loop.
			{
				ExpenseHelper.WaitForControlLegend(controlType.totalExpenseSpendCatergory);
				ExpenseValuesHelper.SetupChartIdForExpenseSpendCategory(); // need to set correct chart id. method above changes it. 
			}
			
			ExpenseValuesHelper.VerifyOneVendorTotalExpenseSpendCategory(); // verify total expense value for current month selection. 
		}
	}
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
		driver.close();
		driver.quit();
	}

	
	
	
	
}
